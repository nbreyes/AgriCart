package com.example.agricart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.example.agricart.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    DrawerLayout DrawerLayout;
    NavigationView SideMenu;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        changeVisibleFragment(new HomeFragment());

        DrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        SideMenu = (NavigationView) findViewById(R.id.SideMenu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toggle = new ActionBarDrawerToggle(this, DrawerLayout, toolbar, R.string.menu_open, R.string.menu_close);
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.seller:
                        break;

                    case R.id.delete:
                        break;

                    case R.id.logout:
                        break;
                }
                return true;
            }
        });
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