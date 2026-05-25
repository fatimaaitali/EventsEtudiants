package com.example.evenementsetudiants;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EventDetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView title, desc, date, time, location;
    Button btnInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// مهم: نحيدو title الافتراضي باش ما يغطيش
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

// السهم يرجع
        toolbar.setNavigationOnClickListener(v -> finish());

        image = findViewById(R.id.eventImage);
        title = findViewById(R.id.eventTitle);
        desc = findViewById(R.id.eventDesc);
        date = findViewById(R.id.eventDate);
        time = findViewById(R.id.eventTime);
        location = findViewById(R.id.eventLocation);
        btnInscription = findViewById(R.id.btnInscription);

        // 🔵 Get data from Intent
        Intent intent = getIntent();

        String t = intent.getStringExtra("title");
        String d = intent.getStringExtra("desc");
        String da = intent.getStringExtra("date");
        String ti = intent.getStringExtra("time");
        String lo = intent.getStringExtra("location");
        int img = intent.getIntExtra("image", 0);

        // 🔵 Set data using string resources with placeholders
        title.setText(t);
        desc.setText(d);
        date.setText(getString(R.string.event_date_format, da));
        time.setText(getString(R.string.event_time_format, ti));
        location.setText(getString(R.string.event_location_format, lo));
        image.setImageResource(img);

        // 🔵 Button click
        btnInscription.setOnClickListener(v -> Toast.makeText(this,
                R.string.registration_success,
                Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

}
