package com.example.fitgenerator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.fitgenerator.R;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTile = findViewById(R.id.tvToolBarTitle);
        tvToolBarTile.setText("Settings");

        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            //Setting the username and email in the preference category
            final EditTextPreference preferenceUsername = getPreferenceManager().findPreference("username");
            EditTextPreference preferenceEmail = getPreferenceManager().findPreference("email");
            preferenceUsername.setText(ParseUser.getCurrentUser().getUsername());
            preferenceEmail.setText(ParseUser.getCurrentUser().getEmail());

            preferenceUsername.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ParseUser.getCurrentUser().setUsername(newValue.toString());
                    ParseUser.getCurrentUser().saveInBackground();
                    preferenceUsername.setText(newValue.toString());
                    return false;
                }
            });

            //When LogOut button is pressed actively logout user
            Preference button = getPreferenceManager().findPreference("exitLink");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ParseUser.logOut();
                    Intent intent = new Intent(getContext(),WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();

                    return false;
                }
            });



        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    public void logOut(){
        ParseUser.logOut();
        Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}