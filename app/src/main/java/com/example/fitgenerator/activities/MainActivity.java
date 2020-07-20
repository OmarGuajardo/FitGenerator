package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.R;
import com.example.fitgenerator.models.ViewAnimation;
import com.example.fitgenerator.adapters.PagerAdapter;
import com.example.fitgenerator.databinding.ActivityMainBinding;
import com.example.fitgenerator.fragments.ClosetFragment;
import com.example.fitgenerator.fragments.FitsFragment;
import com.example.fitgenerator.fragments.LaundryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements ClosetFragment.onPauseListener{

    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    TabLayout tabLayout;
    FloatingActionButton fab;
    Toolbar toolbar;

    //FAB Rotating
    Boolean isRotate = false;

    //Global Fragments
    ClosetFragment closetFragment;
    FitsFragment fitsFragment;
    LaundryFragment laundryFragment;

    View btnFABView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        toolbar = (Toolbar)findViewById(R.id.topAppBar);


        //Toolbar set up
        setSupportActionBar(toolbar);

        //Defining Views
        tabLayout = findViewById(R.id.tabBar);
        fab = binding.btnFAB;

        //Setting up the View pager
        tabLayout.setupWithViewPager(binding.viewPager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),0);

        //Adding the Tabs
        closetFragment = new ClosetFragment();
        fitsFragment = new FitsFragment();
        laundryFragment = new LaundryFragment();
        pagerAdapter.addFragment(fitsFragment,"Fits");
        pagerAdapter.addFragment(closetFragment,"Closet");
        pagerAdapter.addFragment(laundryFragment,"Laundry");
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setCurrentItem(1);

        animateFab(1);
        ViewAnimation.init(binding.fabTop);
        ViewAnimation.init(binding.fabBottom);
        ViewAnimation.init(binding.fabShoes);
        ViewAnimation.init(binding.fabAdd);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabListener(binding.viewPager.getCurrentItem(),view);
                btnFABView = view;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        closetFragment.onStart();
                        break;
                    case 1:
                        fitsFragment.onStart();
                        break;
                    case 2:
                        laundryFragment.onStart();
                        break;
                }
                animateFab(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ParseUser.logOut();
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void fabListener(int position, View view){
        //Defining different events depending on what tab the user is in
        switch(position){
            case 0:
                binding.fabAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCreateItem();
                    }
                });
                binding.fabTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closetFragment.queryCleanItems(Closet.KEY_TOP);
                    }
                });
                binding.fabBottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closetFragment.queryCleanItems(Closet.KEY_BOTTOM);

                    }
                });
                binding.fabShoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closetFragment.queryCleanItems(Closet.KEY_SHOES);
                    }
                });
                isRotate = ViewAnimation.rotateFab(view,!isRotate,binding.btnFAB);
                if(isRotate){
                    ViewAnimation.showIn(binding.fabTop);
                    ViewAnimation.showIn(binding.fabBottom);
                    ViewAnimation.showIn(binding.fabShoes);
                    ViewAnimation.showIn(binding.fabAdd);

                }else{
                    ViewAnimation.showOut(binding.fabTop);
                    ViewAnimation.showOut(binding.fabBottom);
                    ViewAnimation.showOut(binding.fabShoes);
                    ViewAnimation.showOut(binding.fabAdd);
                }
                return;
            case 1:
                fitsFragment.generateOutfit();
                return;
            case 2:
                laundryFragment.washAllItems();
                return;
            default:
                return;
        }
    }

    public void openCreateItem(){
        Intent intent = new Intent(MainActivity.this,CreateItemActivity.class);
        startActivity(intent);
    }

    //Handles the animation of the icons between tabs
    protected void animateFab(final int position) {

//        final int[] colorIntArray = {R.color.walking,R.color.running,R.color.biking,R.color.paddling,R.color.golfing};
        final int[] iconIntArray = {R.drawable.ic_baseline_view_module_24,R.drawable.ic_baseline_rotate_right_24,R.drawable.noun_laundry_2976228};
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
//                fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[position]));
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));
                if((position == 1 || position == 2 )&& isRotate == true){
                    disableIcons();
                }
                // Scale up animation
                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    @Override
    public void disableIcons() {

        if(isRotate) {
            isRotate = false;
            ViewAnimation.rotateFab(btnFABView,false,binding.btnFAB);
            ViewAnimation.showOut(binding.fabTop);
            ViewAnimation.showOut(binding.fabBottom);
            ViewAnimation.showOut(binding.fabShoes);
            ViewAnimation.showOut(binding.fabAdd);

        }
    }
}