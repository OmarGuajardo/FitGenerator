package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.adapters.DetailsAdapter;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityItemDetailsBinding;

import org.parceler.Parcels;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    Toolbar toolbar;
    RecyclerView rvDetails;
    DetailsAdapter detailsAdapter;
    ClothingItem clothingItem;
    ActivityItemDetailsBinding binding;
    ProgressBar imageProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.topAppBar);

        //Extracting the clothingItem
        clothingItem = Parcels.unwrap(getIntent().getParcelableExtra("clothingItem"));
        imageProgressBar = findViewById(R.id.imageProgressBar);

        // Adding back button to the Tool Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTile = findViewById(R.id.tvToolBarTitle);
        tvToolBarTile.setText("Clothing Item");

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailsActivity.this,CreateItemActivity.class);
                intent.putExtra("clothingItemEdit",Parcels.wrap(clothingItem));
                startActivity(intent);
                finish();
            }
        });

        imageProgressBar.setVisibility(View.VISIBLE);
        if(!clothingItem.getImageURL().isEmpty()){
            Glide.with(getApplicationContext())
                    .load(clothingItem.getImageURL())
                    .dontTransform()
                    .into(binding.ivItemPic);
            imageProgressBar.setVisibility(View.GONE);

        }
        else{
            imageProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext())
                    .load(R.drawable.fit_generator_icon)
                    .circleCrop()
                    .into(binding.ivItemPic);

        }

        //Information template
        String[][] dummyInfo = new String[][]{{
            "Class",clothingItem.getClassString()},
                {"Color",clothingItem.getColor()},
                {"Fit",clothingItem.getFit()},
                {"Type",clothingItem.getType()},
                {"Style",clothingItem.getStyle()},
                {"Category",clothingItem.getCategory()},
                {"Number of Wears",String.valueOf(clothingItem.getUses())},
                {clothingItem.getWorn()? "Dirty" : "Clean","  "}};
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