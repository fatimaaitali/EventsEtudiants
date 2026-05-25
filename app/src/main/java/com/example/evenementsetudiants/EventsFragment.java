package com.example.evenementsetudiants;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Event> list;
    EventAdapter adapter;
    DatabaseHelper db;
    String currentRole;

    public EventsFragment() {
        super(R.layout.fragment_events);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sp = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);

        sp.edit().putString("role", "user").apply();
        currentRole = sp.getString("role", "user");
        String email = sp.getString("email", "");

        Toast.makeText(getContext(), "Rôle forcé: " + currentRole, Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.recyclerEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        adapter = new EventAdapter(list, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                if (getContext() != null) {
                    Intent intent = new Intent(getContext(), EventDetailsActivity.class);
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
                Toast.makeText(getContext(), "Vous n'avez pas les droits pour supprimer", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        db = new DatabaseHelper(getContext());
        db.fixAllEventImages(getContext());

        Cursor c = db.getAllEvents();

        if (c.getCount() == 0) {
            String packageName = getContext().getPackageName();

            db.addEvent(
                    "Conférence IA - UMP",
                    "2026-09-20",
                    "15:00",
                    "Faculté des Sciences Oujda",
                    "Conférence sur l'intelligence artificielle et ses applications",
                    "android.resource://" + packageName + "/" + R.drawable.conference_ai
            );

            db.addEvent(
                    "Forum Entreprises",
                    "2026-10-22",
                    "09:00",
                    "ENCG Oujda",
                    "Rencontre entre étudiants et entreprises partenaires",
                    "android.resource://" + packageName + "/" + R.drawable.forum_entreprise
            );

            db.addEvent(
                    "Atelier Java Android",
                    "2026-08-25",
                    "14:00",
                    "EST Oujda",
                    "Atelier pratique sur le développement Android",
                    "android.resource://" + packageName + "/" + R.drawable.atelier_java_android
            );

            db.addEvent(
                    "Journée Sportive",
                    "2026-09-28",
                    "10:00",
                    "Stade universitaire UMP",
                    "Compétitions sportives entre étudiants",
                    "android.resource://" + packageName + "/" + R.drawable.journee_sport
            );

            db.addEvent(
                    "Pièce de théâtre",
                    "2026-12-06",
                    "18:00",
                    "Théâtre universitaire Oujda",
                    "Spectacle culturel organisé par les étudiants",
                    "android.resource://" + packageName + "/" + R.drawable.piece_theatre
            );
        }

        c.close();
        loadData();
    }

    private void loadData() {
        Cursor cursor = db.getAllEvents();
        list.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String location = cursor.getString(4);
                String description = cursor.getString(5);
                String image = cursor.getString(6);

                if (title != null && !title.isEmpty()) {
                    Event event = new Event(
                            id,
                            title,
                            date,
                            time,
                            location,
                            description,
                            image
                    );

                    boolean exists = false;
                    for (Event e : list) {
                        if (e.getId() == id) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        list.add(event);
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
