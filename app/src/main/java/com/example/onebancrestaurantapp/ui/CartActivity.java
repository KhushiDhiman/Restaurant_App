package com.example.onebancrestaurantapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.onebancrestaurantapp.MainActivity;
import com.example.onebancrestaurantapp.R;
import com.example.onebancrestaurantapp.models.Item;
import com.example.onebancrestaurantapp.utils.CartManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private TextView totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        totalText = findViewById(R.id.totalTextView);
        RecyclerView cartRecycler = findViewById(R.id.cartRecyclerView);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Load items from Cart
        Map<Item, Integer> cartMap = CartManager.getInstance().getCartItems();
        List<Item> itemList = new ArrayList<>(cartMap.keySet());

        CartAdapter adapter = new CartAdapter(this, itemList, cartMap);
        cartRecycler.setAdapter(adapter);

        double total = 0;
        for (Item item : cartMap.keySet()) {
            int qty = cartMap.get(item);
            total += Double.parseDouble(item.price) * qty;
        }
        double tax = total * 0.05;
        double grandTotal = total + tax;

        totalText.setText("Net: ₹" + total + "\nTax: ₹" + tax + "\nTotal: ₹" + grandTotal);

        Button placeOrderBtn = findViewById(R.id.placeOrderBtn);
        placeOrderBtn.setOnClickListener(v -> {
            JSONArray itemsArray = new JSONArray();
            for (Item item : cartMap.keySet()) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", item.id);
                    obj.put("name", item.name);
                    obj.put("price", item.price);
                    obj.put("quantity", cartMap.get(item));
                    itemsArray.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Simulate saving order directly (no payment API)
            String fakeRef = "ORDER" + System.currentTimeMillis(); // Unique ID
            String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

            SharedPreferences prefs = getSharedPreferences("orders", MODE_PRIVATE);
            prefs.edit()
                    .putString("last_order_id", fakeRef)
                    .putString("last_order_total", String.valueOf(grandTotal))
                    .putString("last_order_time", time)
                    .apply();

            Toast.makeText(this, "Order placed successfully!\nRef: " + fakeRef, Toast.LENGTH_LONG).show();

            CartManager.getInstance().clearCart();

            // Navigate back to MainActivity
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        Button backToMenu = findViewById(R.id.backToMenuBtn);
        backToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}