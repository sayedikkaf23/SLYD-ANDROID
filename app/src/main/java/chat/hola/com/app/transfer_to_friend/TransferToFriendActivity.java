package chat.hola.com.app.transfer_to_friend;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.DecimalDigitsInputFilter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.ui.transfer.TransferSuccessActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class TransferToFriendActivity extends DaggerAppCompatActivity implements TransferContract.View, Constants.Transaction {


    private Unbinder unbinder;
    @BindView(R.id.ivSenderProfilePic)
    ImageView ivSenderProfilePic;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ivReceiverProfilePic)
    ImageView ivReceiverProfilePic;


    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.tV_title)
    TextView tV_title;

    @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    @BindView(R.id.etNote)
    EditText etNote;
    @BindView(R.id.ll_mainView)
    LinearLayout ll_mainView;
    @BindView(R.id.ll_sucessfulView)
    LinearLayout ll_sucessfulView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_await)
    TextView tv_await;
    @Inject
    TransferContract.Presenter presenter;
    @BindView(R.id.tV_amount)
    TextView tV_amount;
    @BindView(R.id.rL_root)
    RelativeLayout rL_root;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvSenderCurrency)
    TextView tvSenderCurrency;
    @BindView(R.id.tvReceiverCurrency)
    TextView tvReceiverCurrency;
    @BindView(R.id.tvReceive)
    TextView tvReceive;

    private Data user;
    private String userId;
    private Error.Data data;
    private String call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_friend);
        unbinder = ButterKnife.bind(this);

        user = (Data) getIntent().getSerializableExtra(Constants.USER);
        if (user != null) {
            initializeView();
        } else {
            userId = (String) getIntent().getSerializableExtra("userId");
            if (userId != null)
                fetchUserInfo(userId);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initializeView() {
        Glide.with(this).load(sessionManager.getUserProfilePic()).asBitmap().centerCrop().signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.profile_one).into(new BitmapImageViewTarget(ivSenderProfilePic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivSenderProfilePic.setImageDrawable(circularBitmapDrawable);
            }
        });

        Glide.with(this).load(user.getProfilePic()).asBitmap().centerCrop().signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.profile_one).into(new BitmapImageViewTarget(ivReceiverProfilePic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivReceiverProfilePic.setImageDrawable(circularBitmapDrawable);
            }
        });


        tvCurrency.setText(sessionManager.getCurrencySymbol());
        tvSenderCurrency.setText(sessionManager.getCurrency());
        tvReceiverCurrency.setText(user.getCurrency().getCurrency());

        tvUserName.setText(getString(R.string.paying) + " " + user.getUserName());
        etAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});
        etAmount.requestFocus();

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnConfirm.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                if (s.length() > 0) {
                    Double value = Double.valueOf(s.toString());
                    presenter.validateTransfer(user.getId(), user.getUserName(), String.format("%.2f", value), user.getCurrency().getCurrency());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btnConfirm)
    public void next() {
        float amount = Float.parseFloat(etAmount.getText().toString());
        if (amount < 1) {
            showMessage(null, R.string.enter_amount);
        } else if (amount > Double.parseDouble(sessionManager.getWalletBalance())) {
            showMessage(null, R.string.insufficient_balance);
        } else {
            confirm();
        }

    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.rL_done)
    public void done() {
        setResult(1001);
        onBackPressed();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg != null ? msg : getResources().getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void transferInitiatedSuccess(String transactionId, String transactionDate) {
        if (userId == null) {
            startActivity(new Intent(this, TransferSuccessActivity.class)
                    .putExtra(AMOUNT, sessionManager.getCurrencySymbol() + " " + etAmount.getText().toString().trim())
                    .putExtra(NOTE, etNote.getText().toString().trim())
                    .putExtra(TRANSACTION_ID, transactionId)
                    .putExtra(FROM, sessionManager.getUserName())
                    .putExtra(NAME, sessionManager.getUserName())
                    .putExtra(TO, user.getUserName())
                    .putExtra(TRIGGER, TRANSFER)
                    .putExtra(TRANSACTION_DATE, transactionDate));
        } else {
            Toast.makeText(this, "Sent successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void confirm() {
        try {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.paynow_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            TextView tV_payTo, tV_amt, tvBalance;
            ImageView iV_back;
            tV_payTo = (TextView) dialog.findViewById(R.id.tV_payTo);
            tV_amt = dialog.findViewById(R.id.tV_amt);

            iV_back = dialog.findViewById(R.id.iV_back);
            tvBalance = dialog.findViewById(R.id.tvBalance);

            String payto = getString(R.string.paying) + " " + user.getUserName();
            tV_payTo.setText(payto);

            String amt = sessionManager.getCurrencySymbol() + " " + etAmount.getText().toString().trim();
            tV_amt.setText(amt);

            tvBalance.setText("Current balance: " + sessionManager.getCurrencySymbol() + " " + sessionManager.getWalletBalance());

            TextView tvWillReceive = (TextView) dialog.findViewById(R.id.tvWillReceive);
            tvWillReceive.setText(user.getUserName() + " " + getString(R.string.will_receive) + " " + user.getCurrency().getSymbol() + "" + data.getFinalAmount());

            Button btnPayNow = (Button) dialog.findViewById(R.id.btnPayNow);
            btnPayNow.setOnClickListener(v -> {
                presenter.transfer(user.getId(), user.getUserName(), etAmount.getText().toString().trim(), user.getCurrency().getCurrency(), etNote.getText().toString().trim());
                dialog.dismiss();
            });

            iV_back.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showConvertedAmount(Error.Data data) {
        this.data = data;
        tvReceive.setVisibility(data != null ? View.VISIBLE : View.GONE);
        tvReceive.setText(user.getUserName() + " " + getString(R.string.will_receive) + " " + user.getCurrency().getSymbol() + "" + data.getFinalAmount());
    }

    @OnClick(R.id.tvReceive)
    public void information() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment_information);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView tvPopupSent = dialog.findViewById(R.id.tvPopupSent);
        TextView tvPopupReceive = dialog.findViewById(R.id.tvPopupReceive);
        TextView tvPopUpReceiver = dialog.findViewById(R.id.tvPopUpReceiver);
        TextView tvPopupFee = dialog.findViewById(R.id.tvPopupFee);

        tvPopupSent.setText(sessionManager.getCurrencySymbol() + "" + etAmount.getText().toString());
        tvPopUpReceiver.setText(user.getUserName() + " will receive");
        tvPopupReceive.setText(user.getCurrency().getSymbol() + "" + data.getFinalAmount());
        tvPopupFee.setText(user.getCurrency().getSymbol() + "" + data.getAppCommission());

        TextView tvOk = dialog.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void fetchUserInfo(String userId) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ApiOnServer.OTHER_USER_PROFILE + "?memberId=" + userId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("message").equals("success")) {
                        Profile profile = new Gson().fromJson(response.toString(), Profile.class);
                        user = profile.getData().get(0);
                        if (user != null) {
                            initializeView();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> Toast.makeText(TransferToFriendActivity.this, "Oops something went wrong.", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);
                return headers;
            }
        };


        /* Add the request to the RequestQueue.*/
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "fetchUserInfo");
    }

    @Override
    public void alert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialog, which) -> finish());
        builder.show();
    }
}
