package com.example.fitgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fitgenerator.Closet;
import com.example.fitgenerator.databinding.ActivityRegisterBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

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
    }

    public void goHomePage(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
}