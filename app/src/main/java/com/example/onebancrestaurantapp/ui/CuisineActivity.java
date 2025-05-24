package com.example.onebancrestaurantapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onebancrestaurantapp.R;
import com.example.onebancrestaurantapp.models.Item;

import java.util.List;

public class CuisineActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        // Set title in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Card");
        }

        // Get data from Intent
        String cuisineName = getIntent().getStringExtra("cuisine_name");
        List<Item> itemList = (List<Item>) getIntent().getSerializableExtra("item_list");

        // Set title in the view
        TextView title = findViewById(R.id.cuisineTitle);
        title.setText(cuisineName);

        // Set up Grid RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cuisineItemRecycler); // Use this ID in your layout
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new DishAdapter(this, itemList));

        // Checkout Button
        Button checkout = findViewById(R.id.checkoutButton);
        checkout.setOnClickListener(v -> {
            startActivity(new Intent(CuisineActivity.this, CartActivity.class));
        });
    }
}