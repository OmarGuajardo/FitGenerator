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

import com.example.fitgenerator.ClosetAdapter;
import com.example.fitgenerator.R;

import java.util.ArrayList;
import java.util.List;


public class LaundryFragment extends Fragment {

    RecyclerView rvLaundry;
    List<String> closet;
    ClosetAdapter closetAdapter;

    public LaundryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closet = new ArrayList<>();
        closet.add("Needs to be washed");
        closet.add("It smells bad");
        closet.add("Come on bro just wash it");
        closet.add("Item is not clean need to wash");

        rvLaundry = view.findViewById(R.id.rvLaundry);

        //Setting up the Recycler View with the Adapter
//        closetAdapter = new ClosetAdapter(getContext(),closet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvLaundry.setAdapter(closetAdapter);
        rvLaundry.setLayoutManager(linearLayoutManager);
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