package com.example.evenementsetudiants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class  ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<ActivityModel> list;
    private Context context;
    private DatabaseHelper db;
    private String email;

    public ActivityAdapter(Context context, List<ActivityModel> list, String email) {
        this.context = context;
        this.list = list;
        this.email = email;
        this.db = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            txt = view.findViewById(R.id.txtActivity);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ActivityModel activity = list.get(position);

        holder.txt.setText(activity.getName());

        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {

                ActivityModel item = list.get(pos);

                // ❌ غير نحيدوه من DB
                db.deleteParticipation(email, item.getName());

                // ❌ ونحيدوه من list
                list.remove(pos);
                notifyItemRemoved(pos);

                Toast.makeText(context,
                        "Supprimé ❌",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}