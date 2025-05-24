package com.example.onebancrestaurantapp.utils;

import com.example.onebancrestaurantapp.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;

    private final Map<String, Integer> itemCounts = new HashMap<>();
    private final Map<String, Item> itemMap = new HashMap<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(Item item) {
        String id = item.id;
        itemMap.put(id, item);
        itemCounts.put(id, itemCounts.getOrDefault(id, 0) + 1);
    }

    public void removeItem(Item item) {
        String id = item.id;
        if (itemCounts.containsKey(id)) {
            int count = itemCounts.get(id);
            if (count > 1) {
                itemCounts.put(id, count - 1);
            } else {
                itemCounts.remove(id);
                itemMap.remove(id);
            }
        }
    }

    public Map<Item, Integer> getCartItems() {
        Map<Item, Integer> result = new HashMap<>();
        for (String id : itemCounts.keySet()) {
            result.put(itemMap.get(id), itemCounts.get(id));
        }
        return result;
    }

    public void clearCart() {
        itemCounts.clear();
        itemMap.clear();
    }
}
