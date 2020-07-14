package com.example.fitgenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitgenerator.Closet;
import com.example.fitgenerator.ClosetAdapter;
import com.example.fitgenerator.ClothingItem;
import com.example.fitgenerator.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ClosetFragment extends Fragment {

    public static final String TAG = "ClosetFragment";
    RecyclerView rvCloset;
    ClosetAdapter closetAdapter;
    List<String> closet;
    List<ClothingItem> items;

    public ClosetFragment(){
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = new ArrayList<>();

        rvCloset = view.findViewById(R.id.rvCloset);

        //Setting up the Recycler View with the Adapter
        closetAdapter = new ClosetAdapter(getContext(),items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCloset.setAdapter(closetAdapter);
        rvCloset.setLayoutManager(linearLayoutManager);

        queryTop();
    }

   public void queryTop(){

       Closet userCloset = (Closet)ParseUser.getCurrentUser().get("UserCloset");
       ParseRelation<ParseObject> relation = userCloset.getRelation(Closet.KEY_TOP);
       relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {
               items.addAll(fromRelationQuery(objects));
               closetAdapter.notifyDataSetChanged();
           }
       });


   }

    public List<ClothingItem> fromRelationQuery(List<ParseObject> objects){
        List<ClothingItem> items = new ArrayList<>();
        for(ParseObject object : objects){
            ClothingItem newItem = (ClothingItem)object;
            items.add(newItem);
        }
        return items;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closet, container, false);
    }
}