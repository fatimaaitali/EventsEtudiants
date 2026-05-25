package com.example.evenementsetudiants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.net.Uri;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private RecyclerView recyclerViewClubs;

    private ArrayList<ClubModel> clubsList;
    private ImageView image;
    private TextView name, filiere;
    private RecyclerView recyclerView;
    private Button btnLogout;

    private ArrayList<ActivityModel> activitiesList;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = view.findViewById(R.id.profileImage);
        name = view.findViewById(R.id.profileName);
        filiere = view.findViewById(R.id.profileFiliere);

        recyclerView = view.findViewById(R.id.recyclerViewActivities);

        recyclerViewClubs =
                view.findViewById(R.id.recyclerViewClubs);
        btnLogout = view.findViewById(R.id.btnLogout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewClubs.setLayoutManager(
                new LinearLayoutManager(getContext()));

        activitiesList = new ArrayList<>();
        clubsList = new ArrayList<>();

        SharedPreferences sp = requireActivity()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE);

        String email = sp.getString("email", "");

        DatabaseHelper db = new DatabaseHelper(getContext());


        if (!email.isEmpty()) {

            Cursor userCursor = db.getUser(email);

            if (userCursor != null && userCursor.moveToFirst()) {

                name.setText(userCursor.getString(1));
                filiere.setText(userCursor.getString(3));

                String image = userCursor.getString(5);

                if (image != null && !image.isEmpty()) {

                    try {

                        File imgFile = new File(image);

                        if (imgFile.exists()) {

                            Uri uri = Uri.fromFile(imgFile);

                            this.image.setImageURI(uri);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                userCursor.close();
            }


            Cursor cursor = db.getUserActivities(email);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    ActivityModel a = new ActivityModel(
                            cursor.getString(0)
                    );

                    activitiesList.add(a);
                }
                cursor.close();
            }


            ActivityAdapter adapter =
                    new ActivityAdapter(getContext(), activitiesList, email);

            recyclerView.setAdapter(adapter);
            Cursor clubCursor =
                    db.getUserClubs(email);

            if (clubCursor != null) {

                while (clubCursor.moveToNext()) {

                    ClubModel club =
                            new ClubModel(
                                    clubCursor.getString(0)
                            );

                    clubsList.add(club);
                }

                clubCursor.close();
            }

            ClubProfileAdapter clubAdapter =
                    new ClubProfileAdapter(
                            getContext(),
                            clubsList,
                            email);

            recyclerViewClubs.setAdapter(clubAdapter);
        }


        btnLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            requireActivity().finish();
        });
    }
}