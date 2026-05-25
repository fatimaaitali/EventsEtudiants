package com.example.evenementsetudiants;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminClubsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Club> list;
    AdminClubAdapter adapter;
    DatabaseHelper db;
    Button btnAddClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_clubs);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin - Clubs");
        }

        recyclerView = findViewById(R.id.recyclerAdminClubs);
        btnAddClub = findViewById(R.id.btnAddClub);

        db = new DatabaseHelper(this);

        list = new ArrayList<>();
        adapter = new AdminClubAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadClubs();

        btnAddClub.setOnClickListener(v -> {
            startActivity(new Intent(this, AddClubActivity.class));
        });
    }

    private void loadClubs() {

        Cursor cursor = db.getAllClubs();

        list.clear();

        if (cursor != null) {

            while (cursor.moveToNext()) {

                Club club = new Club(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );

                list.add(club);
            }

            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClubs();
    }
}