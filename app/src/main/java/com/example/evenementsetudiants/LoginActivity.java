package com.example.evenementsetudiants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView registerLink;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.registerLink);

        // Database
        db = new DatabaseHelper(this);

        // LOGIN
        btnLogin.setOnClickListener(v -> {

            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        LoginActivity.this,
                        "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                // ✅ ADMIN LOGIN
                if(email.equals("admin@gmail.com")
                        && password.equals("admin")) {

                    SharedPreferences sharedPreferences =
                            getSharedPreferences("user_session", MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", email);
                    editor.putString("role", "admin"); // 🔥 مهم

                    editor.apply();

                    Intent intent = new Intent(
                            LoginActivity.this,
                            AdminActivity.class
                    );

                    startActivity(intent);
                    finish();
                }

                // ✅ USER LOGIN
                else if (db.checkUser(email, password)) {

                    Toast.makeText(
                            LoginActivity.this,
                            "Connexion réussie",
                            Toast.LENGTH_SHORT
                    ).show();

                    // SAVE SESSION
                    SharedPreferences sharedPreferences =
                            getSharedPreferences(
                                    "user_session",
                                    MODE_PRIVATE
                            );

                    SharedPreferences.Editor editor =
                            sharedPreferences.edit();

                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("role", "user");
                    // email
                    editor.putString("email", email);

                    editor.apply();

                    // GO MAIN
                    Intent intent = new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            LoginActivity.this,
                            "Email ou mot de passe incorrect",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        // GO REGISTER
        registerLink.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }
}
