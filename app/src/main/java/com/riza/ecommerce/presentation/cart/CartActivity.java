package com.riza.ecommerce.presentation.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.riza.ecommerce.R;
import com.riza.ecommerce.data.local.entity.CartItem;
import com.riza.ecommerce.databinding.ActivityCartBinding;
import com.riza.ecommerce.presentation.adapter.CartAdapter;
import com.riza.ecommerce.presentation.MainActivity;
import com.riza.ecommerce.presentation.profile.ProfileActivity;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartViewModel viewModel;
    private CartAdapter adapter;
    private static final double SHIPPING_COST = 10.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnCartItemListener(new CartAdapter.OnCartItemListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                item.setQuantity(newQuantity);
                viewModel.updateCartItem(item);
            }

            @Override
            public void onItemDeleted(CartItem item) {
                viewModel.deleteCartItem(item);
                Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupObservers() {
        viewModel.getAllCartItems().observe(this, cartItems -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                binding.emptyLayout.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.cardSummary.setVisibility(View.VISIBLE);
                binding.tvDeleteAll.setVisibility(View.VISIBLE);

                adapter.setCartItems(cartItems);
                updateSummary(cartItems);
            } else {
                binding.emptyLayout.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.cardSummary.setVisibility(View.GONE);
                binding.tvDeleteAll.setVisibility(View.GONE);
            }
        });
    }

    private void updateSummary(List<CartItem> cartItems) {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }
        double total = subtotal + SHIPPING_COST;

        binding.tvSubtotal.setText(String.format(Locale.US, "$%.2f", subtotal));
        binding.tvShipping.setText(String.format(Locale.US, "$%.2f", SHIPPING_COST));
        binding.tvTotal.setText(String.format(Locale.US, "$%.2f", total));
    }

    private void setupListeners() {
        binding.btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Checkout successful! Total: " + binding.tvTotal.getText(),
                    Toast.LENGTH_LONG).show();
            viewModel.clearCart();
        });

        binding.tvDeleteAll.setOnClickListener(v -> {
            viewModel.clearCart();
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });

        // Bottom Navigation
        binding.bottomNavigation.setSelectedItemId(R.id.nav_cart);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}