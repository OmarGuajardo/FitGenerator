package com.example.fitgenerator.fragments.nav_fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fitgenerator.BuildConfig;
import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.MainActivity;
import com.example.fitgenerator.adapters.ShopsAdapter;
import com.example.fitgenerator.models.Shop;
import com.example.fitgenerator.models.ShopResults;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;


public class ShopFragment extends Fragment {

    private static final String NEARBY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    public  String TAG = "ShopFragment";
    List<Shop> shopList;
    RecyclerView rvShop;
    ShopsAdapter shopsAdapter;
    private PlacesClient placesClient;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), BuildConfig.GOOGLE_API_KEY);
        // Create a new PlacesClient instance
        placesClient = Places.createClient(getContext());

        //Setting up the Recycler View
        rvShop = view.findViewById(R.id.rvShop);
        shopList = new ArrayList<>();
        shopsAdapter = new ShopsAdapter(shopList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvShop.setAdapter(shopsAdapter);
        rvShop.setLayoutManager(linearLayoutManager);

        queryShops();
    }

    public void queryShops(){
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.LAT_LNG);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    //Getting the users location so that we can query for shops nearby
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        LatLng lat_lng = response.getPlaceLikelihoods().get(0).getPlace().getLatLng();
                        fetchShopsNearMe(lat_lng.latitude +","+ lat_lng.longitude);
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

    public void fetchShopsNearMe(String userLocation){
        AsyncHttpClient client = new AsyncHttpClient();
        EndPoint newEndPoint = new EndPoint(NEARBY_URL);
        newEndPoint.addParam("key", BuildConfig.GOOGLE_API_KEY);
        newEndPoint.addParam("location",userLocation);
        newEndPoint.addParam("radius","10000");
        newEndPoint.addParam("type","clothing_store");
        client.get(newEndPoint.getEndPoint(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    shopList.clear();
                    shopList.addAll(Shop.fromJSONArray(jsonObject.getJSONArray("results")));
                    Log.d(TAG, "onSuccess: " + shopList.size());
                    shopsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: "+response, throwable);
            }
        });
    }

    private void getLocationPermission() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            Log.d(TAG, "making dialog");
            new AlertDialog.Builder(getContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This permission is needed to use the Shop feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PackageManager.PERMISSION_GRANTED){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                queryShops();
                Snackbar.make(getView()," Permission Granted", Snackbar.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(getView()," Permission Denied", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public static class EndPoint{
        String request_url;
        Boolean first = false;
        public EndPoint(String request_url){
            this.request_url = request_url;
        }
        public String getEndPoint(){
            return request_url;
        }
        public void addParam(String key, String val){
            if(first){
                request_url = request_url + "&"+key+"="+val;
            }
            else{
                request_url = request_url + "?"+key+"="+val;
                first = true;
            }
        }
    }

    // Required empty public constructor
    public ShopFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }


}