package com.example.fitgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityMainBinding;
import com.example.fitgenerator.fragments.ClosetFragment;
import com.example.fitgenerator.fragments.FitsFragment;
import com.example.fitgenerator.fragments.LaundryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    TabLayout tabLayout;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        //Defining Views
        tabLayout = findViewById(R.id.tabBar);
        fab = binding.btnFAB;

        //Setting up the View pager
        tabLayout.setupWithViewPager(binding.viewPager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),0);

        //Adding the Tabs
        pagerAdapter.addFragment(new ClosetFragment(),"Closet");
        pagerAdapter.addFragment(new FitsFragment(),"Fits");
        pagerAdapter.addFragment(new LaundryFragment(),"Laundry");
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setCurrentItem(1);
        animateFab(1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabListener(binding.viewPager.getCurrentItem());
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                animateFab(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
    public void fabListener(int position){
        //Defining different events depending on what tab the user is in
        switch(position){
            case 0:
                openCreateItem();
                return;
            case 1:
                Log.d(TAG, "Generate out fit");
                return;
            case 2:
//                openEditItem();
                Log.d(TAG, "Wash all items");
                return;
            default:
                return;
        }
    }
    public void openCreateItem(){
        Intent intent = new Intent(MainActivity.this,CreateItemActivity.class);
        startActivity(intent);
    }
    public void openEditItem(){
        Intent intent = new Intent(MainActivity.this,ItemDetailsActivity.class);
        startActivity(intent);
    }

    //Handles the animation of the icons between tabs
    protected void animateFab(final int position) {
//        final int[] colorIntArray = {R.color.walking,R.color.running,R.color.biking,R.color.paddling,R.color.golfing};
        final int[] iconIntArray = {R.drawable.ic_baseline_add_24,R.drawable.ic_baseline_rotate_right_24,R.drawable.ic_baseline_restore_from_trash_24};
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
}