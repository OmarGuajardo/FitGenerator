package com.example.fitgenerator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitgenerator.R;

public class FitsFragment extends Fragment {

    CardView itemCardViewTop;
    CardView itemCardViewBottom;
    CardView itemCardViewShoes;
    TextView tvItemNameTop;
    TextView tvItemNameBottom;
    TextView tvItemNameShoes;

    public FitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemCardViewTop= view.findViewById(R.id.itemCardViewTop);
        itemCardViewBottom = view.findViewById(R.id.itemCardViewBottom);
        itemCardViewShoes = view.findViewById(R.id.itemCardViewShoes);
        tvItemNameTop = view.findViewById(R.id.tvItemNameTop);
        tvItemNameBottom = view.findViewById(R.id.tvItemNameBottom);
        tvItemNameShoes = view.findViewById(R.id.tvItemNameShoes);
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