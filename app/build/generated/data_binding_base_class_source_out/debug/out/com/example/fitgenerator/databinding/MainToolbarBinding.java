// Generated by view binder compiler. Do not edit!
package com.example.fitgenerator.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.example.fitgenerator.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MainToolbarBinding implements ViewBinding {
  @NonNull
  private final AppBarLayout rootView;

  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final TabLayout tabBar;

  @NonNull
  public final Toolbar topAppBar;

  @NonNull
  public final TextView tvToolBarTitle;

  private MainToolbarBinding(@NonNull AppBarLayout rootView, @NonNull AppBarLayout appBarLayout,
      @NonNull TabLayout tabBar, @NonNull Toolbar topAppBar, @NonNull TextView tvToolBarTitle) {
    this.rootView = rootView;
    this.appBarLayout = appBarLayout;
    this.tabBar = tabBar;
    this.topAppBar = topAppBar;
    this.tvToolBarTitle = tvToolBarTitle;
  }

  @Override
  @NonNull
  public AppBarLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MainToolbarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MainToolbarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.main_toolbar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MainToolbarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      AppBarLayout appBarLayout = (AppBarLayout) rootView;

      id = R.id.tabBar;
      TabLayout tabBar = rootView.findViewById(id);
      if (tabBar == null) {
        break missingId;
      }

      id = R.id.topAppBar;
      Toolbar topAppBar = rootView.findViewById(id);
      if (topAppBar == null) {
        break missingId;
      }

      id = R.id.tvToolBarTitle;
      TextView tvToolBarTitle = rootView.findViewById(id);
      if (tvToolBarTitle == null) {
        break missingId;
      }

      return new MainToolbarBinding((AppBarLayout) rootView, appBarLayout, tabBar, topAppBar,
          tvToolBarTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
