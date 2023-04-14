package chat.hola.com.app.collections.saved;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.SpacesItemDecoration;
import chat.hola.com.app.collections.collection.CollectionActivity;
import chat.hola.com.app.collections.create_collection.CreateCollectionActivity;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>{@link SavedActivity}</h1>
 * <p>Saved Post screen contain All post and list of collections</p>
 *
 * @author 3Embed
 * @since 20/8/19
 */

public class SavedActivity extends DaggerAppCompatActivity implements SavedContract.View, SavedAdapter.ClickListener {

    @BindView(R.id.iV_add)
    ImageView iV_add;
    @BindView(R.id.rV_collections)
    RecyclerView rV_collections;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    private Unbinder unbinder;

    @Inject
    SavedContract.Presenter presenter;
    @Inject
    SessionManager sessionManager;

    private ArrayList<CollectionData> collectionList = new ArrayList<>();
    private SavedAdapter savedAdapter;

    public static int offset = 0;
    static final int limit = 100;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        unbinder = ButterKnife.bind(this);

        initViews();

        presenter.getCollections(offset, limit);
    }

    /**
     * initialize the views
     */
    private void initViews() {
        savedAdapter = new SavedAdapter(this, collectionList);
        savedAdapter.setClickListener(this);
        rV_collections.setAdapter(savedAdapter);


        // Set spacing the recycler view items
        int spacing = 20; // 200px
        int spanCount = 2;
        manager = new GridLayoutManager(this, spanCount);
        rV_collections.setLayoutManager(manager);
        rV_collections.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, true));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    /**
     * RecycleView Scroll listener to manage pagination.
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
                    presenter.getCollections(offset * limit, limit);
                }
            }
        }
    };

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

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.iV_add)
    public void openCreateCollection() {
        startActivity(new Intent(this, CreateCollectionActivity.class));
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {
        if (sessionManager != null)
            sessionManager.sessionExpired(this);
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
        collectionList.clear();
        savedAdapter.notifyDataSetChanged();
        presenter.getCollections(offset, limit);
    }

    @Override
    public void showProgress(boolean b) {
        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(b);
    }

    @Override
    public void onSuccess(List<CollectionData> data) {
        collectionList.addAll(data);
        savedAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty() {
        if(ll_empty!=null)
        ll_empty.setVisibility(collectionList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void OnItemClick(int pos, CollectionData data) {
        Intent intent = new Intent(this, CollectionActivity.class);
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
        startActivity(intent);
    }
}
