package com.example.gostudy;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore db;
    private SessionManager sessionManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        sessionManager = new SessionManager(getApplicationContext());

        // Get the email of the user from the intent
        String userEmail = getIntent().getStringExtra("username_email");

        // Check if the email is not null
        if (userEmail != null) {
            // Initialize Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Get the reference to the "users" node
            DatabaseReference usersRef = database.getReference("users");
            TextView emailTextView = findViewById(R.id.emailTextView);
            TextView nameTextView = findViewById(R.id.nameTextView);
            TextView passwordTextView = findViewById(R.id.passwordTextView);
            TextView studentIdTextView = findViewById(R.id.studentIdTextView);

            // The query in Realtime Database will be different. We need to find the user by their email.
            // Since we cannot directly query by email, we'll need to fetch all users and filter manually
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through all users to find the matching email
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        if (userEmail.equals(email)) {
                            // Email matches, retrieve user's info
                            String username = userSnapshot.child("name").getValue(String.class);
                            String studentId = userSnapshot.child("studentId").getValue(String.class);

                            // Now you can use the retrieved user info as needed
                            Log.d("ProfileActivity", "Username: " + username);
                            Log.d("ProfileActivity", "Email: " + email);
                            Log.d("ProfileActivity", "Student ID: " + studentId);

                            emailTextView.setText("Email: " + email);
                            nameTextView.setText("Username: " + username);
                            studentIdTextView.setText("Student ID: " + studentId);


                            break; // Stop the loop as we found our user
                        }
                    }
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error on getting data
                    Log.e("ProfileActivity", "Database error", databaseError.toException());
                }
            });
        } else {
            // User email is null, handle this case as needed
            Log.e("ProfileActivity", "User email is null");
        }
    }


}
