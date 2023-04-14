package chat.hola.com.app.profileScreen.channel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.ConfirmDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.model.ContentAdapter;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;

import com.ezcall.android.R;

import dagger.android.support.DaggerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <h>ChannelFragment</h>
 * <p>
 * This fragment show the All channels exist for the particular user
 * along with all post in that channel. in order to do so it uses two Adapter
 * {@link ContentAdapter }
 *
 * @author 3Embed
 * @since 17/2/18.
 */

public class ChannelFragment extends DaggerFragment
        implements ChannelContract.View, ContentAdapter.AdapterClickCallback {

    @Inject
    ChannelPresenter presenter;
    @Inject
    SessionManager sessionManager;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    ContentAdapter contentAdapter;
    @Inject
    BlockDialog dialog;

    @BindView(R.id.ivEmpty)
    ImageView ivEmpty;
    @BindView(R.id.recyclerProfileTab)
    RecyclerView recyclerViewProfileTab;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.scroller)
    ScrollView scroller;
    private Unbinder unbinder;
    private int PAGE_SIZE = 5;
    private int page = 0;
    private String userId;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ChannelData> channelDataList = new ArrayList<>();
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((linearLayoutManager.getChildCount() + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE) {
                            page++;
                            presenter.getChannelData(PAGE_SIZE * page, PAGE_SIZE, userId);
                        }
                    }
                }
            };

    @Inject
    public ChannelFragment() {
    }

    public static ChannelFragment newInstance() {
        return new ChannelFragment();
    }

    @Override
    public void userBlocked() {
        dialog.show();
    }

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
        View rootView = inflater.inflate(R.layout.fragment_channel, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        userId = getArguments().getString("userId");
        presenter.init();
        presenter.subscribeObservers();
        page = 0;
        presenter.getChannelData(page, PAGE_SIZE, userId);
        applyFont();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getChannelData(page, PAGE_SIZE, userId);
    }

    private void applyFont() {
        tvEmpty.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewProfileTab.setLayoutManager(linearLayoutManager);
        recyclerViewProfileTab.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerViewProfileTab.setNestedScrollingEnabled(true);
        scroller.setNestedScrollingEnabled(true);
        recyclerViewProfileTab.setAdapter(contentAdapter);
        channelDataList.clear();

        contentAdapter.setCallback(this);
    }

    @Override
    public void showChannelData(ArrayList<ChannelData> channelData) {
        llEmpty.setVisibility(channelData.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerViewProfileTab.setVisibility(channelData.isEmpty() ? View.GONE : View.VISIBLE);

        if (getContext() != null) {

            ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_channel_gray));
        } else {

            ivEmpty.setImageDrawable(
                    AppController.getInstance().getResources().getDrawable(R.drawable.ic_channel_gray));
        }

        if(page==0)channelDataList.clear();

        if (channelData.size() <= PAGE_SIZE) isLastPage = true;
        channelDataList.addAll(channelData);
        contentAdapter.setData(channelDataList,page==0);
        contentAdapter.notifyDataSetChanged();
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
    public void noData() {
        isLastPage = true;
        if (page!=0) return;

        if (getContext() != null) {

            ivEmpty.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_channel_gray));
        } else {

            ivEmpty.setImageDrawable(
                    AppController.getInstance().getResources().getDrawable(R.drawable.ic_channel_gray));
        }
        llEmpty.setVisibility(View.VISIBLE);
        recyclerViewProfileTab.setVisibility(View.GONE);
    }

    @Override
    public void channelDeleted() {
        channelDataList.clear();
        presenter.getChannelData(0, PAGE_SIZE, userId);
        showMessage("Channel deleted successfully", -1);
    }

    @Override
    public void isLoading(boolean flag) {
        isLoading = flag;
    }

    @Override
    public void showEmptyUi(boolean show) {
        tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onChannelDetail(int position) {
        Intent intent = new Intent(getContext(), TrendingDetail.class);
        intent.putExtra("data", (Serializable) channelDataList.get(position));
        intent.putExtra("call", "profilechannel");
        startActivity(intent);
    }

    @Override
    public void onPostDetail(Data data, int type, View view) {
        List<Data> dataList = new ArrayList<>();
        dataList.add(data);
        Intent intent = new Intent(getContext(), SocialDetailActivity.class);
        intent.putExtra("dataList", (Serializable) dataList);
        startActivity(intent);
    }

    @Override
    public void onDeleteChannel(String channelId) {
        ConfirmDialog dialog = new ConfirmDialog(getContext(), R.drawable.ic_delete, getString(R.string.delete_channel_message), getString(R.string.delete), getString(R.string.cancel));
        Button delete = dialog.findViewById(R.id.btnYes);
        delete.setOnClickListener(v -> {
            presenter.deleteChannel(channelId);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void reload() {
        presenter.getChannelData(0, PAGE_SIZE, userId);
    }
}
