package com.example.fitgenerator.fragments;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fitgenerator.Closet;
import com.example.fitgenerator.ClosetAdapter;
import com.example.fitgenerator.ClothingItem;
import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.MainActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
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
    List<String> closet;
    List<ClothingItem> items;


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

        queryTop();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvCloset);



    }



    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
           int position = viewHolder.getAdapterPosition();
           ClothingItem itemRemove = items.get(position);
            try {
                itemRemove.delete();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            items.remove(position);
           closetAdapter.notifyDataSetChanged();
           Closet.getUserCloset().saveInBackground();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getContext(),c,rvCloset,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeRightBackgroundColor(R.color.colorPrimary)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_restore_from_trash_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


   public  void queryTop(){

       Closet userCloset = (Closet)ParseUser.getCurrentUser().get("UserCloset");
       ParseRelation<ClothingItem> relation = userCloset.getRelation(Closet.KEY_TOP);
//       ParseQuery query = relation.getQuery();
       relation.getQuery().findInBackground(new FindCallback<ClothingItem>() {
           @Override
           public void done(List<ClothingItem> objects, ParseException e) {
               items.clear();
               items.addAll(objects);
               closetAdapter.notifyDataSetChanged();
           }
       });


   }
    public void queryBottom(){

        Closet userCloset = (Closet)ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(Closet.KEY_BOTTOM);
//       ParseQuery query = relation.getQuery();
        relation.getQuery().findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                items.clear();
                items.addAll(objects);
                closetAdapter.notifyDataSetChanged();
            }
        });


    }
    public void queryShoes(){

        Closet userCloset = (Closet)ParseUser.getCurrentUser().get("UserCloset");
        ParseRelation<ClothingItem> relation = userCloset.getRelation(Closet.KEY_SHOES);
//       ParseQuery query = relation.getQuery();
        relation.getQuery().findInBackground(new FindCallback<ClothingItem>() {
            @Override
            public void done(List<ClothingItem> objects, ParseException e) {
                items.clear();
                items.addAll(objects);
                closetAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        queryTop();
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