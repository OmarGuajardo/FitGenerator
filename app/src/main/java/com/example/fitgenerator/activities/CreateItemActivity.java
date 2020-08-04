package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.fragments.LoadingDialog;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityCreateItemBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateItemActivity extends AppCompatActivity
        implements IPickResult
{

    private static final String TAG = "CreateItemActivity";
    ActivityCreateItemBinding binding;
    Toolbar toolbar;
    JSONObject form;
    AutoCompleteTextView[] viewList;
    TextInputLayout[] viewListContainer;
    ClothingItem retreivedItem;
    LoadingDialog loadingDialog;
    byte[] bytes;
    //Vars for taking picture
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // Adding back button to the Tool Bar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView tvToolBarTile = findViewById(R.id.tvToolBarTitle);
        tvToolBarTile.setText("Create New Item");

        //Setting up the form
        viewList = new AutoCompleteTextView[]{binding.tvColor,binding.tvFit,binding.tvType,binding.tvStyle,binding.tvCategory};
        viewListContainer = new TextInputLayout[]{binding.containerColor,binding.containerFit,binding.containerType,binding.containerStyle,binding.containerCategory};
        form = new JSONObject();
        try {
            form.put("Name","");
            form.put("Class","");
            form.put("Color","");
            form.put("Fit","");
            form.put("Type","");
            form.put("Style","");
            form.put("Category","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Setting the Options for Class and Color since they are always going to be there
        String[] Class = new String[]{Closet.KEY_LAYER,Closet.KEY_TOP, Closet.KEY_BOTTOM, Closet.KEY_SHOES};
        String[] Color = new String[]{"Red", "Blue", "Green","Grey", "Purple", "Yellow", "Black", "Brown", "White", "Pink", "Tan", "Orange"};

        //Setting default options and setting up listeners
        refreshOptions("Top");
        addOnChangeListeners();

        //Unwrapping data if there is any
        if(getIntent().getParcelableExtra("clothingItemEdit") != null){
            retreivedItem = Parcels.unwrap(getIntent().getParcelableExtra("clothingItemEdit"));
            try {
                form.put("Name",retreivedItem.getName());
                form.put("Class",retreivedItem.getClassString());
                form.put("Color",retreivedItem.getColor());
                form.put("Fit",retreivedItem.getFit());
                form.put("Type",retreivedItem.getType());
                form.put("Style",retreivedItem.getStyle());
                form.put("Category",retreivedItem.getCategory());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            buildForm();
        }

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

        //Every time the class changes there should be new options
        binding.tvClass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = binding.tvClass.getText().toString();
                binding.containerClass.setError(null);
                refreshOptions(value);
                try {
                    form.put("Class",value);
                    autoFillName();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  handleImageSelection();
            }
        });

        binding.btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chosenClass = binding.tvClass.getText().toString();
                checkForm(chosenClass);

            }
        });
    }



    private void handleImageSelection() {
        PickSetup pickSetup = new PickSetup().setMaxSize(250);
        PickImageDialog.build(pickSetup)
                .show(this);
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            r.getBitmap().compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_check_24));
            binding.btnPicture.setText("Picture Taken!");

        } else {
            binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_attach_file_24));
            binding.btnPicture.setText("Attach Picture");
            Toast.makeText(getApplicationContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void buildForm(){

        binding.tvClass.setText(retreivedItem.getClassString());
        binding.tvColor.setText(retreivedItem.getColor());
        binding.tvFit.setText(retreivedItem.getFit());
        binding.tvType.setText(retreivedItem.getType());
        binding.tvStyle.setText(retreivedItem.getStyle());
        binding.tvName.setText(retreivedItem.getName());
        binding.tvCategory.setText(retreivedItem.getCategory());

        if(!retreivedItem.getImageURL().isEmpty()){
            binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_check_24));
            binding.btnPicture.setText("Picture Received!");
        }
        refreshOptions(retreivedItem.getClassString());
    }

    public void autoFillName() throws JSONException {
        String classString = form.getString("Class");
        String color = form.getString("Color");
        String fit = form.getString("Fit");
        String type = form.getString("Type");
        String style = form.getString("Style");
        if(classString.equals("Shoes")){
            binding.tvName.setText(color+" "+classString);
        }
        else if (classString.equals("Bottom")){
            binding.tvName.setText(type+" "+color+" "+fit+" "+style);
        }
        else{
            binding.tvName.setText(color+" "+fit+" "+style+" "+type);
        }
    }

    public void checkForm(String chosenClass){
        Boolean formReady = true;
        String name = binding.tvName.getText().toString();
        if(!chosenClass.isEmpty()){
            for(TextInputLayout container : viewListContainer){
                if(container.getVisibility() == View.VISIBLE){
                    if(container.getEditText().getText().toString().isEmpty()){
                        container.setError("Missing " + container.getHint());
                        formReady = false;
                    }
                }
            }
            if(name.isEmpty()){
                formReady = false;
                binding.containerName.setError("Missing Name");
            }
        }
        else{
            binding.containerClass.setError("Missing Class");
            formReady = false;
        }
        if(formReady){
           formEnable(false);
           submitClothingItem();
            loadingDialog = new LoadingDialog(CreateItemActivity.this);
            loadingDialog.startLoadingDialog();

        }
    }

    public void submitClothingItem() {
        final ClothingItem clothingItemSubmit;

        try {
            form.put("Name",binding.tvName.getText().toString());
            binding.containerName.setError(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Filling out the item with the form information if new
        if(retreivedItem == null){
            clothingItemSubmit = new ClothingItem();
        }
        else{
            clothingItemSubmit = retreivedItem;
        }
        clothingItemSubmit.setInfoFromJSON(form);

        //Checking to see if user took a picture of the Item
        if(bytes.length != 0){
            clothingItemSubmit.setPicture(new ParseFile("clothing_item_picture",bytes));
        }
        clothingItemSubmit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "error ",e );
                    Toast.makeText(CreateItemActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    formEnable(true);
                    resetForm();
                    String classString = form.getString("Class");
                    Closet.getUserCloset().addItem(clothingItemSubmit,classString);
                    Closet.getUserCloset().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                return;
                            }
                            Log.e(TAG, "error saving profilePic",e );
                        }
                    });
                    loadingDialog.dismissDialog();
                    Snackbar.make(binding.coordinatorLayout, "Item Saved!", Snackbar.LENGTH_SHORT)
                            .show();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    public void formEnable(Boolean enable){
        for(TextInputLayout container : viewListContainer){
            container.setEnabled(enable);
        }
        binding.containerName.setEnabled(enable);
        binding.containerClass.setEnabled(enable);
        binding.btnPicture.setEnabled(enable);
        binding.btnFAB.setEnabled(enable);
        binding.btnFAB.setVisibility(enable? View.VISIBLE : View.GONE);
    }

    public void resetForm(){
       refreshOptions(binding.tvClass.getText().toString());
        binding.containerName.getEditText().setText("");
        try {
            form.put("Name","");
            form.put("Color","");
            form.put("Fit","");
            form.put("Type","");
            form.put("Style","");
            form.put("Category","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_attach_file_24));
        binding.btnPicture.setText("Attach Picture");
        photoFile = null;
    }

    //If the users changes Class from Layer to Top to Bottom to Shoes there should be unique options
    //per class
    public void refreshOptions(String classItem){
        String[] Color = new String[]{"Red", "Blue", "Green","Grey", "Purple", "Yellow", "Black", "Brown", "White", "Pink", "Tan", "Orange"};
        String[] Category = new String[]{"Casual","Semi Formal","Formal"};
        String[] TypeLayer = new String[]{"Wind Breaker","Jacket","Coat","Sweater","Hoodie","Vest","Cardigan"};
        String[] FitTop = new String[]{"Short Sleeve", "Long Sleeve", "Tank Top"};
        String[] TypeTop = new String[]{"Button Up", "Tee Shirt", "V-Neck", "Crop Top", "Off The Shoulder", "Blouse"};
        String[] StyleTop = new String[]{"Basic", "Graphic", "Patterned","Horizontal Stripes", "Vertical Stripes"};
        String[] FitBottom = new String[]{"Straight", "Skinny", "Slim", "Baggy"};
        String[] TypeBottom = new String[]{"Pristine", "High Waisted", "Ripped"};
        String[] StyleBottom = new String[]{"Jeans", "Slacks", "Shorts", "Joggers", "Chinos", "Skirt", "Leggings", "Sweatpants"};
        String[] emptyList = new String[]{};
        String[][] listOptions = new String[][]{};

        switch (classItem){
            case "Layer":
                listOptions = new String[][]{Color,emptyList,TypeLayer,StyleTop,Category};
                break;
            case "Top":
                listOptions = new String[][]{Color,FitTop,TypeTop,StyleTop,Category};
                break;
            case "Bottom":
                listOptions = new String[][]{Color,FitBottom,TypeBottom,StyleBottom,Category};
                break;
            case "Shoes":
                listOptions = new String[][]{Color,emptyList,emptyList,emptyList,Category};
                break;
            default:

        }
        for (int i = 0; i < viewListContainer.length; i++) {
            if(listOptions[i].length != 0){
                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        listOptions[i]);
                viewList[i].setAdapter(newAdapter);
                viewListContainer[i].setVisibility(View.VISIBLE);
            }
            else{
                viewListContainer[i].setVisibility(View.GONE);
            }
            if(retreivedItem == null){
                viewList[i].setText("");
            }
        }
        if(retreivedItem == null){
            binding.tvName.setText("");
        }
    }

    //As the user fills out the options they will be saved in a json object
    //for easier submission of the form
    public void addOnChangeListeners(){
        for (int num = 0; num < viewList.length; num++) {
            final int finalNum = num;
            viewList[num].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String key = viewListContainer[finalNum].getHint().toString();
                    String value = viewList[finalNum].getText().toString();
                    viewListContainer[finalNum].setError(null);
                    try {
                        form.put(key,value);
                        autoFillName();
                    } catch (JSONException e) {
                        Log.d(TAG, "error occured "+e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_check_24));
                binding.btnPicture.setText("Picture Taken!");


            } else { // Result was a failure
                binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_attach_file_24));
                binding.btnPicture.setText("Attach Picture");
                Toast.makeText(getApplicationContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();

            }
        }
    }


}