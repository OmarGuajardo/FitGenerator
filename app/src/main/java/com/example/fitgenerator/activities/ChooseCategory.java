package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityChooseCategoryBinding;
import com.example.fitgenerator.models.Closet;
import com.google.android.material.button.MaterialButton;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.List;

public class ChooseCategory extends AppCompatActivity {

    private static final String TAG = "ChooseCategory";
    ActivityChooseCategoryBinding binding;
    Toolbar toolbar;
    Boolean toggle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTitle = toolbar.findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setText("Choose Category");

        binding.categoryFavorite.tvCategoryName.setText("Favorite");
        binding.categoryOccasion.tvCategoryName.setText("Occasion");
        binding.categorySeason.tvCategoryName.setText("Season");
        binding.categoryRandom.tvCategoryName.setText("Random");

        binding.categoryFavorite.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFit();
            }
        });
        binding.categoryOccasion.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("occasion", "Formal");
            ParseCloud.callFunctionInBackground("categoryOccasion", params, new FunctionCallback<Boolean>() {
                @Override
                public void done(Boolean response, ParseException e) {
                    if(e==null){
                        if(response == true){
                            chooseFit();
                            return;
                        }
                        Toast.makeText(ChooseCategory.this, "Not enough clean items", Toast.LENGTH_SHORT).show();

                    }
                    Log.e(TAG, "error in getting OutFits", e );
                }
            });
            }
        });
        binding.categorySeason.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Get current User's season
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("season", "Summer");
                ParseCloud.callFunctionInBackground("categorySeason", params, new FunctionCallback<Boolean>() {
                    @Override
                    public void done(Boolean response, ParseException e) {
                        if(e==null){
                            if(response == true){
                                chooseFit();
                                return;
                            }
                            Toast.makeText(ChooseCategory.this, "Not enough clean items", Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG, "error in getting OutFits", e );
                    }
                });
            }
        });
        binding.categoryRandom.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFit();
            }
        });

        toggleForm();
        updateLists();


    }

    public void toggleForm(){
        binding.categoryRandom.cvCategory.setEnabled(toggle);
        binding.categorySeason.cvCategory.setEnabled(toggle);
        binding.categoryOccasion.cvCategory.setEnabled(toggle);
        binding.categoryFavorite.cvCategory.setEnabled(toggle);
        toggle = !toggle;
    }

    public void updateLists(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("currentUserCloset", Closet.getUserCloset().getObjectId());
        params.put("temp", 20);
        ParseCloud.callFunctionInBackground("updateLists", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                toggleForm();
            }
        });
    }

    public void chooseFit(){
        Intent intent = new Intent(this,ChooseFit.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
