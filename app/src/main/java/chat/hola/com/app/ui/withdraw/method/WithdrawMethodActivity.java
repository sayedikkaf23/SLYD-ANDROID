package chat.hola.com.app.ui.withdraw.method;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WithdrawMethod;
import chat.hola.com.app.ui.adapter.WithdrawMethodAdapter;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountActivity;
import chat.hola.com.app.ui.withdraw.withdrawallog.WithdrawalLogActivity;

public class WithdrawMethodActivity extends BaseActivity implements WithdrawMethodContract.View, WithdrawMethodAdapter.ClickListner {


    @Inject
    SessionManager sessionManager;
    @Inject
    WithdrawMethodPresenter mPresenter;
    @Inject
    Loader loader;
    @Inject
    List<WithdrawMethod> withdrawMethods;
    @Inject
    WithdrawMethodAdapter adapter;

    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_methods;
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText(getString(R.string.withdraw));
        mPresenter.attach(this);
        mPresenter.walletBalance();
        mPresenter.methods(sessionManager.getCountry());
//        mPresenter.methods("US");

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        adapter.setClickListner(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @OnClick(R.id.ibBack)
    public void ibBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnWithdrawLogs)
    public void btnWithdrawLogs() {
        startActivity(new Intent(this, WithdrawalLogActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void message(String message) {
        runOnUiThread(() -> Toast.makeText(WithdrawMethodActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void message(int message) {
        message(getString(message));
    }

    @Override
    public void showLoader() {
        runOnUiThread(() -> loader.show());
    }

    @Override
    public void hideLoader() {
        runOnUiThread(() -> {
            if (loader != null && loader.isShowing())
                loader.dismiss();
        });
    }

    @Override
    public void onItemSelect(WithdrawMethod method) {
        sessionManager.setPgId(method.getPgId());
        sessionManager.setPgName(method.getName());
        switch (method.getName().toLowerCase()) {
            case "stripe":
                startActivity(new Intent(this, BankAccountActivity.class).putExtra("data", method));
                break;
        }
    }

    @Override
    public void methods(List<WithdrawMethod> methods) {
        withdrawMethods = methods;
        adapter.setData(methods);
    }

    @Override
    public void loadBalance(String currency) {
        String currencySymbol = sessionManager.getCurrencySymbol();
        String balance = currencySymbol + getString(R.string.space) + Utilities.formatMoney(Double.parseDouble(sessionManager.getWalletBalance()));
        tvBalance.setText(balance);
    }
}
