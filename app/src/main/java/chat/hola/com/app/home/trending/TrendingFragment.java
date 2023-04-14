package chat.hola.com.app.home.trending;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.model.ContentAdapter;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.trending.model.Header;
import chat.hola.com.app.home.trending.model.HeaderAdapter;
import chat.hola.com.app.home.trending.model.TrendingContentAdapter;
import chat.hola.com.app.home.trending.model.TrendingItemAdapter;
import chat.hola.com.app.live_stream.Home.live_users.LiveUsersActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.music.MusicActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.stars.StarActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import com.ezcall.android.R;
import dagger.android.support.DaggerFragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * <h>TrendingFragment.class</h>
 * <p>
 * This Fragment shows the trendingPosts row using {@link HeaderAdapter } and
 * {@link ContentAdapter} with recyclerecyclerHeader.nd recyclerecyclerContent.
 * </p>
 *
 * @author 3Embed
 * @since 13/2/18.
 */

@SuppressLint("ValidFragment")
public class TrendingFragment extends DaggerFragment
        implements TrendingContract.View, TrendingContentAdapter.ClickListner,
        TrendingItemAdapter.ClickListner {

    private String categoryId;
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    public static int page = 0;

    @Inject
    public TrendingFragment() {
    }

    @Inject
    TrendingPresenter presenter;
    @Inject
    TrendingContentAdapter contentAdapter;
    @Inject
    HeaderAdapter headerAdapter;

    @BindView(R.id.rvHeader)
    RecyclerView rvHeader;
    @BindView(R.id.gvContent)
    RecyclerView gvContent;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.llHashTags)
    LinearLayout llHashTags;
    @BindView(R.id.tvHashTags)
    TextView tvHashTags;
    //@BindView(R.id.appbarLayout)
    //AppBarLayout appbarLayout;
    //@BindView(R.id.cToolbar)
    //CollapsingToolbarLayout cToolbar;
    @Inject
    TypefaceManager typefaceManager;
    //private boolean searchViewCollapsed;
    private LinearLayoutManager layoutManager;
    private Unbinder unbinder;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Inject
    SessionManager sessionManager;

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

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
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.attachView(this);
        presenter.init();
        isLoading(true);
        changeVisibilityOfViews();
        presenter.loadContent(0, PAGE_SIZE);
        swiperefresh.setOnRefreshListener(this::reload);
        /**
         * Bug Title- Always show search in tabbar and hide coin/live button instead of hiding on scroll
         * Bug Id-NA
         * Fix Description-Remove code to change visibility of search,coin and live view in tabbar on scroll
         * Developer Name-Ashutosh
         * Fix Date-25/6/21
         **/
        //appbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
        //    double percentage = (double) Math.abs(verticalOffset) / cToolbar.getHeight();
        //    if (percentage > 0.6) {
        //        if (!searchViewCollapsed) {
        //            searchViewCollapsed = true;
        //            if (getActivity() != null) {
        //                ((LandingActivity) getActivity()).tvCoins.setVisibility(View.GONE);
        //                ((LandingActivity) getActivity()).ivLiveStream.setVisibility(View.GONE);
        //                ((LandingActivity) getActivity()).tvSearch.setVisibility(View.VISIBLE);
        //            }
        //        }
        //    } else {
        //
        //        if (searchViewCollapsed) {
        //            searchViewCollapsed = false;
        //            if (getActivity() != null) {
        //
        //                ((LandingActivity) getActivity()).tvSearch.setVisibility(View.GONE);
        //                ((LandingActivity) getActivity()).tvCoins.setVisibility(View.VISIBLE);
        //                ((LandingActivity) getActivity()).ivLiveStream.setVisibility(View.VISIBLE);
        //            }
        //        }
        //    }
        //});

        return rootView;
    }

    /*Here we change the common views visibility on selection of fragment*/
    public void changeVisibilityOfViews() {
        LandingActivity mActivity = (LandingActivity) getActivity();
        mActivity.visibleActionBar();

        /*
         *BugId:DUBAND050
         *BugTitle:profile icon unrequired so visiblity gone for now
         *Developer name:Ankit k Tiwary
         * FixDate:6April2021*/
        mActivity.ivProfilePic.setVisibility(View.GONE);
        mActivity.iV_plus.setVisibility(View.GONE);
        mActivity.setTitle("Explore", typefaceManager.getMediumFont());
        mActivity.removeFullScreenFrame();
        mActivity.linearPostTabs.setVisibility(View.GONE);
        //        mActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mActivity.tvCoins.setVisibility(View.VISIBLE);
        //if (searchViewCollapsed) {
        mActivity.tvCoins.setVisibility(View.GONE);
        mActivity.ivLiveStream.setVisibility(View.GONE);
        mActivity.tvSearch.setVisibility(View.VISIBLE);
        //} else {
        //    mActivity.tvSearch.setVisibility(View.GONE);
        //    mActivity.tvCoins.setVisibility(View.VISIBLE);
        //    mActivity.ivLiveStream.setVisibility(View.VISIBLE);
        //}
    }

    @Override
    public void onResume() {
        super.onResume();
        //    presenter.loadHeader();
    }

    //@OnClick(R.id.tvSearch)
    //public void setSearch() {
    //    startActivity(new Intent(getContext(), SearchActivity.class));
    //}

    @Override
    public void initContentRecycler() {
        contentAdapter.setPostListner(this);
        contentAdapter.setViewAllListner(this);
        layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration =
            new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        gvContent.setLayoutManager(layoutManager);
        gvContent.setHasFixedSize(true);
        gvContent.addItemDecoration(decoration);
        gvContent.setAdapter(contentAdapter);
        gvContent.addOnScrollListener(recyclerViewOnScrollListener);
        // presenter.loadContent(presenter.getDefaultCategoryId());
    }

    @Override
    public void initHeaderRecycler() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHeader.setLayoutManager(llm);
        rvHeader.setHasFixedSize(true);
        rvHeader.setAdapter(headerAdapter);
        headerAdapter.setListner(presenter);
        presenter.loadHeader();
    }

    @Override
    public void showHeader(ArrayList<Header> headers) {
        headerAdapter.setData(headers);
        this.categoryId = headers.get(1).getId();
    }

    @Override
    public void showContent(ArrayList<Data> trendings) {

    }

    @Override
    public void onPostClick(List<Data> dataList, int position, View view) {
        Intent intent = new Intent(getContext(), SocialDetailActivity.class);
        intent.putExtra("dataList", (Serializable) dataList);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void isLoading(boolean flag) {
        swiperefresh.setRefreshing(flag);
    }

    @Override
    public void isContentAvailable(boolean flag) {
        llEmpty.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setHashTags(StringBuilder tags) {
        String tag = tags.toString();
        SpannableString spanString = new SpannableString(tag);
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
        findMatch(spanString, matcher);
        tvHashTags.setText(spanString);
        tvHashTags.setMovementMethod(LinkMovementMethod.getInstance());
        isLoading(false);
    }

    @Override
    public void setCategoryId(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        startActivity(new Intent(getContext(), TrendingDetail.class).putExtra("categoryId", categoryId)
                .putExtra("call", "category")
                .putExtra("category", categoryName));
    }

    @Override
    public void onStarCLick() {
        startActivity(new Intent(getContext(), StarActivity.class));
    }

    @Override
    public void onLiveStreamCLick() {
        startActivity(new Intent(getContext(), LiveUsersActivity.class));
        ((LandingActivity) getContext()).overridePendingTransition(R.anim.slide_in_up,
                R.anim.slide_out_up);
    }

    private void findMatch(SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new TagSpannable(getContext(), tag, "trending"), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    public void onDestroy() {
        presenter.detachView();
        if (unbinder != null) unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void reload() {
        presenter.loadContent(0, PAGE_SIZE);
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    //            super.onScrollStateChanged(recyclerView, newState);
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            break;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition =
                            ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
                }
            };

    @Override
    public void viewAll(String hashtag, String totalPosts) {
        //        sessionManager.setHomeTab(3);
        startActivity(new Intent(getContext(), MusicActivity.class).putExtra("hashtag", hashtag)
                .putExtra("call", "hashtag"));
    }

    @Override
    public void postClick(List<Data> dataList, int position, String hashtag) {
        Intent intent = new Intent(getContext(), SocialDetailActivity.class);
        Data data = dataList.get(position);
        intent.putExtra("apiCallRequired", true);
        intent.putExtra("position", position);
        intent.putExtra("hashtag",hashtag);
        intent.putExtra("height", data.getImageUrl1Height());
        intent.putExtra("width", data.getImageUrl1Width());
        intent.putExtra("url", data.getThumbnailUrl1());
        intent.putExtra("postId", data.getPostId());
        intent.putExtra("isPurchased", data.getPurchased());
        intent.putExtra("mediaType", data.getMediaType1());
        intent.putExtra("thumbnailUrl", data.getThumbnailUrl1());
        startActivity(intent);
    }

    @Override
    public void viewAllHeader(String hashtag, String totalPosts) {
        startActivity(new Intent(getContext(), MusicActivity.class).putExtra("hashtag", hashtag)
                .putExtra("totalPosts", totalPosts));
    }
}