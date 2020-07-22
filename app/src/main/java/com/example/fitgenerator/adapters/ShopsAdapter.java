package com.example.fitgenerator.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.R;
import com.example.fitgenerator.models.Shop;

import org.w3c.dom.Text;

import java.util.List;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    String TAG = "ShopsAdapter.java";
    List<Shop> shopList;
    Context context;

    public ShopsAdapter(List<Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.shop_item,parent,false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop newShop = shopList.get(position);
        holder.bind(newShop);
        Log.d(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName;
        TextView tvShopVicinity;
        TextView tvShopOpen;
        RatingBar ratingBar;
        CardView cvShopItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvShopVicinity= itemView.findViewById(R.id.tvShopVicinity);
            tvShopOpen= itemView.findViewById(R.id.tvShopOpen);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            cvShopItem = itemView.findViewById(R.id.cvShopItem);


        }

        public void bind(final Shop newShop) {
            Log.d(TAG, "bind: " + newShop.getName());
            tvShopName.setText(newShop.getName());
            tvShopVicinity.setText(newShop.getVicinity());
            ratingBar.setRating(Float.valueOf((float) newShop.getRating()));

            tvShopOpen.setText(newShop.getOpen_now()? "OPEN":"CLOSED");
            tvShopOpen.setTextColor(newShop.getOpen_now()?context.getResources().getColor(R.color.primaryTextColor)
                    :context.getResources().getColor(R.color.red));
            //This onClickListener will redirect the user to Google Maps
            //where it will start a navigation from where the user is currently located at
            //to the address they selected
            cvShopItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(newShop.getVicinity()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);
                    }
                }
            });
        }


        
        
    }
    
}
