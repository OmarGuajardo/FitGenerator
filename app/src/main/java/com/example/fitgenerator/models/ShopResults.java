package com.example.fitgenerator.models;


import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fitgenerator.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ShopResults extends AppCompatActivity {


    String TAG = "ShopResults.java";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";
    private static final String NEARBY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private String userLocation;

    public ShopResults(double userLatitude, double userLongitude){
        this.userLocation = String.valueOf(userLatitude)+","+String.valueOf(userLongitude);
    }
    public List<Shop> fetchShopsNearMe(){
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
                    shopList.addAll(Shop.fromJSONArray(jsonObject.getJSONArray("results")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: "+response, throwable);
            }
        });

        return shopList;

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
