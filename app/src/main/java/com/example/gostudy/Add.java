package com.example.gostudy;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Add extends AppCompatActivity {

    private Spinner spinner;
    private DatePicker datePicker;
    private EditText timeEditText;
    private EditText urlEditText;
    private EditText descriptionEditText;
    private Button addButton;
    private Button openDatePickerButton;
    private TextView selectedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        // Initialize views
        spinner = findViewById(R.id.spinner);
        urlEditText = findViewById(R.id.urlEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        addButton = findViewById(R.id.addButton);
        timeEditText = findViewById(R.id.timeEditText); // Initialize timeEditText

        String[] spinnerOptions = {"comp", "scsi"};

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = spinnerOptions[position];
                //add handle
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSpinnerItem = spinner.getSelectedItem().toString();


                String selectedDate = selectedDateTextView.getText().toString();


                String selectedTime = timeEditText.getText().toString().trim();


                String enteredURL = urlEditText.getText().toString().trim();


                String enteredDescription = descriptionEditText.getText().toString().trim();

                // Check if any empty
                if (selectedSpinnerItem.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty() || enteredURL.isEmpty() || enteredDescription.isEmpty()) {
                    // Show a message
                    Toast.makeText(Add.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference meetingsRef = FirebaseDatabase.getInstance().getReference().child("meetings");
                String meetingId = meetingsRef.push().getKey();

                Meeting meeting = new Meeting(selectedSpinnerItem, selectedDate, selectedTime, enteredURL, enteredDescription);

                meetingsRef.child(meetingId).setValue(meeting);

                // Show a success message
                Toast.makeText(Add.this, "Meeting added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add.this, MainActivity.class);
                startActivity(intent);

                clearFields();
            }
        });

        openDatePickerButton = findViewById(R.id.openDatePickerButton);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);

        openDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void clearFields() {
        spinner.setSelection(0); // Reset spinner to default selection
        selectedDateTextView.setText("Selected Date: "); // Reset date TextView
        timeEditText.setText(""); // Clear time EditText
        urlEditText.setText(""); // Clear URL EditText
        descriptionEditText.setText(""); // Clear description EditText
    }

    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(Add.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update TextView with selected date
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        selectedDateTextView.setText("Selected Date: " + selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}
