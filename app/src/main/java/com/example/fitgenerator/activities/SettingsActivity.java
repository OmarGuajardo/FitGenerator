package com.example.fitgenerator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.fitgenerator.R;
import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        final Button btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 logOut();
            }
        });
//        getSupportFragmentManager()
////                .beginTransaction()
////                .replace(R.id.settings, new SettingsFragment())
////                .commit();
////        ActionBar actionBar = getSupportActionBar();
////        if (actionBar != null) {
////            actionBar.setDisplayHomeAsUpEnabled(true);
////        }


    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

        }


    }
    public void logOut(){
        ParseUser.logOut();
        Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}