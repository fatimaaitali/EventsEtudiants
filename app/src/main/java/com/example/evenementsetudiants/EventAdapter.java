package com.example.evenementsetudiants;

import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
        void onDeleteClick(Event event);
    }

    public EventAdapter(List<Event> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, date, location;
        Button btn;
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.eventImage);
            title = view.findViewById(R.id.eventTitle);
            date = view.findViewById(R.id.eventDate);
            location = view.findViewById(R.id.eventLocation);
            btn = view.findViewById(R.id.btnParticiper);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event e = list.get(position);

        holder.title.setText(e.getTitle());
        holder.date.setText(e.getDate());
        holder.location.setText(e.getLocation());


        Context context = holder.itemView.getContext();
        String imageUri = e.getImage();

        if (imageUri != null && !imageUri.isEmpty()) {
            try {

                if (imageUri.startsWith("android.resource://")) {
                    holder.image.setImageURI(Uri.parse(imageUri));
                }
                // Pour les URI content://
                else if (imageUri.startsWith("content://")) {
                    holder.image.setImageURI(Uri.parse(imageUri));
                }

                else if (imageUri.startsWith("/")) {

                    File file = new File(imageUri);

                    holder.image.setImageURI(Uri.fromFile(file));
                }

                else if (imageUri.matches("\\d+")) {
                    int resId = Integer.parseInt(imageUri);
                    holder.image.setImageResource(resId);
                }
                else {
                    holder.image.setImageURI(Uri.parse(imageUri));
                }
            } catch (Exception ex) {
                holder.image.setImageResource(R.drawable.conference_ai);
            }
        } else {
            holder.image.setImageResource(R.drawable.conference_ai);
        }

        SharedPreferences sp = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);

        String role = sp.getString("role", "user");
        String email = sp.getString("email", "");


        if (!role.equals("admin")) {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDelete.setEnabled(false);
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setEnabled(true);
        }

        DatabaseHelper db = new DatabaseHelper(context);
        boolean participated = db.alreadyParticipated(email, e.getTitle());

        if (participated) {
            holder.btn.setText("Participé");
            holder.btn.setEnabled(false);
        } else {
            holder.btn.setText("Participer");
            holder.btn.setEnabled(true);
        }


        holder.btn.setOnClickListener(v -> {
            if (!db.alreadyParticipated(email, e.getTitle())) {
                db.addParticipation(email, e.getTitle());
                holder.btn.setText("Participé");
                holder.btn.setEnabled(false);
                Toast.makeText(context, "Participation enregistrée 👍", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Déjà inscrit ❗", Toast.LENGTH_SHORT).show();
            }
        });


        holder.btnDelete.setOnClickListener(v -> {
            if (role.equals("admin") && listener != null) {
                listener.onDeleteClick(e);
            }
        });


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(e);
            } else {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("title", e.getTitle());
                intent.putExtra("desc", e.getDescription());
                intent.putExtra("time", e.getTime());
                intent.putExtra("date", e.getDate());
                intent.putExtra("location", e.getLocation());
                intent.putExtra("image", e.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}