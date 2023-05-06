package com.example.agricart;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Seller extends AppCompatActivity {

    Toolbar toolbar;
    EditText storeName, description;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        storeName = (EditText) findViewById(R.id.storeName);
        description = (EditText) findViewById(R.id.description);
        submit = (Button) findViewById(R.id.submit);

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