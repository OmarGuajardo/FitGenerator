package com.example.fitgenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitgenerator.Closet;
import com.example.fitgenerator.ClosetAdapter;
import com.example.fitgenerator.ClothingItem;
import com.example.fitgenerator.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class LaundryFragment extends Fragment {

    RecyclerView rvLaundry;
    List<ClothingItem> laundryBasket;
    ClosetAdapter closetAdapter;

    public LaundryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        laundryBasket = new ArrayList<>();


        rvLaundry = view.findViewById(R.id.rvLaundry);

        //Setting up the Recycler View with the Adapter
        closetAdapter = new ClosetAdapter(getContext(),laundryBasket);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvLaundry.setAdapter(closetAdapter);
        rvLaundry.setLayoutManager(linearLayoutManager);
        queryCleanItems();
        closetAdapter.notifyDataSetChanged();
    }

    public void queryCleanItems(){
        Closet userCloset = (Closet) ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(Closet.KEY_ALL_ITEMS);
        ParseQuery query = relation.getQuery();
        query.whereEqualTo(ClothingItem.KEY_WORN,true);
        query.findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                laundryBasket.clear();
                laundryBasket.addAll(objects);
                closetAdapter.notifyDataSetChanged();
            }
        });


    }

    public void washAllItems(){
        for(ClothingItem dirtyItem : laundryBasket){
            dirtyItem.setWorn(false);
            dirtyItem.saveInBackground();
        }
        laundryBasket.clear();
        closetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        queryCleanItems();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laundry, container, false);
    }
}