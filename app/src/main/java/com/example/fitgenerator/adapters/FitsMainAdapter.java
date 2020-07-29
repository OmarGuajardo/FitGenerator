package com.example.fitgenerator.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitgenerator.R;
import com.example.fitgenerator.models.Fit;
import com.example.fitgenerator.models.Section;

import java.util.List;

public class FitsMainAdapter extends RecyclerView.Adapter<FitsMainAdapter.ViewHolder> {

    List<Section> sectionList;

    public FitsMainAdapter(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.header_fit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Section section = sectionList.get(position);
        String sectionName = section.getSectionName();
        List<Fit> items = section.getSectionItems();

        holder.sectionNameTextView.setText(sectionName);

        ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter(items);
        holder.childRecyclerView.setAdapter(childRecyclerAdapter);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionNameTextView;
        RecyclerView childRecyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionNameTextView = itemView.findViewById(R.id.sectionNameTextView);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        }
    }
}
