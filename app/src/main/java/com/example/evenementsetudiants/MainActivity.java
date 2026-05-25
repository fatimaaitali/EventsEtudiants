package com.example.evenementsetudiants;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;




public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    Fragment homeFragment = new HomeFragment();
    Fragment eventsFragment = new EventsFragment();
    Fragment calendarFragment = new CalendarFragment();
    Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        loadFragment(homeFragment);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(homeFragment);
            }
            else if (id == R.id.nav_events) {
                loadFragment(eventsFragment);
            }
            else if (id == R.id.nav_calendar) {
                loadFragment(calendarFragment);
            }
            else if (id == R.id.nav_profile) {
                loadFragment(profileFragment);
            }

            return true; // مهم
        });

    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }
    }
}
