package com.example.gostudy;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class Register extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextStudentId, editTextPassword;
    private Button buttonSignup;
    private Switch switchLoginPage;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get references to views
        editTextName = findViewById(R.id.editText);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        switchLoginPage = findViewById(R.id.switch1);

        // Set onClickListener for Signup button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String studentId = editTextStudentId.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Create new user object

                // Register user and save data to Firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    String userId = currentUser.getUid();

                                    // Create a User object
                                    com.example.gostudy.User user = new com.example.gostudy.User(name,email,studentId,password);

                                    // Save the user object to the database under the "users" node with the userId as the key
                                    mDatabase.child("users").child(userId).setValue(user);

                                    Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        // Set onClickListener for Switch to navigate to Login page
        switchLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Login page
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                // Finish the current activity to prevent the user from coming back to the registration page using the back button
                finish();
            }
        });

    }
}
