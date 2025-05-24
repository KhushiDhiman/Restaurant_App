package com.example.onebancrestaurantapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onebancrestaurantapp.R;
import com.example.onebancrestaurantapp.models.Item;
import com.example.onebancrestaurantapp.utils.CartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import androidx.appcompat.widget.Toolbar;




public class CartDetailsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private Button proceedToPaymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);

        // ✅ Setup toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cart");
        }

        recyclerView = findViewById(R.id.cartDetailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Map<Item, Integer> cartItems = CartManager.getInstance().getCartItems();
        List<Item> itemList = new ArrayList<>(cartItems.keySet());

        CartAdapter adapter = new CartAdapter(this, itemList, cartItems);
        recyclerView.setAdapter(adapter);

        proceedToPaymentBtn = findViewById(R.id.proceedToPaymentBtn);
        proceedToPaymentBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartDetailsActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }
}