package com.example.agricart;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SearchStore {
    String storeName;

    public SearchStore() {
    }

    public SearchStore(String storeName){
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }
}
