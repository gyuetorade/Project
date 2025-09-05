// chicha/MainActivity.java
package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(v ->
                startActivity(new Intent(this, MessagingActivity.class))
        );

        loadFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment f = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) f = new HomeFragment();
            else if (id == R.id.nav_search) f = new SearchFragment();
            else if (id == R.id.nav_orders) f = new OrdersFragment();
            else if (id == R.id.nav_settings) f = new SettingsFragment();
            else if (id == R.id.nav_more) f = new MoreFragment();
            return loadFragment(f);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment == null) return false;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        return true;
    }
}
