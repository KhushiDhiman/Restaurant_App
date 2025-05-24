package com.example.onebancrestaurantapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onebancrestaurantapp.R;
import com.example.onebancrestaurantapp.models.Item;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Item> itemList;
    private Map<Item, Integer> cartMap;

    public CartAdapter(Context context, List<Item> itemList, Map<Item, Integer> cartMap) {
        this.context = context;
        this.itemList = itemList;
        this.cartMap = cartMap;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.name.setText(item.name);
        holder.price.setText("₹" + item.price);
        holder.quantity.setText("Qty: " + cartMap.get(item));

        // Load image in background
        new Thread(() -> {
            try {
                URL url = new URL(item.image_url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                holder.itemView.post(() -> holder.image.setImageBitmap(bmp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cartItemName);
            price = itemView.findViewById(R.id.cartItemPrice);
            quantity = itemView.findViewById(R.id.cartItemQuantity);
            image = itemView.findViewById(R.id.cartItemImage);
        }
    }
}
