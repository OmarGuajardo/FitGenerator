// Generated by view binder compiler. Do not edit!
package com.example.fitgenerator.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.example.fitgenerator.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityChooseFitBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton btnFAB;

  @NonNull
  public final ImageView ivBottom;

  @NonNull
  public final ImageView ivLayer;

  @NonNull
  public final ImageView ivShoes;

  @NonNull
  public final ImageView ivTop;

  private ActivityChooseFitBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton btnFAB, @NonNull ImageView ivBottom, @NonNull ImageView ivLayer,
      @NonNull ImageView ivShoes, @NonNull ImageView ivTop) {
    this.rootView = rootView;
    this.btnFAB = btnFAB;
    this.ivBottom = ivBottom;
    this.ivLayer = ivLayer;
    this.ivShoes = ivShoes;
    this.ivTop = ivTop;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityChooseFitBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityChooseFitBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_choose_fit, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityChooseFitBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnFAB;
      FloatingActionButton btnFAB = rootView.findViewById(id);
      if (btnFAB == null) {
        break missingId;
      }

      id = R.id.ivBottom;
      ImageView ivBottom = rootView.findViewById(id);
      if (ivBottom == null) {
        break missingId;
      }

      id = R.id.ivLayer;
      ImageView ivLayer = rootView.findViewById(id);
      if (ivLayer == null) {
        break missingId;
      }

      id = R.id.ivShoes;
      ImageView ivShoes = rootView.findViewById(id);
      if (ivShoes == null) {
        break missingId;
      }

      id = R.id.ivTop;
      ImageView ivTop = rootView.findViewById(id);
      if (ivTop == null) {
        break missingId;
      }

      return new ActivityChooseFitBinding((ConstraintLayout) rootView, btnFAB, ivBottom, ivLayer,
          ivShoes, ivTop);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}