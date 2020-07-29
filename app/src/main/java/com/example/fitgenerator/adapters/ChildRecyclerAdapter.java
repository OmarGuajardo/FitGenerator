package com.example.fitgenerator.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.R;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.models.Fit;

import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.ViewHolder> {

    List<Fit> items;

    public ChildRecyclerAdapter(List<Fit> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fit fit = items.get(position);
        holder.bind(fit);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFitCategory;
        ImageButton btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFitCategory = itemView.findViewById(R.id.tvFitCategory);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);

        }

        public void bind(Fit fit) {
            tvFitCategory.setText(fit.getObjectId());
        }
    }
}
