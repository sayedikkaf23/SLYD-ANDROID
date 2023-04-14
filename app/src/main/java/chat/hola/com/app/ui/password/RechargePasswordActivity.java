package chat.hola.com.app.ui.password;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WithdrawSuccess;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.ui.transfer.TransferSuccessActivity;

/**
 * <h1>RechargePasswordActivity</h1>
 * <p>Recharge amount</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @since : 30 Jan 2020
 */
public class RechargePasswordActivity extends BaseActivity implements RechargePasswordContract.View, Constants.Transaction {

    private Data user;
    private String amount;
    private String note;
    private StripeResponse.Data.ExternalAccounts.Account bankData;
    private String call;

    @Inject
    TypefaceManager font;
    @Inject
    Loader loader;
    @Inject
    RechargePasswordPresenter presenter;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.tvRechargeAmount)
    TextView tvRechargeAmount;
    @BindView(R.id.tvPassword)
    TextView tvPassword;
    @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.tvResend)
    TextView tvResend;

    private CountDownTimer cTimer;
    private InputMethodManager imm;
    private static Bus bus = AppController.getBus();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_password;
    }


    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(font.getSemiboldFont());
        btnConfirm.setTypeface(font.getSemiboldFont());
        tvAmount.setTypeface(font.getSemiboldFont());
        tvCurrency.setTypeface(font.getSemiboldFont());
        etPassword.setTypeface(font.getSemiboldFont());
        tvRechargeAmount.setTypeface(font.getMediumFont());
        tvPassword.setTypeface(font.getMediumFont());
        tvFee.setTypeface(font.getMediumFont());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        super.initView();
        presenter.attach(this);
        Utilities.initSmsRetriever(this);
        bus.register(this);
        tvTitle.setText(R.string.verify);
        presenter.requestOtp();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        user = (Data) getIntent().getSerializableExtra("user");
        amount = getIntent().getStringExtra("amount");
        note = getIntent().getStringExtra("note");
        String fee = getIntent().getStringExtra("fee");

        tvCurrency.setText(sessionManager.getCurrencySymbol());

        if (fee == null || fee.isEmpty())
            fee = "0%";
        tvFee.setText(fee + " " + getString(R.string.fee_message));

//        fee = fee.replace("%", "");
//        Double amt = Double.parseDouble(amount) - ((Double.parseDouble(amount) * Double.parseDouble(fee)) / 100);
        tvAmount.setText(amount);


        bankData = (StripeResponse.Data.ExternalAccounts.Account) getIntent().getSerializableExtra("data");
        call = getIntent().getStringExtra("call");

        tvFee.setVisibility(call != null && call.equals("withdraw") ? View.VISIBLE : View.GONE);
        tvResend.setVisibility(View.GONE);

        tvResend.setVisibility(View.GONE);

        cTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvResend.setVisibility(View.GONE);
                long sec = millisUntilFinished / 1000;
                if (sec > 9) {
                    tvTimer.setText(getString(R.string.string_296) + "" + sec);
                } else {
                    tvTimer.setText(getString(R.string.string_297) + "" + sec);
                }
            }

            public void onFinish() {
                tvResend.setVisibility(View.VISIBLE);
                tvTimer.setVisibility(View.GONE);
            }
        };

        startTimer();

        if (BuildConfig.DEBUG)
            etPassword.setText("1111");
        showKeyboard();
    }

    @Override
    public void startTimer() {
        cTimer.start();
    }

    @OnClick(R.id.ibBack)
    public void backPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard();
        super.onBackPressed();
    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(int message) {
        message(getString(message));
    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader.isShowing())
            loader.dismiss();
    }

    @OnClick(R.id.btnConfirm)
    public void recharge() {
        if (note == null || note.isEmpty())
            note = "Withdraw request";
        presenter.verifyOtp(etPassword.getText().toString());
    }

    @Override
    public void transferred(TransferResponse.Data data) {
        startActivity(new Intent(this, TransferSuccessActivity.class)
                .putExtra(AMOUNT, sessionManager.getCurrencySymbol() + " " + data.getFromAmount())
                .putExtra(NOTE, note)
                .putExtra(IMAGE, user.getProfilePic())
                .putExtra(TRANSACTION_ID, data.getTransactionId())
                .putExtra(FROM, sessionManager.getUserName())
                .putExtra(NAME, user.getUserName())
                .putExtra(TO, user.getUserName())
                .putExtra(TRIGGER, PAYMENT)
                .putExtra(TO_AMOUNT, sessionManager.getCurrencySymbol() + " " + data.getToAmount())
                .putExtra(COMMISSION, sessionManager.getCurrencySymbol() + " " + data.getTransferCommission())
                .putExtra(TRANSACTION_DATE, data.getTransactionTime()));
    }

    @Override
    protected void onDestroy() {
        if (bus != null)
            bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void passwordMatched() {
        Map<String, Object> map = new HashMap<>();
        map.put("bankId", bankData.getId());
        map.put("userId", sessionManager.getUserId());
        map.put("userType", "user");
        map.put("amount", amount);
        map.put("currency", sessionManager.getCurrency());
        map.put("notes", note);
        map.put("pgId", Constants.STRIPE_ID);
        map.put("walletId", sessionManager.getWalletId());
        map.put("withdrawAmount", amount);
        map.put("withdrawCurrency", sessionManager.getCurrency());
        map.put("pgName", Constants.STRIPE);
        map.put("autoPayout", true);


        if (call != null && call.equals("withdraw")) {
            presenter.withdraw(map);
        } else if (user != null) {
            presenter.transferTo(user.getId(), user.getUserName(), note, amount);
        }
    }

    @Override
    public void withdrawSuccess(WithdrawSuccess.Data data) {
        startActivity(new Intent(this, TransferSuccessActivity.class)
                .putExtra(Constants.CALL, "withdraw_completed")
                .putExtra(AMOUNT, sessionManager.getCurrencySymbol() + " " + data.getWithdrawalAmount())
                .putExtra(FEE, sessionManager.getCurrencySymbol() + " " + data.getFee())
                .putExtra(TRANSACTION_ID, data.getTxnId())
                .putExtra(ACC_NO, data.getAccountNumber())
                .putExtra(BANK, data.getBank())
                .putExtra(TRANSACTION_DATE, data.getCreatedAt()));
    }

    @OnClick(R.id.tvResend)
    public void resend() {
        tvTimer.setVisibility(View.VISIBLE);
        presenter.requestOtp();
    }


    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etPassword.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(etPassword, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null) {

            if (etPassword != null)
                imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        }
    }

    @Subscribe
    public void getMessage(JSONObject obj) {
        try {
            if (obj.getString("eventName").equals("OTP_RECEIVED")) {
                etPassword.setText(obj.getString("otp"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
