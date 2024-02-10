package com.example.gostudy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create dummy data for the RecyclerView
        List<String> itemList = new ArrayList<>();
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        // Add more items as needed

        // Create an adapter and set it to the RecyclerView
        MyAdapter adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Initialize the dynamic spinner
        AutoCompleteTextView dynamicSpinner = findViewById(R.id.dynamic_spinner);

        // Create an array of items for the spinner
        String[] spinnerItems = {"Option 1", "Option 2", "Option 3"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        dynamicSpinner.setAdapter(spinnerAdapter);
    }
}
