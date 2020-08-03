package com.example.fitgenerator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.fitgenerator.R;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.models.Fit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseFitAdapter extends PagerAdapter {

    List<HashMap> fitList;
    Context context;
    LayoutInflater layoutInflater;
    List<ClothingItem> currentOutfit;

    ImageView ivTop;
    ImageView ivBottom;
    ImageView ivShoes;
    ImageView ivLayer;

    public ChooseFitAdapter(List<HashMap> fitList, Context context) {
        currentOutfit = new ArrayList<>();
        this.fitList = fitList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fitList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.choose_fit_item,container,false);
        ivTop = view.findViewById(R.id.ivTop);
        ivBottom = view.findViewById(R.id.ivBottom);
        ivLayer = view.findViewById(R.id.ivLayer);
        ivShoes = view.findViewById(R.id.ivShoes);
        updateCurrentOutfit(position);
        container.addView(view);
        return view;
    }

    private void attachPicsToView() {
        List<ImageView> ivViewList = new ArrayList<>();

        if(currentOutfit.size() > 3){
            ivViewList.add(ivLayer);
        }
        else{
            ivLayer.setVisibility(View.GONE);
        }
        ivViewList.add(ivTop);
        ivViewList.add(ivBottom);
        ivViewList.add(ivShoes);
        for(int i = 0; i < currentOutfit.size();i++){
            Glide.with(context)
                    .load(currentOutfit.get(i).getImageURL())
                    .fitCenter()
                    .into(ivViewList.get(i));
        }
    }
    public void updateCurrentOutfit(int position){
        currentOutfit.clear();

        if(fitList.get(position).get("Layer") != null){
            ClothingItem layerChoice =  (ClothingItem)fitList.get(position).get("Layer");
            currentOutfit.add(layerChoice);
        }
        ClothingItem topChoice =  (ClothingItem)fitList.get(position).get("Top");
        ClothingItem bottomChoice =  (ClothingItem)fitList.get(position).get("Bottom");
        ClothingItem shoesChoice =  (ClothingItem)fitList.get(position).get("Shoe");
        currentOutfit.add(topChoice);
        currentOutfit.add(bottomChoice);
        currentOutfit.add(shoesChoice);
       attachPicsToView();
    }

}
