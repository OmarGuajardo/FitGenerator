package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fitgenerator.BuildConfig;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityChooseCategoryBinding;
import com.example.fitgenerator.fragments.SingleChoiceDialogFragment;
import com.example.fitgenerator.fragments.nav_fragments.ShopFragment;
import com.example.fitgenerator.models.Closet;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;

public class ChooseCategory extends AppCompatActivity implements SingleChoiceDialogFragment.SingleChoiceListener {

    private static final String TAG = "ChooseCategory";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    ActivityChooseCategoryBinding binding;
    Toolbar toolbar;
    Boolean toggle = false;
    int currentTemp;
    String category;
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
        binding.categoryFavorite.ivCategoryIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
        binding.categoryOccasion.tvCategoryName.setText("Dress Code");
        binding.categoryOccasion.ivCategoryIcon.setImageResource(R.drawable.ic_baseline_assignment_24);
        binding.categorySeason.tvCategoryName.setText("Season");
        binding.categorySeason.ivCategoryIcon.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        binding.categoryRandom.tvCategoryName.setText("Random");
        binding.categoryRandom.ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shuffle_24);

        binding.categoryFavorite.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                chooseCategory("categoryFavorite", params);
            }
        });
        binding.categoryRandom.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                chooseCategory("categoryRandom", params);
            }
        });
        binding.categoryOccasion.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] choices = new String[]{"Casual", "Semi Formal", "Formal"};
                category = "occasion";
                showOptions("Choose Occasion", choices);
            }
        });
        binding.categorySeason.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] choices = new String[]{"Winter", "Spring", "Summer", "Fall"};
                category = "season";
                showOptions("Choose Season", choices);
            }
        });


        getWeather();

    }

    public void showOptions(String title, String[] choices) {
        DialogFragment singleChoiceDialog = new SingleChoiceDialogFragment(choices);
        singleChoiceDialog.setCancelable(false);
        singleChoiceDialog.show(getSupportFragmentManager(), title);
    }

    public void chooseCategory(final String cloudFunctionName, final HashMap<String, Object> cloudParams) {
        final Snackbar snackbar = Snackbar.make(binding.layoutChooseCategory,"Gathering Data...", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        toggleForm();
        HashMap<String, Object> params = new HashMap<>();
        params.put("currentUserCloset", Closet.getUserCloset().getObjectId());
        params.put("temp", currentTemp);
        ParseCloud.callFunctionInBackground("updateLists", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e == null) {
                    toggleForm();
                    ParseCloud.callFunctionInBackground(cloudFunctionName, cloudParams, new FunctionCallback<Boolean>() {
                        @Override
                        public void done(Boolean response, ParseException e) {
                            if (e == null) {
                                if (response == true) {
                                    snackbar.dismiss();
                                    chooseFit();
                                    return;
                                }
                                Snackbar.make(binding.layoutChooseCategory,"Sorry, not enough items in closet to generate Fit", Snackbar.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, "error in getting OutFits", e);
                        }
                    });
                }
            }
        });
    }

    public void toggleForm() {
        binding.categoryRandom.cvCategory.setEnabled(toggle);
        binding.categorySeason.cvCategory.setEnabled(toggle);
        binding.categoryOccasion.cvCategory.setEnabled(toggle);
        binding.categoryFavorite.cvCategory.setEnabled(toggle);
        toggle = !toggle;
    }



    public void getWeather() {
        toggleForm();
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
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse response = task.getResult();
                        LatLng lat_lng = response.getPlaceLikelihoods().get(0).getPlace().getLatLng();
                        AsyncHttpClient client = new AsyncHttpClient();
                        ShopFragment.EndPoint newEndPoint = new ShopFragment.EndPoint(WEATHER_URL);
                        newEndPoint.addParam("appid", BuildConfig.WEATHER_API_KEY);
                        newEndPoint.addParam("lat", String.valueOf(lat_lng.latitude));
                        newEndPoint.addParam("lon", String.valueOf(lat_lng.longitude));
                        newEndPoint.addParam("units", "imperial");
                        client.get(newEndPoint.getEndPoint(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                JSONObject jsonObject = json.jsonObject;
                                try {
                                    currentTemp = jsonObject.getJSONObject("main").getInt("temp");
                                    JSONObject weatherObject = (JSONObject)jsonObject.getJSONArray("weather").get(0);
                                    int weatherID = weatherObject.getInt("id");
                                    String description = weatherObject.getString("description");
                                    showWeather(weatherID,description);
                                    toggleForm();
//                                    updateLists(currentTemp);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure: " + response, throwable);
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
        } else {
            getLocationPermission();
        }
    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    private void showWeather(int weatherID, String description) {
        binding.tvWeatherDesc.setText(capitalize(description));
        binding.tvTemp.setText(currentTemp+"\u2109");
        String iconResource = "";
        if(weatherID >= 200 && weatherID <= 232){
            iconResource = getString(R.string.wi_thunderstorm);
        }
        else if(weatherID>= 300 && weatherID<=321){
            iconResource = getString(R.string.wi_showers);
        }
        else if(weatherID>= 500 && weatherID<=531){
            iconResource = getString(R.string.wi_rain);
        }
        else if(weatherID>= 600 && weatherID<=622){
            iconResource = getString(R.string.wi_snow);

        }
        else if(weatherID>= 701 && weatherID<=781){
            iconResource = getString(R.string.wi_dust);
        }
        else if(weatherID>= 801 && weatherID<=804){
            iconResource = getString(R.string.wi_cloud);
        }
        else{
            iconResource = getString(R.string.wi_day_sunny);
        }



        binding.myWeatherIcon.setIconResource(iconResource);
    }

    public void updateLists(int temp) {
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
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
    }

    public void chooseFit() {
        Intent intent = new Intent(this, ChooseFit.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeather();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission NOT Granted", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(category, list[position]);
        if (category.equals("occasion")) {
            chooseCategory("categoryOccasion", params);
        } else {
            chooseCategory("categorySeason", params);
        }
    }

    @Override
    public void onNegativeButtonClicked() {
    }
}
