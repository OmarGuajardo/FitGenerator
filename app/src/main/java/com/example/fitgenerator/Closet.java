package com.example.fitgenerator;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Closet")
public class Closet extends ParseObject {
    public static final String KEY_USER = "User";
    public static final String KEY_TOP = "Top";
    public static final String KEY_BOTTOM= "Bottom";
    public static final String KEY_SHOES= "Shoes";
    public static final String KEY_ALL_ITEMS= "allItems";

    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }
    public static Closet getUserCloset(){
        return (Closet)ParseUser.getCurrentUser().get("UserCloset");
    }

    public void addItem(ClothingItem item, String key){
        Closet userCloset = Closet.getUserCloset();
        ParseRelation<ParseObject> relation = userCloset.getRelation(key);
        ParseRelation<ParseObject> relationAll = userCloset.getRelation(KEY_ALL_ITEMS);
        relation.add(item);
        relationAll.add(item);
    }






}
