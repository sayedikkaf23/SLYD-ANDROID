package chat.hola.com.app.blockUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import chat.hola.com.app.blockUser.model.BlockUserAdapter;
import chat.hola.com.app.hastag.AutoCompleteTextView;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
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

public class BlockUserActivity extends DaggerAppCompatActivity implements AdapterView.OnItemClickListener,
        BlockUserContract.View, AutoCompleteTextView.AutoTxtCallback {
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
    BlockUserContract.Presenter commentPresenter;
    @Inject
    BlockUserAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iV_back)
    ImageView iV_back;
    @BindView(R.id.rvCommentList)
    RecyclerView rvCommentList;

    @BindView(R.id.tvTbTitle)
    TextView tvTbTitle;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;

    private LinearLayoutManager layoutManager;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_activity);
        unbinder = ButterKnife.bind(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        postId = getIntent().getStringExtra("postId");
        layoutManager = new LinearLayoutManager(this);
        //tvTbTitle.setTypeface(AppController.getInstance().getSemiboldFont());
        rvCommentList.setLayoutManager(layoutManager);
        rvCommentList.addOnScrollListener(recyclerViewOnScrollListener);
        adapter.setListener(commentPresenter.getPresenter());
        rvCommentList.setAdapter(adapter);
        commentPresenter.getBlockedUsers(0, PAGE_SIZE);

        //toolbarSetup();
        llNetworkError.setErrorListner(this);
    }

    @OnClick({R.id.iV_back})
    public  void  back(){
        onBackPressed();
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

    }

    @Override
    public void sessionExpired() {
        if (sessionManager != null)
            sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void reload() {
        //   commentPresenter.getComments(postId, 0, PAGE_SIZE);
    }


    @Override
    public void openProfile(String userId) {
        startActivity(new Intent(this, ProfileActivity.class).putExtra("userId", userId));
    }

    @Override
    public void confirm(int position, String userName) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Are you sure you want to " + " unblock " + userName + "?");
        confirm.setPositiveButton(R.string.confirm, (dialog, w) -> commentPresenter.unblock(position));
        confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
        confirm.create().show();
    }

    @Override
    public void noContent(boolean show) {
        llEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        rvCommentList.setVisibility(!show ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onHashTag(String tag) {

    }

    @Override
    public void onUserSearch(String tag) {

    }

    @Override
    public void onClear() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
            commentPresenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };

}
