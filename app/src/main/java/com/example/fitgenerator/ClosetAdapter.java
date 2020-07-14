package com.example.fitgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        ImageView ivItemIcon;
        TextView tvItemName;
        TextView tvItemClass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemClass = itemView.findViewById(R.id.tvItemClass);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            ivItemIcon = itemView.findViewById(R.id.ivItemIcon);
        }

        public void bind(String s) {
            tvItemName.setText(s);
        }
    }
}
