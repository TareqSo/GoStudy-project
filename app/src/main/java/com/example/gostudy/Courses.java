package com.example.gostudy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class Courses extends AppCompatActivity {
    private SessionManager sessionManager;
    private Spinner spinner;
    private ListView listView;
    private FirebaseFirestore db;
    private static final String COURSES_COLLECTION = "courses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);

        // Initializations
        initializeViews();
        setupSpinner();
        checkLoginStatus();
        setupBottomNavigation();
    }

    private void initializeViews() {
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getApplicationContext());
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);
    }

    private void setupSpinner() {
        String[] spinnerOptions = {"comp", "scsi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = spinnerOptions[position];
                fetchDataForListView(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional handling when nothing is selected
            }
        });
    }


    private void checkLoginStatus() {
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(Courses.this, Login.class));
            finish();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {
                case "Add":
                    // Handle the "Add" menu item click
                    startActivity(new Intent(Courses.this, Add.class)); // Change AddActivity with the actual activity you want to navigate to
                    return true;
                // Add other cases if you have more menu items
                case "Settings":
                    // Handle the "Settings" menu item click
                    startActivity(new Intent(Courses.this, Settings.class)); // Change SettingsActivity with the actual activity you want to navigate to
                    return true;

                case "Home":
                    // Handle the "Settings" menu item click
                    startActivity(new Intent(Courses.this, MainActivity.class));
                    return true;

                case "Logout":
                    sessionManager.setLoggedIn(false);
                    startActivity(new Intent(Courses.this, Login.class));
                    finish(); // Prevent the user from going back to MainActivity
                    return true;

                default:
                    return false;
            }
        });
    }

    private void fetchDataForListView(String selectedOption) {
        Log.d("Courses", "Fetching data for: " + selectedOption);
        db.collection(COURSES_COLLECTION).document(selectedOption)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("Courses", "Document data: " + documentSnapshot.getData());
                        List<String> dataForListView = (List<String>) documentSnapshot.get("material"); // Use the correct key here
                        if (dataForListView != null && !dataForListView.isEmpty()) {
                            addDataToListView(dataForListView);
                        } else {
                            Log.d("Courses", "No data found for course: " + selectedOption);
                        }
                    } else {
                        Log.d("Courses", "Document does not exist for course: " + selectedOption);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Courses", "Error getting documents: ", e);
                });
    }


    private void addDataToListView(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    private void showToast(String message) {
        Toast.makeText(Courses.this, message, Toast.LENGTH_SHORT).show();
    }
}
