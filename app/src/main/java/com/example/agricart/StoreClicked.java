package com.example.agricart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StoreClicked extends AppCompatActivity {

    TextView storeName, description;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_clicked);

        storeName = findViewById(R.id.storeName);
        description = findViewById(R.id.description);
        toolbar = findViewById(R.id.toolbar);


        Intent intent = getIntent();
        String storeNameText = intent.getExtras().getString("storeName");
        String descriptionText = intent.getExtras().getString("description");

        storeName.setText(storeNameText);
        description.setText(descriptionText);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}