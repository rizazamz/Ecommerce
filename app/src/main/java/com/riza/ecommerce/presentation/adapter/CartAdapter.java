package com.riza.ecommerce.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.riza.ecommerce.R;
import com.riza.ecommerce.data.local.entity.CartItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {  // ← Tambahkan <CartAdapter.CartViewHolder>
    private List<CartItem> cartItems = new ArrayList<>();  // ← Tambahkan <CartItem>
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemDeleted(CartItem item);
    }

    public void setOnCartItemListener(OnCartItemListener listener) {
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {  // ← Tambahkan <CartItem>
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {  // ← Hapus method yang duplikat
        CartItem item = cartItems.get(position);
        holder.tvName.setText(item.getTitle());
        holder.tvPrice.setText(String.format(Locale.US, "$%.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(holder.itemView.getContext())
                .load(item.getThumbnail())
                .placeholder(android.R.color.darker_gray)
                .into(holder.ivImage);

        holder.btnIncrease.setOnClickListener(v -> {
            if (item.getQuantity() < item.getStock() && listener != null) {
                listener.onQuantityChanged(item, item.getQuantity() + 1);
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1 && listener != null) {
                listener.onQuantityChanged(item, item.getQuantity() - 1);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemDeleted(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnIncrease, btnDecrease, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}