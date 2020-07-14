package com.example.fitgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    Context context;
    String[][] itemInfo;

    public DetailsAdapter(Context context, String[][] itemInfo) {
        this.context = context;
        this.itemInfo = itemInfo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(itemInfo[position]);
    }

    @Override
    public int getItemCount() {
        return itemInfo.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescriptionTitle;
        TextView tvDescriptionBody;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescriptionBody = itemView.findViewById(R.id.tvDescriptionBody);
            tvDescriptionTitle = itemView.findViewById(R.id.tvDescriptionTitle);
        }

        public void bind(String[] itemInfo) {
            tvDescriptionTitle.setText(itemInfo[0]);
            tvDescriptionBody.setText(itemInfo[1]);
        }
    }
}