package chat.hola.com.app.ui.recharge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.ConfirmDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Card;
import chat.hola.com.app.models.RechargeResponse;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WithdrawSuccess;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.ui.paymentgateway.PaymentGatewaysActivity;
import chat.hola.com.app.ui.transfer.TransferSuccessActivity;
import retrofit2.Response;

/**
 * <h1>RechargeActivity</h1>
 * <p>Recharge amount</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 30 Jan 2020
 */
public class RechargeActivity extends BaseActivity implements RechargeContract.View, Constants.Transaction {

    private String userId;
    private String name;
    private Data user;
    private Card card;
    private String connect_id;

    @Inject
    TypefaceManager font;
    @Inject
    Loader loader;
    @Inject
    RechargePresenter presenter;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvRechargeAmount)
    TextView tvRechargeAmount;
    @BindView(R.id.tvRecentlyAdd)
    TextView tvRecentlyAdded;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.rgAmountTag)
    RadioGroup rgAmountTag;
    @BindView(R.id.spCurrency)
    Spinner spCurrency;
    @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    @BindView(R.id.btnRecharge)
    Button btnRecharge;

    @BindView(R.id.rlUserInfo)
    RelativeLayout rlUserInfo;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.llAddNote)
    LinearLayout llAddNote;
    @BindView(R.id.tvAddNote)
    TextView tvAddNote;
    @BindView(R.id.etNote)
    TextView etNote;
    @BindView(R.id.dividerUserInfo)
    View dividerUserInfo;

    @BindView(R.id.rlBankInfo)
    RelativeLayout rlBankInfo;
    @BindView(R.id.tvAccountNum)
    TextView tvAccountNum;
    @BindView(R.id.tvBankCode)
    TextView tvBankCode;

    @BindView(R.id.rlCard)
    RelativeLayout rlCard;
    @BindView(R.id.tvCard)
    TextView tvCard;
    @BindView(R.id.tvExpDate)
    TextView tvExpDate;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    private StripeResponse.Data.ExternalAccounts.Account bankData;
    private String call;
    private String fee;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }


    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(font.getSemiboldFont());

        etAmount.setTypeface(font.getBoldFont());
        tvRechargeAmount.setTypeface(font.getMediumFont());
        tvRecentlyAdded.setTypeface(font.getMediumFont());
        tvName.setTypeface(font.getSemiboldFont());
        tvPhoneNumber.setTypeface(font.getMediumFont());
        tvAddNote.setTypeface(font.getMediumFont());
        etNote.setTypeface(font.getMediumFont());
        tvBalance.setTypeface(font.getMediumFont());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        super.initView();
        presenter.attach(this);
        presenter.suggestedAmount();
        tvTitle.setText(getString(R.string.recharge_wallet));

        tvBalance.setText("Balance: " + sessionManager.getCurrencySymbol() +
                getString(R.string.space) + sessionManager.getWalletBalance());

        String currencySymbol = sessionManager.getCurrencySymbol();
        userId = getIntent().getStringExtra("userId");

        user = (Data) getIntent().getSerializableExtra("user");
        card = (Card) getIntent().getSerializableExtra("card");
        name = "Add Card Detail";

        bankData = (StripeResponse.Data.ExternalAccounts.Account) getIntent().getSerializableExtra("data");
        connect_id = getIntent().getStringExtra("connect_id");
        call = getIntent().getStringExtra("call");
        fee = getIntent().getStringExtra("fee");
        if (call != null && call.equals("withdraw")) {
            tvTitle.setText(getString(R.string.withdraw));
            tvRechargeAmount.setText(getString(R.string.withdraw_amount));
            btnRecharge.setText(getString(R.string.withdraw));

            tvRecentlyAdded.setVisibility(View.GONE);
            rgAmountTag.setVisibility(View.GONE);
            tvBalance.setVisibility(View.VISIBLE);
            rlBankInfo.setVisibility(View.VISIBLE);
            dividerUserInfo.setVisibility(View.VISIBLE);

            tvAccountNum.setText("XXXX XXXX XXXX " + bankData.getLast4());
            tvBankCode.setText(bankData.getBankName());
        } else if (card != null) {
            ivImage.setImageBitmap(Utilities.setCreditCardLogo(card.getBrand(), this));
            tvTitle.setText(getString(R.string.recharge_wallet));
            tvRechargeAmount.setText(getString(R.string.recharge_amount));
            btnRecharge.setText(getString(R.string.verify));
            tvRecentlyAdded.setVisibility(View.VISIBLE);
            rlCard.setVisibility(View.VISIBLE);
            dividerUserInfo.setVisibility(View.VISIBLE);

            tvCard.setText("XXXX XXXX XXXX " + card.getLast4());
            String year = card.getExpYear().substring(Math.max(card.getExpYear().length() - 2, 0));
            tvExpDate.setText(card.getExpMonth() + "/" + year);
        } else if (user != null) {
            tvTitle.setText(getString(R.string.send_amount));
            tvRechargeAmount.setText(getString(R.string.enter_amount).toUpperCase());
            btnRecharge.setText(getString(R.string.next));

            tvRecentlyAdded.setVisibility(View.GONE);
            rgAmountTag.setVisibility(View.GONE);
//            llAddNote.setVisibility(View.VISIBLE);
            rlUserInfo.setVisibility(View.VISIBLE);
            dividerUserInfo.setVisibility(View.VISIBLE);

            tvName.setText(user.getUserName());
            tvPhoneNumber.setText(user.getNumber());

            Glide.with(this).load(user.getProfilePic()).asBitmap().centerCrop()
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(new BitmapImageViewTarget(ivPhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivPhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        tvCurrency.setText(currencySymbol);
        etAmount.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                String str = etAmount.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 5, 2);

                if (!str2.equals(str)) {
                    etAmount.setText(str2);
                    int pos = etAmount.getText().length();
                    etAmount.setSelection(pos);
                }
            }
        });
    }

    @OnClick({R.id.ibBack, R.id.tvChangeCard, R.id.tvChangeBank})
    public void backPressed() {
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

    @OnClick(R.id.btnRecharge)
    public void recharge() {
        String amount = etAmount.getText().toString().trim();
        if (!amount.isEmpty() && Double.parseDouble(amount) > 0) {
            if (call != null && call.equals("withdraw")) {
                if (Double.parseDouble(amount) > Double.parseDouble(sessionManager.getWalletBalance())) {
                    message(R.string.insufficient_balance);
                } else {
                    walletWithdrawalConfirmation();
                }
            } else if (user != null) {
                if (Double.parseDouble(amount) > Double.parseDouble(sessionManager.getWalletBalance())) {
                    message(R.string.insufficient_balance);
                } else {
                    presenter.validateTransfer(user, amount, user.getCurrency().getCurrency());
//                    transferConfirmation();
                }
            } else if (card != null) {
                openConfirmationDialog();
            } else {
                startActivity(new Intent(this, PaymentGatewaysActivity.class).putExtra("amount", etAmount.getText().toString().trim()).putExtra("userId", userId));
            }

        } else {
            message(getString(R.string.enter_amount));
        }
    }

    @Override
    public void withdrawSuccess(WithdrawSuccess.Data data) {
        startActivity(new Intent(this, TransferSuccessActivity.class)
                .putExtra(Constants.CALL, "withdraw_completed")
                .putExtra(AMOUNT, sessionManager.getCurrencySymbol() + " " + etAmount.getText().toString())
                .putExtra(FEE, sessionManager.getCurrencySymbol() + " " + data.getFee())
                .putExtra(TRANSACTION_ID, data.getTxnId())
                .putExtra(ACC_NO, data.getAccountNumber())
                .putExtra(BANK, data.getBank())
                .putExtra(TRANSACTION_DATE, data.getCreatedAt()));
    }

    @Override
    public void withdraw(String amount, String currency) {
        Map<String, Object> map = new HashMap<>();
        map.put("pgId", sessionManager.getPgId());
        map.put("bankId", connect_id);
        map.put("walletId", sessionManager.getWalletId());
        map.put("amount", etAmount.getText().toString());
        map.put("withdrawAmount", amount);
//        map.put("withdrawCurrency", sessionManager.getCurrency());
        map.put("withdrawCurrency", "USD");// as per backend dev hardcoding USD
        map.put("pgName", sessionManager.getPgName());
        map.put("autoPayout", true);

//        map.put("notes", etNote.getText().toString());
//        map.put("userId", sessionManager.getUserId());
//        map.put("userType", "user");
//        map.put("currency", sessionManager.getCurrency());

        presenter.withdraw(map);
    }

    @Override
    public void recharge(String pgLinkId) {
        presenter.recharge(card, etAmount.getText().toString(), pgLinkId);
    }

    private void walletWithdrawalConfirmation() {

        String message = getString(R.string.withdrawal_confirmation) + " " + sessionManager.getCurrencySymbol() + etAmount.getText().toString() + " " + getString(R.string.from_wallet);
        ConfirmDialog confirmDialog = new ConfirmDialog(this, R.drawable.ic_wallet, message);
        confirmDialog.show();

        Button btnYes = confirmDialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v -> {
            presenter.withdrawalAmount(etAmount.getText().toString(), true);
            confirmDialog.dismiss();
        });
    }

    private void openConfirmationDialog() {
        String message = getString(R.string.wallet_recharge_confirmation) + " " + sessionManager.getCurrencySymbol() + etAmount.getText().toString() + "?";
        ConfirmDialog confirmDialog = new ConfirmDialog(this, R.drawable.ic_wallet, message);
        confirmDialog.show();

        Button btnYes = confirmDialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v -> {
            presenter.methods(sessionManager.getCountry());
//            presenter.methods("US");
            confirmDialog.dismiss();
        });
    }

    @Override
    public void transfered(Response<TransferResponse> response) {
        startActivity(new Intent(this, TransferSuccessActivity.class)
                .putExtra(Constants.CALL, Constants.DASHBOARD)
                .putExtra(Constants.Transaction.AMOUNT, sessionManager.getCurrencySymbol() + " " + response.body().getData().getFromAmount())
                .putExtra(Constants.Transaction.NOTE, getString(R.string.sent_transaction))
                .putExtra(Constants.Transaction.IMAGE, "")
                .putExtra(Constants.Transaction.TRANSACTION_ID, response.body().getData().getTransactionId())
                .putExtra(Constants.Transaction.FROM, sessionManager.getUserName())
                .putExtra(Constants.Transaction.TO, "")
                .putExtra(Constants.Transaction.NAME, "")
                .putExtra(Constants.Transaction.DETAIL, getString(R.string.sent_transaction))
                .putExtra(Constants.Transaction.TRIGGER, Constants.Transaction.TRANSFER)
                .putExtra(Constants.Transaction.TRANSACTION_DATE, response.body().getData().getTransactionTime()));
        finish();
    }

    @Override
    public void recharged(RechargeResponse.Recharge data) {
        Intent intent = new Intent(this, TransferSuccessActivity.class);
        intent.putExtra("call", "recharge");
        intent.putExtra("recharge", data);
        startActivity(intent);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void setSuggestedAmount(String currency_symbol, List<String> amounts) {
        for (int i = 0; i < amounts.size(); i++) {
            String amount = amounts.get(i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(currency_symbol + " " + amount);
            radioButton.setBackground(getDrawable(R.drawable.amount_selector));
            radioButton.setPadding(30, 30, 30, 30);
            radioButton.setButtonDrawable(null);
            radioButton.setTextSize(15);
            params.setMargins(10, 10, 10, 10);
            radioButton.setLayoutParams(params);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    etAmount.setText(amount);
                }
            });
            rgAmountTag.addView(radioButton);
        }
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }
}
