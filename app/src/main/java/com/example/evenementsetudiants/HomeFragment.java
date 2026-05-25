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
        clubList = new ArrayList<>();

        clubAdapter = new ClubAdapter(clubList, new ClubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Club club) {
                Toast.makeText(getContext(), "Club: " + club.getName(), Toast.LENGTH_SHORT).show();
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle(club.getName())
                        .setMessage("Bienvenue au " + club.getName() + "!\n\n" +
                                "Rejoignez ce club pour participer aux activités et événements.")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onDeleteClick(Club club) {
                Toast.makeText(getContext(), "Vous n'avez pas les droits pour supprimer", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerClubs.setAdapter(clubAdapter);
        db = new DatabaseHelper(getContext());

        // TEMPORAIRE: Vider les anciens clubs pour les recréer avec le bon format
        db.clearAllClubs();  // <--- AJOUTEZ CETTE LIGNE TEMPORAIREMENT

        // Add default clubs if database is empty
        Cursor c = db.getAllClubs();

        if (c.getCount() == 0) {
            // CORRECTION: Utiliser un URI valide
            db.addClubData("Club Robotique", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.robotique);
            db.addClubData("Club Musique", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.musique);
            db.addClubData("Club Sport", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.sport);
            db.addClubData("Club Théâtre", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.conference_ai);
            db.addClubData("Club Développement Web", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.devweb);
            db.addClubData("Club IA", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.ai);
            db.addClubData("Club Entreprenariat", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.entrepr);
            db.addClubData("Club Design", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.design);
            db.addClubData("Club Photographie", "android.resource://" + getContext().getPackageName() + "/" + R.drawable.photographe);
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
