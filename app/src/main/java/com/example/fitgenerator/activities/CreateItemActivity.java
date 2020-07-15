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

import java.util.ArrayList;
import java.util.List;

public class CreateItemActivity extends AppCompatActivity {

    private static final String TAG = "CreateItemActivity";
    ActivityCreateItemBinding binding;
    Toolbar toolbar;
    AutoCompleteTextView[]form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String test = "test";
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        //Setting up the form
        form = new AutoCompleteTextView[]{binding.tvClass,binding.tvColor,binding.tvFit,binding.tvType,binding.tvStyle};

        toolbar = findViewById(R.id.topAppBar);
        // Adding back button to the Tool Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] Class = new String[]{"Top", "Bottom", "Shoes"};
        String[] Color = new String[]{"Red", "Blue", "Green", "Purple", "Yellow", "Black", "Brown", "White", "Pink", "Tan", "Orange"};

        //Setting the Options for Class and Color since they are alwasy going to be there
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                Class);
        binding.tvClass.setAdapter(classAdapter);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                Color);
        binding.tvColor.setAdapter(colorAdapter);

        binding.tvClass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshOptions(binding.tvClass.getText().toString());
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
        for(AutoCompleteTextView view : form){
            Log.d(TAG, "submitClothingItem: result = "+view.getText().toString());
        }
        Log.d(TAG, "submitClothingItem: result = "+binding.tvName.getText().toString());
    }
    public void refreshOptions(String classItem){
//        String[] Color = new String[]{"Red", "Blue", "Green", "Purple", "Yellow", "Black", "Brown", "White", "Pink", "Tan", "Orange"};
        String[] FitTop = new String[]{"Short Sleeve", "Long Sleeve", "Tank Top"};
        String[] TypeTop = new String[]{"Button Up", "Tee Shirt", "V-Neck", "Crop Top", "Off The Shoulder", "Blouse"};
        String[] StyleTop = new String[]{"Basic", "Graphic", "Patterned", "Floral", "Horizontal Stripes", "Vertical Stripes"};
        String[] FitBottom = new String[]{"Straight", "Skinny", "Slim", "Baggy"};
        String[] TypeBottom = new String[]{"Pristine", "High Waisted", "Ripped"};
        String[] StyleBottom = new String[]{"Jeans", "Slacks", "Shorts", "Joggers", "Chinos", "Skirt", "Leggings", "Sweatpants"};
        AutoCompleteTextView[] viewList = new AutoCompleteTextView[]{};
        String[][] listOptions = new String[][]{};

        switch (classItem){
            case "Top":
//                viewList = new AutoCompleteTextView[]{binding.tvColor,binding.tvFit,binding.tvType,binding.tvStyle};
//                listOptions = new String[][]{Color,FitTop,TypeTop,StyleTop};
                viewList = new AutoCompleteTextView[]{binding.tvFit,binding.tvType,binding.tvStyle};
                listOptions = new String[][]{FitTop,TypeTop,StyleTop};
                break;
            case "Bottom":
//                viewList = new AutoCompleteTextView[]{binding.tvColor,binding.tvFit,binding.tvType,binding.tvStyle};
////                listOptions = new String[][]{Color,FitBottom,TypeBottom,StyleBottom};
                viewList = new AutoCompleteTextView[]{binding.tvFit,binding.tvType,binding.tvStyle};
                listOptions = new String[][]{FitBottom,TypeBottom,StyleBottom};
                break;
            case "Shoes":
                binding.containerType.setVisibility(View.GONE);
                binding.containerStyle.setVisibility(View.GONE);
                binding.containerFit.setVisibility(View.GONE);
                binding.tvStyle.setText("");
                binding.tvType.setText("");
                binding.tvFit.setText("");

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
        binding.containerType.setVisibility(View.VISIBLE);
        binding.containerStyle.setVisibility(View.VISIBLE);
        binding.containerFit.setVisibility(View.VISIBLE);
    }


}