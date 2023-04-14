package chat.hola.com.app.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.qr_code_scanner.QRCodeActivity;
import chat.hola.com.app.ui.adapter.WalletTransactionAdapter;
import chat.hola.com.app.ui.cards.CardActivity;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.ui.qrcode.WalletQrCodeActivity;
import chat.hola.com.app.ui.transfer.TransferSuccessActivity;
import chat.hola.com.app.ui.validate.ValidateActivity;
import chat.hola.com.app.ui.withdraw.method.WithdrawMethodActivity;
import chat.hola.com.app.webScreen.WebActivity;

import com.ezcall.android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import javax.inject.Inject;

/**
 * <h1>WalletDashboardActivity</h1>
 * <p>Wallet dashboard displays wallet amount,and can process for recharge wallet, withdraw, scan & pay and transaction logs </p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 29 Jan 2020
 */
public class WalletDashboardActivity extends BaseActivity implements Constants, Constants.Transaction, WalletTransactionAdapter.ClickListner, WalletDashboardContract.View, PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    TypefaceManager font;
    @Inject
    Loader loader;
    @Inject
    WalletDashboardPresenter presenter;
    @Inject
    WalletTransactionAdapter adapter;
    @Inject
    List<WalletTransactionData> transactionDataList;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvHelp)
    TextView tvHelp;
    @BindView(R.id.tvTitleWalletBalance)
    TextView tvTitleWalletBalance;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.btnRecharge)
    Button btnRecharge;
    @BindView(R.id.btnWithdraw)
    Button btnWithdraw;
    @BindView(R.id.tvCards)
    TextView tvCards;
    @BindView(R.id.tvRecentTransactions)
    TextView tvRecentTransactions;
    @BindView(R.id.tvTransactionsFilter)
    TextView tvTransactionsFilter;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.fabScan)
    FloatingActionButton fabScan;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.tvMessage)
    TextView tvErrorTitle;
    @BindView(R.id.tvDescription)
    TextView tvErrorDescription;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private LinearLayoutManager layoutManager;
    private PopupMenu transactionTypePopupMenu;
    private MenuItem menuItem;
    private String currencySymbol = "";
    private String pageState = null;
    private Integer type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_dashboard;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(font.getSemiboldFont());
        tvHelp.setTypeface(font.getSemiboldFont());
        tvBalance.setTypeface(font.getBoldFont());
        tvTitleWalletBalance.setTypeface(font.getMediumFont());
        tvRecentTransactions.setTypeface(font.getMediumFont());
        tvTransactionsFilter.setTypeface(font.getMediumFont());
        btnRecharge.setTypeface(font.getSemiboldFont());
        btnWithdraw.setTypeface(font.getSemiboldFont());
        tvCards.setTypeface(font.getSemiboldFont());
        tvErrorTitle.setTypeface(font.getSemiboldFont());
        tvErrorDescription.setTypeface(font.getMediumFont());
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText(R.string.wallet);
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        adapter.setClickListner(this);
        rvList.setAdapter(adapter);
        presenter.attach(this);
        transactionDataList.clear();

        transactionTypePopupMenu = new PopupMenu(this, tvTransactionsFilter);
        transactionTypePopupMenu.inflate(R.menu.menu_transaction_type);
        transactionTypePopupMenu.setOnMenuItemClickListener(this);

        refresh.setOnRefreshListener(this);

        //to show and hide floating on scroll of recycler view button
        rvList.addOnScrollListener(recyclerViewOnScrollListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.walletBalance();
    }

    @OnClick(R.id.btnRecharge)
    void recharge() {
        startActivity(new Intent(this, CardActivity.class)
                .putExtra(CURRENCY_SYMBOL, currencySymbol)
                .putExtra(USER_ID, sessionManager.getUserId()));
    }

    @OnClick(R.id.btnWithdraw)
    void withdraw() {
        presenter.kycVerification();
    }

    @OnClick(R.id.ivQrCode)
    void qrCode() {
        startActivity(new Intent(this, WalletQrCodeActivity.class));
    }

    @OnClick(R.id.llCards)
    void cards() {
    }

    @OnClick(R.id.tvHelp)
    void help() {
        Intent intent = new Intent(this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(URL, getResources().getString(R.string.aboutUrl));
        bundle.putString(TITLE, getResources().getString(R.string.help_title));
        intent.putExtra(URL_DATA, bundle);
        startActivity(intent);
    }

    @OnClick(R.id.tvTransactionsFilter)
    void transactionFilter() {
        transactionTypePopupMenu.show();
    }

    @OnClick(R.id.ibBack)
    void backPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
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
        try {
            if (loader != null && loader.isShowing()) loader.dismiss();
            if (refresh != null && refresh.isRefreshing()) refresh.setRefreshing(false);
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        menuItem = item;
        switch (item.getItemId()) {
            case R.id.action_transaction_all:
                type = Transaction.Type.ALL;
                tvTransactionsFilter.setText(getString(R.string.all_transactions));
                presenter.transactions(sessionManager.getWalletId(), Transaction.Type.ALL, true, null);
                return true;
            case R.id.action_transaction_pending:
                type = Type.PENDING;
                tvTransactionsFilter.setText(getString(R.string.pending_transactions));
                presenter.transactions(sessionManager.getWalletId(), Type.PENDING, true, null);
                return true;
            case R.id.action_transaction_credit:
                type = Type.CREDIT;
                tvTransactionsFilter.setText(getString(R.string.credit_transactions));
                presenter.transactions(sessionManager.getWalletId(), Transaction.Type.CREDIT, true, null);
                return true;
            case R.id.action_transaction_debit:
                type = Type.DEBIT;
                tvTransactionsFilter.setText(getString(R.string.debit_transactions));
                presenter.transactions(sessionManager.getWalletId(), Transaction.Type.DEBIT, true, null);
                return true;
            default:
                return false;
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void loadBalance(String currency) {
        currencySymbol = sessionManager.getCurrencySymbol();
//        String balance = currencySymbol + " " + Utilities.formatMoney(Double.parseDouble(sessionManager.getWalletBalance()));
        String balance = currencySymbol + " " + sessionManager.getWalletBalance();
        tvBalance.setText(balance);
    }

    @Override
    public void setData(List<WalletTransactionData> transactionDataList, String pageState, boolean isFirst) {
        llEmpty.setVisibility(transactionDataList.isEmpty() ? View.VISIBLE : View.GONE);
        rvList.setVisibility(transactionDataList.isEmpty() ? View.GONE : View.VISIBLE);
        this.pageState = pageState;

        if (isFirst)
            this.transactionDataList.clear();
        this.transactionDataList.addAll(transactionDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void moveNext(Integer verificationStatus) {
        switch (verificationStatus) {
            case 0:
                //not approved
                startActivity(new Intent(this, ValidateActivity.class)
                        .putExtra("image", R.drawable.ic_validate)
                        .putExtra("title", getString(R.string.kyc_not_verified))
                        .putExtra("message", getString(R.string.kyc_not_verified_message))
                );
                break;
            case 1:
                // approved
                startActivity(new Intent(this, WithdrawMethodActivity.class));
                break;
            default:
                //not applied
                startActivity(new Intent(this, KycActivity.class));
                break;
        }
    }

    @Override
    public void noData() {
        llEmpty.setVisibility(View.VISIBLE);
        rvList.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        if (menuItem != null) {
            switch (menuItem.getItemId()) {
                case R.id.action_transaction_all:
                    tvTransactionsFilter.setText(getString(R.string.all_transactions));
                    type = Transaction.Type.ALL;
                    presenter.transactions(sessionManager.getWalletId(), Transaction.Type.ALL, true, null);
                    break;
                case R.id.action_transaction_pending:
                    type = Type.PENDING;
                    tvTransactionsFilter.setText(getString(R.string.pending_transactions));
                    presenter.transactions(sessionManager.getWalletId(), Type.PENDING, true, null);
                    break;
                case R.id.action_transaction_credit:
                    type = Type.CREDIT;
                    tvTransactionsFilter.setText(getString(R.string.credit_transactions));
                    presenter.transactions(sessionManager.getWalletId(), Transaction.Type.CREDIT, true, null);
                    break;
                case R.id.action_transaction_debit:
                    type = Type.DEBIT;
                    tvTransactionsFilter.setText(getString(R.string.debit_transactions));
                    presenter.transactions(sessionManager.getWalletId(), Transaction.Type.DEBIT, true, null);
                    break;
            }
        } else {
            type = Transaction.Type.ALL;
            tvTransactionsFilter.setText(getString(R.string.all_transactions));
            presenter.transactions(sessionManager.getWalletId(), Transaction.Type.ALL, true, null);
        }
    }

    @OnClick(R.id.fabScan)
    public void scan() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(WalletDashboardActivity.this, QRCodeActivity.class);
                            intent.putExtra(Constants.FROM, WALLET);
                            startActivityForResult(intent, SCAN_QR_CODE);
                        } else if (!report.isAnyPermissionPermanentlyDenied()) {
                            message(getString(R.string.need_camera_permission));
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, navigate user to app settings
                            message(getString(R.string.give_camera_permission));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    @Override
    public void onItemClick(WalletTransactionData data) {
        if (data.getTrigger().equals("PAYMENT")) {
            startActivity(new Intent(this, TransferSuccessActivity.class)
                    .putExtra("back", true)
                    .putExtra(Constants.AMOUNT, data.getCurrencySymbol() + " " + data.getAmount())
                    .putExtra(NOTE, data.getDescription())
                    .putExtra(TRANSACTION_ID, data.getTransactionId())
                    .putExtra(Constants.FROM, data.getFrom())
                    .putExtra(TO, data.getTo())
                    .putExtra(TRIGGER, TRANSFER)
                    .putExtra(TRANSACTION_DATE, data.getTimestamp()));
        } else {
            startActivity(new Intent(this, TransferSuccessActivity.class)
                    .putExtra("back", true)
                    .putExtra(CALL, Constants.DASHBOARD)
                    .putExtra(Constants.AMOUNT, sessionManager.getCurrencySymbol() + "" + data.getAmount())
                    .putExtra(NOTE, data.getDescription())
                    .putExtra(Constants.IMAGE, data.getTrigger().equalsIgnoreCase(Trigger.TRANSFER) ? (data.getTxnType() == Type.CREDIT ? data.getSenderImage() : data.getReceiverImage()) : null)
                    .putExtra(TRANSACTION_ID, data.getTransactionId())
                    .putExtra(Transaction.FROM, data.getFrom())
                    .putExtra(TO, data.getTo())
                    .putExtra(FEE, data.getTxn_fee())
                    .putExtra(DETAIL, data.getDescription())
                    .putExtra(TRIGGER, data.getTrigger())
                    .putExtra(MODE, data.getRechargemode())
                    .putExtra(BANK, data.getBank())
                    .putExtra(TRANSACTION_DATE, data.getTimestamp()));
        }
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
                    if (dy > 0 && fabScan.getVisibility() == View.VISIBLE) {
                        fabScan.hide();
                    } else if (dy < 0 && fabScan.getVisibility() != View.VISIBLE) {
                        fabScan.show();
                    }

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    presenter.callApiOnScroll(type, false, pageState, firstVisibleItemPosition, visibleItemCount, totalItemCount);
                }
            };
}
