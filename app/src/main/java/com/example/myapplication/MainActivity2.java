package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.MenuItem;

import com.example.myapplication.databinding.ActivityMain2Binding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity2 extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private ActivityMain2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView
                .setOnItemSelectedListener(this);
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Home firstFragment = new Home();
        Analytics secondFragment = new Analytics();
        History thirdFragment = new History();
            if (item.getItemId()==R.id.home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, firstFragment)
                        .commit();
                return true;
            }

            if (item.getItemId()==R.id.analytics) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, secondFragment)
                        .commit();
                return true;
            }

            if (item.getItemId()==R.id.history) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, thirdFragment)
                        .commit();
                return true;
            }

        return false;
    }
}