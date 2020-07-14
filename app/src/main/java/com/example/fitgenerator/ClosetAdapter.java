package com.example.fitgenerator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.activities.ItemDetailsActivity;

import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {

    Context context;
    List<String> closet;

    public ClosetAdapter(Context context, List<String> closet) {
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
        String s = closet.get(position);
        holder.bind(s);
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

        public void bind(String s) {
            tvItemName.setText(s);
            itemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ItemDetailsActivity.class);
                    context.startActivity(i);
                }
            });
        }
    }
}
