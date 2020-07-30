package com.example.fitgenerator.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.FitDetailsActivity;
import com.example.fitgenerator.models.ClothingItem;
import com.example.fitgenerator.models.Fit;

import org.parceler.Parcels;

import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.ViewHolder> {

    List<Fit> items;
    Context context;
    public ChildRecyclerAdapter(List<Fit> items, Context context) {
        this.items = items;
        this.context = context;
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
        ConstraintLayout layoutFitItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutFitItem = itemView.findViewById(R.id.layoutFitItem);
            tvFitCategory = itemView.findViewById(R.id.tvFitCategory);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);

        }

        public void bind(final Fit fit) {
            tvFitCategory.setText("Chosen from " + fit.getCategory() + " selection");
            btnFavorite.setSelected(fit.getFavorite());
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean state = fit.getFavorite();
                    btnFavorite.setSelected(!state);
                    fit.setFavorite(!state);
                    fit.saveInBackground();




                }
            });
            layoutFitItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FitDetailsActivity.class);
                    intent.putExtra("fitItem", Parcels.wrap(fit));
                    context.startActivity(intent);
                }
            });

        }

    }
}
