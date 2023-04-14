package chat.hola.com.app.home.activity.youTab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersActivity;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestActivity;
import chat.hola.com.app.home.activity.youTab.followrequest.ReuestData;
import chat.hola.com.app.home.activity.youTab.model.Data;
import chat.hola.com.app.home.activity.youTab.model.RequestedChannels;
import chat.hola.com.app.home.model.PostData;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;

/**
 * <h>FacebookFrag.class</h>
 * <p>
 * This fragment show activities of other on your post and use the
 * {@link YouAdapter} with recyclerView.
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class YouFrag extends Fragment
        implements YouContract.View, SwipeRefreshLayout.OnRefreshListener,
        YouAdapter.OnAdapterClickCallback {

    @Inject
    YouAdapter youAdapter;

    @Inject
    YouPresenter presenter;

    @Inject
    TypefaceManager typefaceManager;

    @Inject
    SessionManager sessionManager;

    @BindView(R.id.recyclerYou)
    RecyclerView recyclerYou;

    @BindView(R.id.srSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rlError)
    RelativeLayout rlError;

    @BindView(R.id.tvError)
    TextView tvError;

    @BindView(R.id.btnTryAgain)
    Button btnTryAgain;

    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.tvEmptyTitle)
    TextView tvEmptyTitle;
    @BindView(R.id.tvEmptyMsg)
    TextView tvEmptyMsg;

    @BindView(R.id.ivChannelImage)
    ImageView ivChannelImage;
    @BindView(R.id.tvSubscriptionRequestTitle)
    TextView tvSubscriptionRequestTitle;
    @BindView(R.id.tvSubscriptionRequestSubtitle)
    TextView tvSubscriptionRequestSubtitle;

    @BindView(R.id.ivUserImage)
    ImageView ivUserImage;
    @BindView(R.id.tvFollowRequestTitle)
    TextView tvFollowRequestTitle;
    @BindView(R.id.tvFollowRequestSubtitle)
    TextView tvFollowRequestSubtitle;
    @BindView(R.id.followCount)
    TextView followCount;

    @BindView(R.id.rlFollowRequest)
    RelativeLayout rlFollowRequest;
    @BindView(R.id.rlRequest)
    RelativeLayout rlRequest;
    @BindView(R.id.devider)
    RelativeLayout devider;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;

    private Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int PAGE_SIZE = 10;
    private int page = 0;
    private int requests = 0;
    private ArrayList<Data> dataList = new ArrayList<>();
    List<RequestedChannels> requestedChannels = new ArrayList<>();
    List<ReuestData> followReuest = new ArrayList<>();
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Inject
    public YouFrag() {
    }

    public static YouFrag newInstance() {
        return new YouFrag();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_you_tab, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (presenter != null)
            presenter.attachView(this);
        initRecyclerYou();
        applyFont();
        swipeRefreshLayout.setOnRefreshListener(this);

        Glide.with(getContext()).load(sessionManager.getUserProfilePic()).asBitmap().centerCrop()
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(new BitmapImageViewTarget(ivUserImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivUserImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.channelRequest();
        presenter.followRequest();
    }

    public void applyFont() {
        tvError.setTypeface(typefaceManager.getMediumFont());
        btnTryAgain.setTypeface(typefaceManager.getMediumFont());
        tvEmptyTitle.setTypeface(typefaceManager.getMediumFont());
        tvEmptyMsg.setTypeface(typefaceManager.getRegularFont());
    }

    @Override
    public void showErrorLayout(boolean show) {
        if (show) {
            llEmpty.setVisibility(View.VISIBLE);
        } else {
            llEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void channelRequest(List<RequestedChannels> response) {
        requestedChannels.clear();
        requestedChannels = response;
        if (response.isEmpty()) {
            rlRequest.setVisibility(View.GONE);
            devider.setVisibility(View.GONE);
        } else {
            rlRequest.setVisibility(View.VISIBLE);
            devider.setVisibility(View.VISIBLE);

            Glide.with(getContext()).load(response.get(0).getChannelImageUrl()).asBitmap().centerCrop()
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(ivChannelImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivChannelImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    @Override
    public void followRequest(List<ReuestData> response, Integer request) {
        followReuest.clear();
        followReuest = response;
        requests = request;

        showEmptyRequest(!(requests > 0));

        if (response.isEmpty()) {
            //            rlFollowRequest.setVisibility(View.GONE);
            devider.setVisibility(View.GONE);
        } else {
            if (request > 0) {
                followCount.setVisibility(View.VISIBLE);
                followCount.setText(getString(R.string.double_inverted_comma) + request);
            }
            devider.setVisibility(View.VISIBLE);

            String profilePic =
                    response.get(0).getProfilePic() != null || !response.get(0).getProfilePic().isEmpty()
                            ? response.get(0).getProfilePic() : sessionManager.getUserProfilePic();
            Glide.with(getContext()).load(profilePic).asBitmap().centerCrop()
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(ivUserImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivUserImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    @Override
    public void showEmptyRequest(boolean isEmpty) {
        rlFollowRequest.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.ivUserImage, R.id.rlFollowRequest})
    public void reqFollow() {
        if (requests > 0) {
            startActivity(new Intent(getContext(), FollowRequestActivity.class).putExtra("data",
                    (Serializable) followReuest));
        }
    }

    @OnClick({R.id.ivChannelImage, R.id.rlRequest})
    public void reqSubChannels() {
        startActivity(new Intent(getContext(), ChannelRequestersActivity.class).putExtra("data",
                (Serializable) requestedChannels));
    }

    private void initRecyclerYou() {
        llNetworkError.setErrorListner(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerYou.setLayoutManager(linearLayoutManager);
        recyclerYou.setAdapter(youAdapter);
        if (presenter != null)
            presenter.loadYouData(page * PAGE_SIZE, PAGE_SIZE);
        youAdapter.setClickCallback(this);
        recyclerYou.addOnScrollListener(recyclerOnScrollListener);
    }

    @OnClick(R.id.btnTryAgain)
    public void tryAgain() {
        onRefresh();
    }

    public void isDataLoading(boolean isLoading) {
        if (swipeRefreshLayout != null) {
            if (isLoading) {
                swipeRefreshLayout.setRefreshing(true);
                this.isLoading = true;
            } else {
                swipeRefreshLayout.setRefreshing(false);
                this.isLoading = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.detachView();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        dataList.clear();
        page = 0;
        presenter.loadYouData(page * PAGE_SIZE, PAGE_SIZE);
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
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showYouActivity(ArrayList<Data> dataList, boolean clear) {
        if (dataList != null && !dataList.isEmpty()) {
            if (clear)
                this.dataList.clear();
            this.dataList.addAll(dataList);
            if (this.dataList.isEmpty()) {
                llEmpty.setVisibility(View.VISIBLE);
            } else {
                llEmpty.setVisibility(View.GONE);
                youAdapter.setData(this.dataList);
            }
        }
    }

    @Override
    public void onProfilePicClick(String userId) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onPostClickCallback(PostData data, View view) {
        Intent intent = new Intent(getContext(), SocialDetailActivity.class);
        intent.putExtra("postId", data.getPostId());
        intent.putExtra("call", "activity");
        startActivity(intent);
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
    public void reload() {
        onRefresh();
    }
}