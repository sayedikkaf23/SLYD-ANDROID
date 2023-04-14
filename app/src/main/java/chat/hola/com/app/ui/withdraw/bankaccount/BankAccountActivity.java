package chat.hola.com.app.ui.withdraw.bankaccount;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.ui.recharge.RechargeActivity;
import chat.hola.com.app.ui.stripe.AddStripeActivity;
import chat.hola.com.app.ui.withdraw.adapter.BankAccountAdapter;
import chat.hola.com.app.ui.withdraw.addbankaccount.AddBankAccountActivity;
import chat.hola.com.app.ui.withdraw.bankdetail.BankDetailActivity;

/**
 * <h1>{@link BankAccountActivity}</h1>
 * <p>{@link BankAccountActivity shows the list of bank account of current user} </p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 12 March 2020
 */

public class BankAccountActivity extends BaseActivity implements BankAccountContract.View, SwipeRefreshLayout.OnRefreshListener, BankAccountAdapter.ClickListner {

    @BindView(R.id.btnAddAccount)
    Button btnAddAccount;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBankAccount)
    TextView tvBankAccount;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.tvMessage)
    TextView tvErrorTitle;
    @BindView(R.id.tvDescription)
    TextView tvErrorDescription;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    Loader loader;
    @Inject
    SessionManager sessionManager;
    @Inject
    BankAccountPresenter presenter;
    @Inject
    BankAccountAdapter bankAccountAdapter;
    @Inject
    List<StripeResponse.Data.ExternalAccounts.Account> bankList;

    private int account_status;
    private StripeResponse.Data.ExternalAccounts.Account selectedBank;
    private String fee;
    private String connect_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_account;
    }

    @Override
    public void initView() {
        super.initView();
        presenter.attachView(this);
        tvTitle.setText(getString(R.string.bank));
        swipeRefresh.setOnRefreshListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        bankAccountAdapter.setClickListner(this);
        rvList.setAdapter(bankAccountAdapter);
        swipeRefresh.setOnRefreshListener(this);
    }

    @OnClick(R.id.rlStripeAccount)
    public void stripeAccount() {
        startActivity(new Intent(this, AddStripeActivity.class));
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvBankAccount.setTypeface(typefaceManager.getMediumFont());
        tvErrorTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvErrorDescription.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick({R.id.ibBack})
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnConfirm)
    public void confirm() {
        startActivity(new Intent(this, RechargeActivity.class)
                .putExtra("call", "withdraw")
                .putExtra("data", (Serializable) selectedBank)
                .putExtra("connect_id", connect_id)
                .putExtra("fee", fee));
    }

    @OnClick(R.id.btnAddAccount)
    public void addBankAccount() {
        startActivity(new Intent(this, AddBankAccountActivity.class));
    }

    /**
     * <h1>{@link SwipeRefreshLayout's Method}</h1>
     */
    @Override
    public void onRefresh() {
        presenter.getStripeAccount();
    }

    @Override
    public void showMessage(String msg, int msgId) {

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
    public void showLoader() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoader() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showBankAccount(List<StripeResponse.Data.ExternalAccounts.Account> data, String fee) {
        this.fee = fee;
        rvList.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(!data.isEmpty() ? View.GONE : View.VISIBLE);
        bankList.clear();
        bankList.addAll(data);
        bankAccountAdapter.notifyDataSetChanged();
    }

    @Override
    public void setStatus(String statusMessage, int statusCode) {
        account_status = statusCode;
        tvStatus.setText(statusMessage);
        switch (statusCode) {
            case 1:
                //verified
                tvStatus.setTextColor(getResources().getColor(R.color.green));
                btnAddAccount.setVisibility(View.VISIBLE);
                break;
            case 2:
                //not verified
                tvStatus.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }


    @Override
    public void setList(String id, List<StripeResponse.Data.ExternalAccounts.Account> accounts, String fee) {
        this.fee = fee;
        this.connect_id = id;
        rvList.setVisibility(accounts.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(!accounts.isEmpty() ? View.GONE : View.VISIBLE);
        bankList.clear();
        bankList.addAll(accounts);
        bankAccountAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelect(StripeResponse.Data.ExternalAccounts.Account data) {
        selectedBank = data;
        btnConfirm.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetail(StripeResponse.Data.ExternalAccounts.Account data) {
        Intent intent = new Intent(this, BankDetailActivity.class);
        intent.putExtra("bankData", data);
        startActivity(intent);
    }
}
