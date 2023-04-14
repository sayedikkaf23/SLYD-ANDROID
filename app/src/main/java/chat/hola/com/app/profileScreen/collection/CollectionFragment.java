package chat.hola.com.app.profileScreen.collection;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import chat.hola.com.app.Utilities.SpacesItemDecoration;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.collections.saved.SavedAdapter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import dagger.android.support.DaggerFragment;

import static chat.hola.com.app.profileScreen.collection.CollectionPresenter.PAGE_SIZE;
import static chat.hola.com.app.profileScreen.collection.CollectionPresenter.page;

/*
 * Bug Title: Add new collection APIs.
 * Bug Id: DUBAND060
 * Fix Desc: add last tab in profile as saved
 * Fix Dev: Hardik
 * Fix Date: 15/4/21
 * */

public class CollectionFragment extends DaggerFragment
    implements CollectionContract.View, SavedAdapter.ClickListener {

  @Inject
  CollectionPresenter presenter;
  @Inject
  SessionManager sessionManager;

  private ArrayList<CollectionData> collectionList = new ArrayList<>();
  private SavedAdapter savedAdapter;

  @Inject
  BlockDialog dialog;

  @BindView(R.id.recyclerProfileTab)
  RecyclerView recyclerView;
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
  private GridLayoutManager manager;

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @OnClick(R.id.btnCreatePost)
  public void createpost() {
  }

  public CollectionFragment() {
  }

  public static CollectionFragment newInstance() {
    return new CollectionFragment();
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
    loadData();
    swipeRefresh.setOnRefreshListener(() -> {
      loadData();
    });
    return rootView;
  }

  @Override
  public void loadData() {
    page = 0;
    presenter.getCollections(page*PAGE_SIZE,PAGE_SIZE);
  }

  @Override
  public void updateSavedOnObserve(PostUpdateData saveData) {
  }

  @Override
  public void onSuccess(List<CollectionData> data, boolean isClear) {
    if (isClear)collectionList.clear();
    collectionList.addAll(data);
    savedAdapter.notifyDataSetChanged();
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void setupRecyclerView() {
    page = 0;
    recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    recyclerView.setNestedScrollingEnabled(true);
    scroller.setNestedScrollingEnabled(true);
    savedAdapter = new SavedAdapter(getContext(), collectionList);
    savedAdapter.setClickListener(this);
    recyclerView.setAdapter(savedAdapter);


    // Set spacing the recycler view items
    int spacing = 20; // 200px
    int spanCount = 2;
    manager = new GridLayoutManager(getActivity(), spanCount);
    recyclerView.setLayoutManager(manager);
    recyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, true));
  }


  @Override
  public void isLoading(boolean flag) {
    swipeRefresh.setRefreshing(flag);
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  @Override
  public void showEmptyUi(boolean show) {
    if (getContext() != null) {
      ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.empty_saved_post));
    } else {
      ivEmpty.setImageDrawable(
              AppController.getInstance().getResources().getDrawable(R.drawable.empty_saved_post));
    }
    tvEmpty.setText(getResources().getString(R.string.no_saved_collection));

    recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    llEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
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
  public void OnItemClick(int pos, CollectionData data) {
    Intent intent = new Intent(getContext(), SocialDetailActivity.class);
    intent.putExtra("dataList", (Serializable) data.getPosts());
    intent.putExtra("collectionData", (Serializable) data);
    intent.putExtra("isCollection",true);
    intent.putExtra("position", 0);
    startActivity(intent);
    /*Intent intent = new Intent(getActivity(), CollectionActivity.class);
    if (pos == 0) {
      intent.putExtra("isAllPost", true);
      intent.putExtra("collectionName", getString(R.string.all_post));
      intent.putExtra("collectionId", "");
      intent.putExtra("coverImage", data.getCoverImage());
    } else {
      intent.putExtra("isAllPost", false);
      intent.putExtra("collectionName", data.getCollectionName());
      intent.putExtra("collectionId", data.getId());
      intent.putExtra("coverImage", data.getCoverImage());
    }
    startActivity(intent);*/
  }



  @Override
  public void onResume() {
    super.onResume();
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
          presenter.callApiOnScroll(manager.findFirstVisibleItemPosition(),
              manager.getChildCount(), manager.getItemCount());
        }
      };
}
