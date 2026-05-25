package com.example.evenementsetudiants;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

public class AddClubActivity extends AppCompatActivity {

    EditText editName;
    Button btnSave;
    ImageView imgClub;
    String clubImageUri = "";
    private static final int PICK_IMAGE = 1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);

        getSupportActionBar().setTitle("Add Club");

        db = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            imgClub = findViewById(R.id.imgClub);
            String name = editName.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter club name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (clubImageUri.isEmpty()) {
                clubImageUri =
                        "android.resource://" + getPackageName() + "/" + R.drawable.robotique;
            }

            db.addClubData(name, clubImageUri);

            Toast.makeText(this, "Club added ✔", Toast.LENGTH_SHORT).show();
            finish();
        });
        imgClub.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            if (uri == null) return;

            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            if (requestCode == PICK_IMAGE) {
                clubImageUri = uri.toString();
                imgClub.setImageURI(uri);
            }
        }
    }
}