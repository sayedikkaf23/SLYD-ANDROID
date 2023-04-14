package chat.hola.com.app.socialDetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.DublyCamera.dubbing.DownloadAudioHelper;
import chat.hola.com.app.DublyCamera.dubbing.DownloadResult;
import chat.hola.com.app.DublyCamera.duet.DeeparDuetActivity;
import chat.hola.com.app.DublyCamera.overlay.OverlayConfig;
import chat.hola.com.app.DublyCamera.overlay.OverlayProfileInfoResult;
import chat.hola.com.app.DublyCamera.overlay.OverlayUtils;
import chat.hola.com.app.Share.socialmedia.DownloadMedia;
import chat.hola.com.app.Share.socialmedia.DownloadMediaToShareResult;
import chat.hola.com.app.Share.socialmedia.SupportedSocialMediaAppsConfig;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.collections.saved.SavedActivity;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.CommentAdapter;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.social.CollectionItemAdapter;
import chat.hola.com.app.home.social.SendTipDialog;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.live_stream.utility.TimerHelper;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.music.MusicActivity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.postshare.DownloadMediaResult;
import chat.hola.com.app.postshare.WatermarkConfig;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.followers.FollowerAdapter;
import chat.hola.com.app.profileScreen.followers.FollowersActivity;
import chat.hola.com.app.socialDetail.video_manager.BaseVideoItem;
import chat.hola.com.app.socialDetail.video_manager.ClickListner;
import chat.hola.com.app.socialDetail.video_manager.ItemFactory;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import chat.hola.com.app.webScreen.WebActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>SocialDetailActivity</h1>
 * <p>It shows details of post. user can like, share, report, edit and delete post</p>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/3/2018
 */

public class SocialDetailActivity extends DaggerAppCompatActivity
        implements ClickListner, ActionListner, CollectionItemAdapter.ClickListener,
        ItemAdapter.ItemListener, SocialDetailContract.View, DialogInterface.OnClickListener,
        PopupMenu.OnMenuItemClickListener, DownloadResult, DownloadMediaResult,
        OverlayProfileInfoResult, chat.hola.com.app.comment.model.ClickListner,
        FollowerAdapter.OnFollowUnfollowClickCallback, DownloadMediaToShareResult {
    private static final int COMMENT_COUNT = 1010;
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private MediaPlayer mediaPlayer;
    private TextView tvNoDataFound;

    @Inject
    BlockDialog dialog;
    @Inject
    SocialDetailPresenter mPresenter;
    @Inject
    SessionManager sessionManager;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    AlertDialog.Builder reportDialog;
    @Inject
    ArrayAdapter<String> arrayAdapter;

    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    RoundedImageView ivProfilePic;
    EditText etMessage;
    RecyclerView shareList;
    @BindView(R.id.overlay)
    View overlay;
    @BindView(R.id.cover)
    AppCompatImageView cover;

    // Collection bottom sheet data.
    @BindView((R.id.sheetCollection))
    View sheetCollection;
    @BindView(R.id.cTitle)
    TextView cTitle;
    @BindView(R.id.iV_newCollection)
    ImageView iV_newCollection;
    @BindView(R.id.cBack)
    ImageView cBack;
    @BindView(R.id.rV_collections)
    RecyclerView rV_collections;
    @BindView(R.id.ll_newCollection)
    LinearLayout ll_newCollection;
    @BindView(R.id.iV_cImage)
    ImageView iV_cImage;
    @BindView(R.id.et_cName)
    EditText et_cName;
    @BindView(R.id.tV_cAction)
    TextView tV_cAction;
    @BindView(R.id.blurView)
    View blurView;

    @BindView(R.id.rL_save)
    RelativeLayout rlSave;
    @BindView(R.id.tV_saveTo)
    TextView tvSaveTo;
    @BindView(R.id.tV_savedView)
    TextView tvSavedView;

    CollectionItemAdapter collectionItemAdapter;
    private List<CollectionData> collectionList = new ArrayList<>();
    private BottomSheetBehavior collectionBehavior;
    // used while new collection creation.
    private String collectionImage = "", addToCollectionPostId = "";
    ///////////////////////////////

    private String postId;
    private Data data = null;
    private String userId;
    private String categoryId;
    private Menu menu;

    private boolean apiCallRequired;
    private ItemAdapter itemAdapter;
    @Inject
    List<Data> dataList;

    private PopupMenu popupMenu;

    private final ArrayList<BaseVideoItem> mList = new ArrayList<>();
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private ItemsPositionGetter mItemsPositionGetter;
    private SocialDetailAdapter socialDetailAdapter;
    private LinearLayoutManager mLayoutManager;
    private static int currentPosition = 0;
    private boolean notFirstResume = false;
    private int lastVisibleItemPositionOnActivityStop;
    private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    private double screenRatio = ((double) displayMetrics.heightPixels / displayMetrics.widthPixels);

    //    private boolean needToScrollVideo = false;
    private KeyguardManager keyboardManager;

    // private boolean notFirstResume = true;
    private final VideoPlayerManager<MetaData> mVideoPlayerManager =
            new SingleVideoPlayerManager(new PlayerItemChangeListener() {
                @Override
                public void onPlayerItemChanged(MetaData metaData) {

                }
            });

    private List<Friend> friends = new ArrayList<>();
    //private int position;
    private int pos = 0;
    private RelativeLayout rlRecord, rlEdit, rlDelete, rlReport, rlDownloadGif, rlDuet;
    private TextView tvBookMark;

    private boolean isCollection;
    private CollectionData collectionData;
    private static final int DOWNLOAD_IMAGE_REQUEST = 1;
    private static final int DOWNLOAD_VIDEO_REQUEST = 2;
    private static final int DOWNLOAD_AS_GIF_REQUEST = 3;
    private static final int DUET_REQUEST = 4;
    private static final int RECORD_REQUEST = 5;
    private String fileToBeDownloadedName;
    //Share to social media
    private RelativeLayout rlFacebook;
    private View shareOnSocialMediaView;
    private android.app.AlertDialog shareOnSocialMediaDialog;
    private SupportedSocialMediaAppsConfig supportedSocialMediaAppsConfig;
    private CallbackManager callbackManager;
    private ShareDialog shareOnFacebookDialog;
    private androidx.appcompat.app.AlertDialog shareAlert;
    private AlertProgress alertProgress;
//    private androidx.appcompat.app.AlertDialog alertDialog;
    private android.app.AlertDialog alertDialog;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_detail);
        ButterKnife.bind(this);
        toolbarSetup();
        mediaPlayer = MediaPlayer.create(this, R.raw.coin_spend);

        userId = getIntent().getStringExtra("userId");
        postId = getIntent().getStringExtra("postId");
        data = (Data) getIntent().getSerializableExtra(Constants.SocialFragment.DATA);
        apiCallRequired = getIntent().getBooleanExtra("apiCallRequired", false);
        /*-------collections tags------*/
        isCollection = getIntent().getBooleanExtra("isCollection", false);
        mPresenter.isCollection = isCollection;
        if (isCollection) {
            collectionData = (CollectionData) getIntent().getSerializableExtra("collectionData");
            mPresenter.collectionId = collectionData.getId();
        }
        /*----end collections tags----*/
        if (apiCallRequired) {
            /**
             * For single post data to be fetched when coming from trending fragment screen
             */
            showOverlayDuringApiCall(Objects.requireNonNull(getIntent().getStringExtra("url")),
                getIntent().getStringExtra("height"), getIntent().getStringExtra("width"),
                getIntent().getIntExtra("mediaType", 0),
                getIntent().getBooleanExtra("isPurchased", false),getIntent().getStringExtra("thumbnailUrl"));
            String hashTag = getIntent().getStringExtra("hashtag");
            mPresenter.getPostByHashTag(hashTag, 0, SocialDetailPresenter.PAGE_SIZE, true, false);
        }

        dataList = (List<Data>) getIntent().getSerializableExtra("dataList");
        if (dataList != null && !dataList.isEmpty()) setDataList(dataList, true, false);

        if (dataList == null) dataList = new ArrayList<>();
        pos = getIntent().getIntExtra("position", 0);
        if (!dataList.isEmpty()) data = dataList.get(pos);

        //SocialDetailPresenter.page = getIntent().getIntExtra("page", 0);//+ 1;

        mPresenter.setPageNumber(dataList.size() / PAGE_SIZE);
        mPresenter.subscribePostEditedObserver();

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mLayoutManager.setItemPrefetchEnabled(true);

        rvList.setLayoutManager(mLayoutManager);
        socialDetailAdapter =
                new SocialDetailAdapter(mVideoPlayerManager, SocialDetailActivity.this, mList, dataList,
                        getScreenWidth(), getScreenHeight(), true);
        socialDetailAdapter.setClickListner(this);
        rvList.setAdapter(socialDetailAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvList);

        setupCustomShareDialog();
        setupCustomLikeCommentDialog();
        setupShareBottomSheetDialog();
        setupShareOnSocialMediaBottomSheetDialog();

        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !mList.isEmpty()) {

                    mVideoVisibilityCalculator.onScrollStateIdle(mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                try {
                    if (!mList.isEmpty()) {
                        try {
                            mVideoVisibilityCalculator.onScroll(mItemsPositionGetter,
                                    mLayoutManager.findFirstVisibleItemPosition(),
                                    mLayoutManager.findLastVisibleItemPosition()
                                            - mLayoutManager.findFirstVisibleItemPosition() + 1,
                                    mScrollState);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int visibleItemCount = mLayoutManager.getChildCount();
                    if (apiCallRequired) {

                        mPresenter.callApiOnScroll(mLayoutManager.findFirstVisibleItemPosition(),
                                visibleItemCount, mLayoutManager.getItemCount());
                    }
                    data = dataList.get(visibleItemCount);
                } catch (Exception ignored) {
                }
            }
        });

        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, rvList);
        if (!apiCallRequired) {
            rvList.scrollToPosition(pos);
        }
        //   rvList.smoothScrollToPosition(pos);

        mPresenter.getFollowUsers();
        Glide.with(this)
                .load(
                        sessionManager.getUserProfilePic().replace("upload/", Constants.PROFILE_PIC_SHAPE))

                .asBitmap()
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .centerCrop()
                .into(ivProfilePic);

        if ((data != null && dataList.isEmpty())) {
            //            userId = data.getUserId();
            postId = data.getPostId();
            if (!apiCallRequired) {
                getData(postId);
            }
        } else if (postId != null) {
            if (!apiCallRequired) {
                getData(postId);
            }
        }
        if (dataList != null && !dataList.isEmpty()) postId = dataList.get(pos).getPostId();
        reportDialog.setTitle(R.string.report);
        mPresenter.getReportReasons();
        reportDialog.setAdapter(arrayAdapter, this);
        invalidateOptionsMenu();

        collectionItemAdapter = new CollectionItemAdapter(this, collectionList);
        collectionItemAdapter.setListener(this);
        rV_collections.setAdapter(collectionItemAdapter);
        rV_collections.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        collectionBehavior = BottomSheetBehavior.from(sheetCollection);
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        collectionBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        blurView.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        blurView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        blurView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        blurView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        if (!apiCallRequired) {
            rvList.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (mList.size() > 0) {
                            BaseVideoItem videoItem = mList.get(pos);

                            ViewHolder viewHolder =
                                    (ViewHolder) rvList.findViewHolderForAdapterPosition(pos);
                            if (viewHolder != null) {

                                videoItem.setActive(viewHolder.itemView, pos, false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        keyboardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        callbackManager = CallbackManager.Factory.create();
        shareOnFacebookDialog = new ShareDialog(this);
        shareOnFacebookDialog.registerCallback(callbackManager, shareCallback);
        supportedSocialMediaAppsConfig = new SupportedSocialMediaAppsConfig();
        alertProgress = new AlertProgress(this);
    }

    @OnClick(R.id.blurView)
    public void blurViewClick() {
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetCollection.setVisibility(View.GONE);
    }

    @OnClick({R.id.iV_newCollection})
    public void createNewCollection() {

        iV_newCollection.setVisibility(View.GONE);
        rV_collections.setVisibility(View.GONE);
        ll_newCollection.setVisibility(View.VISIBLE);
        cBack.setVisibility(View.VISIBLE);
        cTitle.setText(getString(R.string.new_collection));
        tV_cAction.setText(getString(R.string.done));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_cName.requestFocus();
                openKeyboard(getApplicationContext());
            }
        }, 500);
    }

    @OnClick({R.id.cBack})
    public void backFromCreateCollection() {

        hideKeyboard(getApplicationContext());

        if (collectionList.isEmpty()) {
            collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            sheetCollection.setVisibility(View.GONE);
        }

        iV_newCollection.setVisibility(View.VISIBLE);
        rV_collections.setVisibility(View.VISIBLE);
        ll_newCollection.setVisibility(View.GONE);
        cBack.setVisibility(View.GONE);
        cTitle.setText(getString(R.string.save_to));
        tV_cAction.setText(getString(R.string.cancel));
    }

    @OnClick(R.id.tV_cAction)
    public void collectionActionClick() {
        if (iV_newCollection.getVisibility() == View.VISIBLE) {
            // visible then cancel show as an action.
            sheetCollection.setVisibility(View.GONE);
            collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            // Gone then done show as an action.
            if (!et_cName.getText().toString().trim().isEmpty()) {
                mPresenter.createCollection(et_cName.getText().toString().trim(), collectionImage,
                        addToCollectionPostId);
            }
        }
    }

    private int calculateFinalPosition(int currentPosition) {

        int size = mList.size();
        if (size == 1) {
            return currentPosition;
        } else if (currentPosition == size - 1) {
            return currentPosition - 1;
        } else {
            return currentPosition + 1;
        }
    }

    private void getData(String postId) {
        mPresenter.getPostById(postId, false);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage(R.string.report_message);
        confirm.setPositiveButton(R.string.confirm,
                (dialog, w) -> mPresenter.reportPost(position, postTobeShared.getPostId(), arrayAdapter.getItem(which),
                        arrayAdapter.getItem(which)));
        confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
        confirm.create().show();
    }

    private void invalidateMenu(Data data) {
        try {
            MenuItem edit = menu.findItem(R.id.action_edit);
            MenuItem report = menu.findItem(R.id.action_report);
            MenuItem delete = menu.findItem(R.id.action_delete);

            boolean flag = data.getUserId() != null && data.getUserId()
                    .equals(AppController.getInstance().getUserId());
            edit.setVisible(flag);
            report.setVisible(!flag);
            delete.setVisible(flag);
            popupMenu.show();
        } catch (Exception ignored) {
        }
    }

    private void toolbarSetup() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.updatePostViewedToServer(mPresenter.getViewPost());
        mVideoPlayerManager.stopAnyPlayback(true);
        lastVisibleItemPositionOnActivityStop = mLayoutManager.findLastVisibleItemPosition();
        showOverlay(lastVisibleItemPositionOnActivityStop);
    }

    @Override
    protected void onDestroy() {
        try {
            mVideoPlayerManager.resetMediaPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
//            if (userId != null) {
//                Intent intent = new Intent();
//                intent.putExtra("userId", userId);
//                setResult(RESULT_OK, intent);
//            }
            try {
                mVideoPlayerManager.resetMediaPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onBackPressed();
            finish();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    public void liked(boolean likeRequest, boolean hasError, String postId) {
        if (hasError) {

            for (int i = 0; i < dataList.size(); i++) {

                if (dataList.get(i).getPostId().equals(postId)) {
                    if (likeRequest) {

                        Data data = dataList.get(i);
                        data.setLiked(false);
                        int count = Integer.parseInt(data.getLikesCount());
                        if (count > 0) {
                            count--;
                        } else {
                            count = 0;
                        }
                        data.setLikesCount(String.valueOf(count));

                        dataList.set(i, data);
                    } else {

                        Data data = dataList.get(i);
                        data.setLiked(true);
                        int count = Integer.parseInt(data.getLikesCount());

                        count++;

                        data.setLikesCount(String.valueOf(count));

                        dataList.set(i, data);
                    }
                    socialDetailAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public void dismissDialog() {
    }

    @Override
    public void addToReportList(ArrayList<String> data) {
        arrayAdapter.clear();
        arrayAdapter.addAll(data);
    }

    @Override
    public void setData(Data data) {
        if (data != null) {
            this.data = data;
            userId = data.getUserId();
            postId = data.getPostId();

            int postion = -1;
            for (int i = 0; i < dataList.size(); i++) {

                if (dataList.get(i).getPostId().equals(postId)) {
                    postion = i;
                    break;
                }
            }

            if (postion != -1) {
                dataList.set(postion, data);
                try {
                    mList.set(postion, ItemFactory.createItem(this, mVideoPlayerManager, data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dataList.add(0, data);
                List<Data> list = new ArrayList<>();
                list.add(data);
                setData(list, true);
            }

            socialDetailAdapter.setDataList(mList, dataList);

//            if (apiCallRequired) {
            //rvList.suppressLayout(true);
            hideOverlay();
            if (dataList.get(0).getMediaType1() == 1) {
                rvList.post(() -> {
                    try {

                        BaseVideoItem videoItem = mList.get(0);

                        ViewHolder viewHolder = (ViewHolder) rvList.findViewHolderForAdapterPosition(0);
                        if (viewHolder != null) {

                            videoItem.setActive(viewHolder.itemView, 0, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            //}
        } else {
            showMessage("", R.string.no_post_available);
            finish();
        }
    }

    @Override
    public void like(boolean like, String postId) {
        try {
            if (like) {
                mPresenter.like(postId);
            } else {
                mPresenter.unlike(postId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleted() {
        onBackPressed();
    }

    @Override
    public void follow(boolean isChecked, String userId) {

        if (isChecked) {
            mPresenter.follow(userId);
        } else {
            mPresenter.unfollow(userId);
        }
    }

    @Override
    public void unFollowUser(String userId) {
        reload();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msgId != 0 ? getString(msgId) : msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_COUNT && resultCode == RESULT_OK) {
            currentPosition = data.getIntExtra("position", 0);
            try {

                socialDetailAdapter.updateCommentCount(data.getStringExtra("commentCount"),
                        data.getStringExtra("postId"), data.getIntExtra("position", 0), rvList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getCollections();
        if (notFirstResume) {

            if (keyboardManager != null && !keyboardManager.isKeyguardLocked()) {

                //it is not locked
                if (dataList != null && !dataList.isEmpty()) {
                    try {

                        if (dataList.get(lastVisibleItemPositionOnActivityStop).getMediaType1() == 1) {

                            playVideoOnResume(lastVisibleItemPositionOnActivityStop,
                                    calculateFinalPosition(lastVisibleItemPositionOnActivityStop));
                        } else {

                            hideOverlay();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //For some cases when screen is showing as locked although it is not(happens rarely),in which case video playback doesnt resume until scrolled
                if (overlay.getVisibility() == View.VISIBLE) {
                    hideOverlay();
                }
            }
        } else {
            notFirstResume = true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void reload() {

        try {
            mVideoPlayerManager.stopAnyPlayback(true);
        } catch (Exception ignore) {
        }
        mPresenter.callSocialApi(0, PAGE_SIZE, true, false);
    }

    @Override
    public void likePost(Data data) {
    }

    @Override
    public void send(int position) {
        //this.position = position;
        if (itemAdapter != null) itemAdapter.setPostItemClickPostion(position);
        if (friends != null && !friends.isEmpty()) {
            refreshFriendList();

            shareDialog.show();
            shareDialog.setContentView(shareView);
            shareDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = shareDialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = 1000;//WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            shareDialog.getWindow().setGravity(Gravity.BOTTOM);
            params.dimAmount = 0.0f;
            shareDialog.getWindow().setAttributes(params);
            shareDialog.getWindow()
                    .setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
            shareDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
        } else {
            Toast.makeText(this, getResources().getString(R.string.please_add_friends),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void comment(Data data, String postId, int position, String commentsCount, boolean commentsEnabled) {
        if (commentsEnabled) {
            currentPosition = position;
            openCustomLikeCommentDialog(data, false);
//      startActivityForResult(new Intent(this, CommentActivity.class).putExtra("position", position)
//          .putExtra("postId", postId)
//          .putExtra("commentsCount", commentsCount), COMMENT_COUNT);
        } else {
            Toast.makeText(SocialDetailActivity.this, R.string.comments_disabled, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void profile(String id, boolean isChannel, boolean isBusiness) {

        if (isBusiness) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("isBusiness", true);
            intent.putExtra(Constants.SocialFragment.USERID, id);
            startActivity(intent);
        } else if (isChannel) {
            Intent intent = new Intent(this, TrendingDetail.class);
            intent.putExtra("channelId", id);
            intent.putExtra("call", "channel");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(Constants.SocialFragment.USERID, id);
            startActivity(intent);
        }
    }

    @Override
    public void category(String categoryId, String categoryName) {
        startActivity(new Intent(this, TrendingDetail.class).putExtra("categoryId", categoryId)
                .putExtra("call", "category")
                .putExtra("category", categoryName));
    }

    @Override
    public void channel(String channelId, String channelName) {
        startActivity(new Intent(this, TrendingDetail.class).putExtra("call", "channel")
                .putExtra("channelId", channelId));
    }

    @Override
    public void music(String id, String name, String path, String artist) {
        startActivity(new Intent(this, MusicActivity.class).putExtra("musicPath", path)
                .putExtra("call", "music")
                .putExtra("musicId", id)
                .putExtra("artist", artist)
                .putExtra("name", name));
    }

    @Override
    public void followers(List<Friend> data) {
        friends = data;
        itemAdapter = new ItemAdapter(this, friends, this, typefaceManager);
        shareList.setAdapter(itemAdapter);
    }

    private void refreshFriendList() {
        for (Friend f : friends) {
            if (f.isSent()) {
                f.setSent(false);
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDataList(List<Data> data, boolean entirelyNewList, boolean refreshRequest) {

        List<Data> nonDuplicateData = new ArrayList<>();

        if (entirelyNewList) {
            nonDuplicateData = data;
        } else {

            Map<String, Integer> postIds = new HashMap<>();

            for (int i = 0; i < dataList.size(); i++) {

                postIds.put(dataList.get(i).getPostId(), i);
            }

            for (int i = 0; i < data.size(); i++) {

                if (postIds.containsKey(data.get(i).getPostId())) {

                    try {
                        dataList.set(postIds.get(data.get(i).getPostId()), data.get(i));
                        mList.set(postIds.get(data.get(i).getPostId()),
                                ItemFactory.createItem(this, mVideoPlayerManager, data.get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    nonDuplicateData.add(data.get(i));
                }
            }
            //HashSet<String> postIds = new HashSet<>();
            //
            //for (int i = 0; i < dataList.size(); i++) {
            //  postIds.add(dataList.get(i).getPostId());
            //}
            //
            //for (int i = 0; i < data.size(); i++) {
            //
            //  if (!postIds.contains(data.get(i).getPostId())) {
            //
            //    nonDuplicateData.add(data.get(i));
            //  }
            //}
        }
        if (refreshRequest) {
            dataList.addAll(0, nonDuplicateData);
        } else {
            if (apiCallRequired) {
                dataList.addAll(nonDuplicateData);
            } else {
                if (!entirelyNewList) {
                    dataList.addAll(nonDuplicateData);
                }
            }
        }
        setData(nonDuplicateData, refreshRequest);
        if (socialDetailAdapter != null) {
            socialDetailAdapter.setDataList(mList, dataList);

            if (apiCallRequired) {
                if ((entirelyNewList || refreshRequest) && mList.size() > 0) {

                    try {
                        if (!dataList.get(pos).getPostId().equals(postId)) {

                            for (int i = 0; i < dataList.size(); i++) {
                                if (dataList.get(i).getPostId().equals(postId)) {
                                    pos = i;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    rvList.scrollToPosition(pos);
                    rvList.post(() -> {
                        try {
                            BaseVideoItem videoItem = mList.get(pos);

                            ViewHolder viewHolder =
                                    (ViewHolder) rvList.findViewHolderForAdapterPosition(pos);
                            if (viewHolder != null) {

                                videoItem.setActive(viewHolder.itemView, pos, false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    hideOverlay();
                }
            }
        }

    }

    @Override
    public void showProgress(boolean b) {
        //TODO
    }

    @Override
    public void postAddedToCollection() {
        sheetCollection.setVisibility(View.GONE);
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        backFromCreateCollection();

        Toast.makeText(this, R.string.added_to_collection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void collectionFetched(List<CollectionData> data) {

        collectionList.clear();
        collectionList.addAll(data);

        if (!collectionList.isEmpty()) collectionList.remove(0);

        collectionItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void collectionCreated() {
        hideKeyboard(this);
        sheetCollection.setVisibility(View.GONE);
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        backFromCreateCollection();
        et_cName.setText("");
        mPresenter.getCollections();

        Toast.makeText(this, R.string.collection_created, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Friend userdata, int position) {
        JSONObject obj;
        obj = new JSONObject();
        try {
            data = dataList.get(position);
            /*
             * Post
             */
            try {
                String documentId =
                        AppController.findDocumentIdOfReceiver(userdata.getId(), Utilities.tsInGmt(),
                                userdata.getFirstName(), userdata.getProfilePic(), "", false,
                                userdata.getUserName(), "", false, userdata.isStar());

                String payload = Utilities.getModifiedImageLink(data.getImageUrl1());
                String tsForServer = Utilities.tsInGmt();
                String tsForServerEpoch = new Utilities().gmtToEpoch(tsForServer);

                obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                obj.put("from", AppController.getInstance().getUserId());
                obj.put("payload", Base64.encodeToString(payload.getBytes("UTF-8"), Base64.DEFAULT).trim());
                obj.put("timestamp", tsForServerEpoch);
                obj.put("id", tsForServerEpoch);
                obj.put("type", "13");
                obj.put("name", AppController.getInstance().getUserName());
                obj.put("postId", postId);
                obj.put("postTitle", data.getTitle());
                obj.put("postType", data.getMediaType1());
                obj.put("to", userdata.getId());
                obj.put("toDocId", documentId);

                Map<String, Object> map = new HashMap<>();
                map.put("message", payload);
                map.put("messageType", "13");
                map.put("isSelf", true);
                map.put("from", AppController.getInstance().getUserId());
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);
                map.put("postId", postId);
                map.put("postTitle", data.getTitle());
                map.put("postType", data.getMediaType1());

                AppController.getInstance()
                        .getDbController()
                        .addNewChatMessageAndSort(documentId, map, tsForServer, "");
                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.put("from", AppController.getInstance().getUserId());
                mapTemp.put("to", userdata.getId());
                mapTemp.put("toDocId", documentId);
                mapTemp.put("id", tsForServerEpoch);
                mapTemp.put("timestamp", tsForServerEpoch);

                String type = Integer.toString(13);
                mapTemp.put("type", type);
                mapTemp.put("postId", postId);
                mapTemp.put("postTitle", etMessage.getText().toString() + "\n" + data.getTitle());
                mapTemp.put("postType", data.getMediaType1());
                mapTemp.put("name", AppController.getInstance().getUserName());
                mapTemp.put("message", payload);

                AppController.getInstance()
                        .getDbController()
                        .addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);

                obj.put("name", AppController.getInstance().getUserName());
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("messageId", tsForServerEpoch);
                map1.put("docId", documentId);
                AppController.getInstance()
                        .publishChatMessage(MqttEvents.Message.value + "/" + userdata.getId(), obj, 1, false,
                                map1);
                friends.get(position).setSent(false);
                //            Toast.makeText(this, "Post sent...", Toast.LENGTH_SHORT).show();

                AppController.getBus().post(new JSONObject().put("eventName", "postSent"));
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFilter(int count) {
        if (count > 0) {
            tvNoDataFound.setVisibility(View.GONE);
            rvComments.setVisibility(View.VISIBLE);
        } else {
            rvComments.setVisibility(View.GONE);
            tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    public void setData(List<Data> dataList, boolean refreshRequest) {
        try {

            for (int i = 0; i < dataList.size(); i++) {

                if (refreshRequest) {

                    mList.add(i, ItemFactory.createItem(this, mVideoPlayerManager, dataList.get(i)));
                } else {

                    mList.add(ItemFactory.createItem(this, mVideoPlayerManager, dataList.get(i)));
                }

                try {
                    if (data != null) {

                        String thumbnailUrl = data.getThumbnailUrl1();
                        if (data.getPurchased()) {
                            if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                                thumbnailUrl =
                                        thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                            }
                        }
                        Glide.with(this)
                                .load(thumbnailUrl)
                                .downloadOnly(Integer.parseInt(data.getImageUrl1Width()),
                                        Integer.parseInt(data.getImageUrl1Height()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openMenu(Data data, ImageButton ibMenu) {
        this.data = data;
        popupMenu = new PopupMenu(this, ibMenu);
        popupMenu.inflate(R.menu.social_detail_menu);
        popupMenu.setOnMenuItemClickListener(this);
        menu = popupMenu.getMenu();
        invalidateMenu(data);
    }

    @Override
    public void view(Data data) {
        mPresenter.addToViewPostList(data);
        //mPresenter.getPostById(data.getPostId(), true);
    }

    @Override
    public void location(Data data) {
        startActivity(new Intent(this, TrendingDetail.class).putExtra("placeId", data.getPlaceId())
                .putExtra("call", "location")
                .putExtra("location", data.getPlace())
                .putExtra("latlong", data.getLocation()));
    }

    @Override
    public void openLikers(Data data) {
        openCustomLikeCommentDialog(data, true);
//        Intent intent = new Intent(this, FollowersActivity.class);
//        intent.putExtra("title", getResources().getString(R.string.likers));
//        intent.putExtra("userId", data.getPostId());
//        startActivity(intent);
    }

    @Override
    public void openViewers(Data data) {
        Intent intent = new Intent(this, FollowersActivity.class);
        intent.putExtra("title", "Viewers");
        intent.putExtra("userId", data.getPostId());
        startActivity(intent);
    }

    @Override
    public void onActionButtonClick(String title, String url) {
        if (!url.contains("http")) url = "http://" + url;
        Intent intent = new Intent(this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        intent.putExtra("url_data", bundle);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case R.id.action_report:
                    reportDialog.show();
                    return true;
                case R.id.action_edit:
                    Intent intentEdit = new Intent(this, PostActivity.class);
                    intentEdit.putExtra("data", data);
                    intentEdit.putExtra("call", "edit");
                    intentEdit.putExtra(Constants.Post.TYPE,
                            data.getMediaType1() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
                    startActivity(intentEdit);
                    finish();
                    return true;
                case R.id.action_delete:
                    reportDialog.setMessage(R.string.postDeleteMsg);
                    reportDialog.setPositiveButton(R.string.yes,
                            (dialog, which) -> mPresenter.deletePost(postId));
                    reportDialog.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                    reportDialog.create().show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    public void playVideoOnResume(int initialPosition, int finalPosition) {
        if (rvList != null) {
            rvList.smoothScrollToPosition(finalPosition);
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (rvList != null) {
                        rvList.smoothScrollToPosition(initialPosition);
                    }
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                hideOverlay();
                            }
                        }, 500);
                    } catch (Exception e) {

                        hideOverlay();
                    }
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void hideOverlay() {
        overlay.setVisibility(View.GONE);
    }

    private void showOverlay(int position) {
        if (position >= 0) {
            if (dataList != null && !dataList.isEmpty()) {
                overlay.setVisibility(View.VISIBLE);

                try {

                    double ratio = ((double) Integer.parseInt(dataList.get(position).getImageUrl1Height()))
                            / Integer.parseInt(dataList.get(position).getImageUrl1Width());

                    if (ratio > screenRatio) {
                        try {
                            String thumbnailUrl = dataList.get(position).getThumbnailUrl1();
                            if (dataList.get(position).getPurchased()) {
                                if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                                    thumbnailUrl =
                                        thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                                }
                            }
                            thumbnailUrl = Utilities.getModifiedThumbnailLinkForPosts(thumbnailUrl,
                                dataList.get(position).getPurchased());

                            DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
                                .load(thumbnailUrl)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                            Glide.with(this).load(Utilities.getCoverImageUrlForPost(
                                    dataList.get(position).getImageUrl1(),
                                    dataList.get(position).getMediaType1(),
                                    dataList.get(position).getPurchased(), thumbnailUrl))
                                    .thumbnail(thumbnailRequest)
                                    .dontAnimate()
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(cover);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String thumbnailUrl = dataList.get(position).getThumbnailUrl1();
                            if (dataList.get(position).getPurchased()) {
                                if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                                    thumbnailUrl =
                                            thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                                }
                            }
                            thumbnailUrl = Utilities.getModifiedThumbnailLinkForPosts(thumbnailUrl,
                                dataList.get(position).getPurchased());

                            DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
                                .load(thumbnailUrl)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                            Glide.with(this)
                                .load(Utilities.getCoverImageUrlForPost(
                                        dataList.get(position).getImageUrl1(),
                                        dataList.get(position).getMediaType1(),
                                        dataList.get(position).getPurchased(), thumbnailUrl))
                                    .thumbnail(thumbnailRequest)
                                    .dontAnimate()
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(cover);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {

                    try {
                        String thumbnailUrl = dataList.get(position).getThumbnailUrl1();
                        if (dataList.get(position).getPurchased()) {
                            if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                                thumbnailUrl =
                                        thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                            }
                        }
                        thumbnailUrl = Utilities.getModifiedThumbnailLinkForPosts(thumbnailUrl,
                            dataList.get(position).getPurchased());

                        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
                            .load(thumbnailUrl)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                        Glide.with(this)
                            .load(Utilities.getCoverImageUrlForPost(
                                dataList.get(position).getImageUrl1(),
                                dataList.get(position).getMediaType1(),
                                dataList.get(position).getPurchased(), thumbnailUrl))
                                .thumbnail(thumbnailRequest)
                                .dontAnimate()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(cover);
                    } catch (Exception ef) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onCollectionSelect(int pos, CollectionData data, Data selectedPost) {
        mPresenter.addPostToCollection(data.getId(), selectedPost.getId());
    }

    @Override
    public void savedClick(int position, Boolean bookMarked) {
        if (!bookMarked) {
            mPresenter.saveToBookmark(position, dataList.get(position).getPostId());
        } else {
            mPresenter.deleteToBookmark(position, dataList.get(position).getPostId());
        }
    }

    @Override
    public void savedLongCick(int position, Boolean isSaved) {
        dataList.get(position).setBookMarked(isSaved);
    }

    @Override
    public void savedViewClick(int position, Data data) {
        startActivity(new Intent(this, SavedActivity.class));
    }

    @Override
    public void saveToCollectionClick(int position, Data data) {
        // This two get data get from post.
        addToCollectionPostId = data.getId();
        collectionImage = data.getThumbnailUrl1();

        Glide.with(this)
                .load(collectionImage)
                .placeholder(R.color.colorBonJour)
                .centerCrop()
                .into(iV_cImage);

        sheetCollection.setVisibility(View.VISIBLE);
        sheetCollection.post(() -> collectionBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        collectionItemAdapter.setSelectedPost(data);

        if (collectionList.isEmpty()) createNewCollection();
    }

    @Override
    public void onSaveToCollectionClick(int position, Data data) {

        // This two get data get from post.
        addToCollectionPostId = data.getId();
        collectionImage = data.getThumbnailUrl1();

        Glide.with(this)
                .load(collectionImage)
                .placeholder(R.color.colorBonJour)
                .centerCrop()
                .into(iV_cImage);

        sheetCollection.setVisibility(View.VISIBLE);
        sheetCollection.post(() -> collectionBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        collectionItemAdapter.setSelectedPost(data);

        if (collectionList.isEmpty()) createNewCollection();
    }

    @Override
    public void bookMarkPostResponse(int pos, boolean isSaved) {
        dataList.get(pos).setBookMarked(isSaved);
        if (!isSaved)
            Toast.makeText(this, getString(R.string.post_removed_bookmark), Toast.LENGTH_LONG).show();
        //    mAdapter.notifyItemChanged(pos);
    }

    /**
     * Used to hide open keyboard
     */
    public void hideKeyboard(Context ctx) {
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v == null) return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Used to open keyboard
     */
    private void openKeyboard(Context ctx) {
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v == null) return;
        inputManager.toggleSoftInputFromWindow(v.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void showAds() {
    }

    @Override
    public void callProductsApi(ArrayList<String> productIds) {

    }

    @Override
    public void sendTip(int position) {
        SendTipDialog sendTipDialog = new SendTipDialog(dataList.get(position), (data, coin, desc) -> {
            mPresenter.sendTipRequest(data, coin, desc,position);
        });
        sendTipDialog.show(this.getSupportFragmentManager(), "send tip");
    }

    @Override
    public void sendTipSuccess(Data data, String coin, int position) {
        mediaPlayer.start();
        Toast.makeText(this, "Tip Sent to " + data.getUsername(), Toast.LENGTH_SHORT).show();
        dataList.get(this.position).setTipsAmount(data.getTipsAmount()+Double.valueOf(coin));
        socialDetailAdapter.notifyItemChanged(this.position);
    }

    /**
     * For record audio functionality
     */
    private void record(String url, long duration) {
        remoteUrl = url.replace(".mp4", ".mp3").replace(".mov", ".mp3");
        this.duration = duration;
        if (ContextCompat.checkSelfPermission(SocialDetailActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            requestAudioRecord();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(getString(R.string.alert));
                alertDialog.setMessage(
                        getString(R.string.ism_storage_allow_access_to_record_with_this_post));
                alertDialog.setCancelable(true);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                        (dialog, which) -> {
                            rvList.suppressLayout(true);
                            ActivityCompat.requestPermissions(SocialDetailActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    RECORD_REQUEST);
                        });

                alertDialog.show();
            } else {
                rvList.suppressLayout(true);
                ActivityCompat.requestPermissions(SocialDetailActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST);
            }
        }
    }

    private void requestAudioRecord() {

        String fileName = "custom_audio.mp3";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SocialDetailActivity.this);
        @SuppressLint("InflateParams") final View vSelectAudio =
                (this).getLayoutInflater().inflate(R.layout.dialog_select_audio, null);

        LinearLayout llDubWithIt = vSelectAudio.findViewById(R.id.llDubWithIt);
        TextView tvDuration = vSelectAudio.findViewById(R.id.tvDuration);
        TextView tvSound = vSelectAudio.findViewById(R.id.tvSound);
        tvSound.setText(getString(R.string.dub_with_it));
        llDubWithIt.setEnabled(true);
        if (duration == 0) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(remoteUrl, new HashMap<String, String>());

                tvDuration.setText(TimerHelper.getDurationString(Integer.parseInt(
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvDuration.setText(TimerHelper.getDurationString(duration / 1000));
        }

        llDubWithIt.setOnClickListener(v -> {
            llDubWithIt.setEnabled(false);
            tvSound.setText(getString(R.string.preparing_sound));
            DownloadAudioHelper.downloadAudio(vSelectAudio.findViewById(R.id.pbDownload), remoteUrl, this,
                    fileName);
        });

        alertDialog.setView(vSelectAudio);

        alert = alertDialog.create();
        //alert.setCancelable(false);
        if (!isFinishing()) alert.show();
    }

    @Override
    public void downloadResult(String result, String fileName, String filePath) {
        if (alert != null && alert.isShowing()) {
            try {
                alert.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fileName != null && filePath != null) {

            Intent intent = new Intent(SocialDetailActivity.this, DeeparFiltersTabCameraActivity.class);

            intent.putExtra("musicId", "");
            intent.putExtra("audio", filePath);
            intent.putExtra("name", fileName);
            intent.putExtra("isRecord", true);
            startActivity(intent);
        } else {
            runOnUiThread(
                    () -> Toast.makeText(SocialDetailActivity.this, result, Toast.LENGTH_SHORT).show());
        }
    }

    private AlertDialog alert;
    private String remoteUrl;
    private long duration;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {

                case DOWNLOAD_IMAGE_REQUEST: {
                    downloadMedia(0, fileToBeDownloadedName, false);
                    break;
                }
                case DOWNLOAD_VIDEO_REQUEST: {
                    downloadMedia(0, fileToBeDownloadedName, true);
                    break;
                }
                case DOWNLOAD_AS_GIF_REQUEST: {
                    downloadMedia(2, fileToBeDownloadedName, null);
                    break;
                }
                case DUET_REQUEST: {
                    downloadMedia(3, fileToBeDownloadedName, null);
                    break;
                }
                case RECORD_REQUEST: {
                    requestAudioRecord();
                    break;
                }
            }
        }
        try {
            new Handler().postDelayed(() -> rvList.suppressLayout(false), 1000);
        } catch (Exception e) {
        }
    }


    /**
     * Allow download option
     */
    private Data postTobeShared;
    private android.app.AlertDialog customShareDialog;
    private android.app.AlertDialog customLikeCommentDialog;
    private android.app.AlertDialog shareDialog;
    private View customShareView;
    private View customLikeCommentView;
    private View shareView;
    private AppCompatImageView ivDownload;
    private AlertDialog alertDialogDownloadMedia;
    private TextView tvUserName, tvLikeDialogUserName;
    private TextView tvViews, tvLikeDialogViews;
    private TextView tvLikes;
    private TextView tvComments;
    private LinearLayout llLikeView;
    private RelativeLayout rlCommentView, rlSendComment;
    private int position = 0;

    @Override
    public void showCustomShareSheet(Data data, int position, long duration) {
        this.postTobeShared = data;
        this.position = position;
        this.duration = duration;
        this.data = data;

        tvUserName.setText(data.getUsername());
        tvViews.setText(data.getDistinctViews() + " " + getString(R.string.views1));
        ivDownload.setSelected(data.isAllowDownload());
        showCustomOptionsForPost();
    }

    private void showCustomOptionsForPost() {
        rlRecord.setVisibility(postTobeShared.getMediaType1() == 1 ? View.VISIBLE : View.GONE);
        rlDuet.setVisibility(postTobeShared.getMediaType1() == 1 ? View.VISIBLE : View.GONE);
        rlDownloadGif.setVisibility(postTobeShared.getMediaType1() == 1 ? View.VISIBLE : View.GONE);

        boolean isSelf = postTobeShared.getUserId().equals(AppController.getInstance().getUserId());
        rlEdit.setVisibility(isSelf ? View.VISIBLE : View.GONE);
        rlDelete.setVisibility(isSelf ? View.VISIBLE : View.GONE);
        rlReport.setVisibility(isSelf ? View.GONE : View.VISIBLE);
        if (!isSelf) {
            rlReport.setVisibility(postTobeShared.getAlreadyReported() ? View.GONE : View.VISIBLE);
        }
        if (postTobeShared.getBookMarked()) {
            tvBookMark.setText(getString(R.string.unbookmark));
        } else {
            tvBookMark.setText(getString(R.string.bookmark));
        }

        customShareDialog.show();
        customShareDialog.setContentView(customShareView);
        customShareDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = customShareDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        customShareDialog.getWindow().setGravity(Gravity.BOTTOM);
        params.dimAmount = 0.0f;
        customShareDialog.getWindow().setAttributes(params);
        customShareDialog.getWindow()
                .setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
        customShareDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
    }

    private void setupCustomShareDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        customShareView = layoutInflater.inflate(R.layout.bottom_sheet_custom_share, null, false);
        customShareDialog = new android.app.AlertDialog.Builder(this).create();

        rlDuet = customShareView.findViewById(R.id.rlDuet);
        //RelativeLayout rlForward = customShareView.findViewById(R.id.rlForward);
        RelativeLayout rlDownload = customShareView.findViewById(R.id.rlDownload);
        RelativeLayout rlBookMark = customShareView.findViewById(R.id.rlBookMark);
        tvBookMark = customShareView.findViewById(R.id.tvBookMark);

        RelativeLayout rlCopyLink = customShareView.findViewById(R.id.rlCopyLink);
        rlDownloadGif = customShareView.findViewById(R.id.rlDownloadGif);
        rlRecord = customShareView.findViewById(R.id.rlRecord);
        RelativeLayout rlMore = customShareView.findViewById(R.id.rlMore);
        rlReport = customShareView.findViewById(R.id.rlReport);
        rlEdit = customShareView.findViewById(R.id.rlEdit);
        rlDelete = customShareView.findViewById(R.id.rlDelete);
        ivDownload = customShareView.findViewById(R.id.ivDownload);
        tvUserName = customShareView.findViewById(R.id.tvUserName);
        tvViews = customShareView.findViewById(R.id.tvViews);
        rlReport.setOnClickListener(view -> {
            dismissCustomShareDialog();
            reportDialog.show();
        });

        rlEdit.setOnClickListener(view -> {
            dismissCustomShareDialog();
            Intent intentEdit = new Intent(SocialDetailActivity.this, PostActivity.class);
            intentEdit.putExtra("data", data);
            intentEdit.putExtra("call", "edit");
            intentEdit.putExtra(Constants.Post.TYPE,
                    data.getMediaType1() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
            startActivity(intentEdit);
        });

        rlDelete.setOnClickListener(view -> {
            dismissCustomShareDialog();
            reportDialog.setTitle(SocialDetailActivity.this.getString(R.string.delete));
            reportDialog.setMessage(R.string.postDeleteMsg);
            reportDialog.setPositiveButton(R.string.yes,
                    (dialog, which) -> mPresenter.deletePost(data.getPostId()));
            reportDialog.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            reportDialog.create().show();
        });
        rlMore.setOnClickListener(view -> {
            dismissCustomShareDialog();
            showShareToSocialMediaDialog();
        });

        rlCopyLink.setOnClickListener(view -> {

            if (postTobeShared.isAllowDownload()) {
                dismissCustomShareDialog();
                if (chat.hola.com.app.postshare.Utilities.copyMediaWithWatermark(this,
                        postTobeShared.getImageUrl1())) {
                    Toast.makeText(this, R.string.link_copied, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.link_copy_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                //Copy disabled toast
                Toast.makeText(this, R.string.copy_disabled, Toast.LENGTH_SHORT).show();
            }
        });

        rlDownload.setOnClickListener(view -> {

            if (postTobeShared.isAllowDownload()) {
                dismissCustomShareDialog();
                if (postTobeShared.getMediaType1() == 0) {
                    downloadMedia(0, System.currentTimeMillis() + ".jpeg", false);
                } else {
                    downloadMedia(0, System.currentTimeMillis() + ".mp4", true);
                }
            } else {
                //Download disabled toast
                Toast.makeText(this, R.string.download_disabled, Toast.LENGTH_SHORT).show();
            }
        });

        rlDownloadGif.setOnClickListener(view -> {

            if (postTobeShared.getMediaType1() == 1) {
                if (postTobeShared.isAllowDownload()) {
                    dismissCustomShareDialog();

                    downloadMedia(1, System.currentTimeMillis() + ".gif", null);
                } else {
                    //Download disabled toast
                    Toast.makeText(this, R.string.download_disabled, Toast.LENGTH_SHORT).show();
                }
            } else {
                //gifs can only created for video posts toast
                Toast.makeText(this, R.string.gif_not_applicable, Toast.LENGTH_SHORT).show();
            }
        });

        rlDuet.setOnClickListener(view -> {

            if (postTobeShared.getMediaType1() == 1) {

                if (postTobeShared.isAllowDuet()) {
                    dismissCustomShareDialog();
                    downloadMedia(3, System.currentTimeMillis() + ".mp4", null);
                } else {
                    Toast.makeText(this, R.string.duet_disabled, Toast.LENGTH_SHORT).show();
                }
            } else {
                //duets only applicable for video posts
                Toast.makeText(this, R.string.duet_not_applicable, Toast.LENGTH_SHORT).show();
            }
        });

        //rlForward.setOnClickListener(v -> {
        //    if (itemAdapter != null) itemAdapter.setPostItemClickPostion(position);
        //    if (friends != null && !friends.isEmpty()) {
        //        dismissCustomShareDialog();
        //        bottomSheet.setVisibility(View.VISIBLE);
        //        bottomSheet.post(() -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        //    } else {
        //        Toast.makeText(this, getResources().getString(R.string.please_add_friends), Toast.LENGTH_SHORT).show();
        //    }
        //});

        rlBookMark.setOnClickListener(v -> {
            dismissCustomShareDialog();
            if (postTobeShared.getBookMarked()) {
                rlSave.setVisibility(View.GONE);
            } else {
                rlSave.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlSave.setVisibility(View.GONE);
                    }
                }, 4000);
            }
            savedClick(position, postTobeShared.getBookMarked());
        });

        rlBookMark.setOnLongClickListener(v -> {
            if (postTobeShared.getBookMarked()) {
                rlSave.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlSave.setVisibility(View.GONE);
                    }
                }, 4000);
                savedLongCick(position, data.getBookMarked());
                return true;
            } else {
                return false;
            }
        });

        rlRecord.setOnClickListener(v -> {
            if (postTobeShared.getMediaType1() == 1) {
                dismissCustomShareDialog();
                record(postTobeShared.getImageUrl1(), duration);
            } else {
                Toast.makeText(this, R.string.record_not_applicable, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.tV_savedView)
    public void savedView() {
        savedViewClick(position, postTobeShared);
    }

    @OnClick(R.id.tV_saveTo)
    public void savedTo() {
        onSaveToCollectionClick(position, postTobeShared);
    }

    private void dismissCustomShareDialog() {
        if (customShareDialog != null && customShareDialog.isShowing()) {
            try {
                customShareDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void downloadResult(String result, String filePath, boolean duet) {

        if (result == null) {
            if (duet) {
                cancelDownloadDialog();
                Intent intent = new Intent(SocialDetailActivity.this, DeeparDuetActivity.class);
                intent.putExtra("duetFilePath", filePath);
                intent.putExtra("remoteUrl", postTobeShared.getImageUrl1());
                startActivity(intent);
            } else {
                runOnUiThread(() -> {

                    if (OverlayConfig.OVERLAY_REQUIRED) {
                        //Add overlay once media is downloaded successfully
                        if (postTobeShared.getMediaType1() == 1) {
                            if (filePath.endsWith(".gif")) {
                                cancelDownloadDialog();
                                Toast.makeText(SocialDetailActivity.this,
                                        getString(R.string.media_downloaded, filePath), Toast.LENGTH_SHORT).show();
                            } else {
                                //add overlay only to videoposts
                                try {
                                    TextView tvDownloadMedia = alertDialogDownloadMedia.findViewById(R.id.tvDownload);
                                    tvDownloadMedia.setText(getString(R.string.processing));
                                } catch (Exception ignore) {
                                }
                                OverlayUtils.prepareOverlay(SocialDetailActivity.this, postTobeShared, filePath,
                                        this);
                            }
                        } else {
                            cancelDownloadDialog();
                            Toast.makeText(SocialDetailActivity.this,
                                    getString(R.string.media_downloaded, filePath), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        cancelDownloadDialog();
                        Toast.makeText(SocialDetailActivity.this,
                                getString(R.string.media_downloaded, filePath), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            cancelDownloadDialog();
            runOnUiThread(
                    () -> Toast.makeText(SocialDetailActivity.this, result, Toast.LENGTH_SHORT).show());
        }
    }

    private void cancelDownloadDialog() {
        if (alertDialogDownloadMedia != null && alertDialogDownloadMedia.isShowing()) {
            try {
                alertDialogDownloadMedia.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadMedia(int type, String filename, Boolean video) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SocialDetailActivity.this);
            @SuppressLint("InflateParams") final View vDownloadFilters = (SocialDetailActivity.this).getLayoutInflater()
                    .inflate(R.layout.dialog_download_media, null);

            TextView tvDownloadMedia = vDownloadFilters.findViewById(R.id.tvDownload);
            if (type == 3) {
                tvDownloadMedia.setText(getString(R.string.record_duet));
            } else {
                tvDownloadMedia.setText(getString(R.string.download_media));
            }

            TextView tvUserName = vDownloadFilters.findViewById(R.id.tvUserName);

            TextView tvCancel = vDownloadFilters.findViewById(R.id.tvCancel);
            tvUserName.setText(postTobeShared.getUsername());

            tvDownloadMedia.setEnabled(true);
            tvCancel.setVisibility(View.VISIBLE);

            tvDownloadMedia.setOnClickListener(view -> {
                tvDownloadMedia.setEnabled(false);
                if (type == 3) {
                    tvDownloadMedia.setText(getString(R.string.preparing_duet));
                } else {
                    tvDownloadMedia.setText(getString(R.string.downloading_media));
                }

                tvCancel.setVisibility(View.GONE);

                if (type == 0) {
                    chat.hola.com.app.postshare.Utilities.downloadMediaWithWatermark(
                            vDownloadFilters.findViewById(R.id.pbDownload),
                            postTobeShared.getImageUrl1(), this, filename, this,
                            postTobeShared.getMediaType1());
                } else if (type == 1) {
                    chat.hola.com.app.postshare.Utilities.downloadMediaAsGifWithWatermark(
                            vDownloadFilters.findViewById(R.id.pbDownload),
                            postTobeShared.getImageUrl1(), this, filename, this);
                } else {
                    //Duet
                    chat.hola.com.app.postshare.Utilities.downloadMediaForDuet(
                            vDownloadFilters.findViewById(R.id.pbDownload),
                            postTobeShared.getImageUrl1(), this, filename, this);
                }
            });

            alertDialog.setView(vDownloadFilters);
            alertDialog.setCancelable(false);
            alertDialogDownloadMedia = alertDialog.create();
            if (!isFinishing()) alertDialogDownloadMedia.show();
            tvCancel.setOnClickListener(view -> alertDialogDownloadMedia.dismiss());
        } else {

            fileToBeDownloadedName = filename;
            int requestCode = 0;
            String requestMessage = "";
            switch (type) {
                case 0: {
                    //image or video download
                    if (video) {
                        requestCode = DOWNLOAD_VIDEO_REQUEST;
                    } else {
                        requestCode = DOWNLOAD_IMAGE_REQUEST;
                    }
                    requestMessage = getString(R.string.ism_storage_allow_access_to_download_post);
                    break;
                }
                case 1: {
                    //gif download
                    requestCode = DOWNLOAD_AS_GIF_REQUEST;
                    requestMessage =
                            getString(R.string.ism_storage_allow_access_to_download_post_as_gif);
                    break;
                }
                //case 2:
                //    break;
                case 3: {
                    //Duet
                    requestCode = DUET_REQUEST;
                    requestMessage = getString(R.string.ism_storage_allow_access_to_record_duet);
                    break;
                }
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(getString(R.string.alert));
                alertDialog.setMessage(requestMessage);
                alertDialog.setCancelable(true);
                int finalRequestCode = requestCode;
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                        (dialog, which) -> {
                            rvList.suppressLayout(true);
                            ActivityCompat.requestPermissions(SocialDetailActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    finalRequestCode);
                        });

                alertDialog.show();
            } else {
                rvList.suppressLayout(true);
                ActivityCompat.requestPermissions(SocialDetailActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
        }
    }

    @Override
    public void postUpdated(String postId, boolean allowComment, boolean allowDownload,
                            boolean allowDuet, Map<String, Object> body) {

        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getPostId().equals(postId)) {
                Data data = dataList.get(i);
                data.setAllowDownload(allowDownload);
                data.setAllowComments(allowComment);
                data.setAllowDuet(allowDuet);
                data.setTitle((String) body.get("title"));
                data.setCategoryId((String) body.get("categoryId"));
                data.setCategoryName((String) body.get("categoryName"));
                data.setPlace((String) body.get("place"));
                data.setPlaceId((String) body.get("placeId"));
                dataList.set(i, data);
                this.data = data;
                socialDetailAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private void showOverlayDuringApiCall(String url, String height, String width,
        Integer mediaType, boolean isPurchased, String thumbnailUrl) {

        overlay.setVisibility(View.VISIBLE);

        if (isPurchased) {
            if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                thumbnailUrl = thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
            }
        }
        thumbnailUrl = Utilities.getModifiedThumbnailLinkForPosts(thumbnailUrl, isPurchased);

        try {

            double ratio = ((double) Integer.parseInt(height)) / Integer.parseInt(width);

            if (ratio > screenRatio) {
                try {
                    DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
                        .load(thumbnailUrl)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                    Glide.with(this)
                        .load(Utilities.getCoverImageUrlForPost(url, mediaType, isPurchased,
                            thumbnailUrl))
                        .thumbnail(thumbnailRequest)
                        .dontAnimate()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(cover);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
                        .load(thumbnailUrl)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                    Glide.with(this)
                        .load(Utilities.getCoverImageUrlForPost(url, mediaType, isPurchased,
                            thumbnailUrl))
                        .thumbnail(thumbnailRequest)
                        .dontAnimate()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(cover);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

            try {
                DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
                    .load(thumbnailUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                Glide.with(this)
                    .load(Utilities.getCoverImageUrlForPost(url, mediaType, isPurchased,
                        thumbnailUrl))
                    .thumbnail(thumbnailRequest)
                    .dontAnimate()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(cover);
            } catch (Exception ef) {
                e.printStackTrace();
            }
        }
    }

    /**
     * For overlay profile info in downloaded media
     */
    @Override
    public void overlayResult(String result) {
        cancelDownloadDialog();

        runOnUiThread(() -> {

            if (result == null) {
                Toast.makeText(SocialDetailActivity.this, getString(R.string.download_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SocialDetailActivity.this, getString(R.string.media_downloaded, result),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EditText etLikeSearch;
    private RecyclerView rvLikes;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private FollowerAdapter likeAdapter;
    private List<Comment> comments = new ArrayList<>();
    private List<chat.hola.com.app.profileScreen.followers.Model.Data> likers = new ArrayList<>();
    private String postIdForLikeComment;
    private LinearLayoutManager layoutManagerComments;
    private LinearLayoutManager layoutManagerLikes;
    private EditText etComment;
    private ImageView ivSendComment;
    private TextView tvNoLike;
    private TextView tvNoComment;

    //like comment view
    private void openCustomLikeCommentDialog(Data data, boolean isFromLike) {

        tvLikeDialogUserName.setText(data.getUsername());
        tvLikeDialogViews.setText(data.getDistinctViews() + " " + getString(R.string.views1));

        tvLikes.setText(data.getLikesCount() + " " + getString(R.string.likes));
        tvComments.setText(data.getCommentCount() + " " + getString(R.string.comments));
        postIdForLikeComment = data.getPostId();
        if (data.getPurchased()) {
            rlSendComment.setVisibility(View.VISIBLE);
        } else {
            rlSendComment.setVisibility(View.GONE);
        }
        switchLikeComment(isFromLike, postIdForLikeComment);

        customLikeCommentDialog.show();
        customLikeCommentDialog.setContentView(customLikeCommentView);
        customLikeCommentDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = customLikeCommentDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 1000;//WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        customLikeCommentDialog.getWindow().setGravity(Gravity.BOTTOM);
        params.dimAmount = 0.0f;
        customLikeCommentDialog.getWindow().setAttributes(params);
        customLikeCommentDialog.getWindow()
                .setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
        customLikeCommentDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
    }

    private void switchLikeComment(boolean isFromLike, String postId) {
        if (isFromLike) {
            mPresenter.likers(postId, 0, PAGE_SIZE);
        } else {
            mPresenter.getComments(postId, 0, PAGE_SIZE);
        }

        tvLikes.setTextColor(getResources().getColor(isFromLike ? R.color.color_black : R.color.disabled));
        tvComments.setTextColor(getResources().getColor(!isFromLike ? R.color.color_black : R.color.disabled));
        llLikeView.setVisibility(isFromLike ? View.VISIBLE : View.GONE);
        rlCommentView.setVisibility(!isFromLike ? View.VISIBLE : View.GONE);
    }

    private void setupCustomLikeCommentDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        customLikeCommentView = layoutInflater.inflate(R.layout.bottom_sheet_custom_like_comment, null, false);
        customLikeCommentDialog = new android.app.AlertDialog.Builder(this).create();

        tvLikeDialogViews = customLikeCommentView.findViewById(R.id.tvViews);
        tvLikeDialogUserName = customLikeCommentView.findViewById(R.id.tvUserName);

        tvLikes = customLikeCommentView.findViewById(R.id.tvLikes);
        tvComments = customLikeCommentView.findViewById(R.id.tvComments);
        llLikeView = customLikeCommentView.findViewById(R.id.llLikeView);
        rlCommentView = customLikeCommentView.findViewById(R.id.rlCommentView);
        rlSendComment = customLikeCommentView.findViewById(R.id.rlSendComment);
        etLikeSearch = customLikeCommentView.findViewById(R.id.etLikeSearch);
        rvLikes = customLikeCommentView.findViewById(R.id.rvLikes);
        rvComments = customLikeCommentView.findViewById(R.id.rvComments);
        etComment = customLikeCommentView.findViewById(R.id.etComment);
        ivSendComment = customLikeCommentView.findViewById(R.id.ivSendComment);
        tvNoLike = customLikeCommentView.findViewById(R.id.tvNoLikes);
        tvNoComment = customLikeCommentView.findViewById(R.id.tvNoComments);
        tvNoLike.setTypeface(typefaceManager.getSemiboldFont());
        tvNoComment.setTypeface(typefaceManager.getSemiboldFont());

        layoutManagerLikes = new LinearLayoutManager(this);
        likeAdapter = new FollowerAdapter(this, typefaceManager);
        likeAdapter.setOnFollowUnfollowClickCallback(this);
        rvLikes.setLayoutManager(layoutManagerLikes);
        rvLikes.setAdapter(likeAdapter);

        layoutManagerComments = new LinearLayoutManager(this);
        commentAdapter = new CommentAdapter(comments, this, typefaceManager);
        commentAdapter.setListener(this);
        rvComments.setLayoutManager(layoutManagerComments);
        rvComments.setAdapter(commentAdapter);

        tvLikes.setOnClickListener(v -> switchLikeComment(true, postIdForLikeComment));
        tvComments.setOnClickListener(v -> switchLikeComment(false, postIdForLikeComment));

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivSendComment.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivSendComment.setOnClickListener(v -> {
            noComment(false);
            mPresenter.addComment(postIdForLikeComment, etComment.getText().toString());
        });

        etLikeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (query.length() > 0)
                    mPresenter.searchLikers(postIdForLikeComment, 0, PAGE_SIZE, query.toString());
                else
                    mPresenter.likers(postIdForLikeComment, 0, PAGE_SIZE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void noLikes(boolean b) {
        tvNoLike.setVisibility(b ? View.VISIBLE : View.GONE);
        rvLikes.setVisibility(!b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void noComment(boolean b) {
        tvNoComment.setVisibility(b ? View.VISIBLE : View.GONE);
        rvComments.setVisibility(!b ? View.VISIBLE : View.GONE);
    }

    private void dismissCustomLikeCommentDialog() {
        if (customShareDialog != null && customShareDialog.isShowing()) {
            try {
                customShareDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setComments(List<Comment> data, boolean b) {
        if (b) comments.clear();
        comments.addAll(data);
        commentAdapter.setData(comments);
    }

    @Override
    public void commented(boolean flag) {
        if (flag) {
//            commentsAdded++;
            if (etComment != null) etComment.setText(getString(R.string.double_inverted_comma));
            int comment = Integer.parseInt(sessionManager.getCommentCount());
            sessionManager.setCommentCount(String.valueOf(++comment));

            if (rvComments != null) {
                try {
                    rvComments.smoothScrollToPosition(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ivSendComment != null) ivSendComment.setVisibility(View.GONE);
        }
    }

    @Override
    public void addToList(Comment comment, String postId) {
        comment.setProfilePic(sessionManager.getUserProfilePic());
        comments.add(0, comment);
        commentAdapter.setData(comments);
        try {
            socialDetailAdapter.updateCommentCount(String.valueOf(comments.size()),
                    postId, currentPosition, rvList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvComments.setText(comments.size() + " " + getString(R.string.comments));
    }

    @Override
    public void clearLikeList(boolean b) {
        likers.clear();
        likeAdapter.setData(likers);
        noLikes(true);
    }

    @Override
    public void showLikes(List<chat.hola.com.app.profileScreen.followers.Model.Data> data) {
        likers.addAll(data);
        likeAdapter.setData(likers);
        noLikes(likers.size()==0);
    }

    @Override
    public void onUserClick(int position, String userId) {
        startActivity(new Intent(this, ProfileActivity.class).putExtra("userId", userId));
    }

    @Override
    public void itemSelect(int position, boolean isSelected) {

    }

    @Override
    public void onFollow(String userId) {
        mPresenter.follow(userId);
    }

    @Override
    public void onUnfollow(String userId) {
        mPresenter.unfollow(userId);
    }

    @Override
    public void onDeleteComment(int position, Comment comment) {
        mPresenter.deleteComment(position, comment.getPostId(), comment.getId());
    }

    @Override
    public void commentDeleted(int position, String commentId) {
        comments.remove(position);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLikeComment(int position, Comment comment, boolean isLike) {
        mPresenter.likeComment(position, comment.getId(), isLike);
    }

    @Override
    public void commentLiked(int position, String commentId, boolean isLike) {
    }

    /**
     * Paid posts
     */
    @Override
    public void payForPost(Data data, int position, boolean isConfirm) {
        if (!isConfirm) {
            openPaidPostConfirmation(data, position);
        }
    }

    @Override
    public void subscribeProfile(Data data, int position, boolean isConfirm) {
        if (!isConfirm) {
            openSubscriptionDialog(data, position);
        } else {
            if (data.isStar()) {
                mPresenter.subscribeStarUser(data, position);
            } else {
                mPresenter.subscribeProfile(data, position);
            }
        }
    }

    public void openPaidPostConfirmation(Data data, int position) {
        View view =
                LayoutInflater.from(this).inflate(R.layout.paid_post_confirmation_dialog, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        TextView tvAmount = dialog.findViewById(R.id.tvAmount);

        tvAmount.setText(String.valueOf(data.getPostAmount()));

        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(v -> {
            mPresenter.payForPost(data, position, true);
            dialog.dismiss();
        });
        btn_cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void insufficientBalance() {
        Utilities.openInsufficientBalanceDialog(this);
    }

    public void openSubscriptionDialog(Data data, int position) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.subscription_confirmation_dialog, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        TextView tVUserName = dialog.findViewById(R.id.tVUserName);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        ImageView profilePic = dialog.findViewById(R.id.profilePic);
        AppCompatImageView ivStarBadge = dialog.findViewById(R.id.ivStarBadge);
        try {
            Glide.with(this)
                    .load(data.getProfilepic())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.profile_one)
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(profilePic);
        } catch (Exception ignore) {
        }
        if (data.isStar()) {
            ivStarBadge.setVisibility(View.VISIBLE);
        } else {
            ivStarBadge.setVisibility(View.GONE);
        }

        tVUserName.setText(data.getUsername());

        tvMessage.setText(
                getString(R.string.subscribe_confirm) + " " + data.getUsername() + " " + getString(
                        R.string.subscribe_for_month_confirmation, data.getSubscriptionAmount()));

        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(v -> {
            if (data.isStar()) {
                mPresenter.subscribeStarUser(data, position);
            } else {
                mPresenter.subscribeProfile(data, position);
            }
            dialog.dismiss();
        });
        btn_cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onPostPurchased(Data data, int position) {
        dataList.set(position, data);
        socialDetailAdapter.setDataList(mList, dataList);
    }

    @Override
    public void onUserSubscribed(Data data, int position) {
        mediaPlayer.start();
        dataList.set(position, data);
        socialDetailAdapter.setDataList(mList, dataList);
    }

    private void setupShareBottomSheetDialog() {

        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        shareView = layoutInflater.inflate(R.layout.bottomsheet_share, null, false);
        shareDialog = new android.app.AlertDialog.Builder(this).create();

        etMessage = shareView.findViewById(R.id.etMessage);
        shareList = shareView.findViewById(R.id.shareList);
        tvNoDataFound = shareView.findViewById(R.id.tvNoDataFound);
        ivProfilePic = shareView.findViewById(R.id.ivProfilePic);
        shareList.setHasFixedSize(true);
        shareList.setLayoutManager(new LinearLayoutManager(this));
        //
        androidx.appcompat.widget.SearchView searchView = shareView.findViewById(R.id.searchView);

        // listening to search query text change
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //itemAdapter.getFilter().filter(query);
                hideKeyboard(SocialDetailActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void postReported(int position, String postId, boolean reportSuccessfull) {
        if (dataList.get(position).getPostId().equals(postId)) {
            Data data = dataList.get(position);
            data.setAlreadyReported(true);
            dataList.set(position, data);
        } else {

            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getPostId().equals(postId)) {
                    Data data = dataList.get(i);
                    data.setAlreadyReported(true);
                    dataList.set(i, data);
                    break;
                }
            }
        }
        socialDetailAdapter.setDataList(mList, dataList);
    }

    private void setupShareOnSocialMediaBottomSheetDialog() {

        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        shareOnSocialMediaView =
                layoutInflater.inflate(R.layout.bottomsheet_share_to_social_media, null, false);
        shareOnSocialMediaDialog = new android.app.AlertDialog.Builder(this).create();

        rlFacebook = shareOnSocialMediaView.findViewById(R.id.rlFacebook);
        RelativeLayout rlDynamicLink = shareOnSocialMediaView.findViewById(R.id.rlDynamicLink);
        RelativeLayout rlOthers = shareOnSocialMediaView.findViewById(R.id.rlOthers);

        rlFacebook.setOnClickListener(v -> {
            dismissShareToSocialMediaDialog();
            downloadMediaToShare(postTobeShared.getImageUrl1(), postTobeShared.getMediaType1() == 0,
                    postTobeShared.getPostId(), true);
        });

        rlDynamicLink.setOnClickListener(v -> {
            dismissShareToSocialMediaDialog();
            prepareDynamicLink(postTobeShared.getPostId());
        });

        rlOthers.setOnClickListener(v -> {
            dismissShareToSocialMediaDialog();

            downloadMediaToShare(postTobeShared.getImageUrl1(), postTobeShared.getMediaType1() == 0,
                    postTobeShared.getPostId(), false);
        });
    }

    private void showShareToSocialMediaDialog() {

        rlFacebook.setVisibility(isFacebookInstalled() ? View.VISIBLE : View.GONE);

        shareOnSocialMediaDialog.show();
        shareOnSocialMediaDialog.setContentView(shareOnSocialMediaView);
        shareOnSocialMediaDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = shareOnSocialMediaDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        shareOnSocialMediaDialog.getWindow().setGravity(Gravity.BOTTOM);
        params.dimAmount = 0.0f;
        shareOnSocialMediaDialog.getWindow().setAttributes(params);
        shareOnSocialMediaDialog.getWindow()
                .setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
        shareOnSocialMediaDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
    }

    private void dismissShareToSocialMediaDialog() {
        if (shareOnSocialMediaDialog != null && shareOnSocialMediaDialog.isShowing()) {
            try {
                shareOnSocialMediaDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareDynamicLink(String postId) {

        showProgressDialog(getResources().getString(R.string.please_wait));
        new Thread(() -> {
            String url =
                    AppController.getInstance().getString(R.string.dynamic_link_base_url) + postId;
            FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLongLink(Uri.parse(
                            AppController.getInstance().getString(R.string.dynamic_long_link_url)
                                    + url
                                    + AppController.getInstance().getString(R.string.dynamic_query_param)))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.putExtra(Intent.EXTRA_SUBJECT,
                                    AppController.getInstance().getString(R.string.app_name));
                            intent.setType("text/plain");
                            Intent chooser = Intent.createChooser(intent,
                                    AppController.getInstance().getString(R.string.share_post));
                            startActivity(chooser);
                        }
                        hideProgressDialog();
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }).start();
    }

    private final FacebookCallback<Sharer.Result> shareCallback =
            new FacebookCallback<Sharer.Result>() {
                @Override
                public void onCancel() {
                    Toast.makeText(SocialDetailActivity.this,
                            AppController.getInstance().getString(R.string.facebook_share_canceled),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(SocialDetailActivity.this,
                            String.format("Error: %s", error.toString()), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(SocialDetailActivity.this,
                            AppController.getInstance().getString(R.string.facebook_share_successfull),
                            Toast.LENGTH_SHORT).show();
                }
            };

    private void shareOnFacebook(boolean isImage, String mediaPath) {

        File media = new File(mediaPath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            uri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider", media);
        } else {
            uri = Uri.fromFile(media);
        }
        if (isImage) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setImageUrl(uri).build();

            SharePhotoContent photoContent =
                    new SharePhotoContent.Builder().addPhoto(sharePhoto).build();

            if (ShareDialog.canShow(SharePhotoContent.class)) {
                shareOnFacebookDialog.show(photoContent);
                //shareDialog.show(photoContent,ShareDialog.Mode.WEB);
            } else {

                runOnUiThread(
                        () -> Toast.makeText(this, getString(R.string.facebook_share_not_available),
                                Toast.LENGTH_SHORT).show());
            }
        } else {
            // Create the URI from the media

            ShareVideo shareVideo = new ShareVideo.Builder().setLocalUrl(uri).build();
            ShareVideoContent videoContent = new ShareVideoContent.Builder().setVideo(shareVideo)
                    .setContentTitle(getString(R.string.share_post_title))
                    .setContentDescription(getString(R.string.share_post_description))
                    //.setPreviewPhoto()
                    .build();
            if (ShareDialog.canShow(ShareVideoContent.class)) {
                shareOnFacebookDialog.show(videoContent);
            } else {

                runOnUiThread(
                        () -> Toast.makeText(this, getString(R.string.facebook_share_not_available),
                                Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void shareOnOtherSocialMediaApps(boolean isImage, String mediaPath) {

        String type;
        if (isImage) {
            type = "image/*";
        } else {
            type = "video/*";
        }

        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType(type);
        List<ResolveInfo> queryIntentActivities =
                getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!queryIntentActivities.isEmpty()) {

            for (ResolveInfo resolveInfo : queryIntentActivities) {
                String packageName = resolveInfo.activityInfo.packageName;

                if (supportedSocialMediaAppsConfig.isSupportedAppInstalled(packageName)) {
                    Intent intent = new Intent();
                    intent.setComponent(
                            new ComponentName(packageName, resolveInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType(type);

                    // Create the URI from the media
                    File media = new File(mediaPath);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        uri = FileProvider.getUriForFile(this,
                                getApplicationContext().getPackageName() + ".provider", media);
                    } else {
                        uri = Uri.fromFile(media);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.share_post_description));
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {

                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0),
                        getString(R.string.choose_app));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(this, getString(R.string.no_apps_installed), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void downloadMediaToShare(String mediaUrl, boolean isImage, String postId,
                                     boolean shareOnFacebook) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog =
                new androidx.appcompat.app.AlertDialog.Builder(this);
        @SuppressLint("InflateParams") final View vDownload = getLayoutInflater().inflate(R.layout.share_confirmation, null);

        AppCompatTextView tvShare = vDownload.findViewById(R.id.tvShare);
        tvShare.setEnabled(true);
        tvShare.setText(getString(R.string.share));
        tvShare.setOnClickListener(v -> {
            shareAlert.setCancelable(false);
            tvShare.setEnabled(false);
            tvShare.setText(getString(R.string.share_preparing));
            String fileName;
            if (isImage) {
                fileName = postId + ".jpg";
            } else {
                fileName = postId + ".mp4";
            }

            DownloadMedia.startMediaDownload(vDownload.findViewById(R.id.pbDownload),
                    mediaUrl.replace("upload/", "upload/" + WatermarkConfig.WATERMARK_CONFIG), this,
                    fileName, isImage, shareOnFacebook);
        });

        alertDialog.setView(vDownload);

        shareAlert = alertDialog.create();
        shareAlert.setCancelable(true);
        if (!isFinishing()) shareAlert.show();
    }

    @Override
    public void downloadResult(String result, String filePath, boolean isImage,
                               boolean shareOnFacebook) {
        if (shareAlert != null && shareAlert.isShowing()) {
            try {
                shareAlert.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            if (shareOnFacebook) {
                shareOnFacebook(isImage, filePath);
            } else {
                shareOnOtherSocialMediaApps(isImage, filePath);
            }
        } else {

            runOnUiThread(
                    () -> Toast.makeText(SocialDetailActivity.this, result, Toast.LENGTH_SHORT).show());
        }
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public boolean isFacebookInstalled() {

        return supportedSocialMediaAppsConfig.isFacebookInstalled(getPackageManager());
    }
}