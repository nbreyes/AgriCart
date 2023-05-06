package com.example.agricart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.agricart.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        changeVisibleFragment(new HomeFragment());

        mainBinding.BottomNavView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    changeVisibleFragment(new HomeFragment());
                    break;
                case R.id.search:
                    changeVisibleFragment(new SearchFragment());
                    break;
                case R.id.history:
                    changeVisibleFragment(new HistoryFragment());
                    break;
            }

            return true;
        });
    }

    public void changeVisibleFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayout, fragment);
        fragmentTransaction.commit();
    }
}