package com.example.evenementsetudiants;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminEventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAddEvent;
    ArrayList<Event> list;

    AdminEventAdapter adapter;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_events);
        getSupportActionBar().setTitle("Admin - Events");
        recyclerView = findViewById(R.id.recyclerAdminEvents);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        list = new ArrayList<>();

        adapter = new AdminEventAdapter(
                this,
                list
        );

        recyclerView.setAdapter(adapter);

        db = new DatabaseHelper(this);

        loadEvents();
        btnAddEvent = findViewById(R.id.btnAddEvent);

        btnAddEvent.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEventActivity.class));
        });
    }

    private void loadEvents() {

        Cursor cursor = db.getAllEvents();



        list.clear();

        if (cursor != null && cursor.moveToFirst()) {

            do {
                Event event = new Event(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );

                list.add(event);

            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }
    @Override

    protected void onResume() {
        super.onResume();
        loadEvents(); // refresh data
    }
}