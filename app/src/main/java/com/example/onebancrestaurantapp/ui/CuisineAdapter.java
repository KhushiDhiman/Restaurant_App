package com.example.onebancrestaurantapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onebancrestaurantapp.R;
import com.example.onebancrestaurantapp.models.Cuisine;

import java.util.List;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.CuisineViewHolder> {

    private Context context;
    private List<Cuisine> cuisineList;
    private OnCuisineClickListener listener;


    public interface OnCuisineClickListener {
        void onCuisineClick(Cuisine cuisine);
    }


    public CuisineAdapter(Context context, List<Cuisine> cuisineList, OnCuisineClickListener listener) {
        this.context = context;
        this.cuisineList = cuisineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CuisineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuisine, parent, false);
        return new CuisineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisineViewHolder holder, int position) {
        Cuisine cuisine = cuisineList.get(position);
        holder.cuisineName.setText(cuisine.cuisine_name);


        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(cuisine.cuisine_image_url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                holder.itemView.post(() -> holder.cuisineImage.setImageBitmap(bmp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Click listener using interface
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CuisineActivity.class);
            intent.putExtra("cuisine_name", cuisine.cuisine_name);
            intent.putExtra("item_list", new ArrayList<>(cuisine.items)); // Make sure items is Serializable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cuisineList.size();
    }

    public static class CuisineViewHolder extends RecyclerView.ViewHolder {
        ImageView cuisineImage;
        TextView cuisineName;

        public CuisineViewHolder(@NonNull View itemView) {
            super(itemView);
            cuisineImage = itemView.findViewById(R.id.cuisineImage);
            cuisineName = itemView.findViewById(R.id.cuisineName);
        }
    }
}