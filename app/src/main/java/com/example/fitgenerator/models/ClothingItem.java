package com.example.fitgenerator.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ParseClassName("ClothingItem")
public class ClothingItem extends ParseObject {
    public static final String KEY_NAME = "Name";
    public static final String KEY_CLASS = "Class";
    public static final String KEY_COLOR = "Color";
    public static final String KEY_FIT = "Fit";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_STYLE = "Style";
    public static final String KEY_WORN = "Worn";
    public static final String KEY_USES = "Uses";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_PICTURE = "Picture";


    //Setters
    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public void setClass(String Class) {
        put(KEY_CLASS, Class);
    }

    public void setColor(String color) {
        put(KEY_COLOR, color);
    }

    public void setFit(String fit) {
        put(KEY_FIT, fit);
    }

    public void setType(String type) {
        put(KEY_TYPE, type);
    }

    public void addUses(){
        increment(KEY_USES);
    }

    public void setStyle(String style) {
        put(KEY_STYLE, style);
    }

    public void setWorn(Boolean worn) {
        put(KEY_WORN, worn);
    }

    public void setPicture(ParseFile picture) {
        put(KEY_PICTURE, picture);
    }

    public void setInfoFromJSON(JSONObject form){
        Iterator<String> iter = form.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = form.get(key);
                put(key,value.toString());
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
    }


    //Getters
    public String getName() {
        return getString(KEY_NAME);
    }

    public String getClassString() {
        return getString(KEY_CLASS);
    }

    public String getColor() { return getString(KEY_COLOR); }

    public String getFit() {
        return getString(KEY_FIT);
    }

    public String getType() {
        return getString(KEY_TYPE);
    }

    public String getStyle() {
        return getString(KEY_STYLE);
    }
    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public String getImageURL(){
        if(getParseFile(KEY_PICTURE)== null){
            return "";
        }
        return getParseFile(KEY_PICTURE).getUrl();
    }

    public boolean getWorn() {
        return getBoolean(KEY_WORN);
    }

    public int getUses() {
        return getInt(KEY_USES);
    }


}
