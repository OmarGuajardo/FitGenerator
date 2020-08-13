package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.R;
import com.example.fitgenerator.adapters.ChooseFitAdapter;
import com.example.fitgenerator.databinding.ActivityChooseFitBinding;
import com.example.fitgenerator.fragments.LoadingDialog;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.models.Fit;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseFit extends AppCompatActivity {

    private static final String TAG = "ChooseFit";
    Toolbar toolbar;
    ActivityChooseFitBinding binding;
    List<HashMap> fitList;
    String category;
    ChooseFitAdapter adapter;
    HorizontalInfiniteCycleViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseFitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTitle = toolbar.findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setText("Choose Fit");

        fitList = new ArrayList<>();

        //Getting outfits from backend
        getOutfits();

        binding.btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChooseFit();
            }
        });
    }

    //Retrieves the position
    public void handleChooseFit(){
        final LoadingDialog loadingDialog = new LoadingDialog(ChooseFit.this);
        loadingDialog.startLoadingDialog();
        final Fit newFit = new Fit();
        for(ClothingItem item : getCurrentOutFit()){
            item.addUses();
            switch (item.getClassString()){
                case "Layer":
                    newFit.setLayer(item);
                    break;
                case "Top":
                    newFit.setTop(item);
                    item.setWorn(true);
                    break;
                case "Bottom":
                    newFit.setBottom(item);
                    item.setWorn(true);
                    break;
                case "Shoes":
                    newFit.setShoes(item);
                    break;
                default:
                    break;
            }
        }
        newFit.setCategory(category);
        newFit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    loadingDialog.dismissDialog();
                    Closet.getUserCloset().addFit(newFit);
                    Closet.getUserCloset().saveInBackground();
                    Snackbar.make(binding.coordinatorLayout, "Outfit Selected!", Snackbar.LENGTH_SHORT)
                            .show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ChooseFit.this,NavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }, 500);



                }

            }
        });
    }

    private List<ClothingItem> getCurrentOutFit() {
        List<ClothingItem> currentOutFit = new ArrayList<>();
        int position = pager.getRealItem();
        if(fitList.get(position).get("Layer") != null){
            ClothingItem layerChoice =  (ClothingItem)fitList.get(position).get("Layer");
            currentOutFit.add(layerChoice);
        }
        ClothingItem topChoice =  (ClothingItem)fitList.get(position).get("Top");
        ClothingItem bottomChoice =  (ClothingItem)fitList.get(position).get("Bottom");
        ClothingItem shoesChoice =  (ClothingItem)fitList.get(position).get("Shoe");
        currentOutFit.add(topChoice);
        currentOutFit.add(bottomChoice);
        currentOutFit.add(shoesChoice);
        return currentOutFit;
    }

    public void getOutfits() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        ParseCloud.callFunctionInBackground("generateOutfits", params, new FunctionCallback<HashMap>() {
            @Override
            public void done(HashMap map, ParseException e) {
                if(e==null){
                    fitList.clear();
                    category = (String)map.get("Category");
                    fitList = (List<HashMap>)map.get("Fits");
                    Log.d(TAG, "fitList size " + fitList.size());
                    pager = findViewById(R.id.horizontal_cycle);
                    adapter = new ChooseFitAdapter(fitList,getApplicationContext());
                    pager.setAdapter(adapter);
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