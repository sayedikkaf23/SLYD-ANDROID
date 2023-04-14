package chat.hola.com.app.ui.dialog;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.OUT_OF_STOCK_DIALOG_TYPE;
import static chat.hola.com.app.Utilities.Constants.TRUE;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import java.util.Objects;

public class CustomDialogUtilBuilder {
  private Activity mActivity;
  private DialogOutOfStockNotifyListener mDialogOutOfStockNotifyListener;
  private NumberOrEmailNotExistListener mNumberOrEmailNotExistListener;
  private int mDialogType;
  private String mMailId;
  private boolean mIsUpDateMandatory;
  private String mEmailOrNumNotExistMsg;

  private CustomDialogUtilBuilder(CustomDialogBuilder customDialogBuilder) {
    mActivity = customDialogBuilder.mActivity;
    mDialogOutOfStockNotifyListener = customDialogBuilder.mDialogOutOfStockNotifyListener;
    this.mNumberOrEmailNotExistListener =
        customDialogBuilder.mNumberOrEmailNotExistListener;
    this.mDialogType = customDialogBuilder.mDialogType;
    this.mMailId = customDialogBuilder.mailId;
    this.mIsUpDateMandatory = customDialogBuilder.mIsUpDateMandatory;
    this.mEmailOrNumNotExistMsg = customDialogBuilder.emailOrNumNotExistMsg;
    switch (mDialogType) {
      case OUT_OF_STOCK_DIALOG_TYPE:
        showOutOfStockDialog();
        break;
    }
  }

  private void showOutOfStockDialog() {
    final Dialog dialog = new Dialog(mActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(TRUE);
    dialog.setContentView(R.layout.dialog_outofstock_notify);
    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(
        new ColorDrawable(android.graphics.Color.TRANSPARENT));
    AppCompatTextView tvSubmit = dialog.findViewById(R.id.tvSubmit);
    AppCompatEditText etEMail = dialog.findViewById(R.id.etEMail);
    if (mMailId != null && !mMailId.isEmpty()) {
      etEMail.setText(mMailId);
      etEMail.setEnabled(FALSE);
      tvSubmit.setTextColor(mActivity.getResources().getColor(R.color.colorProductFreeSpeechBlue));
    }
    AppCompatImageView ivCross = dialog.findViewById(R.id.ivCross);
    etEMail.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        ivCross.setVisibility(editable.toString().isEmpty() ? View.GONE : View.VISIBLE);
        tvSubmit.setTextColor(
            Utilities.isEmail(Objects.requireNonNull(etEMail.getText()).toString())
                ? mActivity.getResources().getColor(R.color.colorProductFreeSpeechBlue)
                : mActivity.getResources().getColor(R.color.colorProductSeekBackground));
        tvSubmit.setEnabled(
            Utilities.isEmail(Objects.requireNonNull(etEMail.getText()).toString()));
      }
    });
    ivCross.setOnClickListener(view -> etEMail.setText(""));
    tvSubmit.setOnClickListener(view -> {
      if (Utilities.isEmail(Objects.requireNonNull(etEMail.getText()).toString())) {
        mDialogOutOfStockNotifyListener.onNotifyMail(
            Objects.requireNonNull(etEMail.getText()).toString());
        dialog.dismiss();
      }
    });
    dialog.show();
  }

  public interface DialogOutOfStockNotifyListener {
    void onNotifyMail(String mail);
  }

  public interface NumberOrEmailNotExistListener {
    void onNumberOrMail(boolean retryOrSignUp);
  }

  public interface VersionClickListener {
    void onVersionChange(boolean versonChange);
  }

  public static class CustomDialogBuilder {
    private Activity mActivity;
    private DialogOutOfStockNotifyListener mDialogOutOfStockNotifyListener;
    private NumberOrEmailNotExistListener mNumberOrEmailNotExistListener;
    private VersionClickListener mVersionClickListener;
    private int mDialogType;
    private String mailId;
    private String emailOrNumNotExistMsg;
    private boolean mIsUpDateMandatory;

    public CustomDialogBuilder(Activity activity,
        DialogOutOfStockNotifyListener dialogOutOfStockNotifyListener, int dialogType) {
      mActivity = activity;
      mDialogOutOfStockNotifyListener = dialogOutOfStockNotifyListener;
      this.mDialogType = dialogType;
    }

    public CustomDialogBuilder(Activity activity,
        NumberOrEmailNotExistListener numberOrEmailNotExistListener, int dialogType) {
      mActivity = activity;
      mNumberOrEmailNotExistListener = numberOrEmailNotExistListener;
      this.mDialogType = dialogType;
    }

    public CustomDialogBuilder(Activity activity,
        VersionClickListener versionClickListener, int dialogType) {
      mActivity = activity;
      mVersionClickListener = versionClickListener;
      this.mDialogType = dialogType;
    }


    public String getMailId() {
      return mailId;
    }

    public void setMailId(String mailId) {
      this.mailId = mailId;
    }

    public String getEmailOrNumNotExistMsg() {
      return emailOrNumNotExistMsg;
    }

    public void setEmailOrNumNotExistMsg(String emailOrNumNotExistMsg) {
      this.emailOrNumNotExistMsg = emailOrNumNotExistMsg;
    }

    public boolean isUpDateMandatory() {
      return mIsUpDateMandatory;
    }

    public void setUpDateMandatory(boolean upDateMandatory) {
      mIsUpDateMandatory = upDateMandatory;
    }

    public void buildCustomDialog() {
      new CustomDialogUtilBuilder(this);
    }
  }
}