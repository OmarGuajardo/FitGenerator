package com.example.fitgenerator.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Fit")
public class Fit extends ParseObject{

    public static final String KEY_LAYER = "Layer";
    public static final String KEY_TOP = "Top";
    public static final String KEY_BOTTOM = "Bottom";
    public static final String KEY_SHOES = "Shoes";
    public static final String KEY_FAVORITE = "Favorite";
    public static final String KEY_CATEGORY = "Category";

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
}
