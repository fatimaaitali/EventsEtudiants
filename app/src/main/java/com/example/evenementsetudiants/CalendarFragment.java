package com.example.evenementsetudiants;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    CalendarView calendarView;
    RecyclerView recyclerView;

    DatabaseHelper db;
    ArrayList<Event> list;
    EventAdapter adapter;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerDayEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new DatabaseHelper(getContext());
        list = new ArrayList<>();


        adapter = new EventAdapter(list, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                // Open event details when clicked
                if (getContext() != null) {
                    android.content.Intent intent = new android.content.Intent(getContext(), EventDetailsActivity.class);
                    intent.putExtra("title", event.getTitle());
                    intent.putExtra("desc", event.getDescription());
                    intent.putExtra("time", event.getTime());
                    intent.putExtra("date", event.getDate());
                    intent.putExtra("location", event.getLocation());
                    intent.putExtra("image", event.getImage());
                    getContext().startActivity(intent);
                }
            }

            @Override
            public void onDeleteClick(Event event) {

                Toast.makeText(getContext(), "Suppression non disponible ici", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);


        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String date = String.format("%04d-%02d-%02d",
                    year, month + 1, dayOfMonth);
            loadEventsByDate(date);
        });


        loadEventsByDate(getTodayDate());
    }


    private String getTodayDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return String.format("%04d-%02d-%02d",
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH));
    }


    private void loadEventsByDate(String date) {
        list.clear();

        Cursor cursor = db.getAllEvents();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String eventDate = cursor.getString(2);
                String time = cursor.getString(3);
                String location = cursor.getString(4);
                String description = cursor.getString(5);
                String image = cursor.getString(6);

                // Compare dates
                if (eventDate.equals(date)) {
                    Event event = new Event(id, title, eventDate, time, location, description, image);
                    list.add(event);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter.notifyDataSetChanged();

        // Show message if no events
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "Aucun événement pour cette date", Toast.LENGTH_SHORT).show();
        }
    }
}
