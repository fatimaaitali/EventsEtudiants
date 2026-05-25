package com.example.evenementsetudiants;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerClubs;
    private List<Club> clubList;
    private ClubAdapter clubAdapter;
    private DatabaseHelper db;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerClubs = view.findViewById(R.id.recyclerClubs);
        recyclerClubs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerClubs = view.findViewById(R.id.recyclerClubs);
        recyclerClubs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        clubList = new ArrayList<>();

        // ✅ ClubAdapter with click listener (without ClubDetailsActivity)
        clubAdapter = new ClubAdapter(clubList, new ClubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Club club) {
                // Show club info in a Toast
                Toast.makeText(getContext(), "Club: " + club.getName(), Toast.LENGTH_SHORT).show();

                // Show an AlertDialog with club info
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle(club.getName())
                        .setMessage("Bienvenue au " + club.getName() + "!\n\n" +
                                "Rejoignez ce club pour participer aux activités et événements.")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onDeleteClick(Club club) {
                // Users cannot delete clubs, only admin
                Toast.makeText(getContext(), "Vous n'avez pas les droits pour supprimer", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerClubs.setAdapter(clubAdapter);
        db = new DatabaseHelper(getContext());

        // Add default clubs if database is empty
        Cursor c = db.getAllClubs();

        if (c.getCount() == 0) {
            db.addClubData("Club Robotique", String.valueOf(R.drawable.robotique));
            db.addClubData("Club Musique", String.valueOf(R.drawable.musique));
            db.addClubData("Club Sport", String.valueOf(R.drawable.sport));
            db.addClubData("Club Théâtre", String.valueOf(R.drawable.conference_ai));
            db.addClubData("Club Développement Web", String.valueOf(R.drawable.devweb));
            db.addClubData("Club IA", String.valueOf(R.drawable.ai));
            db.addClubData("Club Entrepreneuriat", String.valueOf(R.drawable.entrepr));
            db.addClubData("Club Design", String.valueOf(R.drawable.design));
            db.addClubData("Club Photographie", String.valueOf(R.drawable.photographe));
        }

        c.close();
        loadClubs();
    }

    private void loadClubs() {
        Cursor cursor = db.getAllClubs();
        clubList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Club club = new Club(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                clubList.add(club);
            } while (cursor.moveToNext());
            cursor.close();
        }

        clubAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClubs();
    }
}
