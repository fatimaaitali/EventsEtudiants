package com.example.evenementsetudiants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminClubAdapter extends RecyclerView.Adapter<AdminClubAdapter.ViewHolder> {

    Context context;
    List<Club> list;
    DatabaseHelper db;

    public AdminClubAdapter(Context context, List<Club> list) {
        this.context = context;
        this.list = list;
        db = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_club, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Club club = list.get(position);

        holder.txtName.setText(club.getName());

        // DELETE
        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            db.deleteClub(list.get(pos).getId());

            list.remove(pos);
            notifyItemRemoved(pos);

            Toast.makeText(context,
                    "Club supprimé",
                    Toast.LENGTH_SHORT).show();
        });

        // EDIT
        holder.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(context, EditClubActivity.class);
            intent.putExtra("id", club.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
