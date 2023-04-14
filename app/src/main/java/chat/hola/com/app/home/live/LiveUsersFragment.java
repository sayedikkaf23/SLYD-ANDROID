package chat.hola.com.app.home.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.live_stream.Home.LiveBroadcastersAdapter;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerFragment;

/**
 * <h1>LiveUsersActivity</h1>
 * <p>All the live users appears on this screen.</p>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0.
 * @since 26/7/2019.
 */

public class LiveUsersFragment extends DaggerFragment implements LiveUsersContract.View, LiveBroadcastersAdapter.ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    public static int page = 0;

    @BindView(R.id.tvNoBroadcaster)
    TextView tvNoBroadCaster;
    @BindView(R.id.rvLiveBroadcasters)
    RecyclerView onLiveRecyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.appbarLayout)
    AppBarLayout appBarLayout;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager manager;
    @Inject
    LiveUsersPresenter presenter;


    private ArrayList<AllStreamsData> dataStreams = new ArrayList<>();
    private LiveBroadcastersAdapter adapterMainLiveBroadcaster;
    private Bus bus = MQTTManager.getBus();
    private boolean firstTime = true;
    private GridLayoutManager layoutManager;
    private Activity mActivity;

    @Inject
    public LiveUsersFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_broadcaster_list,container,false);
        ButterKnife.bind(this,view);
        presenter.attachView(this);
        appBarLayout.setVisibility(View.GONE);
        int mColumnCount = 2;
        changeVisibilityOfViews();
        layoutManager = new GridLayoutManager(mActivity, mColumnCount);
        onLiveRecyclerView.setLayoutManager(layoutManager);
        adapterMainLiveBroadcaster = new LiveBroadcastersAdapter(mActivity, dataStreams);
        onLiveRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        onLiveRecyclerView.setAdapter(adapterMainLiveBroadcaster);
        adapterMainLiveBroadcaster.setListner(this);
        presenter.allStreamDataRxJava();

        try {

            if (!mqttManager.isMQttConnected()) {

                mqttManager.connectMQttClient(AppController.getInstance().getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkMQttConnection();
        callApi();


        tvNoBroadCaster.setTypeface(typefaceManager.getSemiboldFont());
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApi();
            }
        });
        
        return view;
    }
    
    @OnClick(R.id.btnGoLive)
    public void goLive() {
        startActivity(new Intent(mActivity, DeeparFiltersTabCameraActivity.class).putExtra("isLiveStream", true));
        Objects.requireNonNull(mActivity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!firstTime) {
            callApi();
        } else {
            firstTime = false;
        }
    }

    private void callApi() {
        try {
            presenter.callLiveBroadcaster(0, PAGE_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkMQttConnection() {
        if (mqttManager.isMQttConnected()) {
            subscribeToTopic();
        }
    }

    private void subscribeToTopic() {
        mqttManager.subscribeToTopic(MqttEvents.AllStreams.value + "/", 1);
    }

    @Override
    public void onAllStreamDataReceived(AllStreamsData dataStream) {
        handleAllStreamsData(dataStream);
    }

    @Override
    public void setData(AllStreamsData dataStream) {
        boolean isAdded = false;
        for(int i=0; i<dataStreams.size(); i++){
            if(dataStreams.get(i).getStreamId()!=null
                    && dataStreams.get(i).getStreamId().equals(dataStream.getStreamId())){
                isAdded = true;
                break;
            }
        }
        if(!isAdded)
            dataStreams.add(dataStream);

        tvNoBroadCaster.setVisibility(View.GONE);
        onLiveRecyclerView.setVisibility(View.VISIBLE);
//        adapterMainLiveBroadcaster.setDataStream(dataStreams);
        adapterMainLiveBroadcaster.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void liveBroadCasterData(ArrayList<AllStreamsData> streams) {
        dataStreams.clear();
        dataStreams.addAll(streams);

        if (streams.size() > 0) {
            tvNoBroadCaster.setVisibility(View.GONE);
            onLiveRecyclerView.setVisibility(View.VISIBLE);
            adapterMainLiveBroadcaster.notifyDataSetChanged();
        } else {
            tvNoBroadCaster.setVisibility(View.VISIBLE);
            onLiveRecyclerView.setVisibility(View.GONE);
        }
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Subscribe
    public void getMessage(JSONObject object) {

        try {
            if (object.getString("eventName").equals("connect")) {
                subscribeToTopic();
            } else if (object.getString("eventName").equals("disconnect")) {
                Log.i("", "getMessage: " + "disconnected");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handleAllStreamsData(AllStreamsData dataStream) {
        if (dataStream.getAction().equals("start")) {
            if (!dataStream.getUserId().equals(manager.getUserId())) {
                presenter.following(dataStream);
            }
        } else if (dataStream.getAction().equals("stop") || dataStream.getAction().equals("wait")) {
            if (dataStreams.size() > 0) {
                for (int i = 0; i < dataStreams.size(); i++) {
                    if (dataStream.getStreamId().equals(dataStreams.get(i).getId())) {
                        dataStreams.remove(dataStreams.get(i));
                        adapterMainLiveBroadcaster.notifyDataSetChanged();
                        if (dataStreams.size() > 0) {
                            tvNoBroadCaster.setVisibility(View.GONE);
                            onLiveRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            tvNoBroadCaster.setVisibility(View.VISIBLE);
                            onLiveRecyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void follow(boolean isChecked, String userId) {
        if (!isChecked)
            presenter.unfollow(userId);
        else
            presenter.follow(userId);
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };

    public void changeVisibilityOfViews() {
        ((LandingActivity) mActivity).visibleActionBar();
        ((LandingActivity) mActivity).setTitle("Live",typefaceManager.getMediumFont());
        ((LandingActivity) mActivity).ivProfilePic.setVisibility(View.VISIBLE);
        ((LandingActivity) mActivity).iV_plus.setVisibility(View.GONE);
        ((LandingActivity) mActivity).removeFullScreenFrame();
        ((LandingActivity) mActivity).linearPostTabs.setVisibility(View.GONE);
//        ((LandingActivity) mActivity).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((LandingActivity)mActivity).tvCoins.setVisibility(View.GONE);
        ((LandingActivity)mActivity).tvSearch.setVisibility(View.GONE);
        ((LandingActivity)mActivity).ivLiveStream.setVisibility(View.GONE);
    }
}
