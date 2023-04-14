package chat.hola.com.app.ui.withdraw.bankdetail;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.ui.withdraw.model.bankaccount.BankData;

public class BankDetailActivity extends BaseActivity implements BankDetailContract.View {


    @Inject
    SessionManager sessionManager;
    @Inject
    BankDetailPresenter mPresenter;
    @Inject
    Loader loader;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTitleBankAccountNumber)
    TextView tvTitleBankAccountNumber;
    @BindView(R.id.tvBankAccountNumber)
    TextView tvBankAccountNumber;
    @BindView(R.id.tvTitleBank)
    TextView tvTitleBank;
    @BindView(R.id.tvBank)
    TextView tvBank;
    @BindView(R.id.tvTitleName)
    TextView tvTitleName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvTitleAccountType)
    TextView tvTitleAccountType;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    @BindView(R.id.btnDelete)
    Button btnDelete;

    private StripeResponse.Data.ExternalAccounts.Account data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_detail;
    }

    @Override
    public void initView() {
        super.initView();
        mPresenter.attach(this);
        tvTitle.setText(R.string.title_bank_details);
        data = (StripeResponse.Data.ExternalAccounts.Account) getIntent().getSerializableExtra("bankData");
        if (data != null) {
            tvBankAccountNumber.setText(getString(R.string.account_no_demo) + data.getLast4());
            tvBank.setText(data.getBankName());
            tvAccountType.setText(data.getAccountHolderType());
            tvName.setText(data.getAccountHolderName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @OnClick(R.id.btnDelete)
    public void delete() {
        mPresenter.deleteBank(data);
    }

    @OnClick(R.id.ibBack)
    public void ibBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void message(String message) {
        runOnUiThread(() -> Toast.makeText(BankDetailActivity.this, message, Toast.LENGTH_SHORT).show());
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
    public void bankDeleted() {
        message(R.string.bank_deleted);
        finish();
    }
}
