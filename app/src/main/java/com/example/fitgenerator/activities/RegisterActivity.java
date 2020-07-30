package com.example.fitgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.databinding.ActivityRegisterBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.facebook.ParseFacebookUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        try {
////            PackageInfo info = getPackageManager().getPackageInfo(
////                    "com.example.packagename",
////                    PackageManager.GET_SIGNATURES);
////            for (Signature signature : info.signatures) {
////                MessageDigest md = MessageDigest.getInstance("SHA");
////                md.update(signature.toByteArray());
////                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
////            }
////        }
////        catch (NameNotFoundException e) {
////        }
////        catch (NoSuchAlgorithmException e) {
////        }


        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String username = binding.tvUserName.getText().toString();
               String email = binding.tvEmail.getText().toString();
               String password = binding.tvPassword.getText().toString();
               registerUser(username,email,password);
            }
        });
        binding.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collection<String> permissions = null;
                logInWithFacebook(null);
            }
        });
    }
    

    public void logInWithFacebook(Collection<String> permissions){
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });
    }

    public void goHomePage(){
        Intent intent = new Intent(RegisterActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }
    public void registerUser(String username, String email,String password){
        final ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                //TODO: Make a new closet and attach it to the user
                final Closet newCloset = new Closet();
                newCloset.setUser(newUser);
                newCloset.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        newUser.put("UserCloset",newCloset);
                        newUser.saveInBackground();
                        goHomePage();
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}