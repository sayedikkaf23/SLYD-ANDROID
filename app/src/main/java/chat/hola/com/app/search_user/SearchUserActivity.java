package chat.hola.com.app.search_user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.ezcall.android.R;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>BlockUserActivity</h1>
 * <p>All the comments appears on this screen.
 * User can add new comment also</p>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class SearchUserActivity extends DaggerAppCompatActivity implements SearchUserContract.View {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    public static int page = 0;

    private Unbinder unbinder;
    private String postId;
    private int category;
    private int topStars;
    private boolean isStar;

    private LinearLayoutManager layoutManager;
    private Bus bus = AppController.getBus();

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    SearchUserContract.Presenter presenter;
    @Inject
    SearchUserAdapter adapter;
    @Inject
    BlockDialog dialog;

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.ibClear)
    ImageButton ibClear;
    @BindView(R.id.llError)
    LinearLayout llError;
    @BindView(R.id.rvResult)
    RecyclerView rvResult;

    String call;
    String type;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        etSearch.setFocusableInTouchMode(true);
        etSearch.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        unbinder = ButterKnife.bind(this);
        bus.register(this);

        call = getIntent().getStringExtra("call");
        if (call == null)
            call = "";
        category = getIntent().getIntExtra("category", -1);
        isStar = getIntent().getBooleanExtra("isStar", false);

        switch (category) {
            case 0:
                // top stars
                topStars = 10;
                type = "Star";
                break;
            case 1:
                // top 50 actors
                topStars = 50;
                type = "Actor";
                break;
            case 2:
                // top 50 actress
                topStars = 50;
                type = "Actress";
                break;
        }

        if (category == 3) {
            presenter.myStars(0, 100);
        } else if (category != -1) {
            if (type != null && !type.isEmpty())
                presenter.stars(type, 0, topStars);
            else
                presenter.topStareSearch(category, 0, 100);
        }


        layoutManager = new LinearLayoutManager(this);
        rvResult.setLayoutManager(layoutManager);
        if (!isStar)
            rvResult.addOnScrollListener(recyclerViewOnScrollListener);
        adapter.setStar(isStar);
        adapter.setListener(presenter.getPresenter());
        rvResult.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //minimum 3 letters to search
                if (s.length() > 0) {
                    if (isStar) {
                        presenter.stareSearch(s.toString(), 0, 100);
                    } else {
                        loadData(s.toString());
                    }
                } else {
                    dataAvailable(false);
                }
                ibClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadData(String text) {
        switch (call) {
            case "friend":
                presenter.friendSearch(text, page, PAGE_SIZE);
                break;
            case "friendRequest":
                presenter.friendRequestSearch(text, page, PAGE_SIZE);
                break;
            default:
                presenter.search(text, page, PAGE_SIZE);
                break;
        }
    }

    @OnClick(R.id.ibClear)
    public void clear() {
        etSearch.setText("");
        ibClear.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.ivBack)
    public void close() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        unbinder.unbind();
        super.onDestroy();
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
        loadData(etSearch.getText().toString());
//        presenter.search(etSearch.getText().toString(), 0, PAGE_SIZE);
    }


    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(call, etSearch.getText().toString(), firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };

    @Override
    public void dataAvailable(boolean flag) {
        llError.setVisibility(flag ? View.GONE : View.VISIBLE);
        rvResult.setVisibility(flag ? View.VISIBLE : View.GONE);
//        tvError.setText(getString(R.string.nodata));
    }

    @Override
    public void add(Friend data) {
        Intent intent = new Intent(this, AcceptRequestActivity.class);
        intent.putExtra("userId", data.getId());
        intent.putExtra("userName", data.getUserName());
        intent.putExtra("firstName", data.getUserName());
        intent.putExtra("lastName", "");
        intent.putExtra("profilePic", data.getProfilePic());
        intent.putExtra("mobileNumber", "");
        intent.putExtra("call", "send");
        startActivity(intent);
    }

    @Override
    public void onUserSelected(Friend data) {
//        if (isStar) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", data.getId());
        startActivity(intent);
//        } else {
//            Intent intent = new Intent(this, OpponentProfile.class);
//            intent.putExtra("userId", data.getId());
//            intent.putExtra("userName", data.getUserName());
//            intent.putExtra("firstName", data.getFirstName());
//            intent.putExtra("lastName", data.getLastName());
//            intent.putExtra("profilePic", data.getProfilePic());
//            intent.putExtra("call", "profile");
//            startActivity(intent);
//        }
    }
}
