package com.example.gostudy;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    private Button changePasswordButton;
    private Button contactUsButton;
    private Button sendFeedbackButton;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(Settings.this, Login.class));
            finish(); // Prevent the user from going back to MainActivity
        }


        // Initialize buttons
        changePasswordButton = findViewById(R.id.changePasswordButton);
        contactUsButton = findViewById(R.id.contactUsButton);
        sendFeedbackButton = findViewById(R.id.sendFeedbackButton);

        // Set click listeners
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the functionality for changing password here
            }
        });

        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Contact.class);
                startActivity(intent);
            }
        });

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the functionality for sending feedback here
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Add":
                        // Handle the "Add" menu item click
                        startActivity(new Intent(Settings.this, Add.class)); // Change AddActivity with the actual activity you want to navigate to
                        return true;
                    // Add other cases if you have more menu items
                    case "Settings":
                        // Handle the "Settings" menu item click
                        startActivity(new Intent(Settings.this, Settings.class)); // Change SettingsActivity with the actual activity you want to navigate to
                        return true;

                    case "Home":
                    // Handle the "Settings" menu item click
                    startActivity(new Intent(Settings.this, MainActivity.class));
                    return true;

                    case "Logout":
                        sessionManager.setLoggedIn(false);
                        startActivity(new Intent(Settings.this, Login.class));
                        finish(); // Prevent the user from going back to MainActivity
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
