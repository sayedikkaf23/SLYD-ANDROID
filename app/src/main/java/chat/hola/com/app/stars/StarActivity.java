package chat.hola.com.app.stars;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AddContact.AddContactActivity;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.friends.FriendsActivity;
import chat.hola.com.app.home.contact.ContactPresenter;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.search_user.SearchUserActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class StarActivity extends DaggerAppCompatActivity implements StarAdapter.ClickListner, StarContact.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    SessionManager sessionManager;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    BlockDialog dialog;
    @Inject
    StarContact.Presenter presenter;
    @Inject
    StarAdapter adapter;

    @BindView(R.id.rvFriends)
    RecyclerView rvFriends;
    @BindView(R.id.rvNewFriends)
    RecyclerView rvNewFriends;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvNewFriends)
    TextView tvNewFriends;
    @BindView(R.id.tvGroupChat)
    TextView tvGroupChat;
    @BindView(R.id.tvStarPage)
    TextView tvStarPage;
    @BindView(R.id.tvNewFriendsTitle)
    TextView tvNewFriendsTitle;
    @BindView(R.id.etSearch)
    TextView etSearch;
    @BindView(R.id.ibAddFriend)
    ImageButton ibAddFriend;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.rlNoNewFriends)
    RelativeLayout rlNoNewFriends;
    @BindView(R.id.llNewFriends)
    LinearLayout llNewFriends;
    @BindView(R.id.tvNewFriendCount)
    TextView tvNewFriendCount;
    @BindView(R.id.llFriend)
    LinearLayout llFriend;
    @BindView(R.id.llStar)
    LinearLayout llStar;
    @BindView(R.id.tvTop10Stars)
    TextView tvTop10Stars;
    @BindView(R.id.tvTop50Actors)
    TextView tvTop50Actors;
    @BindView(R.id.tvTop50Actress)
    TextView tvTop50Actress;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    private boolean isStar = true;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_fiend);
        ButterKnife.bind(this);

        setStar(isStar);

        tvNewFriendsTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvName.setTypeface(typefaceManager.getSemiboldFont());
        tvUserName.setTypeface(typefaceManager.getRegularFont());
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvTop10Stars.setTypeface(typefaceManager.getSemiboldFont());
        tvTop50Actors.setTypeface(typefaceManager.getSemiboldFont());
        tvTop50Actress.setTypeface(typefaceManager.getSemiboldFont());
        tvNewFriends.setTypeface(typefaceManager.getSemiboldFont());
        tvGroupChat.setTypeface(typefaceManager.getSemiboldFont());
        tvStarPage.setTypeface(typefaceManager.getSemiboldFont());
        etSearch.setTypeface(typefaceManager.getRegularFont());

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider));
        layoutManager = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(layoutManager);
        rvFriends.setHasFixedSize(true);
        rvFriends.addItemDecoration(itemDecorator);
        adapter.setClickListner(this);
        rvFriends.setAdapter(adapter);

        refresh.setOnRefreshListener(this);
        etSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchUserActivity.class).putExtra("isStar", isStar).putExtra("call", "friend")));

        tvNewFriendCount.setVisibility(ContactPresenter.count > 0 ? View.VISIBLE : View.GONE);
        tvNewFriendCount.setText(String.valueOf(ContactPresenter.count));
        presenter.stars(0, Constants.PAGE_SIZE);

        rvFriends.addOnScrollListener(recyclerViewOnScrollListener);
    }

    @Override
    public void onItemSelect(int position) {
        onUserSelected(position);
    }

    @Override
    public void onUserSelected(int position) {
        onUserSelected(presenter.getFriend(position));
    }

    @OnClick(R.id.rlProfile)
    public void profile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void setStar(boolean isStar) {
        this.isStar = isStar;
        ibBack.setVisibility(isStar ? View.VISIBLE : View.GONE);
        tvTitle.setText(getResources().getString(isStar ? R.string.star_page : R.string.contacs));
        ibAddFriend.setVisibility(isStar ? View.GONE : View.VISIBLE);
        llFriend.setVisibility(isStar ? View.GONE : View.VISIBLE);
        llStar.setVisibility(!isStar ? View.GONE : View.VISIBLE);
        adapter.setStar(isStar);
    }

    @OnClick(R.id.llStarPage)
    public void myStars() {
        startActivity(new Intent(this, SearchUserActivity.class).putExtra("isStar", true).putExtra("category", 3));
    }

    @OnClick(R.id.ibAddFriend)
    public void addFriend() {
        startActivity(new Intent(this, AddContactActivity.class));
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.rlTop10Stars)
    public void topStars() {
        startActivity(new Intent(this, SearchUserActivity.class).putExtra("isStar", true).putExtra("category", 0));
    }

    @OnClick(R.id.llTop50Actress)
    public void topActress() {
        startActivity(new Intent(this, SearchUserActivity.class).putExtra("isStar", true).putExtra("category", 2));
    }

    @OnClick(R.id.llTop50Actors)
    public void topActors() {
        startActivity(new Intent(this, SearchUserActivity.class).putExtra("isStar", true).putExtra("category", 1));
    }


    @OnClick({R.id.rlNoNewFriends, R.id.tvNewFriendsTitle})
    public void newFriends() {
        startActivity(new Intent(this, FriendsActivity.class));
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void reload() {

        rvFriends.setVisibility(View.GONE);
        presenter.stars(0, Constants.PAGE_SIZE);
    }

    @Override
    public void noContent() {
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
    }

    @Override
    public void loadingCompleted() {
        refresh.setRefreshing(false);
        rvFriends.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(true);
        reload();
    }

    @Override
    public void onUserSelected(Friend data) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", data.getId());
        startActivity(intent);
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };
}
