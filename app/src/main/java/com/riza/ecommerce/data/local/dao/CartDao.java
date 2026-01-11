package com.riza.ecommerce.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.riza.ecommerce.data.local.entity.CartItem;
import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_items")
    LiveData<List<CartItem>> getAllCartItems();

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    CartItem getCartItemById(int productId);

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Query("SELECT COUNT(*) FROM cart_items")
    LiveData<Integer> getCartItemCount();
}