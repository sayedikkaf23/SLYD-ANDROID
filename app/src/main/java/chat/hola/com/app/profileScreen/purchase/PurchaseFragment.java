package chat.hola.com.app.profileScreen.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.comment.CommentActivity;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.purchase.model.PurchaseAdapter;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import dagger.android.support.DaggerFragment;

/**
 * Created by ankit on 17/2/18.
 */

public class PurchaseFragment extends DaggerFragment
    implements PurchaseContract.View, PurchaseAdapter.ClickListner {

  @Inject
  PurchasePresenter presenter;
  @Inject
  SessionManager sessionManager;

  private List<Data> data = new ArrayList<>();
  private PurchaseAdapter adapter;
  @Inject
  BlockDialog dialog;

  @BindView(R.id.recyclerProfileTab)
  RecyclerView recyclerViewProfileTab;
  @BindView(R.id.llEmpty)
  LinearLayout llEmpty;
  @BindView(R.id.tvEmpty)
  TextView tvEmpty;
  @BindView(R.id.ivEmpty)
  ImageView ivEmpty;
  @BindView(R.id.scroller)
  ScrollView scroller;
  @BindView(R.id.swipeRefresh)
  SwipeRefreshLayout swipeRefresh;

  private Unbinder unbinder;
  private String type = "";
  public static String userId = "";
  private GridLayoutManager gridLayoutManager;
  private LinearLayoutManager linearLayoutManager;

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @OnClick(R.id.btnCreatePost)
  public void createpost() {
  }

  public PurchaseFragment() {
  }

  public static PurchaseFragment newInstance() {
    return new PurchaseFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.attachView(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_story, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    type = getArguments().getString("name");
    userId = getArguments().getString("userId");
    presenter.init();
    presenter.postObserver();

    loadData();

    swipeRefresh.setOnRefreshListener(() -> {
      loadData();
    });

    return rootView;
  }

  @Override
  public void loadData() {
    PurchasePresenter.page = 0;
        presenter.loadData(PurchasePresenter.page * PurchasePresenter.PAGE_SIZE, PurchasePresenter.PAGE_SIZE,
            type.equals("nogrid"));
  }

  @Override
  public void updateSavedOnObserve(PostUpdateData saveData) {
    for(int i=0; i<data.size(); i++){
      if(data.get(i).getPostId().equals(saveData.getPostId())){
        data.get(i).setBookMarked(saveData.isLike());
        adapter.notifyItemChanged(i);
        break;
      }
    }
  }

  @Override
  public void setupRecyclerView() {
    PurchasePresenter.page = 0;

    gridLayoutManager = new GridLayoutManager(getContext(), 3);
    //recyclerViewProfileTab.setHasFixedSize(true);
    recyclerViewProfileTab.setLayoutManager(gridLayoutManager);
    recyclerViewProfileTab.addOnScrollListener(recyclerViewOnScrollListener);
    recyclerViewProfileTab.setNestedScrollingEnabled(true);
    scroller.setNestedScrollingEnabled(true);
    adapter = new PurchaseAdapter(getContext(), data);
    adapter.setListener(this);
    adapter.setData(type);
    recyclerViewProfileTab.setAdapter(adapter);
  }

  @Override
  public void showData(List<Data> data, boolean isFirst) {

    try {
      if (isFirst) this.data.clear();
      boolean isEmpty = data.isEmpty();
      boolean isMine = userId.equals(AppController.getInstance().getUserId());

      if (isMine) {
        // its my profile
        if (isEmpty) {
          llEmpty.setVisibility(View.VISIBLE);
          ivEmpty.setImageDrawable(requireContext()
              .getResources()
              .getDrawable(R.drawable.empty_post));
          tvEmpty.setText(getResources().getString(R.string.no_purchase_posts));
        }
      } else {
        boolean isPrivate = ProfileActivity.isPrivate;
        int followStatus = ProfileActivity.followStatus;
        // others profile
        switch (followStatus) {
          case 0:
            //not follow
            if (isPrivate) {
              llEmpty.setVisibility(View.VISIBLE);
              ivEmpty.setImageDrawable(requireContext()
                  .getResources()
                  .getDrawable(R.drawable.ic_lock));
              tvEmpty.setText(getResources().getString(R.string.private_no_post));
            } else if (isEmpty) {
              llEmpty.setVisibility(View.VISIBLE);
              ivEmpty.setImageDrawable(requireContext()
                  .getResources()
                  .getDrawable(R.drawable.empty_post));
              tvEmpty.setText(getResources().getString(R.string.no_purchase_posts));
            }
            break;
          case 1:
            //following
            if (isEmpty) {
              llEmpty.setVisibility(View.VISIBLE);
              ivEmpty.setImageDrawable(requireContext()
                  .getResources()
                  .getDrawable(R.drawable.empty_post));
              tvEmpty.setText(getResources().getString(R.string.no_purchase_posts));
            }
            break;
          case 2:
            //requested
            llEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageDrawable(requireContext()
                .getResources()
                .getDrawable(R.drawable.ic_lock));
            tvEmpty.setText(getResources().getString(R.string.private_no_post));
            break;
          default:
            llEmpty.setVisibility(View.GONE);
        }
      }

      recyclerViewProfileTab.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
      this.data.addAll(data);
      adapter.notifyDataSetChanged();
    } catch (NullPointerException e) {

      e.printStackTrace();
    }
  }

  @Override
  public void isLoading(boolean flag) {
    swipeRefresh.setRefreshing(flag);
  }

  @Override
  public void showEmptyUi(boolean show) {
    //llEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @Override
  public void noData() {

    if (!data.isEmpty()) return;

    llEmpty.setVisibility(View.VISIBLE);
    recyclerViewProfileTab.setVisibility(View.GONE);
    if (AppController.getInstance().getUserId().equals(userId)) {
      //my profile

      if (getContext() != null) {
        ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_post));
      } else {
        ivEmpty.setImageDrawable(
            AppController.getInstance().getResources().getDrawable(R.drawable.empty_post));
      }
      tvEmpty.setText(getResources().getString(R.string.no_purchase_posts));
    } else if (ProfileActivity.isPrivate) {
      //other user's profile
      if (getActivity() != null && getActivity() instanceof ProfileActivity && ((ProfileActivity) getActivity()).btnFollow.isChecked()) {

        if (getContext() != null) {
          ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_lock));
        } else {

          ivEmpty.setImageDrawable(
              AppController.getInstance().getResources().getDrawable(R.drawable.ic_lock));
        }
        tvEmpty.setText(getResources().getString(R.string.private_no_post));
      } else {
        if (getContext() != null) {
          ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_post));
        } else {
          ivEmpty.setImageDrawable(
              AppController.getInstance().getResources().getDrawable(R.drawable.empty_post));
        }
        tvEmpty.setText(getResources().getString(R.string.no_purchase_posts));
      }
    }else{
      //not my profile

      if (getContext() != null) {
        ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_post));
      } else {
        ivEmpty.setImageDrawable(
                AppController.getInstance().getResources().getDrawable(R.drawable.empty_post));
      }
      tvEmpty.setText(getResources().getString(R.string.no_purchase_posts));
    }
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
    //   llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (unbinder != null) unbinder.unbind();

    presenter.detachView();
  }

  @Override
  public void onItemClick(int position, int type, View view) {
    //        List<Data> dataList = new ArrayList<>();
    //        dataList.add(data.get(position));
    Intent intent = new Intent(getContext(), SocialDetailActivity.class);
    intent.putExtra("dataList", (Serializable) data);
    intent.putExtra("call", "profile");
    intent.putExtra("position", position);
    startActivity(intent);
  }

  @Override
  public void onCommentClick(String postId) {
    startActivity(new Intent(getContext(), CommentActivity.class).putExtra("postId", postId));
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onLikeClick(String postId, boolean like) {
    if (like) {
      presenter.like(postId, sessionManager);
    } else {
      presenter.unlike(postId);
    }
  }

  @Override
  public void reload() {
    onResume();
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

          presenter.callApiOnScroll(gridLayoutManager.findFirstVisibleItemPosition(),
              gridLayoutManager.getChildCount(), gridLayoutManager.getItemCount(), userId,
              type.equals("nogrid"));
        }
      };
}
