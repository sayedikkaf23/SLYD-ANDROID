package chat.hola.com.app.home.activity.youTab.channelrequesters;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.activity.youTab.channelrequesters.model.ChannelRequesterAdapter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>ChannelRequestersActivity</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class ChannelRequestersActivity extends DaggerAppCompatActivity implements ChannelRequestersContract.View {

    private Unbinder unbinder;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    ChannelRequestersContract.Presenter presenter;
    @Inject
    ChannelRequesterAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvRequestList)
    RecyclerView rvRequestList;
    @BindView(R.id.tvTbTitle)
    TextView tvTbTitle;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_request_activity);
        unbinder = ButterKnife.bind(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        tvTbTitle.setText(getResources().getString(R.string.channel_request));
        tvTbTitle.setTypeface(AppController.getInstance().getSemiboldFont());
        presenter.loadData(getIntent());
        llNetworkError.setErrorListner(this);
        //   presenter.setData((List<RequestedChannels>) getIntent().getSerializableExtra("data"));
        rvRequestList.setLayoutManager(new LinearLayoutManager(this));
        rvRequestList.setAdapter(adapter);
        adapter.setListener(presenter.getPresenter());
        toolbarSetup();
    }

    private void toolbarSetup() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, TextUtils.isEmpty(msg) ? getString(msgId) : msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void requestAccepted(boolean flag) {

    }

    @Override
    public void callChannel(String channelId) {
        startActivity(new Intent(this, TrendingDetail.class).putExtra("channelId", channelId).putExtra("call", "channel"));
    }

    @Override
    public void callUser(String userId) {
        startActivity(new Intent(this, ProfileActivity.class).putExtra("userId", userId));
    }

    @Override
    public void reload() {
        presenter.loadData(getIntent());
    }
}
