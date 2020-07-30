package com.example.fitgenerator.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.Objects;

@ParseClassName("Fit")
@Parcel(analyze = Fit.class)
public class Fit extends ParseObject{

    public static final String KEY_LAYER = "Layer";
    public static final String KEY_TOP = "Top";
    public static final String KEY_BOTTOM = "Bottom";
    public static final String KEY_SHOES = "Shoes";
    public static final String KEY_FAVORITE = "Favorite";
    public static final String KEY_CATEGORY = "Category";
    private static final String TAG = "Fit.java";

    public void setLayer(ClothingItem layer) {
        put(KEY_LAYER, layer);
    }
    public void setTop(ClothingItem top){
        put(KEY_TOP,top);
    }
    public void setBottom(ClothingItem bottom){
        put(KEY_BOTTOM,bottom);
    }
    public void setShoes(ClothingItem shoes){
        put(KEY_SHOES,shoes);
    }
    public void setCategory(String category){
        put(KEY_CATEGORY,category);
    }

    public String getCategory(){
        return getString(KEY_CATEGORY);
    }

    public Boolean getFavorite(){
        return getBoolean(KEY_FAVORITE);
    }

    public void setFavorite(Boolean state){
        put(KEY_FAVORITE,state);

    }


}
