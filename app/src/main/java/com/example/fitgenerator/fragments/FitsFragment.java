package com.example.fitgenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fitgenerator.adapters.FitsMainAdapter;
import com.example.fitgenerator.models.Closet;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.R;
import com.example.fitgenerator.models.Fit;
import com.example.fitgenerator.models.Section;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FitsFragment extends Fragment {

    private static final String TAG = "FitsFragment";
    RecyclerView rvFits;
    List<Fit> listFits;
    LinearLayoutManager layoutManager;
    FitsMainAdapter fitsMainAdapter;
    ProgressBar progressBar;
    List<Section> sectionList = new ArrayList<>();
    public FitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up the Recycler View
        listFits = new ArrayList<>();
        rvFits = view.findViewById(R.id.rvFits);
        fitsMainAdapter = new FitsMainAdapter(sectionList,getContext());
        layoutManager = new LinearLayoutManager(getContext());
        rvFits.setAdapter(fitsMainAdapter);
        rvFits.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvFits.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.progress_bar);

        queryData();
    }



    public void queryData(){
        Closet userCloset = (Closet) ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(Closet.KEY_USER_FITS);
        ParseQuery query = relation.getQuery();
        query.addDescendingOrder(Closet.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Fit>() {
            @Override
            public void done(List<Fit> fits, ParseException e) {
               if(e==null && !fits.isEmpty()){
                   listFits.clear();
                   listFits.addAll(fits);
                   displayData();
               }

            }
        });
    }

    public void displayData() {
        sectionList.clear();
        SimpleDateFormat DateFor = new SimpleDateFormat("EEE MMM d yyyy");
        List<Fit> sectionItems = new ArrayList<>();
        String sectionDate = DateFor.format(listFits.get(0).getCreatedAt());
        for (Fit fit : listFits) {
            String fitDate = DateFor.format(fit.getCreatedAt());
            if (!fitDate.equals(sectionDate)) {
                List<Fit> auxSectionItems = new ArrayList<>();
                auxSectionItems.addAll(sectionItems);
                sectionList.add(new Section(sectionDate, auxSectionItems));
                sectionDate = fitDate;
                sectionItems.clear();
            }
            sectionItems.add(fit);
        }
        sectionList.add(new Section(sectionDate, sectionItems));
        fitsMainAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fits, container, false);
    }


}