package chat.hola.com.app.wallet.wallet_detail;

import android.os.Bundle;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceData;
import dagger.android.support.DaggerAppCompatActivity;

public class WalletActivity extends DaggerAppCompatActivity implements WalletContract.View {


    Unbinder unbinder;

    @BindView(R.id.tV_balance)
    TextView tV_balance;

    @Inject
    WalletContract.Presenter presenter;
    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        unbinder = ButterKnife.bind(this);

        presenter.getWalletBalance();
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
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
    public void showBalance(WalletBalanceData data) {
        String balance = sessionManager.getCurrencySymbol() + " " + data.getBalance() + "";
        tV_balance.setText(balance);
        sessionManager.setWalletBalance(String.valueOf(data.getBalance()));
    }
}
