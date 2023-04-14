package chat.hola.com.app.live_stream.Home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.live_stream.Home.dummy.DummyContent;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BroadcastersFragment extends DaggerFragment implements BroadcastersPresenterContract.BroadcastView {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;

    @BindView(R.id.tvNoBroadcaster)
    TextView tvNoBroadCaster;
    @BindView(R.id.rvLiveBroadcasters)
    RecyclerView onLiveRecyclerView;

    private ArrayList<AllStreamsData> dataStreams = new ArrayList<>();
    private LiveBroadcastersAdapter adapterMainLiveBroadcaster;


    @Inject
    TypefaceManager appTypeface;

    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager manager;

    //    @Inject
//    BroadcastersPresenterImpl.broadCastPresenter presenter;

    @Inject
    BroadcastersPresenterContract.BroadcastPresenter presenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    @Inject
    public BroadcastersFragment() {

    }

    private Bus bus = MQTTManager.getBus();

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BroadcastersFragment newInstance(int columnCount) {
        BroadcastersFragment fragment = new BroadcastersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    private boolean firstTime = true;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcaster_list, container, false);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            onLiveRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            onLiveRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapterMainLiveBroadcaster = new LiveBroadcastersAdapter(context, dataStreams);
        onLiveRecyclerView.setAdapter(adapterMainLiveBroadcaster);

        presenter.allStreamDataRxJava();

        checkMQttConnection();

        callApi();
        bus.register(this);
        tvNoBroadCaster.setTypeface(appTypeface.getSemiboldFont());
        return view;
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
            presenter.callLiveBroadcaster();

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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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
    }

    @Override
    public void showAlert(String message) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    @Subscribe
    public void getMessage(JSONObject object) {

        try {


            if (object.getString("eventName").equals("connect")) {

                subscribeToTopic();
            } else if (object.getString("eventName").equals("disconnect")) {


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        bus.unregister(this);
    }


    private void handleAllStreamsData(AllStreamsData dataStream) {


        if (dataStream.getAction().equals("start")) {

            if (!dataStream.getUserId().equals(manager.getUserId())) {
                dataStreams.add(dataStream);
                tvNoBroadCaster.setVisibility(View.GONE);
                onLiveRecyclerView.setVisibility(View.VISIBLE);
                adapterMainLiveBroadcaster.notifyDataSetChanged();

            }

        } else if (dataStream.getAction().equals("stop")) {
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
}
