package com.example.onebancrestaurantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onebancrestaurantapp.models.Cuisine;
import com.example.onebancrestaurantapp.models.Item;
import com.example.onebancrestaurantapp.network.ApiHelper;
import com.example.onebancrestaurantapp.ui.CartDetailsActivity;
import com.example.onebancrestaurantapp.ui.CartActivity;
import com.example.onebancrestaurantapp.ui.CuisineActivity;
import com.example.onebancrestaurantapp.ui.CuisineAdapter;
import com.example.onebancrestaurantapp.ui.DishAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "API_RESPONSE";
    private List<Cuisine> cuisineList = new ArrayList<>();
    private List<Item> topDishes = new ArrayList<>();

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String savedLang = getSharedPreferences("settings", MODE_PRIVATE).getString("lang", "en");
        setLocale(savedLang);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartDetailsActivity.class);
            startActivity(intent);
        });

        Button languageToggleBtn = findViewById(R.id.languageToggleBtn);
        languageToggleBtn.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
            String currentLang = prefs.getString("lang", "en");
            String newLang = currentLang.equals("en") ? "hi" : "en";
            prefs.edit().putString("lang", newLang).apply();
            setLocale(newLang);
            recreate();
        });


        showPreviousOrderInfo();


        fetchCuisineAndDishes();
    }


    private void showPreviousOrderInfo() {
        SharedPreferences prefs = getSharedPreferences("orders", MODE_PRIVATE);
        String orderId = prefs.getString("last_order_id", null);
        if (orderId != null) {
            String total = prefs.getString("last_order_total", "0");
            String time = prefs.getString("last_order_time", "N/A");

            LinearLayout layout = findViewById(R.id.previousOrderLayout);
            TextView orderTime = findViewById(R.id.previousOrderTime);
            TextView orderAmount = findViewById(R.id.previousOrderAmount);
            TextView orderRef = findViewById(R.id.previousOrderId);
            TextView viewOrderBtn = findViewById(R.id.viewOrderBtn);

            layout.setVisibility(View.VISIBLE);
            orderTime.setText(time);
            orderAmount.setText("₹" + total);
            orderRef.setText("Order ID: #" + orderId);

            if (viewOrderBtn != null) {
                viewOrderBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                });
            }
        }
    }

    private void fetchCuisineAndDishes() {
        new Thread(() -> {
            String jsonRequest = "{ \"page\": 1, \"count\": 10 }";
            String response = ApiHelper.post("get_item_list", jsonRequest);

            if (response != null) {
                Log.d(TAG, response);
                runOnUiThread(() -> parseAndBindData(response));
            } else {
                Log.e(TAG, "API response was null");
            }
        }).start();
    }

    private void parseAndBindData(String response) {
        try {
            JSONObject root = new JSONObject(response);
            JSONArray cuisinesArray = root.getJSONArray("cuisines");

            for (int i = 0; i < cuisinesArray.length(); i++) {
                JSONObject cuisineObj = cuisinesArray.getJSONObject(i);
                Cuisine cuisine = new Cuisine();
                cuisine.cuisine_id = cuisineObj.getString("cuisine_id");
                cuisine.cuisine_name = cuisineObj.getString("cuisine_name");
                cuisine.cuisine_image_url = cuisineObj.getString("cuisine_image_url");

                JSONArray itemsArray = cuisineObj.getJSONArray("items");
                List<Item> itemList = new ArrayList<>();

                for (int j = 0; j < itemsArray.length(); j++) {
                    JSONObject itemObj = itemsArray.getJSONObject(j);
                    Item item = new Item();
                    item.id = itemObj.getString("id");
                    item.name = itemObj.getString("name");
                    item.image_url = itemObj.getString("image_url");
                    item.price = itemObj.getString("price");
                    item.rating = itemObj.getString("rating");
                    itemList.add(item);

                    if (topDishes.size() < 3) {
                        topDishes.add(item);
                    }
                }

                cuisine.items = itemList;
                cuisineList.add(cuisine);
            }

            RecyclerView cuisineRecycler = findViewById(R.id.cuisineRecyclerView);
            cuisineRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            cuisineRecycler.setAdapter(new CuisineAdapter(this, cuisineList, cuisine -> {
                Intent intent = new Intent(MainActivity.this, CuisineActivity.class);
                intent.putExtra("cuisine_name", cuisine.cuisine_name);
                intent.putExtra("item_list", new ArrayList<>(cuisine.items));
                startActivity(intent);
            }));

            RecyclerView topDishesRecycler = findViewById(R.id.topDishesRecyclerView);
            topDishesRecycler.setLayoutManager(new GridLayoutManager(this, 2));
            topDishesRecycler.setAdapter(new DishAdapter(this, topDishes));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
