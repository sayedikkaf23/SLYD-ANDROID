package chat.hola.com.app.Utilities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.HtmlCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

/*binds the mData for events like focus change ,click errors etc */
public class BindingAdapter {
  @androidx.databinding.BindingAdapter(value = {"clearOnFocusAndDispatch",
      "hasFocus"}, requireAll = false)
  public static void setOnLayoutChangeListener(EditText view,
      final View.OnFocusChangeListener listener,
      final View.OnFocusChangeListener focusListener) {
    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          if (listener != null) {
            listener.onFocusChange(v, hasFocus);
          }
        } else {
          if (focusListener != null) {
            focusListener.onFocusChange(v, hasFocus);
          }
        }
      }
    });
  }

  @androidx.databinding.BindingAdapter("android:visibility")
  public static void setVisibility(View view, Boolean value) {
    if (view != null && value != null) {
      view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
  }

  @androidx.databinding.BindingAdapter("android:visi")
  public static void visibility(View view, Boolean value) {
    if (view != null && value != null) {
      view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
  }

  @androidx.databinding.BindingAdapter("visibleIf")
  public static void changeVisibility(@NonNull View view, boolean visible) {
    view.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  @androidx.databinding.BindingAdapter("android:vis")
  public static void setVis(View view, Boolean value) {
    if (view != null && value != null) {
      view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
  }

  @androidx.databinding.BindingAdapter("android:visibilityProductDetail")
  public static void setHistoryProdcuDetailVisible(View view, Boolean value) {
    if (view != null && value != null) {
      view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
  }

  @androidx.databinding.BindingAdapter("android:visibilityHome")
  public static void setHomeScreenVisible(View view, Boolean value) {
    if (view != null && value != null) {
      view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
  }

  @androidx.databinding.BindingAdapter("android:enable")
  public static void setEnabled(View view, Boolean value) {
    if (view != null && value != null) {
      view.setEnabled(value);
    }
  }

  @androidx.databinding.BindingAdapter("android:btnEnabled")
  public static void setEnable(View view, Boolean value) {
    switch (view.getId()) {
      case R.id.btnAddAccount:
        view.setEnabled(value);
        break;
    }
  }

  @androidx.databinding.BindingAdapter({"android:src"})
  public static void setImageViewResource(ImageView imageView, int resource) {
    imageView.setImageResource(resource);
  }

  @androidx.databinding.BindingAdapter({"android:background"})
  public static void setBackgroundColor(View view, Boolean value) {
    view.setBackgroundColor(
        value ? view.getContext().getResources().getColor(R.color.progress_primary)
            : view.getContext().getResources().getColor(R.color.colorSilverChalice));
  }

  @androidx.databinding.BindingAdapter("android:ErrorMessage")
  public static void serErrorMessage(TextInputLayout view, Boolean value) {
    if (view != null && value != null) {
      if (value) {
        view.setErrorEnabled(true);
        switch (view.getId()) {
          case R.id.tilSaveAddressName:
            view.setError(
                view.getContext().getResources().getString(R.string.signUpEnterValidName));
            break;
          case R.id.tilSaveAddressLine1:
            view.setError(view.getContext().getResources().getString(R.string.validAddressError));
            break;
          case R.id.tilSaveAddressArea:
            view.setError(view.getContext().getResources().getString(R.string.validAreaNameError));
            break;
          case R.id.tilSaveAddressCity:
            view.setError(view.getContext().getResources().getString(R.string.validCityNameError));
            break;
          case R.id.tilSaveAddressCountry:
            view.setError(
                view.getContext().getResources().getString(R.string.validCountryNameError));
            break;
          case R.id.tilSaveAddressPostalCode:
            view.setError(
                view.getContext().getResources().getString(R.string.validPostalCodeError));
            break;
        }
      } else {
        if (view.getError() != null
            && !view.getError().toString().isEmpty()) {
          view.setErrorEnabled(false);
          view.setError(null);
        }
      }
    }
  }

  @androidx.databinding.BindingAdapter("android:floatingTint")
  public static void setBackGroundTintFloating(FloatingActionButton view, Boolean value) {
    if (view != null) {
      view.setBackgroundTintList(
          (value) ? ColorStateList.valueOf(
              view.getResources().getColor(R.color.allEastBayColor))
              : ColorStateList.valueOf(
                  view.getResources().getColor(R.color.cadetBlue)));
    }
  }

  @androidx.databinding.BindingAdapter("android:ErrorMsg")
  public static void setOnError(View view, String value) {
    if (view != null && value != null && !value.isEmpty()) {
      Snackbar.make(view, value, Snackbar.LENGTH_SHORT).show();
    }
  }

  @androidx.databinding.BindingAdapter("android:RadioButton")
  public static void checkUncheckRadioButton(AppCompatRadioButton view, Boolean value) {
    if (view != null && value != null) {
      view.setChecked(value);
    }
  }

  @androidx.databinding.BindingAdapter("android:checkBox")
  public static void checkOrUncheckBox(AppCompatCheckBox view, Boolean value) {
    if (view != null && value != null) {
      view.setChecked(value);
    }
  }

  @androidx.databinding.BindingAdapter("android:htmlText")
  public static void setHtmlText(AppCompatTextView view, String value) {
    if (view != null && value != null) {
      view.setText(HtmlCompat.fromHtml(value, 0));
    }
  }

  @androidx.databinding.BindingAdapter({"app:srcCompat"})
  public static void loadImage(ImageView view, String imageUrl) {
    if (imageUrl != null && !"".equals(imageUrl)) {
      Glide.with(view.getContext()).load(imageUrl.trim())
          .asBitmap()
          .centerCrop().placeholder(R.drawable.logo).
          into(new BitmapImageViewTarget(view) {
            @Override
            protected void setResource(Bitmap resource) {
              view.setImageBitmap(resource);
            }
          });
    }
  }

  @androidx.databinding.BindingAdapter("android:visible")
  public static void setVisible(View view, Boolean value) {
    if (view != null && value != null) {
      view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
  }
}
