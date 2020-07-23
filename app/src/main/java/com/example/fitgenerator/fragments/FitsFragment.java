package com.example.fitgenerator.fragments;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.adapters.ClosetAdapter;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.R;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FitsFragment extends Fragment {

    private static final String TAG = "FitsFragment";
    FitsFragmentListener listener;
    List<ClothingItem> fit;
    List<ClothingItem> cleanTop;
    List<ClothingItem> cleanBottom;
    List<ClothingItem> cleanShoes;
    ClosetAdapter closetAdapter;
    RecyclerView rvFits;
    MaterialButton btnUseFit;
    TextView tvInsufficientItems;
    public FitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        cleanTop = new ArrayList<>();
        cleanBottom = new ArrayList<>();
        cleanShoes = new ArrayList<>();
        btnUseFit = view.findViewById(R.id.btnUseFit);
        tvInsufficientItems = view.findViewById(R.id.tvInsufficientItems);


        //Setting up the recycler view
        fit = new ArrayList<>();
        closetAdapter = new ClosetAdapter(getContext(),fit);
        rvFits = view.findViewById(R.id.rvFits);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFits.setLayoutManager(linearLayoutManager);
        rvFits.setAdapter(closetAdapter);

        btnUseFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUseFit.setVisibility(View.INVISIBLE);
                rvFits.setVisibility(View.GONE);
                tvInsufficientItems.setVisibility(View.VISIBLE);
                tvInsufficientItems.setText("Enjoy your fit!");
                for(ClothingItem fitItem : fit){
                    if(!fitItem.getClassString().equals("Shoes")){
                        fitItem.setWorn(true);
                    }
                    fitItem.addUses();
                    fitItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            listener.handleOnLoveIt();
                        }
                    });
                }
            }
        });

        if(fit.isEmpty()){
            Log.d(TAG, "MAKING NEW FIT");
            generateOutfit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void generateOutfit(){

        cleanTop.clear();
        cleanBottom.clear();
        cleanShoes.clear();
        getCleanItems(Closet.KEY_TOP,cleanTop);
        getCleanItems(Closet.KEY_BOTTOM,cleanBottom);
        getCleanItems(Closet.KEY_SHOES,cleanShoes);
    }
    public void designOutfit(){
        if(!cleanTop.isEmpty()&&!cleanBottom.isEmpty()&&!cleanShoes.isEmpty()){
            fit.clear();
            ClothingItem selectedTop = getRandomElement(cleanTop);
            ClothingItem selectedBottom = getRandomElement(cleanBottom);
            ClothingItem selectedShoes= getRandomElement(cleanShoes);
            while(selectedTop.getColor().equals(selectedBottom.getColor())){
                cleanTop.remove(selectedTop);
                selectedTop = getRandomElement(cleanTop);
                if(cleanTop.isEmpty()){
                    break;
                }
            }
            fit.add(selectedTop);
            fit.add(selectedBottom);
            fit.add(selectedShoes);
            closetAdapter.notifyDataSetChanged();
            btnUseFit.setVisibility(View.VISIBLE);
            rvFits.setVisibility(View.VISIBLE);
            tvInsufficientItems.setVisibility(View.INVISIBLE);
        }
    }
    
    public ClothingItem getRandomElement(List<ClothingItem> list){
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
    public void getCleanItems(final String key, final List<ClothingItem> list){
        Closet userCloset = (Closet) ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(key);
        ParseQuery query = relation.getQuery();
        query.whereEqualTo(ClothingItem.KEY_WORN,false);
        query.findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                if(!objects.isEmpty()){
                    list.addAll(objects);
                    designOutfit();
                    return;
                }
                handleInsufficientItems();
            }
        });
    }

    public void handleInsufficientItems(){
        btnUseFit.setVisibility(View.INVISIBLE);
        rvFits.setVisibility(View.GONE);
        tvInsufficientItems.setVisibility(View.VISIBLE);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fits, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FitsFragmentListener) context;

        } catch (Exception e) {
            Log.e("FitsFragment", "onAttach: ",e);
        }

    }

    public interface FitsFragmentListener{
        void handleOnLoveIt();
    }
}