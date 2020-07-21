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

import com.example.fitgenerator.BuildConfig;
import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.MainActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;
import java.util.List;


public class ShopFragment extends Fragment {

    public  String TAG = "ShopFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), BuildConfig.GOOGLE_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getContext());

        int locationPermissionResult = ContextCompat.checkSelfPermission(getContext(),"android.permission.ACCESS_FINE_LOCATION");
        if(locationPermissionResult == PackageManager.PERMISSION_GRANTED){

        }
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ADDRESS);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "granted");
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    Log.i(TAG, "onComplete: ");
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    placeLikelihood.getPlace().getAddress(),
                                    placeLikelihood.getLikelihood()));
                        }
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
            Log.i(TAG, "not granted asking for permission");
            getLocationPermission();

        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }
}