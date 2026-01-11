package com.riza.ecommerce.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.riza.ecommerce.data.local.dao.CartDao;
import com.riza.ecommerce.data.local.database.AppDatabase;
import com.riza.ecommerce.data.local.entity.CartItem;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private CartDao cartDao;
    private LiveData<List<CartItem>> allCartItems;
    private LiveData<Integer> cartItemCount;  // ← Pastikan ini LiveData<Integer>
    private ExecutorService executorService;

    public CartRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartDao = database.cartDao();
        allCartItems = cartDao.getAllCartItems();
        cartItemCount = cartDao.getCartItemCount();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(CartItem cartItem) {
        executorService.execute(() -> cartDao.insert(cartItem));
    }

    public void update(CartItem cartItem) {
        executorService.execute(() -> cartDao.update(cartItem));
    }

    public void delete(CartItem cartItem) {
        executorService.execute(() -> cartDao.delete(cartItem));
    }

    public void clearCart() {
        executorService.execute(() -> cartDao.clearCart());
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }

    public LiveData<Integer> getCartItemCount() {  // ← Return type harus LiveData<Integer>
        return cartItemCount;
    }

    public CartItem getCartItemById(int productId) {
        try {
            return executorService.submit(() -> cartDao.getCartItemById(productId)).get();
        } catch (Exception e) {
            return null;
        }
    }
}