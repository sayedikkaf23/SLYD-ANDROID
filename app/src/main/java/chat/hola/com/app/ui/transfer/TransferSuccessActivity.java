package chat.hola.com.app.ui.transfer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.observer.ApiServiceGenerator;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.RechargeResponse;
import chat.hola.com.app.models.TranResponse;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.ui.dashboard.WalletDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * <h1>TransferSuccessActivity</h1>
 * <p>This activity used to see multiple transaction details, View will be dynamically show/hide based on data</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 11 March 2020
 */
public class TransferSuccessActivity extends BaseActivity implements Constants.Transaction {

    @Inject
    SessionManager sessionManager;

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvTransactionId)
    TextView tvTransactionId;
    @BindView(R.id.tvFrom)
    TextView tvFrom;
    @BindView(R.id.tvTo)
    TextView tvTo;
    @BindView(R.id.tvTitleTo)
    TextView tvTitleTo;
    @BindView(R.id.tvDateAndTime)
    TextView tvDateAndTime;
    @BindView(R.id.tvNote)
    TextView tvNote;

    @BindView(R.id.flToolbar)
    FrameLayout flToolbar;
    @BindView(R.id.llTransaction)
    LinearLayout llTransaction;
    @BindView(R.id.llRechargeMode)
    LinearLayout llRechargeMode;
    @BindView(R.id.llTo)
    LinearLayout llTo;
    @BindView(R.id.llFrom)
    LinearLayout llFrom;
    @BindView(R.id.llWithdraw)
    LinearLayout llWithdraw;
    @BindView(R.id.btnOk)
    Button btnOk;

    @BindView(R.id.tvTitleTransaction)
    TextView tvTitleTransaction;
    @BindView(R.id.tvTransaction)
    TextView tvTransaction;
    @BindView(R.id.tvTitleRechargeMode)
    TextView tvTitleRechargeMode;
    @BindView(R.id.tvRechargeMode)
    TextView tvRechargeMode;
    @BindView(R.id.tvAccNo)
    TextView tvAccNo;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvTitleFee)
    TextView tvTitleFee;
    @BindView(R.id.tvTitleAccNo)
    TextView tvTitleAccNo;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private String call;
    private boolean showBackButton = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_success;
    }

    @Override
    public void initView() {
        super.initView();

        tvTitle.setText(getString(R.string.transaction_detail));

        showBackButton = getIntent().getBooleanExtra("back", false);
        call = getIntent().getStringExtra(Constants.CALL);
        String trigger = getIntent().getStringExtra(TRIGGER);

        String transactionId = getIntent().getStringExtra(TRANSACTION_ID);
        String name = getIntent().getStringExtra(NAME);
        String from = getIntent().getStringExtra(FROM);
        String to = getIntent().getStringExtra(TO);
        String transactionDate = getIntent().getStringExtra(TRANSACTION_DATE);
        String image = getIntent().getStringExtra(IMAGE);
        String amount = getIntent().getStringExtra(AMOUNT);
        String note = getIntent().getStringExtra(NOTE);

        String detail = getIntent().getStringExtra(DETAIL);
        String mode = getIntent().getStringExtra(MODE);
        String bank = getIntent().getStringExtra(BANK);

        String accNo = getIntent().getStringExtra(ACC_NO);
        String fee = getIntent().getStringExtra(FEE);

        RechargeResponse.Recharge recharge = (RechargeResponse.Recharge) getIntent().getSerializableExtra("recharge");

        try {
            if (call != null && call.equals("recharge")) {
                //recharge
                flToolbar.setVisibility(showBackButton?View.VISIBLE:View.GONE);
                btnOk.setVisibility(showBackButton?View.GONE:View.VISIBLE);

                tvTitleFee.setVisibility(View.GONE);
                tvTitleAccNo.setText(getString(R.string.recharge_mode));
                tvName.setText(getString(R.string.wallet_recharged));
                tvAmount.setText(sessionManager.getCurrencySymbol() + getString(R.string.space) + recharge.getAmount());
                tvTransactionId.setText(recharge.getTxnId());
                try {
                    tvDateAndTime.setText(Utilities.getDate(recharge.getTimeStamp()));
                } catch (NumberFormatException e) {
                    tvDateAndTime.setText(Utilities.stringDateFormat(recharge.getTimeStamp()));
                }
                accNo = recharge.getRechargeMode();
                String acc_no;
                try {
                    acc_no = "XXXXXXX" + accNo.substring(accNo.length() - 4);
                } catch (Exception e) {
                    acc_no = accNo;
                }
                tvAccNo.setText(acc_no + " (" + recharge.getBank() + ")");
            } else if (call != null && call.equals("withdraw_completed")) {
                //withdrawal
                flToolbar.setVisibility(showBackButton?View.VISIBLE:View.GONE);
                btnOk.setVisibility(showBackButton?View.GONE:View.VISIBLE);

                tvName.setText(getString(R.string.submitted_succesfully));
                tvAmount.setText(amount);
                tvTransactionId.setText(transactionId);
                tvFee.setText(fee);
                try {
                    tvDateAndTime.setText(Utilities.getDate(transactionDate));
                } catch (NumberFormatException e) {
                    tvDateAndTime.setText(Utilities.stringDateFormat(transactionDate));
                }
                String acc_no;
                try {
                    acc_no = "XXXXXXX" + accNo.substring(accNo.length() - 4);
                } catch (Exception e) {
                    acc_no = accNo;
                }
                tvAccNo.setText(acc_no);
            } else {
                //transfer
                getData(transactionId);
                flToolbar.setVisibility(showBackButton?View.VISIBLE:View.GONE);
                btnOk.setVisibility(showBackButton?View.GONE:View.VISIBLE);

                tvTransactionId.setText(transactionId);
                tvName.setVisibility(View.GONE);
                tvNote.setText(note);
                tvAmount.setText(amount);

                if (trigger != null && trigger.equalsIgnoreCase(TRANSFER)) {
                    tvName.setVisibility(name != null ? View.VISIBLE : View.GONE);
                    llTo.setVisibility(to != null ? View.VISIBLE : View.GONE);
                    llFrom.setVisibility(to != null ? View.VISIBLE : View.GONE);
                    tvTransaction.setText(detail);
                } else {
                    if (mode != null && !mode.isEmpty()) {
                        llRechargeMode.setVisibility(View.VISIBLE);
                        tvRechargeMode.setText(mode);
                    } else if (bank != null && !bank.isEmpty()) {
                        bank = bank.toUpperCase();
                        String acc_no;
                        try {
                            acc_no = "XXXX XXXX " + bank.substring(bank.length() - 4);
                        } catch (Exception e) {
                            acc_no = bank;
                        }
                        llRechargeMode.setVisibility(View.VISIBLE);
                        tvTitleRechargeMode.setText(getString(R.string.account_number));
                        tvRechargeMode.setText(acc_no);
                    }
                }

                try {
                    tvDateAndTime.setText(Utilities.getDate(transactionDate));
                } catch (NumberFormatException e) {
                    tvDateAndTime.setText(Utilities.stringDateFormat(transactionDate));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tvTitleFee)
    public void feeTitle() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(getString(R.string.commission_message));
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btnOk)
    public void ok() {
        if (call != null && (call.equals("withdraw_completed") || call.equals("recharge"))) {
            startActivity(new Intent(this, WalletDashboardActivity.class));
        }
        onBackPressed();
    }

    private void getData(String transactionId) {
        HowdooService service = ApiServiceGenerator.createService(HowdooService.class);
        Call<TranResponse> call = service.transferDetail(AppController.getInstance().getApiToken(), Constants.LANGUAGE, transactionId);
        call.enqueue(new Callback<TranResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<TranResponse> call, @NotNull retrofit2.Response<TranResponse> response) {
                if (response.code() == 200) {
                    try {
                        WalletTransactionData data = response.body().getData();
                        if (data != null) {
                            tvName.setText(data.getDescription());
                            tvFrom.setText(data.getFromCurrencySymbol() + "" + data.getFromAmount());
                            tvTo.setText(data.getToCurrencySymbol() + "" + data.getToAmount());
                            tvFee.setText(data.getToCurrencySymbol() + "" + data.getTransferCommission());
                            tvTitleFee.setVisibility(View.VISIBLE);
                            tvFee.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<TranResponse> call, @NotNull Throwable t) {

            }
        });

    }
}
