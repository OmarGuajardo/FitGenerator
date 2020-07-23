package com.example.fitgenerator.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.R;
import com.example.fitgenerator.databinding.ActivityCreateItemBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;

public class CreateItemActivity extends AppCompatActivity {

    private static final String TAG = "CreateItemActivity";
    ActivityCreateItemBinding binding;
    Toolbar toolbar;
    JSONObject form;
    AutoCompleteTextView[] viewList;
    TextInputLayout[] viewListContainer;
    ClothingItem retreivedItem;

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
                launchCamera();
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

        if(!retreivedItem.getImageURL().isEmpty()){
            binding.btnPicture.setIcon(getDrawable(R.drawable.ic_baseline_check_24));
            binding.btnPicture.setText("Picture Received!");
        }
        setOptions(retreivedItem.getClassString());
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
           Snackbar.make(binding.coordinatorLayout, "Saving item...", Snackbar.LENGTH_LONG)
                    .show();
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
        if(photoFile != null){
            clothingItemSubmit.setPicture(new ParseFile(photoFile));
        }
        clothingItemSubmit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(CreateItemActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
                try {
                    formEnable(true);
                    resetForm();
                    String classString = form.getString("Class");
                    Closet.getUserCloset().addItem(clothingItemSubmit,classString);
                    Closet.getUserCloset().saveInBackground();

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

                listOptions = new String[][]{Color,emptyList,TypeLayer,StyleTop};
                break;
            case "Top":
                listOptions = new String[][]{Color,FitTop,TypeTop,StyleTop};
                break;
            case "Bottom":
                listOptions = new String[][]{Color,FitBottom,TypeBottom,StyleBottom};
                break;
            case "Shoes":
                listOptions = new String[][]{Color,emptyList,emptyList,emptyList};
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
                viewList[i].setText("");
        }
        binding.tvName.setText("");

    }

    //Method to set options in the event that we are calling the activity
    //to EDIT rather than CREATE and item of clothing
    public void setOptions(String classItem){
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

                return;
            default:

        }
        for (int i = 0; i < viewList.length; i++) {
            ArrayAdapter<String> newAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.dropdown_menu_popup_item,
                    listOptions[i]);
            viewList[i].setAdapter(newAdapter);
        }
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

    //This methods calls and intent to launch the camera, take the picture and save the picture as a file that
    //we can then use to put in the ImageView
    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getApplicationContext(), "com.codepath.fileprovider.fitgenerator", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
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