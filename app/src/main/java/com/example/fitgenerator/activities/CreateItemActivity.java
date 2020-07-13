package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityCreateItemBinding;

public class CreateItemActivity extends AppCompatActivity {

    ActivityCreateItemBinding binding;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.topAppBar);
        // Find the toolbar view inside the activity layout
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}