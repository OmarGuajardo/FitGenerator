// Generated by view binder compiler. Do not edit!
package com.example.fitgenerator.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.example.fitgenerator.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnRegister;

  @NonNull
  public final TextView btnReturn;

  @NonNull
  public final EditText tvEmail;

  @NonNull
  public final EditText tvPassword;

  @NonNull
  public final EditText tvUserName;

  private ActivityRegisterBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnRegister,
      @NonNull TextView btnReturn, @NonNull EditText tvEmail, @NonNull EditText tvPassword,
      @NonNull EditText tvUserName) {
    this.rootView = rootView;
    this.btnRegister = btnRegister;
    this.btnReturn = btnReturn;
    this.tvEmail = tvEmail;
    this.tvPassword = tvPassword;
    this.tvUserName = tvUserName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnRegister;
      Button btnRegister = rootView.findViewById(id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.btnReturn;
      TextView btnReturn = rootView.findViewById(id);
      if (btnReturn == null) {
        break missingId;
      }

      id = R.id.tvEmail;
      EditText tvEmail = rootView.findViewById(id);
      if (tvEmail == null) {
        break missingId;
      }

      id = R.id.tvPassword;
      EditText tvPassword = rootView.findViewById(id);
      if (tvPassword == null) {
        break missingId;
      }

      id = R.id.tvUserName;
      EditText tvUserName = rootView.findViewById(id);
      if (tvUserName == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((ConstraintLayout) rootView, btnRegister, btnReturn,
          tvEmail, tvPassword, tvUserName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}