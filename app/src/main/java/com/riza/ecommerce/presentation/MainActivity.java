package com.riza.ecommerce.presentation;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.riza.ecommerce.R;
import com.riza.ecommerce.databinding.ActivityMainBinding;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.presentation.adapter.ProductAdapter;
import com.riza.ecommerce.presentation.cart.CartActivity;
import com.riza.ecommerce.presentation.detail.DetailActivity;
import com.riza.ecommerce.presentation.product.ProductViewModel;
import com.riza.ecommerce.presentation.profile.ProfileActivity;
import com.riza.ecommerce.utils.Resource;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;
    private boolean isLoading = false;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> displayedProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        setupRecyclerView();
        setupObservers();
        setupListeners();

        viewModel.loadProducts(false);
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter();
        adapter = new ProductAdapter();
        int columns = getResources().getInteger(R.integer.product_grid_columns);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!binding.etSearch.getText().toString().isEmpty()) {
                    return;
                }

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        viewModel.loadProducts(false);
                    }
                }
            }
        });
    }

    private void setupObservers() {
        viewModel.getProducts().observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.LOADING) {
                isLoading = true;
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.errorLayout.setVisibility(View.GONE);
            } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                isLoading = false;
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                if (resource.getData() != null && !resource.getData().isEmpty()) {
                    allProducts = new ArrayList<>(resource.getData());
                    displayedProducts = new ArrayList<>(allProducts);

                    // Jika ada search query, filter ulang
                    String currentQuery = binding.etSearch.getText().toString();
                    if (!currentQuery.isEmpty()) {
                        filterProducts(currentQuery);
                    } else {
                        adapter.setProducts(displayedProducts);
                    }

                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                isLoading = false;
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.tvError.setText(resource.getMessage());
            }
        });

        viewModel.getCartItemCount().observe(this, count -> {
            if (count != null && count > 0) {
                binding.tvCartBadge.setVisibility(View.VISIBLE);
                binding.tvCartBadge.setText(String.valueOf(count));
            } else {
                binding.tvCartBadge.setVisibility(View.GONE);
            }
        });
    }

    private void setupListeners() {
        // Swipe Refresh
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.etSearch.setText("");
            viewModel.refreshProducts(true);
        });
        binding.btnRetry.setOnClickListener(v -> {
            binding.etSearch.setText("");
            viewModel.refreshProducts(false);
        });
        binding.cartIconFrame.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Search action button
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus();
                return true;
            }
            return false;
        });

        // Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            // Show all produk
            displayedProducts = new ArrayList<>(allProducts);
            adapter.setProducts(displayedProducts);
            // Update title
            binding.tvTitle.setText("All Products");
        } else {
            // Filter produk berdasarkan query
            List<Product> filtered = new ArrayList<>();
            String lowerCaseQuery = query.toLowerCase().trim();

            for (Product product : allProducts) {
                if (product.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        product.getCategory().toLowerCase().contains(lowerCaseQuery)) {
                    filtered.add(product);
                }
            }
            displayedProducts = filtered;
            adapter.setProducts(displayedProducts);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int columns = getResources().getInteger(R.integer.product_grid_columns);
        GridLayoutManager layoutManager = (GridLayoutManager) binding.recyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.setSpanCount(columns);
            adapter.notifyDataSetChanged();
        }
    }
}