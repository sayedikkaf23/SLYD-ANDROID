package chat.hola.com.app.home.social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.collections.saved.SavedActivity;
import chat.hola.com.app.comment.CommentActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.LandingPresenter;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.social.model.SocialAdapter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Business;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.followers.FollowersActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import chat.hola.com.app.webScreen.WebActivity;
import com.bumptech.glide.Glide;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import dagger.android.support.DaggerFragment;
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class SocialFragment extends DaggerFragment
    implements SocialContract.View, CollectionItemAdapter.ClickListener,
    FriendItemAdapter.ItemListener, DialogInterface.OnClickListener {
  private Unbinder unbinder;
  private LinearLayoutManager layoutManager;
  private List<Data> dataList;
  private boolean isCalled = false;
  private Bus bus = AppController.getBus();

  @Inject
  SocialAdapter mAdapter;
  @Inject
  SessionManager sessionManager;
  @Inject
  LandingPresenter landingPresenter;
  @Inject
  TypefaceManager typefaceManager;
  @Inject
  SocialPresenter mPresenter;
  @Inject
  BlockDialog dialog;
  @Inject
  AlertDialog.Builder reportDialog;
  @Inject
  ArrayAdapter<String> arrayAdapter;

  @BindView(R.id.socialRv)
  RecyclerView socialRv;
  @BindView(R.id.swiperefresh)
  SwipeRefreshLayout swiperefresh;
  @BindView(R.id.llEmpty)
  LinearLayout llEmpty;
  @BindView(R.id.tvEmpty)
  TextView tvEmpty;
  @BindView(R.id.tvMsg)
  TextView tvMsg;
  @BindView(R.id.btnFindPeople)
  Button btnFindPeople;
  @BindView(R.id.llNetworkError)
  InternetErrorView llNetworkError;
  @BindView(R.id.btnDone)
  Button btnDone;
  @BindView(R.id.bottom_sheet)
  View bottomSheet;
  @BindView(R.id.shareList)
  RecyclerView shareList;

  private FriendItemAdapter itemAdapter;
  private List<Friend> friends = new ArrayList<>();
  private List<Friend> tempFriends = new ArrayList<>();
  private BottomSheetBehavior behavior;

  private ProgressDialog pDialog;
  private int position;
  private String postId;

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
  CollectionItemAdapter collectionItemAdapter;
  private List<CollectionData> collectionList = new ArrayList<>();
  private BottomSheetBehavior collectionBehavior;
  // used while new collection creation.
  private String collectionImage = "", addToCollectionPostId = "";
  ///////////////////////////////

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @Inject
  public SocialFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      bus.register(this);
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_social, container, false);
    unbinder = ButterKnife.bind(this, view);
    mPresenter.attachView(this);
    dataList = new ArrayList<>();
    //        mAdapter = new SocialAdapter(getContext(), dataList, typefaceManager);
    changeVisibilityOfViews();
    init();
    applyFont();
    isCalled = true;

    mPresenter.getFriends();
    mPresenter.getCollections();

    behavior = BottomSheetBehavior.from(bottomSheet);
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {

        switch (newState) {
          case BottomSheetBehavior.STATE_HIDDEN:
            itemAdapter.notifyDataSetChanged();
            break;
          case BottomSheetBehavior.STATE_EXPANDED:
            break;
          case BottomSheetBehavior.STATE_COLLAPSED:
            break;
          case BottomSheetBehavior.STATE_DRAGGING:
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

    collectionItemAdapter = new CollectionItemAdapter(getActivity(), collectionList);
    collectionItemAdapter.setListener(this);
    rV_collections.setAdapter(collectionItemAdapter);
    rV_collections.setLayoutManager(
        new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
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

    shareList.setHasFixedSize(true);
    shareList.setLayoutManager(new LinearLayoutManager(getContext()));

    pDialog = new ProgressDialog(getContext(), 0);
    pDialog.setCancelable(false);

    mPresenter.postObserver();
    mPresenter.callSocialApi(SocialPresenter.PAGE_SIZE * SocialPresenter.page,
        SocialPresenter.PAGE_SIZE, false);

    ((LandingActivity) getActivity()).setTitle(getActivity().getString(R.string.starchat),
        new TypefaceManager(getActivity()).getDublyLogo());

    mPresenter.getReportReasons();
    reportDialog.setAdapter(arrayAdapter, this);
    return view;
  }

  @OnClick(R.id.blurView)
  public void blurViewClick() {
    collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    sheetCollection.setVisibility(View.GONE);
  }

  @OnClick({ R.id.iV_newCollection })
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
        openKeyboard(Objects.requireNonNull(getActivity()));
      }
    }, 500);
  }

  @OnClick({ R.id.cBack })
  public void backFromCreateCollection() {

    hideKeyboard(Objects.requireNonNull(getActivity()));

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

  @OnClick(R.id.root)
  public void mainViewClick() {
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    sheetCollection.setVisibility(View.GONE);
  }

  /*Here we change the common views visibility on selection of fragment*/
  public void changeVisibilityOfViews() {
    landingPresenter.getActivity().visibleActionBar();
    landingPresenter.getActivity().linearPostTabs.setVisibility(View.GONE);
    landingPresenter.getActivity().tvSearch.setVisibility(View.GONE);
  }

  @Subscribe
  public void getMessage(JSONObject object) {
    try {
      if (object.getString("eventName").equals("postCompleted")) {
        //
        //Handler handler = new Handler();
        //handler.postDelayed(() -> {
        //  try {
        //    shareToSocial(object.getString("type"), object.getString("mediaPath"),
        //        object.getString("url"), object.getBoolean("facebook"),
        //        object.getBoolean("twitter"), object.getBoolean("instagram"));
        //  } catch (JSONException e) {
        //    e.printStackTrace();
        //  }
        //  mPresenter.callSocialApi(SocialPresenter.PAGE_SIZE * SocialPresenter.page,
        //      SocialPresenter.PAGE_SIZE, false);
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
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",
            media);
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

  private void applyFont() {
    tvEmpty.setTypeface(typefaceManager.getMediumFont());
    tvMsg.setTypeface(typefaceManager.getRegularFont());
    btnFindPeople.setTypeface(typefaceManager.getSemiboldFont());
    btnDone.setTypeface(typefaceManager.getSemiboldFont());
  }

  @OnClick(R.id.btnDone)
  public void done() {
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  @OnClick(R.id.btnFindPeople)
  public void findPeopleToFollow() {
    Intent intent = new Intent(getContext(), DiscoverActivity.class);
    intent.putExtra("caller", "SettingsActivity");
    intent.putExtra("is_contact", true);
    startActivity(intent);
  }

  private void init() {
    layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setItemPrefetchEnabled(true);

    swiperefresh.setOnRefreshListener(mPresenter.getPresenter());
    socialRv.setLayoutManager(layoutManager);
    mAdapter.setListener(mPresenter.getPresenter());
    socialRv.addOnScrollListener(recyclerViewOnScrollListener);
    socialRv.setAdapter(mAdapter);
    llNetworkError.setErrorListner(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.getCollections();
  }

  @Override
  public void onPause() {
    super.onPause();
    isCalled = false;
  }

  @Override
  public void onItemClick(int position) {
    try {
      Intent intent = new Intent(getContext(), SocialDetailActivity.class);
      intent.putExtra(Constants.SocialFragment.DATA, dataList.get(position));
      intent.putExtra("dataList", (Serializable) dataList);
      intent.putExtra("position", position);
      startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUserClick(int position) {
    Business business = dataList.get(position).getBusiness();
    boolean isBusiness =
        business != null && !business.getBusinessPostType().equalsIgnoreCase("regular");
    boolean isChannel = dataList.get(position).getChannelId() != null && !dataList.get(position)
        .getChannelId()
        .isEmpty();

    if (isBusiness) {
      Intent intent = new Intent(getContext(), ProfileActivity.class);
      intent.putExtra("isBusiness", true);
      intent.putExtra(Constants.SocialFragment.USERID, dataList.get(position).getUserId());
      startActivity(intent);
    } else if (isChannel) {
      Intent intent = new Intent(getContext(), TrendingDetail.class);
      intent.putExtra("channelId", dataList.get(position).getChannelId());
      intent.putExtra("call", "channel");
      startActivity(intent);
    } else {
      Intent intent = new Intent(getContext(), ProfileActivity.class);
      intent.putExtra(Constants.SocialFragment.USERID, dataList.get(position).getUserId());
      startActivity(intent);
    }
  }

  @Override
  public void showEmptyUi(boolean show) {
    llEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
    socialRv.setVisibility(show ? View.GONE : View.VISIBLE);
  }

  @Override
  public void onChannelClick(String channelId) {
    Intent intent = new Intent(getContext(), TrendingDetail.class);
    intent.putExtra("call", "channel");
    intent.putExtra("channelId", channelId);
    startActivity(intent);
  }

  @Override
  public void setData(List<Data> data, boolean clear) {
    if (clear) dataList.clear();
    dataList.addAll(data);
    mAdapter.setData(dataList);
    swiperefresh.setRefreshing(false);
  }

  @Override
  public void viewAllComments(String postId) {
    sessionManager.setHomeTab(1);
    startActivity(new Intent(getContext(), CommentActivity.class).putExtra("postId", postId));
  }

  @Override
  public void send(int position) {
    this.position = position;
    tempFriends = friends;
    if (tempFriends != null && !tempFriends.isEmpty()) {
      itemAdapter.setData(tempFriends);
      bottomSheet.setVisibility(View.VISIBLE);
      bottomSheet.post(() -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
    } else {
      Toast.makeText(getContext(),
          getContext().getResources().getString(R.string.please_add_friends), Toast.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  public void share(int position) {
    showProgressDialog(getResources().getString(R.string.please_wait));
    Data data = dataList.get(position);
    new Thread(() -> {
      String url = "https://ezcallapp.page.link/post/" + data.getPostId();
       FirebaseDynamicLinks.getInstance()
          .createDynamicLink()
          .setLongLink(Uri.parse("https://ezcallapp.page.link?link=" + url + "&apn=com.ezcall.android"))
          .buildShortDynamicLink()
          .addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
              Uri shortLink = task.getResult().getShortLink();
              Intent intent = new Intent(Intent.ACTION_SEND);
              intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
              intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
              intent.setType("text/plain");
              Intent chooser = Intent.createChooser(intent, getString(R.string.selectApp));
              startActivity(chooser);
            }
            hideProgressDialog();
          });
    }).start();
  }

  /**
   * To show progress dialog
   */
  private void showProgressDialog(String message) {
    pDialog.setMessage(message);
    if (pDialog != null && !pDialog.isShowing()) {
      pDialog.show();
      ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

      bar.getIndeterminateDrawable()
          .setColorFilter(ContextCompat.getColor(getContext(), R.color.color_black),
              android.graphics.PorterDuff.Mode.SRC_IN);
    }
  }

  /**
   * To hide progress dialog
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void hideProgressDialog() {
    if (pDialog.isShowing()) {
      Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();

      if (context instanceof Activity) {

        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
          pDialog.dismiss();
        }
      } else {

        try {
          pDialog.dismiss();
        } catch (final IllegalArgumentException e) {
          e.printStackTrace();
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void like(int position, boolean liked) {
    Data data = dataList.get(position);
    if (liked) {
      mPresenter.like(data.getPostId(), sessionManager);
      dataList.get(position).setLiked(true);
      dataList.get(position)
          .setLikesCount(String.valueOf(Integer.parseInt(data.getLikesCount()) + 1));
    } else {
      if (Integer.parseInt(data.getLikesCount()) > 0) {
        mPresenter.dislike(data.getPostId());
        dataList.get(position).setLiked(false);
        dataList.get(position)
            .setLikesCount(String.valueOf(Integer.parseInt(data.getLikesCount()) - 1));
      }
    }
    mAdapter.setData(dataList);
  }

  @Override
  public void friends(List<Friend> friends) {
    if (friends != null && !friends.isEmpty()) {

      if (this.friends == null) this.friends = new ArrayList<>();
      this.friends.clear();
      this.friends.addAll(friends);
      itemAdapter = new FriendItemAdapter(getContext(), this.friends, this, typefaceManager);
      shareList.setAdapter(itemAdapter);
    }
  }

  @Override
  public void openProfile(String userId) {
    Intent intent = new Intent(getContext(), ProfileActivity.class);
    intent.putExtra(Constants.SocialFragment.USERID, userId);
    startActivity(intent);
  }

  @Override
  public void onViewClick(Data data) {
    Intent intent = new Intent(getContext(), FollowersActivity.class);
    intent.putExtra("title", "Viewers");
    intent.putExtra("userId", data.getPostId());
    startActivity(intent);
  }

  @Override
  public void onLikeClick(Data data) {
    Intent intent = new Intent(getContext(), FollowersActivity.class);
    intent.putExtra("title", getContext().getResources().getString(R.string.likers));
    intent.putExtra("userId", data.getPostId());
    startActivity(intent);
  }

  @Override
  public void edit(Data data) {
    Intent intentEdit = new Intent(getContext(), PostActivity.class);
    intentEdit.putExtra("data", data);
    intentEdit.putExtra("call", "edit");
    intentEdit.putExtra(Constants.Post.TYPE,
        data.getMediaType1() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
    startActivity(intentEdit);
  }

  @Override
  public void delete(Data data) {
    reportDialog.setTitle(R.string.Delete);
    reportDialog.setMessage(R.string.postDeleteMsg);
    reportDialog.setPositiveButton(R.string.yes,
        (dialog, which) -> mPresenter.deletePost(data.getPostId()));
    reportDialog.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
    reportDialog.create().show();
  }

  @Override
  public void report(Data data) {
    postId = data.getId();
    reportDialog.setTitle(R.string.Report);
    reportDialog.show();
  }

  @Override
  public void addToReportList(ArrayList<String> data) {
    arrayAdapter.clear();
    arrayAdapter.addAll(data);
  }

  @Override
  public void onClick(DialogInterface dialogInterface, int which) {
    AlertDialog.Builder confirm = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
    confirm.setMessage(R.string.report_message);
    confirm.setPositiveButton(R.string.confirm,
        (dialog, w) -> mPresenter.reportPost(postId, arrayAdapter.getItem(which),
            arrayAdapter.getItem(which)));
    confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
    confirm.create().show();
    //        mPresenter.reportPost(postId, arrayAdapter.getItem(which), arrayAdapter.getItem(which));
  }

  @Override
  public void showMessage(String msg, int msgId) {
    Toast.makeText(getContext(),
        msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId), Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onActionButtonClick(String title, String url) {
    if (!url.contains("http")) url = "http://" + url;
    //        Uri uri = Uri.parse(url);
    //        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    //        startActivity(intent);
    Intent intent = new Intent(getContext(), WebActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("url", url);
    bundle.putString("title", title);
    intent.putExtra("url_data", bundle);
    startActivity(intent);
  }

  @Override
  public void isLoading(boolean flag) {
    assert swiperefresh != null;
    swiperefresh.setRefreshing(flag && !swiperefresh.isRefreshing());
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
  public void onDestroy() {
    mPresenter.detachView();
    if (unbinder != null) unbinder.unbind();
    super.onDestroy();
  }

  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          int visibleItemCount = layoutManager.getChildCount();
          int totalItemCount = layoutManager.getItemCount();
          int firstVisibleItemPosition =
              ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
          mPresenter.prefetchImage(firstVisibleItemPosition);
          mPresenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
          behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
      };

  @Override
  public void reload() {
    llNetworkError.setVisibility(View.GONE);
    mPresenter.onRefresh();
  }

  @Override
  public void onItemClick(Friend friend, int position) {
    Data data = dataList.get(this.position);
    JSONObject obj;
    obj = new JSONObject();

    /*
     * Post
     */
    try {
      //            btnDone.setVisibility(View.VISIBLE);
      String documentId =
          AppController.findDocumentIdOfReceiver(friend.getId(), Utilities.tsInGmt(),
              friend.getFirstName(), friend.getProfilePic(), "", false, friend.getUserName(), "",
              false, friend.isStar());

      String payload = Utilities.getModifiedImageLink(
          data.getImageUrl1());//data.getMediaType1() == 0 ? Utilities.getModifiedImageLink(data.getImageUrl1()) : data.getImageUrl1().replace("mp4", "jpg");
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
      obj.put("to", friend.getId());
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
      mapTemp.put("to", friend.getId());
      mapTemp.put("toDocId", documentId);
      mapTemp.put("id", tsForServerEpoch);
      mapTemp.put("timestamp", tsForServerEpoch);

      String type = Integer.toString(13);
      mapTemp.put("type", type);
      mapTemp.put("postId", data.getPostId());
      mapTemp.put("postTitle", data.getTitle());
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
          .publishChatMessage(MqttEvents.Message.value + "/" + friend.getId(), obj, 1, false, map1);
      friends.get(position).setSent(false);
      //            done.setVisibility(isPostSentToAnyOne() ? View.VISIBLE : View.GONE);
      //            Toast.makeText(getContext(), "Post sent", Toast.LENGTH_SHORT).show();
    } catch (JSONException | UnsupportedEncodingException e) {
      e.printStackTrace();
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
  public void bookMarkPostResponse(int pos, boolean isSaved) {
    dataList.get(pos).setBookMarked(isSaved);
    if(!isSaved)
      Toast.makeText(getActivity(),getString(R.string.post_removed_bookmark),Toast.LENGTH_LONG).show();
    //    mAdapter.notifyItemChanged(pos);
  }

  @Override
  public void savedViewClick(int position, Data data) {
    startActivity(new Intent(getActivity(), SavedActivity.class));
  }

  @Override
  public void saveToCollectionClick(int position, Data data) {

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

  @Override
  public void showProgress(boolean b) {
    if (b) {
      showProgressDialog(getString(R.string.please_wait));
    } else {
      hideProgressDialog();
    }
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
  public void collectionCreated() {
    hideKeyboard(Objects.requireNonNull(getActivity()));
    sheetCollection.setVisibility(View.GONE);
    collectionBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    backFromCreateCollection();
    et_cName.setText(getString(R.string.double_inverted_comma));
    mPresenter.getCollections();

    Toast.makeText(getContext(), R.string.collection_created, Toast.LENGTH_SHORT).show();
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
}