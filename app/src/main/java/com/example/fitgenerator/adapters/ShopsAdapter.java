package com.example.fitgenerator.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        View movieView = inflater.inflate(R.layout.clothing_item,parent,false);
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
        TextView tvName;
        TextView tvSubName;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvSubName = itemView.findViewById(R.id.tvItemClass);
            icon = itemView.findViewById(R.id.ivItemIcon);


        }

        public void bind(final Shop newShop) {
            Log.d(TAG, "bind: " + newShop.getName());
            tvName.setText(newShop.getName());
            tvSubName.setText(newShop.getVicinity());
            icon.setImageResource(R.drawable.ic_baseline_shopping_cart_24);

            //This onClickListener will redirect the user to Google Maps
            //where it will start a navigation from where the user is currently located at
            //to the address they selected
            icon.setOnClickListener(new View.OnClickListener() {
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
