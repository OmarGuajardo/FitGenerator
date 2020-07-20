package com.example.fitgenerator.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitgenerator.R;
import com.example.fitgenerator.activities.CreateItemActivity;
import com.example.fitgenerator.activities.ItemDetailsActivity;
import com.example.fitgenerator.models.ClothingItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseException;

import org.parceler.Parcels;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    BottomSheetListener listener;
    ClothingItem item;
    public BottomSheetDialog(ClothingItem item){
        this.item = item;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,container,false);
        MaterialCardView cvShare = v.findViewById(R.id.cvShare);
        MaterialCardView cvEdit = v.findViewById(R.id.cvEdit);
        MaterialCardView cvDelete = v.findViewById(R.id.cvDelete);
        cvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Delete item from list
                try {
                    item.delete();
                    listener.handleOnDelete(item);
                    dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        cvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Wrap the post selected and start a new CreateItem activity
                Intent intent = new Intent(getContext(), CreateItemActivity.class);
                intent.putExtra("clothingItemEdit", Parcels.wrap(item));
                startActivity(intent);

            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetListener)context;

        } catch (Exception e) {
            Log.e("BottomSheetDialog", "onAttach: ",e);
        }

    }

    public interface BottomSheetListener{
        void handleOnDelete(ClothingItem item);
    }
}
