package com.example.fitgenerator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityMainBinding;
import com.example.fitgenerator.fragments.ClosetFragment;
import com.example.fitgenerator.fragments.FitsFragment;
import com.example.fitgenerator.fragments.LaundryFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TabLayout tabLayout;
    TabItem tabCloset;
    TabItem tabFits;
    TabItem tabLaundry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        tabLayout = findViewById(R.id.tabBar);

        tabLayout.setupWithViewPager(binding.viewPager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager()
                ,0);

        pagerAdapter.addFragment(new ClosetFragment(),"Closet");
        pagerAdapter.addFragment(new FitsFragment(),"Fits");
        pagerAdapter.addFragment(new LaundryFragment(),"Laundry");
        binding.viewPager.setAdapter(pagerAdapter);



    }
}