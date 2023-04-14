package chat.hola.com.app.profileScreen.followers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.profileScreen.followers.Model.Data;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by ankit on 19/3/18.
 */

public class FollowersActivity extends DaggerAppCompatActivity implements FollowersContract.View, ConnectivityReceiver.ConnectivityReceiverListener
        , FollowerAdapter.OnFollowUnfollowClickCallback, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    FollowersPresenter presenter;
    @Inject
    SessionManager sessionManager;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    FollowerAdapter followerAdapter;

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.recyclerFollowers)
    RecyclerView recyclerFollowers;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvEmptyMsg)
    TextView tvEmptyMsg;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.etSearch)
    EditText etSearch;

    private Unbinder unbinder;
    boolean isFollowerPage = true;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int PAGE_SIZE = Constants.PAGE_SIZE;
    private String title;
    private int page = 0;
    private String userId;
    LinearLayoutManager linearLayoutManager;
    List<Data> dataList = new ArrayList<>();
    @Inject
    BlockDialog dialog;
    private int call = 1;
    private String searchText = "";

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int[] firstVisibleItemPositions = new int[PAGE_SIZE];
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(call, userId, firstVisibleItemPosition, visibleItemCount, totalItemCount, searchText);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        unbinder = ButterKnife.bind(this);
        presenter.init();
        llNetworkError.setErrorListner(this);
        setReceivedData();
        setSwipeCallback();
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void reload() {
        onRefresh();
    }

    private void setSwipeCallback() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setReceivedData() {
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title", "Followers");
        userId = bundle.getString("userId", AppController.getInstance().getUserId());
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
        if (title != null) {
            switch (title) {
                case "Viewers":
                    llSearch.setVisibility(View.VISIBLE);
                    tvTitle.setText("" + title);
                    call = 3;
                    presenter.viewers(userId, PAGE_SIZE * page, PAGE_SIZE); // here userId is PostId
                    break;
                case "Likers":
                    llSearch.setVisibility(View.VISIBLE);
                    tvTitle.setText("" + title);
                    call = 0;
                    presenter.likers(userId, PAGE_SIZE * page, PAGE_SIZE); // here userId is PostId
                    break;
                case "Followers":
                    llSearch.setVisibility(View.VISIBLE);
                    String followers = bundle.getString("followers", "0");
                    tvTitle.setText("" + title);
                    call = 1;
                    //tvFollowerCount.setText(""+followers+" "+getResources().getString(R.string.followersTitleText));
                    presenter.loadFollowersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                    followerAdapter.setFollowers(true);
                    break;
                case "Subscribers":
                    llSearch.setVisibility(View.GONE);
                    String subscribers = bundle.getString("subscribers", "0");
                    tvTitle.setText("" + title);
                    call = 4;
                    //tvFollowerCount.setText(""+followers+" "+getResources().getString(R.string.followersTitleText));
                    presenter.loadSubscribersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                    followerAdapter.setFollowers(true);
                    break;
                default:
                    llSearch.setVisibility(View.VISIBLE);
                    String following = bundle.getString("following", "0");
                    tvTitle.setText("" + title);
                    call = 2;
                    //tvFollowerCount.setText(""+following+" "+getResources().getString(R.string.followingTitleText));
                    presenter.loadFolloweesData(PAGE_SIZE * page, PAGE_SIZE, userId);
                    followerAdapter.setFollowers(false);
                    break;
            }
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    page = 0;
                    PAGE_SIZE = 20;
                    switch (title) {
                        case "Viewers":
                            presenter.viewers(userId, PAGE_SIZE * page, PAGE_SIZE); // here userId is PostId
                            call = 3;
                            break;
                        case "Likers":
                            presenter.likers(userId, PAGE_SIZE * page, PAGE_SIZE); // here userId is PostId
                            call = 0;
                            break;
                        case "Followers":
                            presenter.loadFollowersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                            call = 1;
                            break;
                        case "Subscribers":
                            presenter.loadSubscribersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                            call = 4;
                            break;
                        default:
                            presenter.loadFolloweesData(PAGE_SIZE * page, PAGE_SIZE, userId);
                            call = 2;
                            break;
                    }

                } else {
                    searchText = s.toString();
                    switch (title) {
                        case "Viewers":
                            presenter.searchViewers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText); // here userId is PostId
                            call = 31;
                            break;
                        case "Likers":
                            presenter.searchLikers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText); // here userId is PostId                            call = 1;
                            call = 11;
                            break;
                        case "Followers":
                            presenter.searchFollwers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                            call = 21;
                            break;
                        case "Subscribers":
                            presenter.loadSubscribersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                            call = 4;
                            break;
                        default:
                            presenter.searchFollwees(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                            call = 22;
                            break;
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        followerAdapter.setOnFollowUnfollowClickCallback(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        onUserLeaveHint();
        // AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(this, getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void applyFont() {
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        //tvFollowerCount.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void recyclerViewSetup() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerFollowers.setLayoutManager(linearLayoutManager);
        recyclerFollowers.setHasFixedSize(false);
        recyclerFollowers.setAdapter(followerAdapter);
        recyclerFollowers.addOnScrollListener(recyclerViewOnScrollListener);
    }

    @Override
    public void showFollowers(List<Data> followers) {
        swipeRefreshLayout.setVisibility(followers.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(followers.isEmpty() ? View.VISIBLE : View.GONE);
        tvEmptyMsg.setText(getString(R.string.followers_empty));
        if (followers.size() < PAGE_SIZE)
            isLastPage = true;
        this.dataList.addAll(followers);
        followerAdapter.setData(this.dataList);
    }

    @Override
    public void showFollowees(List<Data> followees) {
        swipeRefreshLayout.setVisibility(followees.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(followees.isEmpty() ? View.VISIBLE : View.GONE);
        tvEmptyMsg.setText(getString(R.string.follow_someone));
        if (followees.size() < PAGE_SIZE)
            isLastPage = true;
        this.dataList.addAll(followees);
        followerAdapter.setData(this.dataList);
    }


    @Override
    public void invalidateBtn(int index, boolean isFollowing) {

    }

    @Override
    public void isDataLoading(boolean show) {
        if (show) {
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(true);
        } else {
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void clearList(boolean b) {
        if (b)
            dataList.clear();
    }

    @Override
    public void showEmpty() {
        llEmpty.setVisibility( View.VISIBLE );
        tvEmptyMsg.setText(getString(R.string.space));
    }

    @Override
    public void showSubscribers(List<Data> data) {
        swipeRefreshLayout.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
        tvEmptyMsg.setText(getString(R.string.no_one_subscriber));
        if (data.size() < PAGE_SIZE)
            isLastPage = true;
        this.dataList.addAll(data);
        followerAdapter.setData(this.dataList);
    }

    @OnClick(R.id.ivBack)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(this, isConnected ? "Internet connected" : "No internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollow(String userId) {
        presenter.follow(userId);
    }

    @Override
    public void onUnfollow(String userId) {
        presenter.unFollow(userId);
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            dataList.clear();
            page = 0;
            switch (call) {
                case 0:
                    presenter.likers(userId, PAGE_SIZE * page, PAGE_SIZE);
                    break;
                case 11:
                    presenter.searchLikers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                    break;
                case 3:
                    presenter.viewers(userId, PAGE_SIZE * page, PAGE_SIZE);
                    break;
                case 31:
                    presenter.searchViewers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                    break;
                case 1:
                    presenter.loadFollowersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                    break;
                case 2:
                    presenter.loadFolloweesData(PAGE_SIZE * page, PAGE_SIZE, userId);
                    break;
                case 21:
                    presenter.searchFollwers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                    break;
                case 22:
                    presenter.searchFollwees(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                    break;
            }
        }
    }
}
