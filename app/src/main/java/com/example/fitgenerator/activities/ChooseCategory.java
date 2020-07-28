package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fitgenerator.BuildConfig;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityChooseCategoryBinding;
import com.example.fitgenerator.fragments.nav_fragments.ShopFragment;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.Shop;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;

public class ChooseCategory extends AppCompatActivity {

    private static final String TAG = "ChooseCategory";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    ActivityChooseCategoryBinding binding;
    Toolbar toolbar;
    Boolean toggle = false;
    int currentTemp;
    private PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the SDK
        Places.initialize(getApplicationContext(), BuildConfig.GOOGLE_API_KEY);
        // Create a new PlacesClient instance
        placesClient = Places.createClient(getApplicationContext());

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
        binding.categoryRandom.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFit();
            }
        });
        binding.categoryOccasion.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateLists(currentTemp);
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
                chooseCategory("categorySeason",params);
            }
        });


        getWeather();


    }

    public void chooseCategory(final String cloudFunctionName, final HashMap<String,Object> cloudParams){
        toggleForm();
        HashMap<String, Object> params = new HashMap<>();
        params.put("currentUserCloset", Closet.getUserCloset().getObjectId());
        params.put("temp", currentTemp);
        ParseCloud.callFunctionInBackground("updateLists", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e==null){
                    toggleForm();
                    ParseCloud.callFunctionInBackground(cloudFunctionName, cloudParams, new FunctionCallback<Boolean>() {
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
            }
        });
    }

    public void toggleForm(){
        binding.categoryRandom.cvCategory.setEnabled(toggle);
        binding.categorySeason.cvCategory.setEnabled(toggle);
        binding.categoryOccasion.cvCategory.setEnabled(toggle);
        binding.categoryFavorite.cvCategory.setEnabled(toggle);
        toggle = !toggle;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeather();
    }
    
    public void getWeather(){
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.LAT_LNG);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    //Getting the users location so that we can query for shops nearby
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        LatLng lat_lng = response.getPlaceLikelihoods().get(0).getPlace().getLatLng();
                        AsyncHttpClient client = new AsyncHttpClient();
                        ShopFragment.EndPoint newEndPoint = new ShopFragment.EndPoint(WEATHER_URL);
                        newEndPoint.addParam("appid", BuildConfig.WEATHER_API_KEY);
                        newEndPoint.addParam("lat",String.valueOf(lat_lng.latitude));
                        newEndPoint.addParam("lon",String.valueOf(lat_lng.longitude));
                        newEndPoint.addParam("units","imperial");
                        client.get(newEndPoint.getEndPoint(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                JSONObject jsonObject = json.jsonObject;
                                try {
                                    currentTemp = jsonObject.getJSONObject("main").getInt("temp");
                                    updateLists(currentTemp);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure: "+response, throwable);
                            }
                        });
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        }
        else{
            getLocationPermission();
        }
    }

    public void updateLists(int temp){
        toggleForm();
        HashMap<String, Object> params = new HashMap<>();
        params.put("currentUserCloset", Closet.getUserCloset().getObjectId());
        params.put("temp", temp);
        ParseCloud.callFunctionInBackground("updateLists", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                toggleForm();
            }
        });
    }

    private void getLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PackageManager.PERMISSION_GRANTED){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission NOT Granted", Toast.LENGTH_SHORT).show();

            }
        }
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
