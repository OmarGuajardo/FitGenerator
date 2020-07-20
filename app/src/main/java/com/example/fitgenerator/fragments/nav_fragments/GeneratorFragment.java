package com.example.fitgenerator.fragments.nav_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.CreateItemActivity;
import com.example.fitgenerator.activities.MainActivity;
import com.example.fitgenerator.adapters.PagerAdapter;
import com.example.fitgenerator.fragments.ClosetFragment;
import com.example.fitgenerator.fragments.FitsFragment;
import com.example.fitgenerator.fragments.LaundryFragment;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class GeneratorFragment extends Fragment {

    private static final String TAG = "GeneratorFragment";
    //Global Vals
    TabLayout tabLayout;
    ClosetFragment closetFragment;
    FitsFragment fitsFragment;
    LaundryFragment laundryFragment;

    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    FloatingActionButton fab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Defining Views
        tabLayout = getActivity().findViewById(R.id.tabBar);
        fab = view.findViewById(R.id.btnFAB);
        tabLayout.setVisibility(View.VISIBLE);

        //Setting up the View pager
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(),0);

        //Adding the Tabs
        closetFragment = new ClosetFragment();
        fitsFragment = new FitsFragment();
        laundryFragment = new LaundryFragment();
        pagerAdapter.addFragment(fitsFragment,"Fits");
        pagerAdapter.addFragment(closetFragment,"Closet");
        pagerAdapter.addFragment(laundryFragment,"Laundry");
        viewPager.setAdapter(pagerAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabListener(viewPager.getCurrentItem(),view);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        fitsFragment.onStart();
                        break;
                    case 1:
                        closetFragment.onStart();
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

    public void fabListener(int position, View view){
        //Defining different events depending on what tab the user is in
        switch(position){
            case 0:
                fitsFragment.generateOutfit();
                return;
            case 1:
                openCreateItem();
                return;
            case 2:
                laundryFragment.washAllItems();
                return;
            default:
                return;
        }
    }

    public void refreshCloset(){
        closetFragment.onStart();
    }


    // Required empty public constructor
    public GeneratorFragment() { }

    public void openCreateItem(){
        Intent intent = new Intent(getContext(), CreateItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generator, container, false);
    }

    //Handles the animation of the icons between tabs
    protected void animateFab(final int position) {
        final int[] iconIntArray = {R.drawable.ic_baseline_rotate_right_24,R.drawable.ic_baseline_add_24,R.drawable.noun_laundry_2976228};
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));
                // Scale up animation
                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        fab.startAnimation(shrink);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.tabBar).setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.tabBar).setVisibility(View.VISIBLE);
    }


}