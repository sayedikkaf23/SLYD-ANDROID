package chat.hola.com.app.collections.add_to_collection;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.SpacesItemDecoration;
import chat.hola.com.app.collections.collection.CollectionPostAdapter;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.collections.model.PostData;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>{@link AddToCollectionActivity}</h1>
 * <p1>This activity show the list of post which need to be add in collection</p1>
 * @author : 3Embed
 * @since : 21/8/19
 */

public class AddToCollectionActivity extends DaggerAppCompatActivity implements AddToCollectionContract.View, CollectionPostAdapter.ClickListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rV_posts)
    RecyclerView rV_posts;
    @BindView(R.id.tV_title)
    TextView tV_title;
    @BindView(R.id.iV_done)
    ImageView iV_done;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    private Unbinder unbinder;
    @Inject
    AddToCollectionContract.Presenter presenter;
    private String collectionId = "";

    public static int offset = 0;
    static final int limit = 50;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private boolean isAllPost;
    private GridLayoutManager manager;
    private List<PostData> postList = new ArrayList<>();
    private CollectionPostAdapter adapter;
    private String collectionName = "";
    public int spacing;
    private List<String> selectedPostIds = new ArrayList<>();

    private boolean isNew = false;
    private String coverImage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_collection);
        unbinder = ButterKnife.bind(this);
        initData();
        initViews();
    }

    /**
     * Initialize data from intent.
     */
    private void initData() {
        isNew = getIntent().getBooleanExtra("isNew",false);

        if(!isNew)
            collectionId = getIntent().getStringExtra("collectionId");

        collectionName = getIntent().getStringExtra("collectionName");
    }

    /**
     * Initialize views
     */
    private void initViews() {
        String title = getString(R.string.add_to)+" "+collectionName;
        tV_title.setText(title);
        adapter = new CollectionPostAdapter(this,postList);
        adapter.setClickListener(this);

        // Set spacing the recycler view items
        spacing = 5; // 100px
        int spanCount = 3;
        manager = new GridLayoutManager(this, spanCount);
        rV_posts.setLayoutManager(manager);
        rV_posts.addItemDecoration(new SpacesItemDecoration(spanCount,spacing,false));
        rV_posts.addOnScrollListener(recyclerViewOnScrollListener);
        rV_posts.setAdapter(adapter);

        if(isNew)
            presenter.getAllPost(offset, limit);
        else
            presenter.getCollectionPost(collectionId,offset,limit);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                postList.clear();
                adapter.notifyDataSetChanged();

                if(isNew)
                    presenter.getAllPost(offset, limit);
                else
                    presenter.getCollectionPost(collectionId,offset,limit);
            }
        });
    }

    /**
     * RecycleView to handle scroll for pagination.
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
                    if(isNew)
                        presenter.getAllPost(offset, limit);
                    else
                        presenter.getCollectionPost(collectionId, offset, limit);

                }
            }
        }
    };

    @OnClick(R.id.iV_back)
    public void back(){
        onBackPressed();
    }

    @OnClick(R.id.iV_done)
    public void done() {
        if(!selectedPostIds.isEmpty()) {
            if(isNew)
                presenter.createCollection(collectionName,coverImage,selectedPostIds);
            else
                presenter.addPostToCollection(collectionId, selectedPostIds);
        }
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

    }

    @Override
    public void OnItemClick(int pos, PostData data) {
        if(!data.isSelected()) {
            data.setSelected(true);
            selectedPostIds.add(data.getId());

            if(coverImage.isEmpty())
                coverImage = data.getThumbnailUrl();

            adapter.notifyItemChanged(pos);
        }else {
            data.setSelected(false);
            selectedPostIds.remove(data.getId());

            adapter.notifyItemChanged(pos);
        }
    }

    @Override
    public void showProgress(boolean show) {
        swipeRefresh.setRefreshing(show);
        isLoading=show;
    }

    @Override
    public void collectionPostResponse(List<PostData> data) {
        isLastPage = data.size()<limit;
        postList.addAll(data);
        adapter.notifyDataSetChanged();
        iV_done.setVisibility(postList!=null && !postList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void successfullyAdded() {
        onBackPressed();
    }

    @Override
    public void showEmpty() {
        ll_empty.setVisibility(postList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void createdSuccess(CreateCollectionResponse body) {
        setResult(101);
        onBackPressed();
    }
}
