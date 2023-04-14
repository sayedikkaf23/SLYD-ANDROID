package chat.hola.com.app.ui.paymentgateway;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PaymentGateway;
import chat.hola.com.app.ui.adapter.PaymentGatewayAdapter;
import chat.hola.com.app.ui.wallet.WebActivity;

/**
 * <h1>PaymentGatewaysActivity</h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 30 Jan 2020
 */
public class PaymentGatewaysActivity extends BaseActivity implements PaymentGatewaysContract.View, PaymentGatewayAdapter.ClickListner {

    private String amount;
    private String userId;
    private String name;

    @Inject
    TypefaceManager font;
    @Inject
    Loader loader;
    @Inject
    List<PaymentGateway> paymentGateways;
    @Inject
    PaymentGatewaysPresenter presenter;
    @Inject
    SessionManager sessionManager;
    @Inject
    PaymentGatewayAdapter adapter;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvPaymentGateways)
    RecyclerView rvPaymentGateways;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_gateways;
    }


    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(font.getSemiboldFont());
    }

    @Override
    public void initView() {
        super.initView();
        name = getIntent().getStringExtra("name");
        amount = getIntent().getStringExtra("amount");
        userId = getIntent().getStringExtra("userId");
        presenter.attach(this);
        tvTitle.setText(R.string.select_payment_mode);
        presenter.getData();
        adapter.setClickListner(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPaymentGateways.setLayoutManager(linearLayoutManager);
        rvPaymentGateways.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rvPaymentGateways.setAdapter(adapter);
    }

    @OnClick(R.id.ibBack)
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

    @Override
    public void setData(List<PaymentGateway> paymentGateways) {
        this.paymentGateways.clear();
        this.paymentGateways.addAll(paymentGateways);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelect(PaymentGateway data) {
        startActivity(new Intent(this, WebActivity.class).putExtra("name", name).putExtra("amount", amount).putExtra("userId", userId).putExtra("data", data));
        finish();
    }
}
