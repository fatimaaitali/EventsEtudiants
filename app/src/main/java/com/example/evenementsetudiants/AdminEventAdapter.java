package com.example.evenementsetudiants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminEventAdapter
        extends RecyclerView.Adapter<AdminEventAdapter.ViewHolder> {

    Context context;

    List<Event> list;

    DatabaseHelper db;

    public AdminEventAdapter(Context context, List<Event> list) {

        this.context = context;
        this.list = list;

        db = new DatabaseHelper(context);
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitle;

        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);

            btnEdit = itemView.findViewById(R.id.btnEdit);

            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_admin_event,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Event event = list.get(position);

        holder.txtTitle.setText(event.getTitle());

        // DELETE
        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {

                Event currentEvent = list.get(pos);

                db.deleteEvent(currentEvent.getId());

                list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, list.size());

                Toast.makeText(context,
                        "Event supprimé",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // EDIT
        holder.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra("id", event.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
