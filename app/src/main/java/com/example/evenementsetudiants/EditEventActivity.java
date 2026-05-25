package com.example.evenementsetudiants;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditEventActivity extends AppCompatActivity {

    EditText editTitle, editDate, editTime, editLocation, editDescription;
    Button btnUpdate;

    DatabaseHelper db;
    int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        getSupportActionBar().setTitle("Edit Event");

        db = new DatabaseHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editLocation = findViewById(R.id.editLocation);
        editDescription = findViewById(R.id.editDescription);
        btnUpdate = findViewById(R.id.btnUpdate);

        eventId = getIntent().getIntExtra("id", -1);

        loadEventData();

        btnUpdate.setOnClickListener(v -> {

            db.updateEventFull(
                    eventId,
                    editTitle.getText().toString(),
                    editDate.getText().toString(),
                    editTime.getText().toString(),
                    editLocation.getText().toString(),
                    editDescription.getText().toString()
            );

            Toast.makeText(this,
                    "Event updated ✔",
                    Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    private void loadEventData() {

        Cursor cursor = db.getAllEvents();

        while (cursor.moveToNext()) {

            if (cursor.getInt(0) == eventId) {

                editTitle.setText(cursor.getString(1));
                editDate.setText(cursor.getString(2));
                editTime.setText(cursor.getString(3));
                editLocation.setText(cursor.getString(4));
                editDescription.setText(cursor.getString(5));

                break;
            }
        }

        cursor.close();
    }
}