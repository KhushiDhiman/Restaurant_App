package com.example.onebancrestaurantapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onebancrestaurantapp.R;
import com.example.onebancrestaurantapp.models.Item;
import com.example.onebancrestaurantapp.utils.CartManager;

import java.net.URL;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {


    private final Context context;
    private final List<Item> dishList;

    public DishAdapter(Context context, List<Item> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Item item = dishList.get(position);
        holder.dishName.setText(item.name);
        holder.dishPrice.setText("₹" + item.price);
        holder.dishRating.setText("⭐ " + item.rating);

        // Load image from URL
        if (item.image_url != null && !item.image_url.isEmpty()) {
            new Thread(() -> {
                try {
                    URL url = new URL(item.image_url);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    holder.itemView.post(() -> holder.dishImage.setImageBitmap(bmp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Initialize quantity
        final int[] quantity = {1};
        holder.quantityText.setText(String.valueOf(quantity[0]));

        // Increase button
        holder.increaseBtn.setOnClickListener(v -> {
            quantity[0]++;
            holder.quantityText.setText(String.valueOf(quantity[0]));
        });

        // Decrease button
        holder.decreaseBtn.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                holder.quantityText.setText(String.valueOf(quantity[0]));
            }
        });

        // Add to Cart
        holder.addToCartBtn.setOnClickListener(v -> {
            for (int i = 0; i < quantity[0]; i++) {
                CartManager.getInstance().addItem(item);
            }
            Toast.makeText(context, item.name + " x" + quantity[0] + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView dishName, dishPrice, dishRating, quantityText;
        Button increaseBtn, decreaseBtn, addToCartBtn;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.dishImage);
            dishName = itemView.findViewById(R.id.dishName);
            dishPrice = itemView.findViewById(R.id.dishPrice);
            dishRating = itemView.findViewById(R.id.dishRating);
            quantityText = itemView.findViewById(R.id.quantityText);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);

        }
    }
}