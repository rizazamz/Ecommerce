package com.riza.ecommerce.presentation.detail;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.riza.ecommerce.data.local.entity.CartItem;
import com.riza.ecommerce.data.repository.CartRepository;
import com.riza.ecommerce.data.repository.ProductRepository;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.utils.Resource;

public class DetailViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private CartRepository cartRepository;

    public DetailViewModel(Application application) {
        super(application);
        productRepository = new ProductRepository();
        cartRepository = new CartRepository(application);
    }

    public LiveData<Resource<Product>> getProductById(int id) {
        return productRepository.getProductById(id);
    }

    public void addToCart(Product product) {
        CartItem existingItem = cartRepository.getCartItemById(product.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartRepository.update(existingItem);
        } else {
            CartItem newItem = new CartItem(
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getThumbnail(),
                    1,
                    product.getStock()
            );
            cartRepository.insert(newItem);
        }
    }
}