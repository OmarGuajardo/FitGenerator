package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fitgenerator.BuildConfig;
import com.example.fitgenerator.fragments.BottomSheetDialog;
import com.example.fitgenerator.fragments.FitsFragment;
import com.example.fitgenerator.fragments.LaundryFragment;
import com.example.fitgenerator.fragments.nav_fragments.GeneratorFragment;
import com.example.fitgenerator.R;
import com.example.fitgenerator.fragments.nav_fragments.HistoryFragment;
import com.example.fitgenerator.fragments.nav_fragments.ShopFragment;
import com.example.fitgenerator.models.ClothingItem;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

public class NavigationActivity extends AppCompatActivity
        implements BottomSheetDialog.BottomSheetListener,
        LaundryFragment.LaundryFragmentListener {


    private static final String TAG = "NavigationActivity";
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    GeneratorFragment generatorFragment;
    HistoryFragment historyFragment;
    ShopFragment shopFragment;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        generatorFragment = new GeneratorFragment();
        historyFragment = new HistoryFragment();
        shopFragment = new ShopFragment();
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        selectDrawerItem(navigationView.getCheckedItem());
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

        public void selectDrawerItem(MenuItem menuItem) {
            // Create a new fragment and specify the fragment to show based on nav item clicked
            fragment = null;
            Class fragmentClass;
            switch(menuItem.getItemId()) {
                case R.id.navFitGenerator:
                    fragmentClass = GeneratorFragment.class;
                    break;
                case R.id.navPastFits:
                    fragmentClass = HistoryFragment.class;
                    break;
                case R.id.navShop:
                    fragmentClass = ShopFragment.class;
                    break;
                case R.id.navSettings:
                    Intent i = new Intent(NavigationActivity.this,SettingsActivity.class);
                    startActivity(i);
                    return;
                default:
                    fragmentClass = GeneratorFragment.class;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            TextView tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
            tvToolBarTitle.setText(menuItem.getTitle());
            // Close the navigation drawer
            mDrawer.closeDrawers();
        }

    public void refreshCloset(){
        generatorFragment = (GeneratorFragment)fragment;
        generatorFragment.refreshCloset();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void handleOnDelete(ClothingItem item) {
        refreshCloset();
    }


    @Override
    public void handleWashItem() {
        refreshCloset();
    }
}