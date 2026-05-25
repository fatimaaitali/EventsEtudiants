package com.example.evenementsetudiants;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> list;
    private NotificationAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        list = new ArrayList<>();
        adapter = new NotificationAdapter(list);

        recyclerView.setAdapter(adapter);


        try (Cursor checkCursor = db.getNotifications()) {
            if (checkCursor == null || checkCursor.getCount() == 0) {
                db.addNotification("Atelier informatique demain");
                db.addNotification("Votre inscription est confirmée");
            }
        }

        loadNotifications();
    }

    private void loadNotifications() {
        Cursor cursor = db.getNotifications();
        
        int oldSize = list.size();
        if (oldSize > 0) {
            list.clear();
            adapter.notifyItemRangeRemoved(0, oldSize);
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String message = cursor.getString(1); // Assuming index 1 is the message column
                list.add(message);
            }
            cursor.close();
        }

        if (!list.isEmpty()) {
            adapter.notifyItemRangeInserted(0, list.size());
        }
    }
}
