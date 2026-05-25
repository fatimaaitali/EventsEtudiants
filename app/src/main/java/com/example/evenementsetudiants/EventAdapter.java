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
import com.bumptech.glide.Glide;

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

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.eventImage);
            title = view.findViewById(R.id.eventTitle);
            date = view.findViewById(R.id.eventDate);
            location = view.findViewById(R.id.eventLocation);
            btn = view.findViewById(R.id.btnParticiper);
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

        // Chargement de l'image avec Glide
        Context context = holder.itemView.getContext();
        String imageUri = e.getImage();

        loadEventImage(context, imageUri, holder.image);

        SharedPreferences sp = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String role = sp.getString("role", "user");
        String email = sp.getString("email", "");

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

    private void loadEventImage(Context context, String imageUri, ImageView imageView) {
        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                // Utiliser Glide pour charger l'image
                if (imageUri.startsWith("android.resource://") ||
                        imageUri.startsWith("content://") ||
                        imageUri.startsWith("file://")) {

                    Glide.with(context)
                            .load(Uri.parse(imageUri))
                            .placeholder(R.drawable.conference_ai)
                            .error(R.drawable.conference_ai)
                            .into(imageView);
                }
                // Si c'est un chemin de fichier
                else if (imageUri.startsWith("/")) {
                    Glide.with(context)
                            .load(new File(imageUri))
                            .placeholder(R.drawable.conference_ai)
                            .error(R.drawable.conference_ai)
                            .into(imageView);
                }
                // Si c'est un ID de ressource
                else if (imageUri.matches("\\d+")) {
                    int resId = Integer.parseInt(imageUri);
                    imageView.setImageResource(resId);
                }
                // Sinon, essayer directement
                else {
                    Glide.with(context)
                            .load(imageUri)
                            .placeholder(R.drawable.conference_ai)
                            .error(R.drawable.conference_ai)
                            .into(imageView);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                imageView.setImageResource(R.drawable.conference_ai);
            }
        } else {
            imageView.setImageResource(R.drawable.conference_ai);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}