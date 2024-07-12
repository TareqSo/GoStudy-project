package com.example.gostudy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;


    private Spinner spinner;
    private RecyclerView coursesRecyclerView;
    private static final String COURSES_COLLECTION = "courses";

    private Button upperLeftButton, upperRightButton, upperMainButton, upperMain2Button,
            upperMain3Button, upperMain4Button;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button upperRightButton = findViewById(R.id.upper_right_button);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getEmail();

        sessionManager = new SessionManager(getApplicationContext());



        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish(); // Prevent the user from going back to MainActivity
        }

        // Initialize views
        spinner = findViewById(R.id.spinner);
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        upperLeftButton = findViewById(R.id.upper_left_button);
        upperRightButton = findViewById(R.id.upper_right_button);
        upperMainButton = findViewById(R.id.upper_main_button);
        upperMain2Button = findViewById(R.id.upper_main2_button);
        upperMain3Button = findViewById(R.id.upper_main3_button);
        upperMain4Button = findViewById(R.id.upper_main4_button);

        // Set up the RecyclerView with a LayoutManager and Adapter
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // or GridLayoutManager, etc.
        // Initialize your adapter with an empty list first
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(new ArrayList<>());
        coursesRecyclerView.setAdapter(adapter);


        // Define spinner options
        String[] spinnerOptions = {"comp", "scsi"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerOptions);

        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter2);

        // Set up item selection listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                String selectedOption = spinnerOptions[position];
                // Fetch data from Firestore based on the selected option
                fetchDataFromFirestore(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected (optional)
            }
        });
        upperRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Profile.class);
                intent.putExtra("username_email", currentUser); // Replace documentPath with the actual document path
                startActivity(intent);
            }
        });
        upperMain3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Add.class);
                startActivity(intent);
            }
        });
        upperMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Upcoming.class);
                startActivity(intent);
            }
        });

        upperMain2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Courses.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Add":
                        // Handle the "Add" menu item click
                        startActivity(new Intent(MainActivity.this, Add.class)); // Change AddActivity with the actual activity you want to navigate to
                        return true;
                    // Add other cases if you have more menu items
                    case "Settings":
                        // Handle the "Settings" menu item click
                        startActivity(new Intent(MainActivity.this, Settings.class)); // Change SettingsActivity with the actual activity you want to navigate to
                        return true;

                    case "Home":
                        // Handle the "Settings" menu item click
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        return true;

                    case "Logout":
                        sessionManager.setLoggedIn(false);
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish(); // Prevent the user from going back to MainActivity
                        return true;

                    default:
                        return false;
                }
            }
        });










    }

    private void fetchDataFromFirestore(String selectedOption) {
        Log.d("MainActivity", "Fetching data for: " + selectedOption);
        db.collection(COURSES_COLLECTION).document(selectedOption)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("MainActivity", "Document data: " + documentSnapshot.getData());
                        List<String> materials = (List<String>) documentSnapshot.get("material");
                        if (materials != null && !materials.isEmpty()) {
                            generateButtons(materials);
                        } else {
                            Log.d("MainActivity", "No materials found for course: " + selectedOption);
                        }
                    } else {
                        Log.d("MainActivity", "Document does not exist for course: " + selectedOption);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MainActivity", "Error getting documents: ", e);
                });
    }



    private void generateButtons(List<String> materials) {
        // Obtain the existing adapter attached to the RecyclerView
        // Inside onCreate or after you have initialized your RecyclerView
        coursesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Set 2 columns
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(new ArrayList<>());
        coursesRecyclerView.setAdapter(adapter);


        // Check if the adapter is not null
        if (adapter != null) {
            // Update the adapter's data with the new list of materials
            adapter.updateData(materials);
        } else {
            // If the adapter has not been initialized yet, initialize it with the materials
            adapter = new RecyclerViewAdapter(materials);
            coursesRecyclerView.setAdapter(adapter);
        }
    }

}
