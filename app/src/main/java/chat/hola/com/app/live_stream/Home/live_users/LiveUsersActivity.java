package chat.hola.com.app.live_stream.Home.live_users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.CameraActivity;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.live_stream.Home.LiveBroadcastersAdapter;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>LiveUsersActivity</h1>
 * <p>All the live users appears on this screen.</p>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0.
 * @since 26/7/2019.
 */

public class LiveUsersActivity extends DaggerAppCompatActivity implements LiveUsersContract.View, LiveBroadcastersAdapter.ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    public static int page = 0;
    boolean fromLandingActivity;
    @BindView(R.id.tvNoBroadcaster)
    TextView tvNoBroadCaster;
    @BindView(R.id.rvLiveBroadcasters)
    RecyclerView onLiveRecyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager manager;
    @Inject
    LiveUsersContract.Presenter presenter;


    private ArrayList<AllStreamsData> dataStreams = new ArrayList<>();
    private LiveBroadcastersAdapter adapterMainLiveBroadcaster;
    private Bus bus = MQTTManager.getBus();
    private boolean firstTime = true;
    private GridLayoutManager layoutManager;
    private List<String> streamIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_broadcaster_list);
        ButterKnife.bind(this);
        fromLandingActivity = getIntent().getBooleanExtra("fromLandingActivity", false);
        int mColumnCount = 2;
        layoutManager = new GridLayoutManager(this, mColumnCount);
        onLiveRecyclerView.setLayoutManager(layoutManager);
        adapterMainLiveBroadcaster = new LiveBroadcastersAdapter(this, dataStreams);
        onLiveRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        onLiveRecyclerView.setAdapter(adapterMainLiveBroadcaster);
        adapterMainLiveBroadcaster.setListner(this);
        presenter.allStreamDataRxJava();

        bus.register(this);
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
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (fromLandingActivity) {
            startActivity(new Intent(this, LandingActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btnGoLive)
    public void goLive() {
        startActivity(new Intent(this, CameraActivity.class).putExtra("isLiveStream", true));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
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

        streamIds.clear();
        for (AllStreamsData data : dataStreams) {
            streamIds.add(data.getStreamId());
        }
        if (!streamIds.contains(dataStream.getStreamId()))
            dataStreams.add(dataStream);

        tvNoBroadCaster.setVisibility(View.GONE);
        onLiveRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
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
}
