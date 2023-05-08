package com.example.agricart;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Store {
    String storeName, description;

    public Store() {
    }

    public Store(String storeName, String description) {
        this.storeName = storeName;
        this.description = description;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getDescription() {
        return description;
    }
}
