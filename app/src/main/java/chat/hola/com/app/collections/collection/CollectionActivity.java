package chat.hola.com.app.collections.collection;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.SpacesItemDecoration;
import chat.hola.com.app.collections.add_to_collection.AddToCollectionActivity;
import chat.hola.com.app.collections.edit_collection.EditCollectionActivity;
import chat.hola.com.app.collections.model.PostData;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>{@link CollectionActivity}</h1>
 * <p1>This activity show the list of post in collection</p1>
 * @author : 3Embed
 * @since : 20/8/19
 */

public class CollectionActivity extends DaggerAppCompatActivity implements CollectionContract.View, CollectionPostAdapter.ClickListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rV_posts)
    RecyclerView rV_posts;
    @BindView(R.id.tV_title)
    TextView tV_title;
    @BindView(R.id.iV_option)
    ImageView iV_option;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    private Unbinder unbinder;
    @Inject
    CollectionContract.Presenter presenter;
    private String collectionId = "";

    public static int offset = 0;
    static final int limit = 50;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private boolean isAllPost, isImageSelection;
    private GridLayoutManager manager;
    private List<PostData> postList = new ArrayList<>();
    private CollectionPostAdapter adapter;
    private String collectionName = "";
    public int spacing;
    private String coverImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        unbinder = ButterKnife.bind(this);
        initData();
        initViews();
    }

    /**
     * Initialize data from intent.
     */
    private void initData() {
        isAllPost = getIntent().getBooleanExtra("isAllPost", true);
        isImageSelection = getIntent().getBooleanExtra("isImageSelection", false);
        collectionId = getIntent().getStringExtra("collectionId");
        collectionName = getIntent().getStringExtra("collectionName");
        coverImage = getIntent().getStringExtra("coverImage");
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tV_title.setText(collectionName);
        adapter = new CollectionPostAdapter(this, postList);
        adapter.setClickListener(this);

        // Set spacing the recycler view items
        spacing = 5; // 100px
        int spanCount = 3;
        manager = new GridLayoutManager(this, spanCount);
        rV_posts.setLayoutManager(manager);
        rV_posts.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, false));
        rV_posts.addOnScrollListener(recyclerViewOnScrollListener);
        rV_posts.setAdapter(adapter);

        if (isAllPost) {
            presenter.getAllPost(offset, limit);
            iV_option.setVisibility(View.GONE);
        } else {
            presenter.getCollectionPost(collectionId, offset, limit);
            iV_option.setVisibility(View.VISIBLE);

            if (isImageSelection)
                iV_option.setVisibility(View.GONE);
        }


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                postList.clear();
                adapter.notifyDataSetChanged();
                if (isAllPost)
                    presenter.getAllPost(offset, limit);
                else
                    presenter.getCollectionPost(collectionId, offset, limit);
            }
        });
    }

    /**
     * RecycleView scroll listener for handle pagination.
     */
    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount;
            int totalItemCount;
            int firstVisibleItemPosition;
            visibleItemCount = manager.getChildCount();
            totalItemCount = manager.getItemCount();
            firstVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= offset) {
                    offset++;
                    if (isAllPost)
                        presenter.getAllPost(offset * limit, limit);
                    else
                        presenter.getCollectionPost(collectionId, offset * limit, limit);
                }
            }
        }
    };

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.iV_option)
    public void optionClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.collection_option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch (which) {
                    /**
                     * Edit Collection select
                     */
                    case 0:
                        Intent i = new Intent(CollectionActivity.this, EditCollectionActivity.class);
                        i.putExtra("collectionId", collectionId);
                        i.putExtra("collectionName", collectionName);
                        i.putExtra("coverImage", coverImage);
                        startActivity(i);
                        finish();
                        break;
                    /**
                     * Add to Collection select
                     */
                    case 1:
                        Intent intent = new Intent(CollectionActivity.this, AddToCollectionActivity.class);
                        intent.putExtra("collectionId", collectionId);
                        intent.putExtra("collectionName", collectionName);
                        startActivity(intent);
                        break;
                }
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {
        offset = 0;
        postList.clear();
        adapter.notifyDataSetChanged();
        if (isAllPost)
            presenter.getAllPost(offset, limit);
        else
            presenter.getCollectionPost(collectionId, offset, limit);
    }

    @Override
    public void showProgress(boolean show) {
        try {
            isLoading = show;
            swipeRefresh.setRefreshing(show);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void collectionPostResponse(List<PostData> data) {
        isLastPage = data.size() < limit;
        postList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty() {
       ll_empty.setVisibility(postList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void OnItemClick(int pos, PostData data) {
        if (isImageSelection) {
            setResult(2001, new Intent().putExtra("coverImage", data.getThumbnailUrl()));
            onBackPressed();
        }else {
            Intent intent = new Intent(this, SocialDetailActivity.class);
            intent.putExtra("postId", data.getId());
            startActivity(intent);
        }
    }
}
