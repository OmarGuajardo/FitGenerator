package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Permission;

public class NavigationActivity extends AppCompatActivity
        implements BottomSheetDialog.BottomSheetListener,
        LaundryFragment.LaundryFragmentListener,
        IPickResult {


    private static final String TAG = "NavigationActivity";
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    GeneratorFragment generatorFragment;
    HistoryFragment historyFragment;
    ShopFragment shopFragment;
    ImageView ivProfilePic;
    TextView tvUserName;
    TextView tvUserEmail;
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

        View headView = nvDrawer.getHeaderView(0);
        ivProfilePic = headView.findViewById(R.id.ivProfilePic);
        tvUserName = headView.findViewById(R.id.tvUserName);
        tvUserEmail = headView.findViewById(R.id.tvUserEmail);
        Glide.with(getApplicationContext())
                .load(R.drawable.fit_generator_icon)
                .circleCrop()
                .into(ivProfilePic);
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImageSelection();
            }
        });

        setUpProfile();


    }

    private void setUpProfile() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseFile profilePicFile = currentUser.getParseFile("profilePicture");
        if(profilePicFile != null){
            Glide.with(getApplicationContext())
                    .load(profilePicFile.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        }
        tvUserName.setText(currentUser.getUsername());
        tvUserEmail.setText(currentUser.getEmail() == null ? "" : currentUser.getEmail());

    }


    private void handleImageSelection() {
        PickSetup pickSetup = new PickSetup().setMaxSize(250);
        PickImageDialog.build(pickSetup)
                .show(this);
    }

    @Override
    public void onPickResult(final PickResult r) {
        if (r.getError() == null) {

            Glide.with(getApplicationContext())
                    .load(r.getUri())
                    .circleCrop()
                    .into(ivProfilePic);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            r.getBitmap().compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            final ParseFile newProfilePicFile = new ParseFile("profile_picture",bytes);
            newProfilePicFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        ParseUser.getCurrentUser().put("profilePicture",newProfilePicFile);
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    Log.d(TAG, "");
                                    return;
                                }
                                Log.e(TAG, "error saving user ", e);
                            }
                        });
                        return;
                    }
                    Log.e(TAG, "error saving pic ", e);
                }
            });
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
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
        switch (menuItem.getItemId()) {
            case R.id.navFitGenerator:
                fragmentClass = GeneratorFragment.class;
                break;
            case R.id.navShop:
                fragmentClass = ShopFragment.class;
                break;
            case R.id.navSettings:
                Intent i = new Intent(NavigationActivity.this, SettingsActivity.class);
                startActivity(i);
                return;
            default:
                fragmentClass = GeneratorFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "something went wrong trying to make fragment", e);
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

    public void refreshCloset() {
        generatorFragment = (GeneratorFragment) fragment;
        if(generatorFragment != null){
            generatorFragment.refreshCloset();
        }
    }

    @Override
    public void handleOnDelete(ClothingItem item) { refreshCloset(); }

    @Override
    public void handleWashItem() { refreshCloset(); }

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

}