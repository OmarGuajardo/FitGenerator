package com.example.fitgenerator.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.fragments.BottomSheetDialog;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.ItemDetailsActivity;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {

    Context context;
    List<ClothingItem> closet;

    public ClosetAdapter(Context context, List<ClothingItem> closet) {
        this.context = context;
        this.closet = closet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.clothing_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClothingItem item = closet.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return closet.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView itemCardView;
        ImageView ivItemIcon;
        TextView tvItemName;
        TextView tvItemClass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemClass = itemView.findViewById(R.id.tvItemClass);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            ivItemIcon = itemView.findViewById(R.id.ivItemIcon);
            itemCardView = itemView.findViewById(R.id.itemCardView);
        }



        public void bind(final ClothingItem item) {
            tvItemName.setText(item.getString(ClothingItem.KEY_NAME));
            itemCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(item);
                    bottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager()
                            ,"bottomSheetDialog");
                    return false;
                }
            });
            itemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ItemDetailsActivity.class);
                    i.putExtra("clothingItem", Parcels.wrap(item));
                    context.startActivity(i);
                }
            });
            tvItemClass.setText(item.getClassString());
            if(item.getClassString().equals("Top")){
                ivItemIcon.setImageResource(R.drawable.shirt_icon);
            }
            else if(item.getClassString().equals("Bottom")){
                ivItemIcon.setImageResource(R.drawable.pants_icon);
            }
            else{
                ivItemIcon.setImageResource(R.drawable.shoes_icon);

            }
        }
    }
}
