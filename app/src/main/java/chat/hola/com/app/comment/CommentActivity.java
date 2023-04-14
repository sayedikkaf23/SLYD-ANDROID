package chat.hola.com.app.comment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.comment.model.CommentAdapter;
import chat.hola.com.app.hastag.AutoCompleteTextView;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.profileScreen.ProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

/**
 * <h1>BlockUserActivity</h1>
 * <p>All the comments appears on this screen.
 * User can add new comment also</p>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class CommentActivity extends DaggerAppCompatActivity
    implements AdapterView.OnItemClickListener, CommentContract.View, TextWatcher,
    AutoCompleteTextView.AutoTxtCallback {
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  public static int page = 0;

  private Unbinder unbinder;
  private String postId;

  @Inject
  TypefaceManager typefaceManager;
  @Inject
  SessionManager sessionManager;
  @Inject
  CommentContract.Presenter commentPresenter;
  @Inject
  CommentAdapter adapter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.rvCommentList)
  RecyclerView rvCommentList;
  @BindView(R.id.ivUserProfilePic)
  ImageView ivUserProfilePic;
  @BindView(R.id.etComment)
  AutoCompleteTextView etComment;
  @BindView(R.id.ibSend)
  ImageButton ibSend;
  @BindView(R.id.tvTbTitle)
  TextView tvTbTitle;
  @BindView(R.id.llNetworkError)
  InternetErrorView llNetworkError;
  @BindView(R.id.swipeRefresh)
  SwipeRefreshLayout swipeRefresh;
  @Inject
  PostObserver postObserver;
  private LinearLayoutManager layoutManager;
  @Inject
  BlockDialog dialog;

  @Override
  public void userBlocked() {
    dialog.show();
  }

  private int initialCommentsCount;

  private int commentsAdded;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.comment_activity);
    unbinder = ButterKnife.bind(this);
    //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

    postId = getIntent().getStringExtra("postId");
    try {
      initialCommentsCount = Integer.parseInt(getIntent().getStringExtra("commentsCount"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    layoutManager = new LinearLayoutManager(this);
    if(ibSend!=null)ibSend.setEnabled(false);
    tvTbTitle.setTypeface(AppController.getInstance().getSemiboldFont());
    etComment.setTypeface(AppController.getInstance().getRegularFont());
    etComment.addTextChangedListener(this);

    Glide.with(this).load(sessionManager.getUserProfilePic()).asBitmap().centerCrop()
        //.signature(new StringSignaturcoe(String.valueOf(System.currentTimeMillis())))
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.skipMemoryCache(true)
        .into(new BitmapImageViewTarget(ivUserProfilePic) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            ivUserProfilePic.setImageDrawable(circularBitmapDrawable);
          }
        });

    rvCommentList.setLayoutManager(layoutManager);
    rvCommentList.addOnScrollListener(recyclerViewOnScrollListener);

    adapter.setListener(commentPresenter.getPresenter());
    rvCommentList.setAdapter(adapter);
    commentPresenter.getComments(postId, 0, PAGE_SIZE);

    etComment.setOnItemClickListener(this);
    etComment.setListener(this);
    toolbarSetup();
    llNetworkError.setErrorListner(this);

    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        reload();
      }
    });
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
  public void onBackPressed() {

    if (commentsAdded > 0) {
      if (postObserver != null) postObserver.postData(true);
    }

    setResult(RESULT_OK, new Intent().putExtra("position", getIntent().getIntExtra("position", 0))
        .putExtra("commentCount", String.valueOf(initialCommentsCount + commentsAdded))
        .putExtra("postId", postId));

    finish();
  }

  @OnClick(R.id.ibSend)
  public void send() {
    if (postId != null) {
      if(ibSend!=null)ibSend.setEnabled(false);
      commentPresenter.addComment(postId, etComment.getText().toString());
    }
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    super.onDestroy();
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if(ibSend!=null)ibSend.setEnabled(charSequence.length() > 0);
  }

  @Override
  public void afterTextChanged(Editable editable) {

  }

  @Override
  public void showMessage(String msg, int msgId) {

  }

  @Override
  public void sessionExpired() {
    if (sessionManager != null) sessionManager.sessionExpired(this);
  }

  @Override
  public void isInternetAvailable(boolean flag) {
    //        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
  }

  @Override
  public void reload() {
    commentPresenter.getComments(postId, 0, PAGE_SIZE);
  }

  @Override
  public void commented(boolean flag) {
    if (flag) {
      commentsAdded++;
      if (etComment != null) etComment.setText(getString(R.string.double_inverted_comma));
      int comment = Integer.parseInt(sessionManager.getCommentCount());
      sessionManager.setCommentCount(String.valueOf(++comment));
      //            adapter.notifyDataSetChanged();

      if (rvCommentList != null) {
        try {
          rvCommentList.smoothScrollToPosition(0);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if(ibSend!=null)ibSend.setEnabled(true);
    }
  }

  @Override
  public void openProfile(String userId) {
    startActivity(new Intent(this, ProfileActivity.class).putExtra("userId", userId));
  }

  @Override
  public void setTag(Hash_tag_people_pojo tag) {
    etComment.updateHashTagDetails(tag);
  }

  @Override
  public void setUser(Hash_tag_people_pojo tag) {
    etComment.updateUserSearch(tag);
  }

  @Override
  public void showProgress(boolean show) {


    if(swipeRefresh!=null)
    swipeRefresh.setRefreshing(show);
  }

  @Override
  public void onHashTag(String tag) {
    commentPresenter.searchHashTag(tag);
  }

  @Override
  public void onUserSearch(String tag) {
    commentPresenter.searchUserTag(tag);
  }

  @Override
  public void onClear() {

  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    etComment.setText(etComment.getText().toString().replace("##", "#"));
    etComment.setSelection(etComment.getText().length());
  }

  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          int visibleItemCount = layoutManager.getChildCount();
          int totalItemCount = layoutManager.getItemCount();
          int firstVisibleItemPosition =
              ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
          commentPresenter.callApiOnScroll(postId, firstVisibleItemPosition, visibleItemCount,
              totalItemCount);
        }
      };
}
