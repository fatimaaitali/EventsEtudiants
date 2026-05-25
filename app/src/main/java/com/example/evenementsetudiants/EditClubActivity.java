package com.example.evenementsetudiants;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditClubActivity extends AppCompatActivity {

    EditText editName, editImage;
    Button btnUpdate;

    DatabaseHelper db;
    int clubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_club);

        getSupportActionBar().setTitle("Edit Club");

        db = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editImage = findViewById(R.id.editImage);
        btnUpdate = findViewById(R.id.btnUpdate);

        // 📥 get id from intent
        clubId = getIntent().getIntExtra("id", -1);

        loadClubData();

        btnUpdate.setOnClickListener(v -> {

            String name = editName.getText().toString();
            String imageStr = editImage.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this,
                        "Enter club name",
                        Toast.LENGTH_SHORT).show();
                return;
            }



            if (imageStr.isEmpty()) {
                imageStr = "android.resource://" +
                        getPackageName() + "/" + R.drawable.ic_launcher_foreground;
            }

            db.updateClub(clubId, name, imageStr);

            Toast.makeText(this,
                    "Club updated ✔",
                    Toast.LENGTH_SHORT).show();

            finish(); // رجوع ل AdminClubsActivity
        });
    }

    private void loadClubData() {

        Cursor cursor = db.getAllClubs();

        if (cursor != null && cursor.moveToFirst()) {

            do {
                if (cursor.getInt(0) == clubId) {

                    editName.setText(cursor.getString(1));
                    editImage.setText(cursor.getString(2));

                    break;
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
    }
}