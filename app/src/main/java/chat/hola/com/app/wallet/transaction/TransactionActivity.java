package chat.hola.com.app.wallet.transaction;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.wallet.transaction.all.AllFragment;
import chat.hola.com.app.wallet.transaction.credit.CreditFragment;
import chat.hola.com.app.wallet.transaction.debit.DebitFragment;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceData;
import dagger.android.support.DaggerAppCompatActivity;

public class TransactionActivity extends DaggerAppCompatActivity implements TransactionContract.View {

    @Inject
    TransactionPagerAdap transactionPagerAdap;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tV_balance)
    TextView tV_balance;
    private Unbinder unbinder;
    @Inject
    TransactionContract.Presenter presenter;
    @Inject
    ArrayList<Fragment> fragments;
    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        unbinder = ButterKnife.bind(this);

        viewPager.setAdapter(transactionPagerAdap);
        tabLayout.setupWithViewPager(viewPager);

        presenter.getWalletBalance();

        tV_balance.setText(sessionManager.getCoinBalance());
        //presenter.getTransactionList();
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(msgId), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void updateData() {
        ((AllFragment) fragments.get(0)).transactionAdap.notifyDataSetChanged();
        ((CreditFragment) fragments.get(1)).transactionAdap.notifyDataSetChanged();
        ((DebitFragment) fragments.get(2)).transactionAdap.notifyDataSetChanged();
    }
}
