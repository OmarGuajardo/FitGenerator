package com.example.fitgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.fitgenerator.databinding.ActivityMainBinding;
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