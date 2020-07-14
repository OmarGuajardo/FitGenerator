package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.DetailsAdapter;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityItemDetailsBinding;

public class ItemDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvDetails;
    DetailsAdapter detailsAdapter;

    ActivityItemDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.topAppBar);

        // Adding back button to the Tool Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Name of Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailsActivity.this,CreateItemActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Glide.with(getApplicationContext())
                .load("https://target.scene7.com/is/image/Target/GUEST_657addf4-97db-485e-b4b1-954ca50de598?wid=488&hei=488&fmt=pjpeg")
                .circleCrop()
                .into(binding.ivItemPic);

        //Dummy Info
        String[][] dummyInfo = new String[][]{{"Class","Top"},{"Color","Red"},{"Fit","Long Sleeve"},{"Type","Tee Shit"},{"Style","Vertical Stripes"}};
        //Setting up Recycler View
        detailsAdapter = new DetailsAdapter(getApplicationContext(),dummyInfo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvDetails.setLayoutManager(linearLayoutManager);
        binding.rvDetails.setAdapter(detailsAdapter);


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}