package chat.hola.com.app.friends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AddContact.AddContactActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.search_user.SearchUserActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>FriendsActivity</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class FriendsActivity extends DaggerAppCompatActivity implements FriendsContract.View {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;

    private Unbinder unbinder;
    private String postId;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    FriendsContract.Presenter presenter;
    @Inject
    FriendsAdapter adapter;
    @Inject
    BlockDialog dialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etSearch)
    TextView etSearch;
    @BindView(R.id.srSwipe)
    SwipeRefreshLayout srSwipe;
    @BindView(R.id.llMobileContact)
    LinearLayout llMobileContact;
    @BindView(R.id.tvMobileContact)
    TextView tvMobileContact;

    private LinearLayoutManager layoutManager;
    private Bus bus = AppController.getBus();

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frinds_activity);
        unbinder = ButterKnife.bind(this);
        bus.register(this);

        setSupportActionBar(toolbar);
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        etSearch.setTypeface(typefaceManager.getRegularFont());
        tvMobileContact.setTypeface(typefaceManager.getSemiboldFont());

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider));
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.addOnScrollListener(recyclerViewOnScrollListener);
        rvList.addItemDecoration(itemDecorator);
        adapter.setClickListner(presenter.getPresenter());
        rvList.setAdapter(adapter);

        srSwipe.setOnRefreshListener(this::reload);
        etSearch.setOnClickListener(v -> startActivity(new Intent(FriendsActivity.this, SearchUserActivity.class).putExtra("call", "friend")));

//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                presenter.friendsSearch(s.toString(), 0, PAGE_SIZE);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        presenter.friends(0, PAGE_SIZE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("reloadFriends"))
                presenter.friends(0, PAGE_SIZE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivBack)
    public void close() {
        onBackPressed();
    }

    @OnClick(R.id.addContact)
    public void addContact() {
        startActivity(new Intent(this, AddContactActivity.class));
    }

    @OnClick(R.id.llMobileContact)
    public void mobileContact() {
        startActivity(new Intent(this, DiscoverActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        try {
            bus.unregister(this);
            unbinder.unbind();
            super.onDestroy();
        } catch (Exception ignored) {
        }
    }


    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {
        if (sessionManager != null)
            sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
    }

    @Override
    public void reload() {
        presenter.friends(0, PAGE_SIZE);
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
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };


    @Override
    public void openProfile(Friend data) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", data.getId());
//        intent.putExtra("userName", data.getUserName());
//        intent.putExtra("firstName", data.getFirstName());
//        intent.putExtra("lastName", data.getLastName());
//        intent.putExtra("profilePic", data.getProfilePic());
//        intent.putExtra("call", "profile");
        startActivity(intent);
    }

    @Override
    public void loading(boolean b) {
        if (srSwipe != null)
            srSwipe.setRefreshing(b);
    }

    @Override
    public void openRequest(Friend data) {
        Intent intent = new Intent(this, AcceptRequestActivity.class);
        intent.putExtra("userId", data.getId());
        intent.putExtra("userName", data.getUserName());
        intent.putExtra("firstName", data.getFirstName());
        intent.putExtra("lastName", data.getLastName());
        intent.putExtra("profilePic", data.getProfilePic());
        intent.putExtra("mobileNumber", data.getNumber());
        intent.putExtra("message", data.getMessage());
        intent.putExtra("call", "receive");
        startActivity(intent);
    }
}
