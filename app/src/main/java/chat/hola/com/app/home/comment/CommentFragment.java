package chat.hola.com.app.home.comment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.hastag.AutoCompleteTextView;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.profileScreen.ProfileActivity;
import dagger.android.support.DaggerFragment;

public class CommentFragment extends DaggerFragment implements AdapterView.OnItemClickListener, CommentFragmentContract.View, TextWatcher, AutoCompleteTextView.AutoTxtCallback {

    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    public static int page = 0;

    private String postId;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    CommentFragmentPresenter commentPresenter;
    @Inject
    CommentFragmentAdapter adapter;
    @Inject
    List<Comment> comments;

    @BindView(R.id.rvCommentList)
    RecyclerView rvCommentList;
    @BindView(R.id.ivUserProfilePic)
    ImageView ivUserProfilePic;
    @BindView(R.id.etComment)
    AutoCompleteTextView etComment;
    @BindView(R.id.ibSend)
    ImageButton ibSend;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.appbarLayout)
    AppBarLayout appBarLayout;
    @Inject
    PostObserver postObserver;
    @Inject
    BlockDialog dialog;

    private LinearLayoutManager layoutManager;
    private int initialCommentsCount;
    private int commentsAdded;

    @Inject
    public CommentFragment() {
    }

    public CommentFragment(String postId, int initialCommentsCount) {
        this.postId = postId;
        this.initialCommentsCount = initialCommentsCount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_activity, container, false);
        ButterKnife.bind(this, view);
        commentPresenter.bindView(this);
        appBarLayout.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        if (ibSend != null) ibSend.setEnabled(false);
        etComment.setTypeface(AppController.getInstance().getRegularFont());
        etComment.addTextChangedListener(this);

        Glide.with(getContext()).load(sessionManager.getUserProfilePic()).asBitmap().centerCrop()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .into(new BitmapImageViewTarget(ivUserProfilePic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
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
        llNetworkError.setErrorListner(this);
        swipeRefresh.setOnRefreshListener(this::reload);

        return view;
    }

    @OnClick(R.id.ibSend)
    public void send() {
        if (postId != null) {
            if (ibSend != null) ibSend.setEnabled(false);
            commentPresenter.addComment(postId, etComment.getText().toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (ibSend != null) ibSend.setEnabled(charSequence.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {
        if (sessionManager != null) sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        //        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void userBlocked() {
        dialog.show();
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
            if (ibSend != null) ibSend.setEnabled(true);
        }
    }

    @Override
    public void openProfile(String userId) {
        startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("userId", userId));
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
        if (swipeRefresh != null)
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

    public String getUserId(int position) {
        return comments.get(position).getCommentedByUserId();
    }

    public void selectItem(int position, boolean isSelected) {
        comments.get(position).setSelected(isSelected);
    }
    public  int getTotalComments() {
        return comments.size();
    }


    public void setData(List<Comment> data) {
        if (data != null) {
            this.comments.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public void clearList() {
        this.comments.clear();
    }

    public   void addToList(Comment data) {
        data.setProfilePic(sessionManager.getUserProfilePic());
        data.setCommentedBy(sessionManager.getUserName());
        data.setCommentedByUserId(AppController.getInstance().getUserId());
        comments.add(0, data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void commentDeleted(int position, String commentId) {
        comments.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void commentLiked(int position, String commentId, boolean isLike) {
    }
}
