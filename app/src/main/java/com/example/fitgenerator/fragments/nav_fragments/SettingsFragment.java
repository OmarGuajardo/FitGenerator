package com.example.fitgenerator.fragments.nav_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.WelcomeActivity;
import com.parse.ParseUser;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SettingOptions settingOptions = new SettingOptions();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings,settingOptions )
                .commit();

    }

    public static class SettingOptions extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            //Setting the username and email in the preference category
            final EditTextPreference preferenceUsername = getPreferenceManager().findPreference("username");
            final EditTextPreference preferenceEmail = getPreferenceManager().findPreference("email");
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
            preferenceEmail.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ParseUser.getCurrentUser().setEmail(newValue.toString());
                    ParseUser.getCurrentUser().saveInBackground();
                    preferenceEmail.setText(newValue.toString());
                    return false;
                }
            });

            //When LogOut button is pressed actively logout user
            Preference button = getPreferenceManager().findPreference("exitLink");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ParseUser.logOut();
                    Intent intent = new Intent(getContext(), WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();

                    return false;
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}