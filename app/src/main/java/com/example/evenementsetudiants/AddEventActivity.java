package com.example.evenementsetudiants;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    EditText editTitle, editDate, editTime, editLocation, editDescription, editImage;
    Button btnSave;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getSupportActionBar().setTitle("Add Event");

        db = new DatabaseHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editLocation = findViewById(R.id.editLocation);
        editDescription = findViewById(R.id.editDescription);
        editImage = findViewById(R.id.editImage);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {

            String title = editTitle.getText().toString();
            String date = editDate.getText().toString();
            String time = editTime.getText().toString();
            String location = editLocation.getText().toString();
            String desc = editDescription.getText().toString();
            String imageStr = editImage.getText().toString();

            if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {

                Toast.makeText(this,
                        "Fill required fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // default image if empty
            if (imageStr.isEmpty()) {
                imageStr = "android.resource://" +
                        getPackageName() +
                        "/" +
                        R.drawable.event1;
            }

            db.addEvent(title, date, time, location, desc, imageStr);

            Toast.makeText(this,
                    "Event added ✔",
                    Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}