package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityChooseFitBinding;

public class ChooseFit extends AppCompatActivity {

    Toolbar toolbar;
    ActivityChooseFitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseFitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTitle = toolbar.findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setText("Choose Fit");

        Glide.with(getApplicationContext())
                .load("https://lp2.hm.com/hmgoepprod?set=quality[79],source[/c1/b1/c1b1bbc63a23984e66f3e32f1ce5b45ad8b0956f.jpg],origin[dam],category[men_tshirtstanks_shortsleeve],type[LOOKBOOK],res[m],hmver[1]&call=url[file:/product/main]")
                .fitCenter()
                .into(binding.ivTop);
        Glide.with(getApplicationContext())
                .load("https://lp2.hm.com/hmgoepprod?set=quality[79],source[/b9/fc/b9fc3df031aa5903d876dad04d3cbd462d43b726.jpg],origin[dam],category[men_trousers_chinos_skinny_all],type[LOOKBOOK],res[m],hmver[1]&call=url[file:/product/main]")
                .fitCenter()
                .into(binding.ivBottom);
        Glide.with(getApplicationContext())
                .load("https://lp2.hm.com/hmgoepprod?set=quality[79],source[/bb/2c/bb2cc7a85ae612d153a031e2657821cca6da474c.jpg],origin[dam],category[men_jacketscoats_bikerjackets],type[LOOKBOOK],res[s],hmver[1]&call=url[file:/product/main]")
                .fitCenter()
                .into(binding.ivLayer);
        Glide.with(getApplicationContext())
                .load("https://lp2.hm.com/hmgoepprod?set=quality[79],source[/9c/41/9c4105e213f927ca3430ab502e1de5edabeb9263.jpg],origin[dam],category[],type[DESCRIPTIVESTILLLIFE],res[s],hmver[1]&call=url[file:/product/main]")
                .fitCenter()
                .into(binding.ivShoes);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}