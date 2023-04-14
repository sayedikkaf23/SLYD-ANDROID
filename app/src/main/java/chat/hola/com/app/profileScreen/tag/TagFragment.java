package chat.hola.com.app.profileScreen.tag;

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
import chat.hola.com.app.profileScreen.story.StoryPresenter;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import com.ezcall.android.R;
import dagger.android.support.DaggerFragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

/**
 * Created by ankit on 17/2/18.
 */

public class TagFragment extends DaggerFragment
    implements TagContract.View, TagAdapter.ClickListner {

  private static final String TAG = TagFragment.class.getSimpleName();
  @Inject
  TagPresenter presenter;
  @Inject
  SessionManager sessionManager;
  private TagAdapter adapter;
  private List<Data> data = new ArrayList<>();
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
  private String userId = "";
  private GridLayoutManager gridLayoutManager;
  private LinearLayoutManager linearLayoutManager;
  private String userName;

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @OnClick(R.id.btnCreatePost)
  public void createpost() {
  }

  public TagFragment() {
  }

  public static TagFragment newInstance() {
    return new TagFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.attachView(this);
  }

  @Override
  public void viewAppear() {
    try {
      if (data != null) data.clear();
      presenter.init();
      presenter.taggedPosts(userName, 0, TagPresenter.PAGE_SIZE);
      swipeRefresh.setOnRefreshListener(() -> {
        presenter.taggedPosts(userName,0,TagPresenter.PAGE_SIZE);
      });
    } catch (Exception ignored) {
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_tagged, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    type = getArguments().getString("name");
    userId = getArguments().getString("userId");
    userName = getArguments().getString("userName");

    presenter.initObservers();
    viewAppear();
    return rootView;
  }

  @Override
  public void setupRecyclerView() {
    TagPresenter.page = 0;
    gridLayoutManager = new GridLayoutManager(getContext(), 3);
    recyclerViewProfileTab.setLayoutManager(gridLayoutManager);
    recyclerViewProfileTab.addOnScrollListener(recyclerViewOnScrollListener);
    recyclerViewProfileTab.setNestedScrollingEnabled(true);
    adapter = new TagAdapter(getContext(), data);
    adapter.setListener(this);
    adapter.setData(type);
    recyclerViewProfileTab.setAdapter(adapter);
    scroller.setNestedScrollingEnabled(true);
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
          ivEmpty.setImageDrawable(Objects.requireNonNull(getContext())
              .getResources()
              .getDrawable(R.drawable.empty_tag));
          tvEmpty.setText(getResources().getString(R.string.create_post_message));
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
              ivEmpty.setImageDrawable(Objects.requireNonNull(getContext())
                  .getResources()
                  .getDrawable(R.drawable.ic_lock));
              tvEmpty.setText(getResources().getString(R.string.private_no_post));
            } else if (isEmpty) {
              llEmpty.setVisibility(View.VISIBLE);
              ivEmpty.setImageDrawable(Objects.requireNonNull(getContext())
                  .getResources()
                  .getDrawable(R.drawable.empty_tag));
              tvEmpty.setText(getResources().getString(R.string.no_post));
            }
            break;
          case 1:
            //following
            if (isEmpty) {
              llEmpty.setVisibility(View.VISIBLE);
              ivEmpty.setImageDrawable(Objects.requireNonNull(getContext())
                  .getResources()
                  .getDrawable(R.drawable.empty_tag));
              tvEmpty.setText(getResources().getString(R.string.no_post));
            }
            break;
          case 2:
            //requested
            llEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageDrawable(Objects.requireNonNull(getContext())
                .getResources()
                .getDrawable(R.drawable.ic_lock));
            tvEmpty.setText(getResources().getString(R.string.private_no_post));
            break;
          default:
            llEmpty.setVisibility(View.GONE);
        }
      }

      recyclerViewProfileTab.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
      adapter.setData(data);
      this.data.addAll(data);
      //            adapter.notifyDataSetChanged();
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
        ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_tag));
      } else {
        ivEmpty.setImageDrawable(
            AppController.getInstance().getResources().getDrawable(R.drawable.empty_tag));
      }
      tvEmpty.setText(getResources().getString(R.string.no_tag_post_show));
    } else if (ProfileActivity.isPrivate) {
      //other user's profile
      if (getActivity() != null && getActivity() instanceof ProfileActivity &&((ProfileActivity) getActivity()).btnFollow.isChecked()) {

        if (getContext() != null) {
          ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_lock));
        } else {
          ivEmpty.setImageDrawable(
              AppController.getInstance().getResources().getDrawable(R.drawable.ic_lock));
        }

        tvEmpty.setText(getResources().getString(R.string.private_no_post));
      } else {
        if (getContext() != null) {

          ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_tag));
        } else {

          ivEmpty.setImageDrawable(
              AppController.getInstance().getResources().getDrawable(R.drawable.empty_tag));
        }
        tvEmpty.setText(getResources().getString(R.string.no_tag_post_show));
      }
    }else{
      //not my profile
      if (getContext() != null) {
        ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_tag));
      } else {
        ivEmpty.setImageDrawable(
                AppController.getInstance().getResources().getDrawable(R.drawable.empty_tag));
      }
      tvEmpty.setText(getResources().getString(R.string.no_tag_post_show));
    }
  }

  @Override
  public void updateSavedOnObserve(PostUpdateData postUpdateData) {
    for(int i=0; i<data.size(); i++){
      if(data.get(i).getPostId().equals(postUpdateData.getPostId())){
        data.get(i).setBookMarked(postUpdateData.isLike());
        adapter.notifyItemChanged(i);
        break;
      }
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
  public void onItemClick(int position, Data data, int type, View view) {
    //        List<Data> dataList = new ArrayList<>();
    //        dataList.add(data);
    Intent intent = new Intent(getContext(), SocialDetailActivity.class);
    intent.putExtra("dataList", (Serializable) this.data);
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
              gridLayoutManager.getChildCount(), gridLayoutManager.getItemCount(), userName,
              type.equals("nogrid"));
        }
      };
}
