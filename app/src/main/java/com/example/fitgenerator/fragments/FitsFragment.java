package com.example.fitgenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitgenerator.Closet;
import com.example.fitgenerator.ClosetAdapter;
import com.example.fitgenerator.ClothingItem;
import com.example.fitgenerator.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FitsFragment extends Fragment {

    private static final String TAG = "FitsFragment";
    List<ClothingItem> fit;
    List<ClothingItem> cleanTop;
    List<ClothingItem> cleanBottom;
    List<ClothingItem> cleanShoes;
    ClosetAdapter closetAdapter;
    RecyclerView rvFits;
    public FitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cleanTop = new ArrayList<>();
        cleanBottom = new ArrayList<>();
        cleanShoes = new ArrayList<>();

        //Setting up the recycler view
        fit = new ArrayList<>();
        closetAdapter = new ClosetAdapter(getContext(),fit);
        rvFits = view.findViewById(R.id.rvFits);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFits.setLayoutManager(linearLayoutManager);
        rvFits.setAdapter(closetAdapter);

        generateOutfit();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void generateOutfit(){
        getCleanItems(Closet.KEY_TOP,cleanTop);
        getCleanItems(Closet.KEY_BOTTOM,cleanBottom);
        getCleanItems(Closet.KEY_SHOES,cleanShoes);

    }
    public void designOutfit(){
        if(!cleanTop.isEmpty()&&!cleanBottom.isEmpty()&&!cleanShoes.isEmpty()){
            fit.add(cleanTop.get(0));
            fit.add(cleanBottom.get(0));
            fit.add(cleanShoes.get(0));
            closetAdapter.notifyDataSetChanged();
        }
    }
    public void getCleanItems(String key, final List<ClothingItem> list){
        Closet userCloset = (Closet) ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(key);
        ParseQuery query = relation.getQuery();
        query.whereEqualTo(ClothingItem.KEY_WORN,false);
        query.findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                list.clear();
                list.addAll(objects);
                designOutfit();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fits, container, false);
    }
}