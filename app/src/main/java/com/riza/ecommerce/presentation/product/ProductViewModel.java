package com.riza.ecommerce.presentation.product;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.riza.ecommerce.data.repository.CartRepository;
import com.riza.ecommerce.data.repository.ProductRepository;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.utils.Resource;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final MutableLiveData<Resource<List<Product>>> productsLiveData;
    private final List<Product> allProducts;
    private int currentSkip;
    private static final int PAGE_SIZE = 20;

    public ProductViewModel(Application application) {
        super(application);
        productRepository = new ProductRepository();
        cartRepository = new CartRepository(application);
        productsLiveData = new MutableLiveData<>();
        allProducts = new ArrayList<>();
        currentSkip = 0;
    }

    public LiveData<Resource<List<Product>>> getProducts() {
        return productsLiveData;
    }
    public void refreshProducts(boolean simulateError) {
        allProducts.clear();
        currentSkip = 0;
        loadProducts(simulateError);
    }

    public void loadProducts(boolean simulateError) {
        productRepository
                .getProducts(PAGE_SIZE, currentSkip, simulateError)
                .observeForever(resource -> {
                    productsLiveData.setValue(resource);
                });
    }

    public LiveData<Integer> getCartItemCount() {
        return cartRepository.getCartItemCount();
    }
}