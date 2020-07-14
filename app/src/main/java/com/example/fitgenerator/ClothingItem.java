package com.example.fitgenerator;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
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


    public void setName(String name){
        put(KEY_NAME,name);
    }public void setClass(String Class){
        put(KEY_CLASS,Class);
    }public void setColor(String color){
        put(KEY_COLOR,color);
    }public void setFit(String fit){
        put(KEY_FIT,fit);
    }public void setType(String type){
        put(KEY_TYPE,type);
    }public void setStyle(String style){
        put(KEY_STYLE,style);
    }public void setWorn(Boolean worn){
        put(KEY_NAME,worn);
    }

    public String getName(){
        return getString(KEY_NAME);
    }
     public String getClassString(){
        return getString(KEY_CLASS);
    }
     public String getColor(){
        return getString(KEY_COLOR);
    }
     public String getFit(){
        return getString(KEY_FIT);
    }
     public String getType(){
        return getString(KEY_TYPE);
    }
     public String getStyle(){
        return getString(KEY_STYLE);
    }
     public boolean getWorn(){
        return getBoolean(KEY_WORN);
    }
    public int getUses(){
        return getInt(KEY_USES);
    }


}
