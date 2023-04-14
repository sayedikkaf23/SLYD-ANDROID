package chat.hola.com.app.home.activity.youTab.followrequest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.profileScreen.ProfileActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>ChannelRequestersActivity</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class FollowRequestActivity extends DaggerAppCompatActivity implements FollowRequestContract.View {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private Unbinder unbinder;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    FollowRequestContract.Presenter presenter;
    @Inject
    FollowRequestAdapter adapter;

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

    private LinearLayoutManager layoutManager;

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
        tvTbTitle.setText(getResources().getString(R.string.follow_request));
        tvTbTitle.setTypeface(AppController.getInstance().getSemiboldFont());
//        presenter.loadData(getIntent());
        presenter.getData(0, PAGE_SIZE);
        llNetworkError.setErrorListner(this);
        layoutManager = new LinearLayoutManager(this);
        rvRequestList.setLayoutManager(layoutManager);
        rvRequestList.addOnScrollListener(recyclerViewOnScrollListener);
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
    public void callUser(String userId) {
        startActivity(new Intent(this, ProfileActivity.class).putExtra("userId", userId));
    }

    @Override
    public void reload() {
        presenter.getData(0, PAGE_SIZE);
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int[] firstVisibleItemPositions = new int[PAGE_SIZE];
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };
}
