// Generated by view binder compiler. Do not edit!
package com.example.webtoon_project.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.webtoon_project.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySignBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText REdtEmail;

  @NonNull
  public final EditText REdtName;

  @NonNull
  public final EditText REdtPassword;

  @NonNull
  public final Button registerButtonM;

  private ActivitySignBinding(@NonNull LinearLayout rootView, @NonNull EditText REdtEmail,
      @NonNull EditText REdtName, @NonNull EditText REdtPassword, @NonNull Button registerButtonM) {
    this.rootView = rootView;
    this.REdtEmail = REdtEmail;
    this.REdtName = REdtName;
    this.REdtPassword = REdtPassword;
    this.registerButtonM = registerButtonM;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySignBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySignBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sign, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySignBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.R_edt_email;
      EditText REdtEmail = ViewBindings.findChildViewById(rootView, id);
      if (REdtEmail == null) {
        break missingId;
      }

      id = R.id.R_edt_name;
      EditText REdtName = ViewBindings.findChildViewById(rootView, id);
      if (REdtName == null) {
        break missingId;
      }

      id = R.id.R_edt_password;
      EditText REdtPassword = ViewBindings.findChildViewById(rootView, id);
      if (REdtPassword == null) {
        break missingId;
      }

      id = R.id.register_button_M;
      Button registerButtonM = ViewBindings.findChildViewById(rootView, id);
      if (registerButtonM == null) {
        break missingId;
      }

      return new ActivitySignBinding((LinearLayout) rootView, REdtEmail, REdtName, REdtPassword,
          registerButtonM);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}