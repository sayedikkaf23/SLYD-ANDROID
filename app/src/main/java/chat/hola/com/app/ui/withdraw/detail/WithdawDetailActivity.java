package chat.hola.com.app.ui.withdraw.detail;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
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
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WithdawStatus;
import chat.hola.com.app.models.WithdrawLog;
import chat.hola.com.app.ui.adapter.WithdrawStatusAdapter;

/**
 * <h1>{@link WithdawDetailActivity}</h1>
 * <p>{@link WithdawDetailActivity shows the list of bank account of current user} </p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 12 March 2020
 */

public class WithdawDetailActivity extends BaseActivity implements WithdrawDetailContract.View {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvWithdrawalMode)
    TextView tvWithdrawalMode;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.llReason)
    LinearLayout llReason;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    Loader loader;
    @Inject
    SessionManager sessionManager;
    @Inject
    WithdrawDetailPresenter presenter;
    @Inject
    WithdrawStatusAdapter withdawStatusAdapter;
    @Inject
    List<WithdawStatus> withdawStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdaw_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        super.initView();
        presenter.attachView(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(withdawStatusAdapter);
        WithdrawLog.DataResponse.Data data = (WithdrawLog.DataResponse.Data) getIntent().getSerializableExtra("data");
        if (data != null) {
            llReason.setVisibility(data.getReason() != null && !data.getReason().isEmpty() ? View.VISIBLE : View.GONE);
            String id = data.getWithdrawid().toUpperCase();
            String title;
            try {
                title = getString(R.string.account_no_demo) + getString(R.string.space) + id.substring(id.length() - 4);
            } catch (Exception e) {
                title = id;
            }

            tvTitle.setText(title);
            tvReason.setText(data.getReason());
            tvDate.setText(Utilities.getDate(data.getUpdatedatTimestamp()));
            tvAmount.setText(sessionManager.getCurrencySymbol() +
                    getString(R.string.double_inverted_comma) + data.getAmount());
            tvWithdrawalMode.setText(data.getPgname());
            presenter.withdawStatuses(data.getWithdrawid());
        }
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader != null && loader.isShowing())
            loader.dismiss();
    }

    @Override
    public void withdawStatuses(List<WithdawStatus> withdawStatuses) {
        this.withdawStatus.clear();
        this.withdawStatus.addAll(withdawStatuses);
        withdawStatusAdapter.setData(withdawStatus);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg != null ? msg : getString(msgId), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
