package com.example.gostudy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Upcoming extends AppCompatActivity {

    private Spinner spinnerCourses;
    private ListView listViewMeetings;
    private ArrayAdapter<String> meetingsAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming);

        spinnerCourses = findViewById(R.id.spinnerCourses);
        listViewMeetings = findViewById(R.id.listViewMeetings);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        setupSpinner();
    }

    private void setupSpinner() {
        // This is just an example, replace with your actual courses
        String[] courses = new String[]{"scsi", "Comp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapter);

        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourse = (String) parent.getItemAtPosition(position);
                fetchMeetings(selectedCourse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected if necessary
            }
        });
    }

    private void fetchMeetings(String selectedCourse) {
        databaseReference.child("meetings").orderByChild("type").equalTo(selectedCourse)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Meeting> meetingsList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Meeting meeting = snapshot.getValue(Meeting.class);
                            if (meeting != null && selectedCourse.equals(meeting.getType())) {
                                meetingsList.add(meeting);
                            }
                        }
                        populateListView(meetingsList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                        Log.e("Courses", "Error getting meetings data: " + databaseError.toException());
                    }
                });
    }



    private void populateListView(List<Meeting> meetings) {
        List<String> meetingStrings = new ArrayList<>();
        for (Meeting meeting : meetings) {
            meetingStrings.add(meeting.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meetingStrings);
        listViewMeetings.setAdapter(adapter);
    }

}
