package com.example.evenementsetudiants;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    EditText etTitle, etDate, etTime, etLocation, etDescription;
    Button btnAddEvent, btnUpdateEvent, btnDeleteEvent, btnViewEvents;
    ImageView imgEvent;

    EditText etClubName;
    Button btnAddClub, btnUpdateClub, btnDeleteClub, btnViewClubs;
    ImageView imgClub;

    RecyclerView recyclerEvents, recyclerClubs;

    private static final int PICK_EVENT_IMAGE = 1;
    private static final int PICK_CLUB_IMAGE = 2;

    String eventImageUri = "";
    String clubImageUri = "";
    int selectedEventId = -1;
    int selectedClubId = -1;

    DatabaseHelper db;
    EventAdapter eventAdapter;
    ClubAdapter clubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initializeViews();
        setupDatabase();
        setupClickListeners();
        loadEventList();
        loadClubList();
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);
        imgEvent = findViewById(R.id.imgEvent);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnUpdateEvent = findViewById(R.id.btnUpdateEvent);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);
        btnViewEvents = findViewById(R.id.btnViewEvents);
        recyclerEvents = findViewById(R.id.recyclerEvents);

        etClubName = findViewById(R.id.etClubName);
        imgClub = findViewById(R.id.imgClub);
        btnAddClub = findViewById(R.id.btnAddClub);
        btnUpdateClub = findViewById(R.id.btnUpdateClub);
        btnDeleteClub = findViewById(R.id.btnDeleteClub);
        btnViewClubs = findViewById(R.id.btnViewClubs);
        recyclerClubs = findViewById(R.id.recyclerClubs);
    }

    private void setupDatabase() {
        db = new DatabaseHelper(this);
        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerClubs.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnAddEvent.setOnClickListener(v -> addEvent());
        btnUpdateEvent.setOnClickListener(v -> updateEvent());
        btnDeleteEvent.setOnClickListener(v -> deleteSelectedEvent());
        btnViewEvents.setOnClickListener(v -> toggleEventList());

        btnAddClub.setOnClickListener(v -> addClub());
        btnUpdateClub.setOnClickListener(v -> updateClub());
        btnDeleteClub.setOnClickListener(v -> deleteSelectedClub());
        btnViewClubs.setOnClickListener(v -> toggleClubList());

        imgEvent.setOnClickListener(v -> pickImage(PICK_EVENT_IMAGE));
        imgClub.setOnClickListener(v -> pickImage(PICK_CLUB_IMAGE));
    }

    private void addEvent() {
        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Titre et Date sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (eventImageUri.isEmpty()) {
            eventImageUri = "android.resource://" + getPackageName() + "/" + R.drawable.conference_ai;
        }

        db.addEvent(title, date, time.isEmpty() ? "00:00" : time,
                location.isEmpty() ? "Non spécifié" : location,
                description.isEmpty() ? "Aucune description" : description,
                eventImageUri);

        Toast.makeText(this, "Événement ajouté ✅", Toast.LENGTH_SHORT).show();
        clearEventForm();
        loadEventList();
    }

    private void updateEvent() {
        if (selectedEventId == -1) {
            Toast.makeText(this, "Sélectionnez un événement à modifier", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Titre et Date sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageToSave = eventImageUri;
        if (imageToSave == null || imageToSave.isEmpty()) {
            Cursor cursor = db.getAllEvents();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == selectedEventId) {
                        imageToSave = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                        break;
                    }
                }
                cursor.close();
            }
        }

        if (imageToSave == null || imageToSave.isEmpty()) {
            imageToSave = "android.resource://" + getPackageName() + "/" + R.drawable.conference_ai;
        }

        android.database.sqlite.SQLiteDatabase sqldb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("time", time);
        values.put("location", location);
        values.put("description", description);
        values.put("image", imageToSave);

        sqldb.update("events", values, "id=?", new String[]{String.valueOf(selectedEventId)});
        sqldb.close();

        Toast.makeText(this, "Événement modifié avec succès ✅", Toast.LENGTH_SHORT).show();
        clearEventForm();
        loadEventList();
    }

    private void deleteSelectedEvent() {
        if (selectedEventId == -1) {
            Toast.makeText(this, "Sélectionnez d'abord un événement à supprimer", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            title = "cet événement";
        }

        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'événement")
                .setMessage("Voulez-vous vraiment supprimer \"" + title + "\" ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    db.deleteEvent(selectedEventId);
                    Toast.makeText(this, "Événement supprimé ✅", Toast.LENGTH_SHORT).show();
                    clearEventForm();
                    loadEventList();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void loadEventList() {
        Cursor cursor = db.getAllEvents();
        List<Event> events = convertEvents(cursor);
        // Adapter sans icône de suppression
        eventAdapter = new EventAdapter(events, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                populateEventForm(event);
            }

            @Override
            public void onDeleteClick(Event event) {
                // Ne rien faire - suppression uniquement depuis le formulaire
            }
        });
        recyclerEvents.setAdapter(eventAdapter);
    }

    private void populateEventForm(Event event) {
        selectedEventId = event.getId();
        etTitle.setText(event.getTitle());
        etDate.setText(event.getDate());
        etTime.setText(event.getTime());
        etLocation.setText(event.getLocation());
        etDescription.setText(event.getDescription());
        eventImageUri = event.getImage();

        if (eventImageUri != null && !eventImageUri.isEmpty()) {
            try {
                imgEvent.setImageURI(Uri.parse(eventImageUri));
            } catch (Exception e) {
                imgEvent.setImageResource(R.drawable.conference_ai);
            }
        } else {
            imgEvent.setImageResource(R.drawable.conference_ai);
        }

        Toast.makeText(this, "Sélectionné: " + event.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void clearEventForm() {
        selectedEventId = -1;
        etTitle.setText("");
        etDate.setText("");
        etTime.setText("");
        etLocation.setText("");
        etDescription.setText("");
        eventImageUri = "";
        imgEvent.setImageResource(R.drawable.conference_ai);
    }

    private void addClub() {
        String clubName = etClubName.getText().toString().trim();

        if (clubName.isEmpty()) {
            Toast.makeText(this, "Nom du club est obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        if (clubImageUri.isEmpty()) {
            clubImageUri = "android.resource://" + getPackageName() + "/" + R.drawable.robotique;
        }

        db.addClubData(clubName, clubImageUri);

        Toast.makeText(this, "Club ajouté ✅", Toast.LENGTH_SHORT).show();
        clearClubForm();
        loadClubList();
    }

    private void updateClub() {
        if (selectedClubId == -1) {
            Toast.makeText(this, "Sélectionnez un club à modifier", Toast.LENGTH_SHORT).show();
            return;
        }

        String clubName = etClubName.getText().toString().trim();

        if (clubName.isEmpty()) {
            Toast.makeText(this, "Nom du club est obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageToSave = clubImageUri;
        if (imageToSave == null || imageToSave.isEmpty()) {
            Cursor cursor = db.getAllClubs();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == selectedClubId) {
                        imageToSave = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                        break;
                    }
                }
                cursor.close();
            }
        }

        if (imageToSave == null || imageToSave.isEmpty()) {
            imageToSave = "android.resource://" + getPackageName() + "/" + R.drawable.robotique;
        }

        db.updateClub(selectedClubId, clubName, imageToSave);

        Toast.makeText(this, "Club modifié avec succès ✅", Toast.LENGTH_SHORT).show();
        clearClubForm();
        loadClubList();
    }

    private void deleteSelectedClub() {
        if (selectedClubId == -1) {
            Toast.makeText(this, "Sélectionnez d'abord un club à supprimer", Toast.LENGTH_SHORT).show();
            return;
        }

        String clubName = etClubName.getText().toString().trim();
        if (clubName.isEmpty()) {
            clubName = "ce club";
        }

        new AlertDialog.Builder(this)
                .setTitle("Supprimer le club")
                .setMessage("Voulez-vous vraiment supprimer le club \"" + clubName + "\" ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    db.deleteClub(selectedClubId);
                    Toast.makeText(this, "Club supprimé ✅", Toast.LENGTH_SHORT).show();
                    clearClubForm();
                    loadClubList();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void loadClubList() {
        Cursor cursor = db.getAllClubs();
        List<Club> clubs = convertClubs(cursor);
        // Adapter sans icône de suppression
        clubAdapter = new ClubAdapter(clubs, new ClubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Club club) {
                populateClubForm(club);
            }

            @Override
            public void onDeleteClick(Club club) {
                // Ne rien faire - suppression uniquement depuis le formulaire
            }
        });
        recyclerClubs.setAdapter(clubAdapter);
    }

    private void populateClubForm(Club club) {
        selectedClubId = club.getId();
        etClubName.setText(club.getName());
        clubImageUri = club.getImage();

        if (clubImageUri != null && !clubImageUri.isEmpty()) {
            try {
                imgClub.setImageURI(Uri.parse(clubImageUri));
            } catch (Exception e) {
                imgClub.setImageResource(R.drawable.robotique);
            }
        } else {
            imgClub.setImageResource(R.drawable.robotique);
        }

        Toast.makeText(this, "Sélectionné: " + club.getName(), Toast.LENGTH_SHORT).show();
    }

    private void clearClubForm() {
        selectedClubId = -1;
        etClubName.setText("");
        clubImageUri = "";
        imgClub.setImageResource(R.drawable.robotique);
    }

    private void toggleEventList() {
        if (recyclerEvents.getVisibility() == View.GONE) {
            loadEventList();
            recyclerEvents.setVisibility(View.VISIBLE);
            btnViewEvents.setText("Cacher Events");
        } else {
            recyclerEvents.setVisibility(View.GONE);
            btnViewEvents.setText("Voir Events");
        }
    }

    private void toggleClubList() {
        if (recyclerClubs.getVisibility() == View.GONE) {
            loadClubList();
            recyclerClubs.setVisibility(View.VISIBLE);
            btnViewClubs.setText("Cacher Clubs");
        } else {
            recyclerClubs.setVisibility(View.GONE);
            btnViewClubs.setText("Voir Clubs");
        }
    }

    private void pickImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    private List<Event> convertEvents(Cursor cursor) {
        List<Event> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Event e = new Event();
                e.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                e.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                e.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                e.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                e.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                e.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                e.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                list.add(e);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    private List<Club> convertClubs(Cursor cursor) {
        List<Club> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Club c = new Club();
                c.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                c.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                list.add(c);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) return;

            try {
                getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            if (requestCode == PICK_EVENT_IMAGE) {
                eventImageUri = uri.toString();
                imgEvent.setImageURI(uri);
            } else if (requestCode == PICK_CLUB_IMAGE) {
                clubImageUri = uri.toString();
                imgClub.setImageURI(uri);
            }
        }
    }
}