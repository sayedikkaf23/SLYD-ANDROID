package chat.hola.com.app.ui.withdraw.withdrawallog;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WithdrawLog;
import chat.hola.com.app.ui.adapter.WithdrawLogsAdapter;
import chat.hola.com.app.ui.stripe.AddStripeActivity;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountActivity;
import chat.hola.com.app.ui.withdraw.detail.WithdawDetailActivity;

/**
 * <h1>{@link WithdrawalLogActivity}</h1>
 * <p> shows the list of withdraw logs</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 12 March 2020
 */

public class WithdrawalLogActivity extends BaseActivity implements WithdrawLogsAdapter.ClickListner,
        WithdrawalLogContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tvBalanceAmount)
    TextView tvBalanceAmount;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvWithdrawLogs)
    TextView tvWithdrawLogs;
    @BindView(R.id.rvLogList)
    RecyclerView rvLogList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.tvMessage)
    TextView tvErrorTitle;
    @BindView(R.id.tvDescription)
    TextView tvErrorDescription;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    Loader loader;
    @Inject
    SessionManager sessionManager;
    @Inject
    WithdrawalLogPresenter presenter;
    @Inject
    WithdrawLogsAdapter adapter;

    private String currencySymbol;
    private LinearLayoutManager layoutManager;
    private String pageState = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdrawal_log;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvBalance.setTypeface(typefaceManager.getBoldFont());
        tvBalanceAmount.setTypeface(typefaceManager.getMediumFont());
        tvWithdrawLogs.setTypeface(typefaceManager.getMediumFont());
        tvErrorTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvErrorDescription.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText(getString(R.string.withdrawal_logs));
        presenter.attachView(this);
        presenter.walletBalance();
        swipeRefresh.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(this);
        rvLogList.setLayoutManager(layoutManager);
        adapter.setClickListner(this);
        rvLogList.setAdapter(adapter);
        presenter.withdrawLogs(pageState);
        rvLogList.addOnScrollListener(recyclerViewOnScrollListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

//    @OnClick(R.id.btnWithdraw)
//    public void withDraw() {
//        startActivity(new Intent(this, WithdrawMethodActivity.class));
//    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void loadBalance(String currency) {
        currencySymbol = sessionManager.getCurrencySymbol();
        String balance = currencySymbol + getString(R.string.space) + Utilities.formatMoney(Double.parseDouble(sessionManager.getWalletBalance()));
        tvBalance.setText(balance);
    }
    @Override
    public void empty() {
        rvLogList.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }
    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader.isShowing())
            loader.dismiss();

        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showWithdrawLogs(List<WithdrawLog.DataResponse.Data> data, String pageState) {
        this.pageState = pageState;
        rvLogList.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(!data.isEmpty() ? View.GONE : View.VISIBLE);
        adapter.setData(data);
    }

    @Override
    public void setStatus(String status, int i) {
        switch (i) {
            case 0:
                //create account
                startActivity(new Intent(this, AddStripeActivity.class));
                break;
            case 1:
                //verified
                startActivity(new Intent(this, BankAccountActivity.class));
                break;
            case 2:
                //unverified
                showMessage(getString(R.string.stripe_account_not_verified), -1);
                break;
        }
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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

    /**
     * <h1>{@link SwipeRefreshLayout's Method}</h1>
     */
    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(WithdrawLog.DataResponse.Data data) {
        startActivity(new Intent(this, WithdawDetailActivity.class).putExtra("data", data));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    presenter.callApiOnScroll(false, pageState, firstVisibleItemPosition, visibleItemCount, totalItemCount);
                }
            };

    @OnClick(R.id.llAddBankAccount)
    public void stripeAccount() {
        presenter.getStripeAccount();
    }
}
