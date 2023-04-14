package chat.hola.com.app.home.xclusive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.DublyCamera.dubbing.DownloadAudioHelper;
import chat.hola.com.app.DublyCamera.dubbing.DownloadResult;
import chat.hola.com.app.DublyCamera.duet.DeeparDuetActivity;
import chat.hola.com.app.DublyCamera.overlay.OverlayConfig;
import chat.hola.com.app.DublyCamera.overlay.OverlayProfileInfoResult;
import chat.hola.com.app.DublyCamera.overlay.OverlayUtils;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.coin.base.CoinActivity;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.collections.saved.SavedActivity;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.CommentAdapter;
import chat.hola.com.app.ecom.home.ProductListBottomSheet;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.adapter.SuggestUserAdapter;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.User;
import chat.hola.com.app.home.social.CollectionItemAdapter;
import chat.hola.com.app.home.social.SendTipDialog;
import chat.hola.com.app.live_stream.Home.live_users.LiveUsersActivity;
import chat.hola.com.app.live_stream.utility.TimerHelper;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.music.MusicActivity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.postshare.DownloadMediaResult;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.followers.FollowerAdapter;
import chat.hola.com.app.profileScreen.followers.FollowersActivity;
import chat.hola.com.app.socialDetail.ActionListner;
import chat.hola.com.app.socialDetail.ItemAdapter;
import chat.hola.com.app.socialDetail.SocialDetailAdapter;
import chat.hola.com.app.socialDetail.ViewHolder;
import chat.hola.com.app.socialDetail.video_manager.BaseVideoItem;
import chat.hola.com.app.socialDetail.video_manager.ClickListner;
import chat.hola.com.app.socialDetail.video_manager.ItemFactory;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import chat.hola.com.app.webScreen.WebActivity;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.CategoryDetails;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;
import dagger.android.support.DaggerFragment;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class XclusiveFragment extends DaggerFragment
    implements XclusiveContract.View, ClickListner, ActionListner, ItemAdapter.ItemListener,
    DialogInterface.OnClickListener, PopupMenu.OnMenuItemClickListener,
    CollectionItemAdapter.ClickListener, DownloadResult, DownloadMediaResult,
    OverlayProfileInfoResult, chat.hola.com.app.comment.model.ClickListner,
    FollowerAdapter.OnFollowUnfollowClickCallback, SuggestUserAdapter.ClickListener {

    private static final int COMMENT_COUNT = 1010;
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private static final String TAG = XclusiveFragment.class.getSimpleName();
    private ProductListBottomSheet customBottomSheetDialogFragment;
    private RelativeLayout rlRecord, rlEdit, rlDelete, rlReport, rlDownloadGif, rlDuet;
    private TextView tvBookMark;
    private TextView tvNoDataFound;


    @Inject
    public XclusiveFragment() {
    }

    @Inject
    BlockDialog dialog;
    @Inject
    XclusivePresenter mPresenter;
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

    //Share dialog
    RoundedImageView ivProfilePic;
    EditText etMessage;
    RecyclerView shareList;

    @BindView(R.id.overlay)
    View overlay;

    @BindView(R.id.cover)
    AppCompatImageView cover;
    @BindView(R.id.tvCoinBalance)
    public TextView tvCoinBalance;

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

    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.tvMsg)
    TextView tvMsg;

    @BindView(R.id.rL_save)
    RelativeLayout rlSave;
    @BindView(R.id.tV_saveTo)
    TextView tvSaveTo;
    @BindView(R.id.tV_savedView)
    TextView tvSavedView;

    @BindView(R.id.rvSuggestUser)
    RecyclerView rvSuggestUser;

    CollectionItemAdapter collectionItemAdapter;
    private List<CollectionData> collectionList = new ArrayList<>();
    private BottomSheetBehavior collectionBehavior;
    // used while new collection creation.
    private String collectionImage = "", addToCollectionPostId = "";
    ///////////////////////////////

    private String postId;
    private Data data = null;
    private int position = 0;
    private String userId;
    private String categoryId;
    private Menu menu;
    private String call;
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

    //    private boolean needToScrollVideo = false;
    private KeyguardManager keyboardManager;
    private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    private double screenRatio = ((double) displayMetrics.heightPixels / displayMetrics.widthPixels);

    // private boolean notFirstResume = true;
    private final VideoPlayerManager<MetaData> mVideoPlayerManager =
        new SingleVideoPlayerManager(new PlayerItemChangeListener() {
            @Override
            public void onPlayerItemChanged(MetaData metaData) {

            }
        });

    private List<Friend> friends = new ArrayList<>();

    private Unbinder unbinder;
    private Activity mActivity;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @Inject
    SuggestUserAdapter suggestUserAdapter;

    /*This is use for begin with 0 index*/
    public static final int page = 0;
    public static final int pageSize = Constants.PAGE_SIZE;

    private Bus bus = AppController.getBus();
    private MediaPlayer mediaPlayer;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        try {
            bus.register(this);
        } catch (IllegalArgumentException ignored) {
            ignored.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        changeVisibilityOfViews();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.coin_spend);
        tvCoinBalance.setText(sessionManager.getCoinBalance());

        swipeRefresh.setProgressViewOffset(false, CommonClass.dpToPx(mActivity, 20),
            CommonClass.dpToPx(mActivity, 50));

        mPresenter.attachView(this);
        mPresenter.subscribePostUpdateObserver();

        dataList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(mLayoutManager);

        socialDetailAdapter =
            new SocialDetailAdapter(mVideoPlayerManager, mActivity, mList, dataList,
                getScreenWidth(), getScreenHeight(),false);

        socialDetailAdapter.setClickListner(this);

        rvList.setAdapter(socialDetailAdapter);
        try {
            mVideoPlayerManager.stopAnyPlayback(true);
        } catch (Exception ignore) {
        }
        mPresenter.fetchXclusivePosts(page, pageSize, true, false);

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

                    mPresenter.callApiOnScroll(mLayoutManager.findFirstVisibleItemPosition(),
                        visibleItemCount, mLayoutManager.getItemCount());
                    data = dataList.get(visibleItemCount);
                    position = visibleItemCount;
                } catch (Exception ignored) {

                }
            }
        });

        swipeRefresh.setOnRefreshListener(
            () ->{
                try {
                        mVideoPlayerManager.stopAnyPlayback(true);
                    } catch (Exception ignore) {
                    }
                mPresenter.fetchXclusivePosts(page, pageSize, false, true);});

        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, rvList);

        mPresenter.getFollowUsers();
        //mPresenter.getCollections();
        try {
            Glide.with(mActivity)
                .load(sessionManager.getUserProfilePic().replace("upload/", Constants.PROFILE_PIC_SHAPE))
                .asBitmap()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .centerCrop()
                .into(ivProfilePic);
        }catch(Exception ignore){}
        reportDialog.setTitle(R.string.report);
        mPresenter.getReportReasons();
        reportDialog.setAdapter(arrayAdapter, this);

        collectionItemAdapter = new CollectionItemAdapter(getActivity(), collectionList);
        collectionItemAdapter.setListener(this);
        rV_collections.setAdapter(collectionItemAdapter);
        rV_collections.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        collectionBehavior = BottomSheetBehavior.from(sheetCollection);
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        collectionBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        blurView.setVisibility(View.GONE);
                        ((LandingActivity) mActivity).iVcamera.setColorFilter(null);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        blurView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        blurView.setVisibility(View.VISIBLE);
                        ((LandingActivity) mActivity).iVcamera.setColorFilter(
                            ContextCompat.getColor(mActivity, R.color.white));
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

        keyboardManager = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);

        //suggest user
        rvSuggestUser.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        suggestUserAdapter.setClickListener(this);
        rvSuggestUser.setAdapter(suggestUserAdapter);
        mPresenter.suggestUser();


        tvEmpty.setText(getString(R.string.no_xclusive_posts));
        tvMsg.setText(getString(R.string.no_xclusive_posts_follow));


        return view;
    }

    @OnClick(R.id.llLive)
    public void activity() {
        if (getActivity() != null) {
            if (AppController.getInstance().isGuest()) {

                AppController.getInstance().openSignInDialog(getActivity());
            } else {

                startActivity(new Intent(getContext(), LiveUsersActivity.class).putExtra("isLiveStream", true).putExtra("fromLandingActivity", true));
                getActivity().overridePendingTransition(R.anim.left_enter, R.anim.left_exit);
            }
        }
    }

    @OnClick(R.id.llCoinBalance)
    public void coin() {
        if (getActivity() != null) {
            if (AppController.getInstance().isGuest()) {

                AppController.getInstance().openSignInDialog(getActivity());
            } else {
                startActivity(new Intent(getContext(), CoinActivity.class));
            }
        }
    }

    @OnClick(R.id.btnFindPeople)
    public void findPeopleToFollow() {
        Intent intent = new Intent(mActivity, DiscoverActivity.class);
        intent.putExtra("caller", "SettingsActivity");
        intent.putExtra("is_contact", true);
        startActivity(intent);
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

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(mActivity);
        confirm.setMessage(R.string.report_message);
        confirm.setPositiveButton(R.string.confirm,
            (dialog, w) -> mPresenter.reportPost(position,postTobeShared.getPostId(), arrayAdapter.getItem(which),
                arrayAdapter.getItem(which)));
        confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
        confirm.create().show();
    }

    private void invalidateMenu(Data data) {
        try {

            postId = data.getPostId();

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
            ignored.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        holdLastPositionBeforeLeave();
    }

    private void holdLastPositionBeforeLeave() {
        mVideoPlayerManager.stopAnyPlayback(true);
        lastVisibleItemPositionOnActivityStop = mLayoutManager.findLastVisibleItemPosition();
        showOverlay(lastVisibleItemPositionOnActivityStop);
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();

        if (unbinder != null) unbinder.unbind();

        if (bus != null) bus.unregister(this);

        try {
            mVideoPlayerManager.resetMediaPlayer();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void resetMediaPlayer() {
        try {
            mVideoPlayerManager.resetMediaPlayer();
            lastVisibleItemPositionOnActivityStop = mLayoutManager.findLastVisibleItemPosition();
            showOverlay(lastVisibleItemPositionOnActivityStop);
        } catch (Exception e) {
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
                    mList.set(postion, ItemFactory.createItem(mActivity, mVideoPlayerManager, data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dataList.add(0,data);
                List<Data> list = new ArrayList<>();
                list.add(data);
                setData(list, true);
            }

            socialDetailAdapter.setDataList(mList, dataList);
        } else {
            showMessage("", R.string.no_post_available);
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

    public void changeVisibilityOfViews() {
        LandingActivity mActivity = (LandingActivity) getActivity();
        mActivity.hideActionBar();
        mActivity.fullScreenFrame();
        mActivity.linearPostTabs.setVisibility(View.VISIBLE);
//        mActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (collectionBehavior != null) {
            collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        mActivity.bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.transparent));
        mActivity.iVcamera.setColorFilter(null);
        mActivity.tvSearch.setVisibility(View.GONE);
        //        if(socialDetailAdapter!=null)
        //            socialDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCollectionSelect(int pos, CollectionData data, Data selectedPost) {
        mPresenter.addPostToCollection(data.getId(), selectedPost.getId());
    }

    @Override
    public void deleted(String postId) {
        /* current user delete their post deleted */

        int position = -1;
        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getPostId().equals(postId)) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            dataList.remove(position);
            mList.remove(position);
            socialDetailAdapter.notifyDataSetChanged();
        }
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
//        for (int i = 0; i < dataList.size(); i++) {
//            if (dataList.get(i).getUserId().equals(userId)) {
//                dataList.remove(i);
//                mList.remove(i);
//            }
//        }
//        socialDetailAdapter.setDataList(mList,dataList);
        reload();
    }

    @Override
    public void postUpdated(String postId, boolean allowComment, boolean allowDownload, boolean allowDuet, Map<String, Object> body) {

    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(mActivity, msgId != 0 ? getString(msgId) : msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(mActivity);
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_COUNT && resultCode == RESULT_OK) {
            currentPosition = data.getIntExtra("position", 0);

            try {
                socialDetailAdapter.updateCommentCount(data.getStringExtra("commentCount"),
                    data.getStringExtra("postId"), data.getIntExtra("position", 0), rvList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resetPlayBackOnList() {
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
                    hideOverlay();
                    e.printStackTrace();
                }
            }
        } else {
            //For some cases when screen is showing as locked although it is not(happens rarely),in which case video playback doesnt resume until scrolled
            if (overlay.getVisibility() == View.VISIBLE) {
                hideOverlay();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.getCollections();
        mPresenter.getFollowUsers();
        if (notFirstResume) {
            if (isVisible()) {
                resetPlayBackOnList();
            }
        } else {
            notFirstResume = true;
        }
    }

    //    @Override
    //    public boolean onSupportNavigateUp() {
    //        onBackPressed();
    //        return true;
    //    }

    /*
     * Bug Title: media playing stuck
     * Bug Id: XXXXXXXX
     * Fix Description: stop media player when reload
     * Developer Name: Hardik
     * Fix Date: 9/4/2021
     * */

    @Override
    public void reload() {
        try {
            mVideoPlayerManager.stopAnyPlayback(true);
        } catch (Exception ignore) {
        }
        mPresenter.fetchXclusivePosts(page, pageSize, true, false);
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
                .setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.color.transparent));
            shareDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
        } else {
            Toast.makeText(mActivity, getResources().getString(R.string.please_add_friends), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void profile(String id, boolean isChannel, boolean isBusiness) {
        if (isBusiness) {
            Intent intent = new Intent(mActivity, ProfileActivity.class);
            intent.putExtra("isBusiness", true);
            intent.putExtra(Constants.SocialFragment.USERID, id);
            startActivity(intent);
        } else if (isChannel) {
            Intent intent = new Intent(mActivity, TrendingDetail.class);
            intent.putExtra("channelId", id);
            intent.putExtra("call", "channel");
            startActivity(intent);
        } else {
            Intent intent = new Intent(mActivity, ProfileActivity.class);
            intent.putExtra(Constants.SocialFragment.USERID, id);
            startActivity(intent);
        }
    }

    @Override
    public void comment(Data data, String postId, int position, String commentsCount, boolean commentsEnabled) {
        if (commentsEnabled) {
            currentPosition = position;
            openCustomLikeCommentDialog(data, false);
//            startActivityForResult(
//                    new Intent(mActivity, CommentActivity.class).putExtra("position", position)
//                            .putExtra("postId", postId)
//                            .putExtra("commentsCount", commentsCount), COMMENT_COUNT);
        } else {
            Toast.makeText(mActivity, R.string.comments_disabled, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void category(String categoryId, String categoryName) {
        startActivity(new Intent(mActivity, TrendingDetail.class).putExtra("categoryId", categoryId)
            .putExtra("call", "category")
            .putExtra("category", categoryName));
    }

    @Override
    public void channel(String channelId, String channelName) {
        startActivity(new Intent(mActivity, TrendingDetail.class).putExtra("call", "channel")
            .putExtra("channelId", channelId));
    }

    @Override
    public void music(String id, String name, String path, String artist) {
        startActivity(new Intent(mActivity, MusicActivity.class).putExtra("musicPath", path)
                .putExtra("call", "music")
                .putExtra("musicId", id)
                .putExtra("artist", artist)
                .putExtra("name", name));
    }

    @Override
    public void followers(List<Friend> data) {
        friends = data;
        itemAdapter = new ItemAdapter(mActivity, friends, this, typefaceManager);
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
            dataList.clear();
            mList.clear();
            showEmpty(data.isEmpty());
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
                            ItemFactory.createItem(getActivity(), mVideoPlayerManager,
                                data.get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    nonDuplicateData.add(data.get(i));
                }
            }
        }

        if (refreshRequest) {
            dataList.addAll(0, nonDuplicateData);
        } else {
            dataList.addAll(nonDuplicateData);
        }

        setData(nonDuplicateData, refreshRequest);

        socialDetailAdapter.setDataList(mList, dataList);

        if ((entirelyNewList || refreshRequest) && mList.size() > 0) {
            rvList.post(() -> {
                try {
                    BaseVideoItem videoItem = mList.get(0);

                    ViewHolder viewHolder = (ViewHolder) rvList.findViewHolderForAdapterPosition(0);
                    if (viewHolder != null) {

                        videoItem.setActive(viewHolder.itemView, 0,false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        showEmpty(dataList.isEmpty());
    }

    @Override
    public void showProgress(boolean show) {
        if (swipeRefresh != null) swipeRefresh.setRefreshing(show);
    }

    @Override
    public void showEmpty(boolean show) {
        llEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        rvList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void bookMarkPostResponse(int pos, boolean isSaved) {
        dataList.get(pos).setBookMarked(isSaved);
        if(!isSaved)
            Toast.makeText(mActivity,getString(R.string.post_removed_bookmark),Toast.LENGTH_LONG).show();
    }

    @Override
    public void collectionCreated() {
        hideKeyboard(requireActivity());
        sheetCollection.setVisibility(View.GONE);
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        backFromCreateCollection();
        et_cName.setText(getString(R.string.double_inverted_comma));
        mPresenter.getCollections();

        Toast.makeText(getContext(), R.string.collection_created, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void postAddedToCollection() {
        sheetCollection.setVisibility(View.GONE);
        collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        backFromCreateCollection();

        Toast.makeText(getContext(), R.string.added_to_collection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void collectionFetched(List<CollectionData> data) {
        collectionList.clear();
        collectionList.addAll(data);

        if (!collectionList.isEmpty()) collectionList.remove(0);

        collectionItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateSavedOnObserve(PostUpdateData savedData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getPostId() != null && dataList.get(i)
                .getPostId()
                .equals(savedData.getPostId())) {
                dataList.get(i).setBookMarked(savedData.isLike());
                socialDetailAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onItemClick(Friend userdata, int position) {
        JSONObject obj;
        obj = new JSONObject();
        try {
            //this.position=position;
            data = dataList.get(position);
            /*
             * Post
             */
            try {
                String documentId =
                    AppController.findDocumentIdOfReceiver(userdata.getId(), Utilities.tsInGmt(),
                        userdata.getFirstName(), userdata.getProfilePic(), "", false,
                        userdata.getUserName(), "", false, userdata.isStar());

                String payload = data.getMediaType1() == 0 ? data.getImageUrl1()
                    : data.getImageUrl1().replace("mp4", "jpg");
                String tsForServer = Utilities.tsInGmt();
                String tsForServerEpoch = new Utilities().gmtToEpoch(tsForServer);

                obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                obj.put("from", AppController.getInstance().getUserId());
                obj.put("payload", Base64.encodeToString(payload.getBytes("UTF-8"), Base64.DEFAULT).trim());
                obj.put("timestamp", tsForServerEpoch);
                obj.put("id", tsForServerEpoch);
                obj.put("type", "13");
                obj.put("name", AppController.getInstance().getUserName());
                obj.put("postId", data.getPostId());
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
                map.put("postId", data.getPostId());
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
                mapTemp.put("postId", data.getPostId());
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
                    .publishChatMessage(MqttEvents.Message.value + "/" + userdata.getId(), obj, 1,
                        false, map1);
                Toast.makeText(mActivity, "Post sent...", Toast.LENGTH_SHORT).show();

                bus.post(new JSONObject().put("eventName", "postSent"));
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

                    mList.add(i, ItemFactory.createItem(mActivity, mVideoPlayerManager, dataList.get(i)));
                } else {

                    mList.add(ItemFactory.createItem(mActivity, mVideoPlayerManager, dataList.get(i)));
                }
                try {
                    if (data != null) {
                        String thumbnailUrl = data.getThumbnailUrl1();
                        if(data.getPurchased()) {
                        if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                            thumbnailUrl =
                                thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                        }}
                        Glide.with(mActivity)
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
        popupMenu = new PopupMenu(mActivity, ibMenu);
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
        startActivity(
            new Intent(mActivity, TrendingDetail.class).putExtra("placeId", data.getPlaceId())
                .putExtra("call", "location")
                .putExtra("location", data.getPlace())
                .putExtra("latlong", data.getLocation()));
    }

    @Override
    public void openLikers(Data data) {
        openCustomLikeCommentDialog(data, true);
//        Intent intent = new Intent(mActivity, FollowersActivity.class);
//        intent.putExtra("title", getResources().getString(R.string.likers));
//        intent.putExtra("userId", data.getPostId());
//        startActivity(intent);
    }

    @Override
    public void openViewers(Data data) {
        Intent intent = new Intent(mActivity, FollowersActivity.class);
        intent.putExtra("title", "Viewers");
        intent.putExtra("userId", data.getPostId());
        startActivity(intent);
    }

    @Override
    public void onActionButtonClick(String title, String url) {
        if (!url.contains("http")) url = "http://" + url;
        Intent intent = new Intent(mActivity, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        intent.putExtra("url_data", bundle);
        startActivity(intent);
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
    public void savedLongCick(int position, Boolean bookMarked) {

    }

    @Override
    public void savedViewClick(int position, Data data) {
        startActivity(new Intent(getActivity(), SavedActivity.class));
    }

    @Override
    public void onSaveToCollectionClick(int position, Data data) {
        // This two get data get from post.
        addToCollectionPostId = data.getId();
        collectionImage = data.getThumbnailUrl1();

        Glide.with(getActivity())
            .load(collectionImage)
            .placeholder(R.color.colorBonJour)
            .centerCrop()
            .into(iV_cImage);

        sheetCollection.setVisibility(View.VISIBLE);
        sheetCollection.post(() -> collectionBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        collectionItemAdapter.setSelectedPost(data);

        if (collectionList.isEmpty()) createNewCollection();
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
                collectionBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                openKeyboard(requireActivity());
            }
        }, 500);
    }

    @OnClick({R.id.cBack})
    public void backFromCreateCollection() {

        hideKeyboard(requireActivity());

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

    @OnClick(R.id.rlItem)
    public void mainViewClick() {

        sheetCollection.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case R.id.action_report:
                    reportDialog.show();
                    return true;
                case R.id.action_edit:
                    Intent intentEdit = new Intent(mActivity, PostActivity.class);
                    intentEdit.putExtra("data", data);
                    intentEdit.putExtra("call", "edit");
                    intentEdit.putExtra(Constants.Post.TYPE,
                        data.getMediaType1() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
                    startActivity(intentEdit);
                    //finish();
                    return true;
                case R.id.action_delete:
                    reportDialog.setTitle(mActivity.getString(R.string.delete));
                    reportDialog.setMessage(R.string.postDeleteMsg);
                    reportDialog.setPositiveButton(R.string.yes,
                        (dialog, which) -> mPresenter.deletePost(postId));
                    reportDialog.setNegativeButton(R.string.no,
                        (dialog, which) -> dialog.dismiss());
                    reportDialog.create().show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    public void playVideoOnResume(int initialPosition, int finalPosition) {
        if(rvList!=null) {
            rvList.smoothScrollToPosition(finalPosition);
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(rvList!=null) {
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
            hideOverlay();
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
        if (overlay != null) overlay.setVisibility(View.GONE);
    }

    private void showOverlay(int position) {
        if (position >= 0) {
            if (dataList != null && !dataList.isEmpty()) {
                if (overlay != null) overlay.setVisibility(View.VISIBLE);

                try {

                    double ratio =
                        ((double) Integer.parseInt(dataList.get(position).getImageUrl1Height())) / Integer.parseInt(dataList.get(position).getImageUrl1Width());

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

                            DrawableRequestBuilder<String> thumbnailRequest = Glide.with(mActivity)
                                .load(thumbnailUrl)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                            Glide.with(mActivity)
                                .load(Utilities.getCoverImageUrlForPost(
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

                            DrawableRequestBuilder<String> thumbnailRequest = Glide.with(mActivity)
                                .load(thumbnailUrl)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                            Glide.with(mActivity)
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

                        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(mActivity)
                            .load(thumbnailUrl)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                        Glide.with(mActivity)
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
        }    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("postCompleted")) {
                //
                //Handler handler = new Handler();
                //handler.postDelayed(() -> {
                //    try {
                //        shareToSocial(object.getString("type"), object.getString("mediaPath"),
                //            object.getString("url"), object.getBoolean("facebook"),
                //            object.getBoolean("twitter"), object.getBoolean("instagram"));
                //    } catch (JSONException e) {
                //        e.printStackTrace();
                //    }
                //    try {
                //        mVideoPlayerManager.stopAnyPlayback(true);
                //    } catch (Exception ignore) {
                //    }
                //    mPresenter.fetchXclusivePosts(page, pageSize, false, true);
                //}, 1500);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void shareToSocial(String type, String mediaPath, String url, boolean facebook,
        boolean twitter, boolean instagram) {

        if (twitter) {
            shareToSocial(type, mediaPath, "com.twitter.android");
        }

        if (facebook) {
            shareToSocial(type, mediaPath, "com.facebook.katana");
        }

        if (instagram) {
            shareToSocial(type, mediaPath, "com.instagram.android");
        }
    }

    /*Sharign the data in instagram. */
    private void shareToSocial(String type, String mediaPath, String app) {
        Log.d("insta image path", mediaPath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type.equals("1") ? "video/*" : "image/*");
        try {
            File media = new File(mediaPath);

            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                uri = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider", media);
            } else {
                uri = Uri.fromFile(media);
            }

            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage(app);

            startActivity(Intent.createChooser(share, "Share to"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to hide open keyboard
     */
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager =
            (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null) return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Used to open keyboard
     */
    private void openKeyboard(Context ctx) {
        InputMethodManager inputManager =
            (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null) return;
        inputManager.toggleSoftInputFromWindow(v.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void showAds() {
        LandingActivity mActivity = (LandingActivity) getActivity();
        assert mActivity != null;
        Log.d(TAG, "showAds: " + mActivity.mInterstitialAd.isLoaded());
        if (mActivity.mInterstitialAd.isLoaded()) {
            holdLastPositionBeforeLeave();
            mActivity.mInterstitialAd.show();
        }
    }

    @Override
    public void callProductsApi(ArrayList<String> productIds) {
        mPresenter.callProductsAPi(productIds);
    }

    @Override
    public void sendTip(int position) {
        SendTipDialog sendTipDialog = new SendTipDialog(dataList.get(position), (data, coin, desc) -> {
            mPresenter.sendTipRequest(data, coin, desc,position);
        });
        sendTipDialog.show(getActivity().getSupportFragmentManager(), "send tip");
    }

    /*shows the products */
    public void showProducts(ArrayList<CategoryDetails> products) {
        if (customBottomSheetDialogFragment == null) {
            customBottomSheetDialogFragment = new ProductListBottomSheet(products);
        }
        if (!customBottomSheetDialogFragment.isAdded()) {
            customBottomSheetDialogFragment.show(getChildFragmentManager(),
                ProductListBottomSheet.Companion.getTAG());
        }
    }

    /**
     * For record audio functionality
     */
    private void record(String url, long duration) {
        remoteUrl = url.replace(".mp4", ".mp3").replace(".mov", ".mp3");
        this.duration = duration;
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {

            requestAudioRecord();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                alertDialog.setTitle(getString(R.string.alert));
                alertDialog.setMessage(
                    getString(R.string.ism_storage_allow_access_to_record_with_this_post));
                alertDialog.setCancelable(true);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                    (dialog, which) -> {
                        rvList.suppressLayout(true);
                        requestPermissions(
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            RECORD_REQUEST);
                    });

                alertDialog.show();
            } else {
                rvList.suppressLayout(true);
                requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    RECORD_REQUEST);
            }
        }
    }

    private void requestAudioRecord() {

        String fileName = "custom_audio.mp3";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        @SuppressLint("InflateParams")
        final View vSelectAudio =
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
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
                    / 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvDuration.setText(TimerHelper.getDurationString(duration / 1000));
        }

        llDubWithIt.setOnClickListener(v -> {
            llDubWithIt.setEnabled(false);
            tvSound.setText(getString(R.string.preparing_sound));
            DownloadAudioHelper.downloadAudio(vSelectAudio.findViewById(R.id.pbDownload), remoteUrl,
                this, fileName);
        });

        alertDialog.setView(vSelectAudio);

        alert = alertDialog.create();
        //alert.setCancelable(false);
        if (isAdded() && !mActivity.isFinishing()) alert.show();
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

            Intent intent = new Intent(mActivity, DeeparFiltersTabCameraActivity.class);

            intent.putExtra("musicId", "");
            intent.putExtra("audio", filePath);
            intent.putExtra("name", fileName);
            intent.putExtra("isRecord", true);
            startActivity(intent);
        } else {
            if (getActivity() != null) {
                getActivity().runOnUiThread(
                    () -> Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show());
            }
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
    private RelativeLayout rlCommentView,rlSendComment;

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
            .setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.color.transparent));
        customShareDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
    }

    private void setupCustomShareDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        customShareView = layoutInflater.inflate(R.layout.bottom_sheet_custom_share, null, false);
        customShareDialog = new android.app.AlertDialog.Builder(mActivity).create();

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
            Intent intentEdit = new Intent(mActivity, PostActivity.class);
            intentEdit.putExtra("data", data);
            intentEdit.putExtra("call", "edit");
            intentEdit.putExtra(Constants.Post.TYPE,
                data.getMediaType1() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
            startActivity(intentEdit);
        });

        rlDelete.setOnClickListener(view -> {
            dismissCustomShareDialog();
            reportDialog.setTitle(mActivity.getString(R.string.delete));
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
                if (chat.hola.com.app.postshare.Utilities.copyMediaWithWatermark(mActivity,
                    postTobeShared.getImageUrl1())) {
                    Toast.makeText(mActivity, R.string.link_copied, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, R.string.link_copy_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                //Copy disabled toast
                Toast.makeText(mActivity, R.string.copy_disabled, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mActivity, R.string.download_disabled, Toast.LENGTH_SHORT).show();
            }
        });

        rlDownloadGif.setOnClickListener(view -> {

            if (postTobeShared.getMediaType1() == 1) {
                if (postTobeShared.isAllowDownload()) {
                    dismissCustomShareDialog();

                    downloadMedia(1, System.currentTimeMillis() + ".gif", null);
                } else {
                    //Download disabled toast
                    Toast.makeText(mActivity, R.string.download_disabled, Toast.LENGTH_SHORT).show();
                }
            } else {
                //gifs can only created for video posts toast
                Toast.makeText(mActivity, R.string.gif_not_applicable, Toast.LENGTH_SHORT).show();
            }
        });

        rlDuet.setOnClickListener(view -> {

            if (postTobeShared.getMediaType1() == 1) {

                if (postTobeShared.isAllowDuet()) {
                    dismissCustomShareDialog();

                    downloadMedia(3, System.currentTimeMillis() + ".mp4", null);
                } else {
                    Toast.makeText(mActivity, R.string.duet_disabled, Toast.LENGTH_SHORT).show();
                }
            } else {
                //duets only applicable for video posts
                Toast.makeText(mActivity, R.string.duet_not_applicable, Toast.LENGTH_SHORT).show();
            }
        });

        //rlForward.setOnClickListener(v -> {
        //    if (itemAdapter != null) itemAdapter.setPostItemClickPostion(position);
        //    if (friends != null && !friends.isEmpty()) {
        //        dismissCustomShareDialog();
        //        bottomSheet.setVisibility(View.VISIBLE);
        //        bottomSheet.post(() -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        //    } else {
        //        Toast.makeText(mActivity, getResources().getString(R.string.please_add_friends), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mActivity, R.string.record_not_applicable, Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(mActivity, DeeparDuetActivity.class);
                intent.putExtra("duetFilePath", filePath);
                intent.putExtra("remoteUrl", postTobeShared.getImageUrl1());
                startActivity(intent);
            } else {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {

                        if (OverlayConfig.OVERLAY_REQUIRED) {

                            if (postTobeShared.getMediaType1() == 1) {

                                if (filePath.endsWith(".gif")) {
                                    cancelDownloadDialog();
                                    Toast.makeText(mActivity,
                                        getString(R.string.media_downloaded, filePath),
                                        Toast.LENGTH_SHORT).show();
                                } else {
                                    //add overlay only to videoposts
                                    try {
                                        TextView tvDownloadMedia =
                                            alertDialogDownloadMedia.findViewById(R.id.tvDownload);
                                        tvDownloadMedia.setText(getString(R.string.processing));
                                    } catch (Exception ignore) {
                                    }
                                    OverlayUtils.prepareOverlay(mActivity, postTobeShared, filePath, this);
                                }
                            } else {
                                cancelDownloadDialog();
                                Toast.makeText(mActivity,
                                    getString(R.string.media_downloaded, filePath),
                                    Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            cancelDownloadDialog();
                            Toast.makeText(mActivity,
                                getString(R.string.media_downloaded, filePath), Toast.LENGTH_SHORT)
                                .show();
                        }
                    });
                } else {
                    cancelDownloadDialog();
                }
            }
        } else {
            cancelDownloadDialog();
            if (getActivity() != null) {
                getActivity().
                    runOnUiThread(
                        () -> Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show());
            }
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
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
            @SuppressLint("InflateParams")
            final View vDownloadFilters =
                (mActivity).getLayoutInflater().inflate(R.layout.dialog_download_media, null);

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
                        postTobeShared.getImageUrl1(), this, filename, mActivity,
                        postTobeShared.getMediaType1());
                } else if (type == 1) {
                    chat.hola.com.app.postshare.Utilities.downloadMediaAsGifWithWatermark(
                        vDownloadFilters.findViewById(R.id.pbDownload),
                        postTobeShared.getImageUrl1(), this, filename, mActivity);
                } else {
                    //Duet
                    chat.hola.com.app.postshare.Utilities.downloadMediaForDuet(
                        vDownloadFilters.findViewById(R.id.pbDownload),
                        postTobeShared.getImageUrl1(), this, filename, mActivity);
                }
            });

            alertDialog.setView(vDownloadFilters);
            alertDialog.setCancelable(false);
            alertDialogDownloadMedia = alertDialog.create();
            if (mActivity != null && !mActivity.isFinishing()) alertDialogDownloadMedia.show();
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

            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                alertDialog.setTitle(getString(R.string.alert));
                alertDialog.setMessage(requestMessage);
                alertDialog.setCancelable(true);
                int finalRequestCode = requestCode;
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                    (dialog, which) -> {
                        rvList.suppressLayout(true);
                        requestPermissions(
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            finalRequestCode);
                    });

                alertDialog.show();
            } else {
                rvList.suppressLayout(true);
                requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    requestCode);
            }
        }
    }

    public void postUpdated(String postId, boolean allowComment, boolean allowDownload,
        boolean allowDuet) {

        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getPostId().equals(postId)) {
                Data data = dataList.get(i);
                data.setAllowDownload(allowDownload);
                data.setAllowComments(allowComment);
                data.setAllowDuet(allowDuet);

                dataList.set(i, data);
                this.data = data;
                socialDetailAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * For overlay profile info in downloaded media
     */
    @Override
    public void overlayResult(String result) {
        cancelDownloadDialog();

        if (getActivity() != null) {

            getActivity().runOnUiThread(() -> {

                if (result == null) {
                    Toast.makeText(mActivity, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, getString(R.string.media_downloaded, result),
                        Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        customLikeCommentDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.color.transparent));
        customLikeCommentDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
    }

    private void switchLikeComment(boolean isFromLike, String postId) {
        if (isFromLike) {
            mPresenter.likers(postId, 0, pageSize);
        } else {
            mPresenter.getComments(postId, 0, pageSize);
        }

        tvLikes.setTextColor(getResources().getColor(isFromLike ? R.color.color_black : R.color.disabled));
        tvComments.setTextColor(getResources().getColor(!isFromLike ? R.color.color_black : R.color.disabled));
        llLikeView.setVisibility(isFromLike ? View.VISIBLE : View.GONE);
        rlCommentView.setVisibility(!isFromLike ? View.VISIBLE : View.GONE);
    }

    private void setupCustomLikeCommentDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        customLikeCommentView = layoutInflater.inflate(R.layout.bottom_sheet_custom_like_comment, null, false);
        customLikeCommentDialog = new android.app.AlertDialog.Builder(mActivity).create();

        tvLikeDialogViews = customLikeCommentView.findViewById(R.id.tvViews);
        tvLikeDialogViews.setTypeface(typefaceManager.getSemiboldFont());
        tvLikeDialogUserName = customLikeCommentView.findViewById(R.id.tvUserName);
        tvLikeDialogUserName.setTypeface(typefaceManager.getBoldFont());

        tvLikes = customLikeCommentView.findViewById(R.id.tvLikes);
        tvLikes.setTypeface(typefaceManager.getSemiboldFont());
        tvComments = customLikeCommentView.findViewById(R.id.tvComments);
        tvComments.setTypeface(typefaceManager.getSemiboldFont());
        llLikeView = customLikeCommentView.findViewById(R.id.llLikeView);
        rlCommentView = customLikeCommentView.findViewById(R.id.rlCommentView);
        rlSendComment = customLikeCommentView.findViewById(R.id.rlSendComment);
        etLikeSearch = customLikeCommentView.findViewById(R.id.etLikeSearch);
        etLikeSearch.setTypeface(typefaceManager.getSemiboldFont());
        rvLikes = customLikeCommentView.findViewById(R.id.rvLikes);
        rvComments = customLikeCommentView.findViewById(R.id.rvComments);
        etComment = customLikeCommentView.findViewById(R.id.etComment);
        etComment.setTypeface(typefaceManager.getSemiboldFont());
        ivSendComment = customLikeCommentView.findViewById(R.id.ivSendComment);
        tvNoLike = customLikeCommentView.findViewById(R.id.tvNoLikes);
        tvNoComment = customLikeCommentView.findViewById(R.id.tvNoComments);
        tvNoLike.setTypeface(typefaceManager.getSemiboldFont());
        tvNoComment.setTypeface(typefaceManager.getSemiboldFont());

        layoutManagerLikes = new LinearLayoutManager(getContext());
        likeAdapter = new FollowerAdapter(getContext(), typefaceManager);
        likeAdapter.setOnFollowUnfollowClickCallback(this);
        rvLikes.setLayoutManager(layoutManagerLikes);
        rvLikes.setAdapter(likeAdapter);

        layoutManagerComments = new LinearLayoutManager(getContext());
        commentAdapter = new CommentAdapter(comments, getContext(), typefaceManager);
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
                if (query.length() > 0) {
                    mPresenter.searchLikers(postIdForLikeComment, 0, PAGE_SIZE, query.toString());
                } else {
                    mPresenter.likers(postIdForLikeComment, 0, PAGE_SIZE);
                }
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
    public void addToList(Comment comment,String postId) {
        comment.setProfilePic(sessionManager.getUserProfilePic());
        comments.add(0,comment);
        commentAdapter.setData(comments);
        try {
            socialDetailAdapter.updateCommentCount(String.valueOf(comments.size()),
                postId, currentPosition, rvList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvComments.setText(comments.size()+" "+getString(R.string.comments));
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
        startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("userId", userId));
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

    @Override
    public void setSuggestedUsers(ArrayList<User> users) {
        suggestUserAdapter.setData(users);
    }

    @Override
    public void onFollow(String userId, boolean follow, int position) {
        if (follow) {
            mPresenter.follow(userId);
        } else {
            mPresenter.unfollow(userId);
        }
    }

    @Override
    public void onProfileClick(String userId) {
        onUserClick(0, userId);
    }

    public void getGuestDataFirstTime() {
        try {
            mVideoPlayerManager.stopAnyPlayback(true);
        } catch (Exception ignore) {
        }
        mPresenter.fetchXclusivePosts(page, pageSize, true, false);
    }

    //    public void showBalance(WalletResponse.Data.Wallet coinWallet) {
    //        if (coinWallet != null) {
    //            sessionManager.setCoinWalletId(coinWallet.getWalletid());
    //            sessionManager.setCoinBalance(coinWallet.getBalance());
    //            String coin = Utilities.formatMoney(Double.valueOf(sessionManager.getCoinBalance()));
    //            tvCoinBalance.setText(coin);
    //        }
    //    }
    @Override
    public void sendTipSuccess(Data data, String coin, int position) {
        mediaPlayer.start();
        Toast.makeText(getActivity(), "Tip Sent to " + data.getUsername(), Toast.LENGTH_SHORT)
            .show();
        ((LandingActivity)mActivity).getWalletBalance();
        dataList.get(this.position).setTipsAmount(data.getTipsAmount()+Double.valueOf(coin));
        socialDetailAdapter.notifyItemChanged(this.position);
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

        View view = LayoutInflater.from(mActivity)
            .inflate(R.layout.paid_post_confirmation_dialog, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
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
        Utilities.openInsufficientBalanceDialog(mActivity);
    }

    public void openSubscriptionDialog(Data data, int position) {
        View view = LayoutInflater.from(mActivity)
            .inflate(R.layout.subscription_confirmation_dialog, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.setContentView(view);

        TextView tVUserName = dialog.findViewById(R.id.tVUserName);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        ImageView profilePic = dialog.findViewById(R.id.profilePic);
        AppCompatImageView ivStarBadge = dialog.findViewById(R.id.ivStarBadge);
        try {
            Glide.with(mActivity)
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
        mediaPlayer.start();
        dataList.set(position, data);
        socialDetailAdapter.setDataList(mList, dataList);
        ((LandingActivity)mActivity).getWalletBalance();
    }

    @Override
    public void onUserSubscribed(Data data, int position) {
        mediaPlayer.start();
        dataList.set(position, data);
        socialDetailAdapter.setDataList(mList, dataList);
        ((LandingActivity)mActivity).getWalletBalance();
    }
    private void setupShareBottomSheetDialog() {

        LayoutInflater layoutInflater =
            (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        shareView = layoutInflater.inflate(R.layout.bottomsheet_share, null, false);
        shareDialog = new android.app.AlertDialog.Builder(mActivity).create();

        etMessage = shareView.findViewById(R.id.etMessage);
        shareList = shareView.findViewById(R.id.shareList);
        tvNoDataFound = shareView.findViewById(R.id.tvNoDataFound);
        ivProfilePic = shareView.findViewById(R.id.ivProfilePic);
        shareList.setHasFixedSize(true);
        shareList.setLayoutManager(new LinearLayoutManager(mActivity));
        //
        androidx.appcompat.widget.SearchView searchView = shareView.findViewById(R.id.searchView);

        // listening to search query text change
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //itemAdapter.getFilter().filter(query);
                hideKeyboard(mActivity);
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
        if(dataList.get(position).getPostId().equals(postId)){
            Data data=dataList.get(position);
            data.setAlreadyReported(true);
            dataList.set(position,data);
        }else{

            for(int i=0;i<dataList.size();i++){
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
            (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        shareOnSocialMediaView =
            layoutInflater.inflate(R.layout.bottomsheet_share_to_social_media, null, false);
        shareOnSocialMediaDialog = new android.app.AlertDialog.Builder(mActivity).create();

        rlFacebook = shareOnSocialMediaView.findViewById(R.id.rlFacebook);
        RelativeLayout rlDynamicLink = shareOnSocialMediaView.findViewById(R.id.rlDynamicLink);
        RelativeLayout rlOthers = shareOnSocialMediaView.findViewById(R.id.rlOthers);

        rlFacebook.setOnClickListener(v -> {
            dismissShareToSocialMediaDialog();
            if (getActivity() != null) {
                ((LandingActivity) getActivity()).downloadMediaToShare(
                    postTobeShared.getImageUrl1(), postTobeShared.getMediaType1() == 0,
                    postTobeShared.getPostId(), true);
            }
        });

        rlDynamicLink.setOnClickListener(v -> {
            dismissShareToSocialMediaDialog();
            if (getActivity() != null) {
                ((LandingActivity) getActivity()).prepareDynamicLink(postTobeShared.getPostId());
            }
        });

        rlOthers.setOnClickListener(v -> {
            dismissShareToSocialMediaDialog();
            if (getActivity() != null) {
                ((LandingActivity) getActivity()).downloadMediaToShare(
                    postTobeShared.getImageUrl1(), postTobeShared.getMediaType1() == 0,
                    postTobeShared.getPostId(), false);
            }
        });
    }

    private void showShareToSocialMediaDialog() {

        if (getActivity() != null) {
            rlFacebook.setVisibility(
                ((LandingActivity) getActivity()).isFacebookInstalled() ? View.VISIBLE : View.GONE);

            shareOnSocialMediaDialog.show();
            shareOnSocialMediaDialog.setContentView(shareOnSocialMediaView);
            shareOnSocialMediaDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params =
                shareOnSocialMediaDialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            shareOnSocialMediaDialog.getWindow().setGravity(Gravity.BOTTOM);
            params.dimAmount = 0.0f;
            shareOnSocialMediaDialog.getWindow().setAttributes(params);
            shareOnSocialMediaDialog.getWindow()
                .setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.color.transparent));
            shareOnSocialMediaDialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
        }
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

    @Override
    public void onPause() {
        super.onPause();
        updateViewCountoServer();
    }

    public void updateViewCountoServer() {
        mPresenter.updatePostViewedToServer(mPresenter.getViewPost());
    }
}
