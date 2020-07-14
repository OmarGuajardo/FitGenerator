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


public class ClosetFragment extends Fragment {

    RecyclerView rvCloset;
    ClosetAdapter closetAdapter;
    List<String> closet;

    public ClosetFragment(){
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closet = new ArrayList<>();
        closet.add("Item 1");
        closet.add("Item 10");
        closet.add("Item 15");
        closet.add("Item 11");
        closet.add("Item 15");
        closet.add("Item 12");
        closet.add("Item 3");
        rvCloset = view.findViewById(R.id.rvCloset);

        //Setting up the Recycler View with the Adapter
        closetAdapter = new ClosetAdapter(getContext(),closet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCloset.setAdapter(closetAdapter);
        rvCloset.setLayoutManager(linearLayoutManager);


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