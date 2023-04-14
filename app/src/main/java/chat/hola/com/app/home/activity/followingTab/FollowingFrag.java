package chat.hola.com.app.home.activity.followingTab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.activity.followingTab.model.Following;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;

/**
 * <h>FollowFrag.class</h>
 * <p>
 * This fragment shows list of posts , links and following info in
 * a recyclerView which is populated by {@link FollowingAdapter}
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class FollowingFrag extends Fragment implements FollowingContract.View, SwipeRefreshLayout.OnRefreshListener {
    private Unbinder unbinder;

    LinearLayoutManager linearLayoutManager;
    @Inject
    FollowingPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    FollowingAdapter followingAdapter;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.recyclerPost)
    RecyclerView mRecyclerPost;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.tvEmptyTitle)
    TextView tvEmptyTitle;
    @BindView(R.id.tvEmptyMsg)
    TextView tvEmptyMsg;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<Following> followings = new ArrayList<>();
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Inject
    public FollowingFrag() {
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_following_tab, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.attachView(this);
        presenter.init();
        llNetworkError.setErrorListner(this);
        applyFont();
        swipeRefresh.setOnRefreshListener(this);
        return rootView;
    }

    private void applyFont() {
        tvEmptyTitle.setTypeface(typefaceManager.getMediumFont());
        tvEmptyMsg.setTypeface(typefaceManager.getRegularFont());
    }

    @Override
    public void initPostRecycler() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerPost.setLayoutManager(linearLayoutManager);
        mRecyclerPost.setHasFixedSize(true);
        mRecyclerPost.setAdapter(followingAdapter);
        mRecyclerPost.addOnScrollListener(recyclerOnScrollListener);
        presenter.loadFollowing(0, 20);
    }

    @Override
    public void showFollowings(ArrayList<Following> followings, boolean clear) {
        if (clear)
            this.followings.clear();

        if (followings != null && !followings.isEmpty()) {
            this.followings.addAll(followings);
            followingAdapter.setData(this.followings);
            followingAdapter.setClickListner(presenter);
        } else {
            noData(clear);
        }
    }

    @Override
    public void loading(boolean isLoading) {
        swipeRefresh.setRefreshing(isLoading);
    }

    @Override
    public void noData(boolean isFirstPage) {
        if(isFirstPage) {
            this.followings.clear();
            followingAdapter.notifyDataSetChanged();
            llEmpty.setVisibility(View.VISIBLE);
            mRecyclerPost.setVisibility(View.GONE);
        }else {
            mRecyclerPost.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserClicked(String userId) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onMediaClick(int position, View view) {
        Intent intent = new Intent(getContext(), SocialDetailActivity.class);
        intent.putExtra("postId", followings.get(position).getData().getPostId());
        startActivity(intent);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(getContext(), getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void reload() {
        initPostRecycler();
    }

    @Override
    public void onRefresh() {
        presenter.loadFollowing(0, 20);
    }

    RecyclerView.OnScrollListener recyclerOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };
}
