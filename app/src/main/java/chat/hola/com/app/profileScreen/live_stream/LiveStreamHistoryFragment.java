package chat.hola.com.app.profileScreen.live_stream;

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
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.VideoStreamingActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.preview.PreviewActivity;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.profile_story.model.ProfileStoryAdap;
import com.ezcall.android.R;
import dagger.android.support.DaggerFragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LiveStreamHistoryFragment extends DaggerFragment
    implements LiveStreamHistoryContract.View, ProfileStoryAdap.ClickListner {

  @Inject
  LiveStreamHistoryPresenter presenter;
  @Inject
  SessionManager sessionManager;

  private Unbinder unbinder;
  private String type = "";
  private String userId = "";
  private GridLayoutManager gridLayoutManager;
  private ProfileStoryAdap adapter;
  private List<StoryPost> storyPosts = new ArrayList<>();

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

  public static LiveStreamHistoryFragment newInstance() {
    return new LiveStreamHistoryFragment();
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
    View rootView = inflater.inflate(R.layout.fragment_live_stream_history, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    type = getArguments().getString("name");
    userId = getArguments().getString("userId");
    presenter.init();

    if (userId != null && !userId.equals("")) {
      presenter.loadData(userId, 0, LiveStreamHistoryPresenter.PAGE_SIZE);
    }

    return rootView;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (unbinder != null) unbinder.unbind();

    presenter.detachView();
  }

  @Override
  public void setupRecyclerView() {
    gridLayoutManager = new GridLayoutManager(getContext(), 3);
    recyclerViewProfileTab.setLayoutManager(gridLayoutManager);
    recyclerViewProfileTab.addOnScrollListener(recyclerViewOnScrollListener);
    recyclerViewProfileTab.setNestedScrollingEnabled(true);
    scroller.setNestedScrollingEnabled(true);
    adapter = new ProfileStoryAdap(getContext(), storyPosts, this);
    adapter.setData(type);
    recyclerViewProfileTab.setAdapter(adapter);
  }

  @Override
  public void showData(List<StoryPost> storyPosts, boolean isFirst) {
    if (isFirst){
    this.storyPosts.clear();}
    this.storyPosts.addAll(storyPosts);
    if (this.storyPosts.isEmpty()) {
      llEmpty.setVisibility(View.VISIBLE);
    } else {
      llEmpty.setVisibility(View.GONE);
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void isLoading(boolean flag) {

  }

  @Override
  public void showEmptyUi(boolean show) {

  }

  @Override
  public void streamDeleteSuccess(boolean isDeleted) {
    if (userId != null && !userId.equals("")) {
      presenter.loadData(userId, 0, LiveStreamHistoryPresenter.PAGE_SIZE);
    }
  }

  @Override
  public void noData() {

    if (!storyPosts.isEmpty()) return;

    llEmpty.setVisibility(View.VISIBLE);
    recyclerViewProfileTab.setVisibility(View.GONE);
    if (AppController.getInstance().getUserId().equals(userId)) {
      //my profile
      if (getContext() != null) {
        ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_stories));
      } else {

        ivEmpty.setImageDrawable(
            AppController.getInstance().getResources().getDrawable(R.drawable.empty_stories));
      }

      tvEmpty.setText(getResources().getString(R.string.no_stream_show));
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
          ivEmpty.setImageDrawable(
              getContext().getResources().getDrawable(R.drawable.empty_stories));
        } else {

          ivEmpty.setImageDrawable(
              AppController.getInstance().getResources().getDrawable(R.drawable.empty_stories));
        }
        tvEmpty.setText(getResources().getString(R.string.no_stream_show));
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

  }

  @Override
  public void userBlocked() {

  }

  @Override
  public void reload() {

  }

  private void startPreviewActivity(List<StoryData> data, boolean isMystory, int postion) {

    Intent a = new Intent(getActivity(), PreviewActivity.class);
    a.putExtra(PreviewActivity.IS_IMMERSIVE_KEY, false);
    a.putExtra(PreviewActivity.IS_CACHING_ENABLED_KEY, false);
    a.putExtra(PreviewActivity.IS_TEXT_PROGRESS_ENABLED_KEY, false);
    a.putExtra(PreviewActivity.STATUS_IS_MY_STORY, isMystory);
    a.putExtra("position", postion);
    a.putExtra(PreviewActivity.MY_STORY_POSTS, (Serializable) data);

    if (isMystory) {
      a.putExtra(PreviewActivity.MY_STORY_POSTS, (Serializable) data);
    } else {
      a.putExtra(PreviewActivity.ALL_STORY_POST, (Serializable) data);
    }

    startActivity(a);
  }

  @Override
  public void onItemClick(int position) {
//    List<StoryData> storyData = new ArrayList<>();
//    List<StoryPost> story = new ArrayList<>();
//    story.add(storyPosts.get(position));
//    storyData.add(0, new StoryData());
//    storyData.get(0).setPosts(story);
//    startPreviewActivity(storyData, false, 0);

    startActivity(VideoStreamingActivity.getStartIntent(getActivity(),storyPosts.get(position).getUrlPath()));

  }

  @Override
  public void onLikeClick(String postId, boolean like) {

  }

  @Override
  public void onCommentClick(String postId) {

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

          presenter.callApiOnScroll(userId, gridLayoutManager.findFirstVisibleItemPosition(),
              gridLayoutManager.getChildCount(), gridLayoutManager.getItemCount());
        }
      };

  @Override
  public void onOptionItemClick(StoryPost data, int position, ImageView imageView) {
    PopupMenu popup = new PopupMenu(getActivity(), imageView);
    popup.inflate(R.menu.stream_popup_menu);
    popup.setOnMenuItemClickListener(item -> {
      if (item.getItemId() == R.id.delete) {
        presenter.deleteStream(data.getStreamId());
        return true;
      }
      return false;
    });
    popup.show();
  }
}
