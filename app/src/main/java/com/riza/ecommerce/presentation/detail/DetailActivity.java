package com.riza.ecommerce.presentation.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.riza.ecommerce.databinding.ActivityDetailBinding;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.utils.Resource;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private DetailViewModel viewModel;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductDetail(productId);
        }

        setupListeners();
    }

    private void loadProductDetail(int productId) {
        viewModel.getProductById(productId).observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.contentLayout.setVisibility(View.GONE);
            } else if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                binding.progressBar.setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.VISIBLE);
                currentProduct = (Product) resource.getData();
                displayProduct(currentProduct);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProduct(Product product) {
        binding.toolbar.setTitle(product.getTitle());
        binding.tvProductName.setText(product.getTitle());
        binding.tvProductPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        binding.tvProductCategory.setText("Category: " + product.getCategory());
        binding.tvProductStock.setText(String.format("Stock: %d", product.getStock()));
        binding.tvProductDescription.setText(product.getDescription());

        Glide.with(this)
                .load(product.getThumbnail())
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivProductImage);
    }

    private void setupListeners() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                if (currentProduct.getStock() > 0) {
                    viewModel.addToCart(currentProduct);
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}