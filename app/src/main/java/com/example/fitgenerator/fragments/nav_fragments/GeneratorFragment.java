package com.example.fitgenerator.fragments.nav_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitgenerator.R;
import com.example.fitgenerator.adapters.PagerAdapter;
import com.example.fitgenerator.fragments.generator_fragments.ClosetFragment;
import com.example.fitgenerator.fragments.generator_fragments.FitsFragment;
import com.example.fitgenerator.fragments.generator_fragments.LaundryFragment;
import com.google.android.material.tabs.TabLayout;


public class GeneratorFragment extends Fragment {

    //Global Vals
    TabLayout tabLayout;
    ClosetFragment closetFragment;
    FitsFragment fitsFragment;
    LaundryFragment laundryFragment;

    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    View view;

    public GeneratorFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_generator, container, false);

        // Inflate the layout for this fragment
        //Defining Views
        tabLayout = getActivity().findViewById(R.id.tabBar);
        tabLayout.setVisibility(View.VISIBLE);

        //Setting up the View pager
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(),0);

        //Adding the Tabs
        closetFragment = new ClosetFragment();
        fitsFragment = new FitsFragment();
        laundryFragment = new LaundryFragment();
        pagerAdapter.addFragment(closetFragment,"Closet");
        pagerAdapter.addFragment(fitsFragment,"Fits");
        pagerAdapter.addFragment(laundryFragment,"Laundry");
        viewPager.setAdapter(pagerAdapter);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.tabBar).setVisibility(View.GONE);
    }
}