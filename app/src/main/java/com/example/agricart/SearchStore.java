package com.example.agricart;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SearchStore {
    String storeName, description;



    public SearchStore() {
    }

    public SearchStore(String storeName, String description){
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

