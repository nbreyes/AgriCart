package com.example.agricart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Seller extends AppCompatActivity {

    Toolbar toolbar;
    EditText storeName, description;
    Button submit;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        storeName = (EditText) findViewById(R.id.storeName);
        description = (EditText) findViewById(R.id.description);
        submit = (Button) findViewById(R.id.submit);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void InsertData() {
        String nameStore = storeName.getText().toString();
        String storeDescription = description.getText().toString();
        String id = databaseReference.push().getKey();

        Store store = new Store(nameStore, storeDescription);
        databaseReference.child("Store").child(id).setValue(store)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Seller.this, "Store Created!", Toast.LENGTH_LONG);
                        }
                    }
                });
    }
}