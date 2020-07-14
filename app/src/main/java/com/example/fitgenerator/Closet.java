package com.example.fitgenerator;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Closet")
public class Closet extends ParseObject {
    public static final String KEY_USER = "User";
    public static final String KEY_TOP = "Top";
    public static final String KEY_BOTTOM= "Bottom";
    public static final String KEY_SHOES= "Shoes";

    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }



}
