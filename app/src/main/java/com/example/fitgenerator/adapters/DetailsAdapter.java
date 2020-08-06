package com.example.fitgenerator.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.R;

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
        LinearLayout itemDetailsContainer;
        TextView tvDescriptionTitle, tvDescriptionBody;
        View dummyView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescriptionBody = itemView.findViewById(R.id.tvDescriptionBody);
            tvDescriptionTitle = itemView.findViewById(R.id.tvDescriptionTitle);
            itemDetailsContainer = itemView.findViewById(R.id.itemDetailsContainer);
            dummyView = itemView.findViewById(R.id.dummyView);
        }

        public void bind(String[] itemInfo) {
            if(!itemInfo[1].isEmpty()){
                tvDescriptionTitle.setText(itemInfo[0]);
                tvDescriptionBody.setText(itemInfo[1]);

            }
            else
            {
                itemDetailsContainer.setVisibility(View.GONE);
            }

        }
    }
}
