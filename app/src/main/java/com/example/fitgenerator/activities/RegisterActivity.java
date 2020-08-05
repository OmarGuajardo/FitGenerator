package com.example.fitgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.fitgenerator.R;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.databinding.ActivityRegisterBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;


public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String username = binding.tvUserName.getText().toString();
               String email = binding.tvEmail.getText().toString();
               String password = binding.tvPassword.getText().toString();
               String confirmPassword = binding.tvConfirmPassword.getText().toString();
               registerUser(username,email,password,confirmPassword);
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
                logInWithFacebook(null);
            }
        });



        binding.tvBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                chooseDate();
            }
        });

    }

    private void chooseDate() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        final long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");
        builder.setSelection(today);
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.tvBirthDate.setText(materialDatePicker.getHeaderText());
            }
        });

    }




    public void logInWithFacebook(Collection<String> permissions){
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    final Closet newCloset = new Closet();
                    newCloset.setUser(user);
                    newCloset.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            user.put("UserCloset",newCloset);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        goHomePage();
                                    }
                                }
                            });
                        }
                    });
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    goHomePage();
                }
            }
        });
    }

    public void goHomePage(){
        Intent intent = new Intent(RegisterActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }
    public void registerUser(String username, String email,String password,String confirmPassword){
        if(password.equals(confirmPassword)) {
            final ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.put("birthDate",binding.tvBirthDate.getText().toString());
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        //TODO: Make a new closet and attach it to the user
                        final Closet newCloset = new Closet();
                        newCloset.setUser(newUser);
                        newCloset.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                newUser.put("UserCloset", newCloset);
                                newUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            goHomePage();
                                            return;
                                        }
                                        Log.e(TAG, "error creating user ", e);
                                    }
                                });
                            }
                        });
                    }
                }
            });
            return;
        }
        binding.tvConfirmPassword.setError("Passwords must match");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}