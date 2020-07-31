package com.example.fitgenerator.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    String id;
    String place_id;
    String name;
    String vicinity;
    double rating;
    Boolean open_now;

    public Shop(JSONObject jsonObject) throws JSONException {

        this.place_id = jsonObject.getString("place_id");
        this.name = jsonObject.getString("name");
        this.vicinity = jsonObject.getString("vicinity");
        this.rating = jsonObject.getDouble("rating");
        this.open_now = jsonObject.getJSONObject("opening_hours").getBoolean("open_now");
    }

    public static List<Shop> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Shop> shopList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            shopList.add(new Shop(jsonArray.getJSONObject(i)));
        }
        return shopList;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double getRating() {
        return rating;
    }

    public Boolean getOpen_now() {
        return open_now;
    }
}
