package com.example.agricart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.agricart.R.id;
import com.example.agricart.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    DrawerLayout DrawerLayout;
    NavigationView SideMenu;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    TextView username;
    FirebaseUser user;
    FirebaseAuth auth;

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

        View view = SideMenu.getHeaderView(0);
        username = view.findViewById(R.id.username);
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        user = auth.getCurrentUser();
        if (user != null) {
            username.setText(user.getEmail());
        }

        user = FirebaseAuth.getInstance().getCurrentUser();


        toggle = new ActionBarDrawerToggle(this, DrawerLayout, toolbar, R.string.menu_open, R.string.menu_close);
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.seller:
                        Intent intent = new Intent(getApplicationContext(),Seller.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.recommendation:
                       Intent secondIntent = new Intent(getApplicationContext(),Recommendation.class);
                       startActivity(secondIntent);
                       finish();
                        break;

                    case R.id.delete:
                        DrawerLayout.closeDrawer(GravityCompat.START);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Delete Account");
                        dialog.setMessage("Deleting this account cannot be undone. You will not be able to use the app using this account.");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Account Deleted",Toast.LENGTH_LONG).show();
                                            Intent thirdIntent = new Intent(getApplicationContext(),Login.class);
                                            startActivity(thirdIntent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "Delete Unsuccessful!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        DrawerLayout.closeDrawer(GravityCompat.START);
                        Intent fourthIntent = new Intent(getApplicationContext(),Login.class);
                        startActivity(fourthIntent);
                        finish();
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