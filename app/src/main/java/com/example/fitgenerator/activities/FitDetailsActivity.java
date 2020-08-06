package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityFitDetailsBinding;
import com.example.fitgenerator.databinding.ChooseFitItemBinding;
import com.example.fitgenerator.databinding.ChooseFitItemRowBinding;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.models.Fit;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.parceler.Parcels;

public class FitDetailsActivity extends AppCompatActivity {

    private static final String TAG = "FitDetailsActivity";
    ActivityFitDetailsBinding binding;
    Toolbar toolbar;
    Fit fit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityFitDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTitle = toolbar.findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setText("Fit Chosen");

        fit = Parcels.unwrap(getIntent().getParcelableExtra("fitItem"));

        if(fit.getParseObject(Fit.KEY_LAYER) != null){
            displayImage(binding.rowLayer,Fit.KEY_LAYER);
        }
        displayImage(binding.rowTop,Fit.KEY_TOP);
        displayImage(binding.rowBottom,Fit.KEY_BOTTOM);
        displayImage(binding.rowShoes,Fit.KEY_SHOES);



    }

    public void displayImage(final ChooseFitItemRowBinding ref, final String itemClass){
        ref.ivItemPic.setVisibility(View.VISIBLE);
        fit.getParseObject(itemClass).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null){
                    if(object != null){
                        ClothingItem item = (ClothingItem)object;
                        Glide.with(getApplicationContext())
                                .load(item.getImageURL())
                                .dontTransform()
                                .into(ref.ivItemPic);
                        ref.tvItemClass.setText(item.getClassString());
                        ref.tvItemName.setText(item.getName());
                    }

                }
                else{
                    Log.d(TAG, "error: ");
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}