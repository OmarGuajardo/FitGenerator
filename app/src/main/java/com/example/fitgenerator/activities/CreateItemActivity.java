package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityCreateItemBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateItemActivity extends AppCompatActivity {

    private static final String TAG = "CreateItemActivity";
    ActivityCreateItemBinding binding;
    Toolbar toolbar;
    JSONObject form;
    AutoCompleteTextView[] viewList;
    TextInputLayout[] viewListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setting up the form
        viewList = new AutoCompleteTextView[]{binding.tvColor,binding.tvFit,binding.tvType,binding.tvStyle};
        viewListContainer = new TextInputLayout[]{binding.containerColor,binding.containerFit,binding.containerType,binding.containerStyle};
        form = new JSONObject();
        try {
            form.put("Name","");
            form.put("Class","");
            form.put("Color","");
            form.put("Fit","");
            form.put("Type","");
            form.put("Style","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addOnChangeListeners();

        //Setting the Options for Class and Color since they are always going to be there
        String[] Class = new String[]{"Top", "Bottom", "Shoes"};
//        String[] Color = new String[]{"Red", "Blue", "Green","Grey", "Purple", "Yellow", "Black", "Brown", "White", "Pink", "Tan", "Orange"};
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                Class);
        binding.tvClass.setAdapter(classAdapter);
//        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(
//                getApplicationContext(),
//                R.layout.dropdown_menu_popup_item,
//                Color);
//        binding.tvColor.setAdapter(colorAdapter);

        //Every time the class changes there should be new options
        binding.tvClass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = binding.tvClass.getText().toString();
                refreshOptions(value);
                try {
                    form.put("Class",value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClothingItem();
            }
        });




    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    public void submitClothingItem(){
        try {
            form.put("Name",binding.tvName.getText().toString());
            Log.d(TAG, "submitClothingItem: form = " +form.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //If the users changes Class from Top to Bottom to Shoes there should be unique options
    //per class
    public void refreshOptions(String classItem){
        String[] FitTop = new String[]{"Short Sleeve", "Long Sleeve", "Tank Top"};
        String[] Color = new String[]{"Red", "Blue", "Green","Grey", "Purple", "Yellow", "Black", "Brown", "White", "Pink", "Tan", "Orange"};
        String[] TypeTop = new String[]{"Button Up", "Tee Shirt", "V-Neck", "Crop Top", "Off The Shoulder", "Blouse"};
        String[] StyleTop = new String[]{"Basic", "Graphic", "Patterned", "Floral", "Horizontal Stripes", "Vertical Stripes"};
        String[] FitBottom = new String[]{"Straight", "Skinny", "Slim", "Baggy"};
        String[] TypeBottom = new String[]{"Pristine", "High Waisted", "Ripped"};
        String[] StyleBottom = new String[]{"Jeans", "Slacks", "Shorts", "Joggers", "Chinos", "Skirt", "Leggings", "Sweatpants"};
        String[][] listOptions = new String[][]{};

        switch (classItem){
            case "Top":
                listOptions = new String[][]{Color,FitTop,TypeTop,StyleTop};
                break;
            case "Bottom":
                listOptions = new String[][]{Color,FitBottom,TypeBottom,StyleBottom};
                break;
            case "Shoes":
                binding.containerType.setVisibility(View.GONE);
                binding.containerStyle.setVisibility(View.GONE);
                binding.containerFit.setVisibility(View.GONE);
                binding.tvStyle.setText("");
                binding.tvType.setText("");
                binding.tvFit.setText("");
                binding.tvColor.setText("");

                return;
            default:

        }
        for (int i = 0; i < viewList.length; i++) {
            ArrayAdapter<String> newAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.dropdown_menu_popup_item,
                    listOptions[i]);
            viewList[i].setAdapter(newAdapter);
            viewList[i].setText("");
        }
        binding.tvName.setText("");
        binding.containerType.setVisibility(View.VISIBLE);
        binding.containerStyle.setVisibility(View.VISIBLE);
        binding.containerFit.setVisibility(View.VISIBLE);
    }

    //As the user fills out the options they will be saved in a json object
    //for easier submission of the form
    public void addOnChangeListeners(){
        for (int num = 0; num < viewList.length; num++) {
            final int finalNum = num;
            viewList[num].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String key = viewListContainer[finalNum].getHint().toString();
                    String value = viewList[finalNum].getText().toString();
                    try {
                        form.put(key,value);
                        Log.d(TAG, "key = " + key + " value = "+value);
                    } catch (JSONException e) {
                        Log.d(TAG, "error occured "+e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

}