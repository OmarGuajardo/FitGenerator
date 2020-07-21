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
import com.example.fitgenerator.models.Shop;
import com.example.fitgenerator.models.ShopResults;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shopList = new ArrayList<>();
        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), BuildConfig.GOOGLE_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getContext());


        fetchShopsNearMe("26.314947,-98.216675");

    }

    public void fetchShopsNearMe(String userLocation){
        final List<Shop> shopList = new ArrayList<>();
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
                    Shop newShop = new Shop(jsonObject.getJSONArray("results").getJSONObject(0));
                    Log.i(TAG, "newShop "+ newShop.getName());
                    shopList.clear();
                    shopList.addAll(Shop.fromJSONArray(jsonObject.getJSONArray("results")));
                    for(Shop shop : shopList){
                        Log.d(TAG, "Name: "+shop.getName()+", Vicinity " + shop.getVicinity());
                    }
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
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to use the Shop feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
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
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PackageManager.PERMISSION_GRANTED){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public ShopFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }


    public class EndPoint{
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

}