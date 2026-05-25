package com.example.evenementsetudiants;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClubProfileAdapter
        extends RecyclerView.Adapter<ClubProfileAdapter.MyViewHolder> {

    Context context;
    ArrayList<ClubModel> list;
    String email;

    DatabaseHelper db;

    public ClubProfileAdapter(Context context,
                              ArrayList<ClubModel> list,
                              String email) {

        this.context = context;
        this.list = list;
        this.email = email;

        db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_activity,
                        parent,
                        false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        ClubModel club = list.get(position);

        holder.txt.setText(club.getName());

        holder.btn.setText("Quitter");

        holder.btn.setOnClickListener(v -> {

            db.leaveClub(email, club.getName());

            list.remove(position);

            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView txt;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt = itemView.findViewById(R.id.txtActivity);
            btn = itemView.findViewById(R.id.btnDelete);
        }
    }
}