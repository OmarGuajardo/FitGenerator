package com.example.fitgenerator.fragments;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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


public class LaundryFragment extends Fragment {

    RecyclerView rvLaundry;
    List<ClothingItem> laundryBasket;
    ClosetAdapter closetAdapter;
    ProgressBar progressBar;
    Boolean showProgressBar = false;
    TextView tvInsufficientItems;


    // Required empty public constructor
    public LaundryFragment() {}

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
        progressBar = view.findViewById(R.id.laundryLoadingBar);
        tvInsufficientItems = view.findViewById(R.id.tvInsufficientItems);
        queryCleanItems();
        closetAdapter.notifyDataSetChanged();


        //Swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvLaundry);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ClothingItem itemRemove = laundryBasket.get(position);
            itemRemove.setWorn(false);
            laundryBasket.remove(position);
            closetAdapter.notifyDataSetChanged();
            itemRemove.saveInBackground();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getContext(),c,rvLaundry,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(R.color.colorPrimary)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_bathtub_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    public void queryCleanItems(){
        toggleLoading();
        Closet userCloset = (Closet) ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(Closet.KEY_ALL_ITEMS);
        ParseQuery query = relation.getQuery();
        query.whereEqualTo(ClothingItem.KEY_WORN,true);
        query.findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                tvInsufficientItems.setVisibility(objects.isEmpty()? View.VISIBLE : View.INVISIBLE);
                toggleLoading();
                laundryBasket.clear();
                laundryBasket.addAll(objects);
                closetAdapter.notifyDataSetChanged();
            }
        });
    }

    public void toggleLoading(){
        progressBar.setVisibility(showProgressBar? View.INVISIBLE: View.VISIBLE);
        rvLaundry.setVisibility(showProgressBar? View.VISIBLE: View.INVISIBLE);
        showProgressBar = !showProgressBar;
    }

    public void washAllItems(){
        for(ClothingItem dirtyItem : laundryBasket){
            dirtyItem.setWorn(false);
            dirtyItem.saveInBackground();
        }
        tvInsufficientItems.setVisibility(View.VISIBLE);
        laundryBasket.clear();
        closetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
//        queryCleanItems();
    }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laundry, container, false);
    }
}