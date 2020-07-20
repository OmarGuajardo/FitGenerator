package com.example.fitgenerator.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.adapters.ClosetAdapter;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ClosetFragment extends Fragment {

    public static final String TAG = "ClosetFragment";
    RecyclerView rvCloset;
    ClosetAdapter closetAdapter;
    List<ClothingItem> items;
    String currentClass;
    public onPauseListener listener;
    Boolean showProgressBar = false;
    Boolean showInsufficientItems = false;
    ProgressBar progressBar;
    TextView tvInsufficientItems;

    public ClosetFragment(){
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Setting up the Recycler View with the Adapter
        items = new ArrayList<>();
        rvCloset = view.findViewById(R.id.rvCloset);
        closetAdapter = new ClosetAdapter(getContext(),items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCloset.setAdapter(closetAdapter);
        rvCloset.setLayoutManager(linearLayoutManager);

        //Getting the data
        progressBar = view.findViewById(R.id.closetLoadingBar);
        tvInsufficientItems = view.findViewById(R.id.tvInsufficientItems);
        queryCleanItems(Closet.KEY_TOP);


    }





    //Getting the clean items for a specific class of item and populating the RV
    public void queryCleanItems(final String key){
        toggleLoading();
        Closet userCloset = (Closet) ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(key);
        ParseQuery query = relation.getQuery();
        query.whereEqualTo(ClothingItem.KEY_WORN,false);
        query.findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                tvInsufficientItems.setVisibility(objects.isEmpty()? View.VISIBLE : View.INVISIBLE);
                tvInsufficientItems.setText("No " + key +" In Closet");
                toggleLoading();
                items.clear();
                items.addAll(objects);
                closetAdapter.notifyDataSetChanged();
            }
        });
        currentClass = key;
    }

    public void toggleLoading(){
        progressBar.setVisibility(showProgressBar? View.INVISIBLE: View.VISIBLE);
        rvCloset.setVisibility(showProgressBar? View.VISIBLE: View.INVISIBLE);
        showProgressBar = !showProgressBar;
    }
    public void toggleInsufficientItems(){
        tvInsufficientItems.setVisibility(showInsufficientItems? View.INVISIBLE: View.VISIBLE);
        showInsufficientItems = !showInsufficientItems;
    }

    @Override
    public void onStart() {
        super.onStart();
        queryCleanItems(currentClass);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closet, container, false);
    }

    //Attaches the function from TimeLineActivity to this Fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (onPauseListener) context;
        } catch (Exception e) {
            Log.e("ComposeDialog", "compose dialog error : ", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        listener.disableIcons();
    }

    public interface onPauseListener{
        void disableIcons();
    }

}