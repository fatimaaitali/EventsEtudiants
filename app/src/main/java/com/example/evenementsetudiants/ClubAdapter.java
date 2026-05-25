package com.example.evenementsetudiants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {

    private List<Club> clubList;
    private OnItemClickListener listener;

    // Interface pour les clics admin
    public interface OnItemClickListener {
        void onItemClick(Club club);
        void onDeleteClick(Club club);
    }

    // Constructeur pour AdminActivity (avec listener)
    public ClubAdapter(List<Club> clubList, OnItemClickListener listener) {
        this.clubList = clubList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_club, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        Club club = clubList.get(position);
        holder.txtClubName.setText(club.getName());

        Context context = holder.itemView.getContext();

        // Charger l'image correctement
        String imageUri = club.getImage();
        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                // Essayer de charger comme URI
                if (imageUri.startsWith("content://") || imageUri.startsWith("file://") || imageUri.startsWith("android.resource://")) {
                    Glide.with(context)
                            .load(Uri.parse(imageUri))
                            .placeholder(R.drawable.robotique)
                            .error(R.drawable.robotique)
                            .into(holder.imgClub);
                } else {
                    // Essayer de charger comme resource ID
                    int resId = Integer.parseInt(imageUri);
                    holder.imgClub.setImageResource(resId);
                }
            } catch (Exception e) {
                // Si erreur, utiliser image par défaut
                holder.imgClub.setImageResource(R.drawable.robotique);
            }
        } else {
            holder.imgClub.setImageResource(R.drawable.robotique);
        }

        // Vérifier le rôle de l'utilisateur
        SharedPreferences sp = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String role = sp.getString("role", "user");

        // Pour Admin: afficher les boutons Modifier/Supprimer
        if (role.equals("admin")) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnJoin.setVisibility(View.GONE); // Cacher le bouton Rejoindre pour admin

            // Bouton Modifier
            holder.btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(club);
                }
            });

            // Bouton Supprimer
            holder.btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(club);
                }
            });
        } else {
            // Pour utilisateur normal: afficher bouton Rejoindre/Quitter
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.VISIBLE);

            String email = sp.getString("email", "");
            DatabaseHelper db = new DatabaseHelper(context);
            boolean joined = db.isInClub(email, club.getName());
            holder.btnJoin.setText(joined ? "Quitter" : "Rejoindre");

            holder.btnJoin.setOnClickListener(v -> {
                if (db.isInClub(email, club.getName())) {
                    db.leaveClub(email, club.getName());
                    holder.btnJoin.setText("Rejoindre");
                    Toast.makeText(context, "Vous avez quitté le club", Toast.LENGTH_SHORT).show();
                } else {
                    db.joinClub(email, club.getName());
                    holder.btnJoin.setText("Quitter");
                    Toast.makeText(context, "Vous avez rejoint le club", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Click sur l'item pour voir les détails
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && role.equals("admin")) {
                listener.onItemClick(club);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    // Méthode pour mettre à jour la liste
    public void updateList(List<Club> newList) {
        this.clubList = newList;
        notifyDataSetChanged();
    }

    public static class ClubViewHolder extends RecyclerView.ViewHolder {
        ImageView imgClub;
        TextView txtClubName;
        Button btnJoin;
        Button btnEdit;
        Button btnDelete;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            imgClub = itemView.findViewById(R.id.imgClub);
            txtClubName = itemView.findViewById(R.id.txtClubName);
            btnJoin = itemView.findViewById(R.id.btnJoin);
            btnEdit = itemView.findViewById(R.id.btnEditClub);
            btnDelete = itemView.findViewById(R.id.btnDeleteClub);
        }
    }
}
