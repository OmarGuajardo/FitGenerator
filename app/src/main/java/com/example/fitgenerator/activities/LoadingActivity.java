package com.example.fitgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityLoadingBinding;
import com.parse.ParseUser;

public class LoadingActivity extends AppCompatActivity {

    ActivityLoadingBinding binding;
    Animation topAnim,bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        binding.ivAppIcon.setAnimation(topAnim);
        binding.tvSlogan.setAnimation(bottomAnim);



//        if(ParseUser.getCurrentUser() == null){
//            goWelcomePage();
//        }
//        else{
//            goNavPage();
//        }
    }



    public void goWelcomePage(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this,WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
    public void goNavPage(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this,NavigationActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}