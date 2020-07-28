package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityChooseFitBinding;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ClothingItem;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ChooseFit extends AppCompatActivity {

    private static final String TAG = "ChooseFit";
    Toolbar toolbar;
    ActivityChooseFitBinding binding;
    List<ClothingItem> currentOutfit;
    int currentOutfitIndex = -1;
    List<HashMap> fitList;
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

        getOutfits();

    }

    public void updateCurrentOutfit(int increment){
        currentOutfitIndex += increment;
        if(currentOutfitIndex >= fitList.size()){
            currentOutfitIndex = 0;
        }
        else if(currentOutfitIndex < 0){
            currentOutfitIndex = fitList.size()-1;
        }
        if(fitList.get(currentOutfitIndex).get("Layer") != null){
            ClothingItem layerChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Layer");
            ClothingItem topChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Top");
            ClothingItem bottomChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Bottom");
            ClothingItem shoesChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Shoes");
            currentOutfit.add(layerChoice);
            currentOutfit.add(topChoice);
            currentOutfit.add(bottomChoice);
            currentOutfit.add(shoesChoice);
        }
        else {
            ClothingItem topChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Top");
            ClothingItem bottomChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Bottom");
            ClothingItem shoesChoice =  (ClothingItem)fitList.get(currentOutfitIndex).get("Shoes");
            currentOutfit.add(topChoice);
            currentOutfit.add(bottomChoice);
            currentOutfit.add(shoesChoice);
        }
    }
    public void getOutfits() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("temp", 100);
        ParseCloud.callFunctionInBackground("generateOutfits", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e==null){
                    fitList= (List<HashMap>) object;
                    return;
                }
                Log.e(TAG, "error in getting OutFits", e );
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}