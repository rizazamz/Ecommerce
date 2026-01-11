package com.riza.ecommerce.presentation.cart;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.riza.ecommerce.data.local.entity.CartItem;
import com.riza.ecommerce.data.repository.CartRepository;
import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository repository;
    private LiveData<List<CartItem>> allCartItems;  // ← Tambahkan <CartItem>

    public CartViewModel(Application application) {
        super(application);
        repository = new CartRepository(application);
        allCartItems = repository.getAllCartItems();
    }

    public LiveData<List<CartItem>> getAllCartItems() {  // ← Tambahkan <CartItem>
        return allCartItems;
    }

    public void updateCartItem(CartItem cartItem) {
        repository.update(cartItem);
    }

    public void deleteCartItem(CartItem cartItem) {
        repository.delete(cartItem);
    }

    public void clearCart() {
        repository.clearCart();
    }

    public LiveData<Integer> getCartItemCount() {
        return repository.getCartItemCount();
    }
}