package chat.hola.com.app.home.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AddContact.AddContactActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.home.LandingPresenter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.search.SearchActivity;
import chat.hola.com.app.search_user.SearchUserActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>ContactFragment</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class ContactFragment extends DaggerFragment
    implements ContactContract.View, SwipeRefreshLayout.OnRefreshListener {
  private Unbinder unbinder;

  @Inject
  SessionManager sessionManager;
  @Inject
  LandingPresenter landingPresenter;
  @Inject
  TypefaceManager typefaceManager;
  @Inject
  ContactPresenter mPresenter;
  @Inject
  ContactAdapter contactAdapter;
  @Inject
  FriendRequestAdapter friendRequestAdapter;
  @Inject
  BlockDialog dialog;

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
  @BindView(R.id.htab_appbar)
  AppBarLayout appbar;
  @BindView(R.id.htab_collapse_toolbar)
  CollapsingToolbarLayout collapsing_toolbar;

  private boolean isStar = false;
  private Bus bus = AppController.getBus();

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @Inject
  public ContactFragment() {
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
    View view = inflater.inflate(R.layout.fragment_contact_fiend, container, false);
    unbinder = ButterKnife.bind(this, view);
    mPresenter.attachView(this);
    changeVisibilityOfViews();
    try {
        if (getArguments() != null) {
            isStar = getArguments().getBoolean("isStar");
        } else {
            isStar = false;
        }
    } catch (Exception ignored) {
      isStar = false;
    }

    setStar(isStar);

    tvName.setText(sessionManager.getFirstName() + getString(R.string.space) + sessionManager.getLastName());
    tvUserName.setText(
        getResources().getString(R.string.starchat_id) +
                getString(R.string.semicolon)+getString(R.string.space)
                + sessionManager.getUserName());

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

    rvNewFriends.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    rvNewFriends.setHasFixedSize(true);
    friendRequestAdapter.setClickListner(mPresenter);
    rvNewFriends.setAdapter(friendRequestAdapter);

    DividerItemDecoration itemDecorator =
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider));
    rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));
    rvFriends.setHasFixedSize(true);
    rvFriends.addItemDecoration(itemDecorator);
    contactAdapter.setClickListner(mPresenter);
    rvFriends.setAdapter(contactAdapter);

    refresh.setOnRefreshListener(this);
    etSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //ContactFragment.this.startActivity(new Intent(ContactFragment.this.getContext(), SearchUserActivity.class).putExtra("isStar", isStar).putExtra("call", "friend"));
        Intent intent = new Intent(ContactFragment.this.getContext(), SearchActivity.class);
        ContactFragment.this.startActivity(intent);
      }
    });
    mPresenter.observe();

    tvNewFriendCount.setVisibility(ContactPresenter.count > 0 ? View.VISIBLE : View.GONE);
    tvNewFriendCount.setText(String.valueOf(ContactPresenter.count));

    AppController.getInstance().subscribeToTopic(AppController.getInstance().getUserId(), 1);
    return view;
  }

  /*Here we change the common views visibility on selection of fragment*/
  public void changeVisibilityOfViews() {
    landingPresenter.getActivity().hideActionBar();
    landingPresenter.getActivity().removeFullScreenFrame();
    landingPresenter.getActivity().linearPostTabs.setVisibility(View.GONE);
    landingPresenter.getActivity().tvSearch.setVisibility(View.GONE);
  }

  @OnClick(R.id.rlProfile)
  public void profile() {
    startActivity(new Intent(getContext(), ProfileActivity.class));
  }

  public void setStar(boolean isStar) {
    this.isStar = isStar;
    ibBack.setVisibility(isStar ? View.VISIBLE : View.GONE);
    ibAddFriend.setVisibility(isStar ? View.GONE : View.VISIBLE);
    llFriend.setVisibility(isStar ? View.GONE : View.VISIBLE);
    llStar.setVisibility(!isStar ? View.GONE : View.VISIBLE);
    contactAdapter.setStar(isStar);
    try {
      tvTitle.setText(getResources().getString(isStar ? R.string.star_page : R.string.contacs));
    } catch (Exception e) {
      e.printStackTrace();
    }
    reload();
  }

  @OnClick(R.id.llStarPage)
  public void myStars() {
    startActivity(new Intent(getContext(), SearchUserActivity.class).putExtra("isStar", true)
        .putExtra("category", 3));
  }

  @OnClick(R.id.ibAddFriend)
  public void addFriend() {
    startActivity(new Intent(getContext(), AddContactActivity.class));
  }

  @OnClick(R.id.ibBack)
  public void back() {
    getActivity().onBackPressed();
  }

  @OnClick(R.id.rlTop10Stars)
  public void topStars() {
    startActivity(new Intent(getContext(), SearchUserActivity.class).putExtra("isStar", true)
        .putExtra("category", 0));
  }

  @OnClick(R.id.llTop50Actress)
  public void topActress() {
    startActivity(new Intent(getContext(), SearchUserActivity.class).putExtra("isStar", true)
        .putExtra("category", 2));
  }

  @OnClick(R.id.llTop50Actors)
  public void topActors() {
    startActivity(new Intent(getContext(), SearchUserActivity.class).putExtra("isStar", true)
        .putExtra("category", 1));
  }

  @OnClick(R.id.llStarPage)
  public void starpage() {
    //        ((LandingActivity) getActivity()).openStar();
  }

  @OnClick({ R.id.rlNoNewFriends, R.id.tvNewFriendsTitle })
  public void newFriends() {
    //startActivity(new Intent(getActivity(), FriendsActivity.class));
    startActivity(new Intent(getActivity(), AddContactActivity.class));
  }

  @Subscribe
  public void getMessage(JSONObject object) {
    try {

      if (object.has("eventName")) {

        if (object.getString("eventName").equals("FetchFriends")) {

          reload();
        }
      } else {
        switch (object.getString("type")) {
          case "remove Friend":
            reload();
            break;
          case "new Friend":
            mPresenter.newFriends();
            reload();
            break;
        }
      }
    } catch (JSONException ignored) {
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    try {
      if (ivProfilePic != null && getContext() != null) {
        Glide.with(getContext()).load(sessionManager.getUserProfilePic()).asBitmap().centerCrop()
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //    .skipMemoryCache(true)
            .into(new BitmapImageViewTarget(ivProfilePic) {
              @Override
              protected void setResource(Bitmap resource) {
                if (getContext() != null) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  ivProfilePic.setImageDrawable(circularBitmapDrawable);
                }
              }
            });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void showMessage(String msg, int msgId) {

  }

  @Override
  public void sessionExpired() {
    sessionManager.sessionExpired(getContext());
  }

  @Override
  public void isInternetAvailable(boolean flag) {

  }

  @Override
  public void onDestroy() {
    mPresenter.detachView();
    if (unbinder != null) unbinder.unbind();
    bus.unregister(this);
    super.onDestroy();
  }

  @Override
  public void reload() {
    rvFriends.setVisibility(View.GONE);
    if (isStar) {
      mPresenter.stars();
    } else {
      mPresenter.newFriends();
      mPresenter.friends();
    }
  }

  @Override
  public void openChatForItem(Friend friend, int position) {
    if (isStar) {
      Intent intent = new Intent(getActivity(), ProfileActivity.class);
      intent.putExtra("userId", friend.getId());
      startActivity(intent);
    } else {
      Intent intent = new Intent(getActivity(), ChatMessageScreen.class);
      intent.putExtra("receiverUid", friend.getId());
      intent.putExtra("isStar", friend.isStar());
      intent.putExtra("receiverName",
          CommonClass.createFullName(friend.getFirstName(), friend.getLastName()));

      String docId = AppController.getInstance().findDocumentIdOfReceiver(friend.getId(), "");

      if (docId.isEmpty()) {
        docId = AppController.findDocumentIdOfReceiver(friend.getId(), Utilities.tsInGmt(),
            CommonClass.createFullName(friend.getFirstName(), friend.getLastName()),
            friend.getProfilePic(), "", false, friend.getUserName(), "", false, friend.isStar());
      }

      intent.putExtra("documentId", docId);
      intent.putExtra("receiverIdentifier", friend.getUserName());
      intent.putExtra("receiverImage", friend.getProfilePic());
      intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));
      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

      startActivity(intent);
    }
  }

  @Override
  public void noContent() {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
    if (appbar.getLayoutParams() != null) {
      CoordinatorLayout.LayoutParams layoutParams =
          (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
      AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
      appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
        @Override
        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
          return false;
        }
      });
      layoutParams.setBehavior(appBarLayoutBehaviour);
    }
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
    if (isStar) {
      Intent intent = new Intent(getActivity(), ProfileActivity.class);
      intent.putExtra("userId", data.getId());
      startActivity(intent);
    }
    //        else {
    //            Intent intent = new Intent(getActivity(), OpponentProfile.class);
    //            intent.putExtra("userId", data.getId());
    //            intent.putExtra("userName", data.getUserName());
    //            intent.putExtra("firstName", data.getFirstName());
    //            intent.putExtra("lastName", data.getLastName());
    //            intent.putExtra("profilePic", data.getProfilePic());
    //            intent.putExtra("call", "profile");
    //            startActivity(intent);
    //        }
  }

  @Override
  public void friendRequests(boolean isNewFriendRequest) {
    //        rlNoNewFriends.setVisibility(isNewFriendRequest ? View.GONE : View.VISIBLE);
    //        llNewFriends.setVisibility(!isNewFriendRequest ? View.GONE : View.VISIBLE);
  }

  @Override
  public void onFriendRequestSelected(Friend data) {
    Intent intent = new Intent(getActivity(), AcceptRequestActivity.class);
    intent.putExtra("userId", data.getId());
    intent.putExtra("userName", data.getUserName());
    intent.putExtra("firstName", data.getFirstName());
    intent.putExtra("lastName", data.getLastName());
    intent.putExtra("profilePic", data.getProfilePic());
    intent.putExtra("mobileNumber", data.getNumber());
    intent.putExtra("call", "receive");
    startActivity(intent);
  }

  @Override
  public void newFriendCount(int count) {
    tvNewFriendCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    tvNewFriendCount.setText(String.valueOf(count));
  }

  @Override
  public void scroll(boolean empty) {
    if (appbar.getLayoutParams() != null) {
      CoordinatorLayout.LayoutParams layoutParams =
          (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
      AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
      appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
        @Override
        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
          return !empty;
        }
      });
      layoutParams.setBehavior(appBarLayoutBehaviour);
    }
  }

  @Override
  public void isFollowing(int pos, int status) {
    mPresenter.model.friendList.get(pos).setFollowStatus(status);
    contactAdapter.notifyItemChanged(pos);
  }
}