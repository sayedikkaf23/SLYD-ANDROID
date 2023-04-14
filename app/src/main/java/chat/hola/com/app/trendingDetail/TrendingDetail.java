package chat.hola.com.app.trendingDetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.Purchase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.model.ContentAdapter;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.Location;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;
import chat.hola.com.app.search.SearchActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.trendingDetail.model.TrendingAdapter;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * <h>TrendingDetail.class</h>
 * <p>This is Activity is used to show trendingPost in detail
 * (it uses {@link TrendingAdapter} class with recyclerView) initiated
 * by {@link ContentAdapter}.</p>
 *
 * @author 3Embed
 * @version 1.0
 * @since 14/2/18.
 */

public class TrendingDetail extends DaggerAppCompatActivity implements TrendingDtlContract.View, OnMapReadyCallback, SandriosCamera.CameraCallback {
    private GoogleMap googleMap;
    private Unbinder unbinder;
    private ChannelData data;
    private String call;
    private boolean isMine = false;
    private String channelId;

    @Inject
    TrendingDtlPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    TrendingAdapter trendingDtlAdapter;
    @Inject
    SessionManager sessionManager;
    @Inject
    PostObserver postObserver;

    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.profilePicIv)
    ImageView profilePicIv;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivImageTop)
    ImageView ivImageTop;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubscribers)
    TextView tvSubscriber;
    @BindView(R.id.tbSubscribe)
    ToggleButton tbSubscribe;
    @BindView(R.id.recyclerTrendingDetail)
    RecyclerView recyclerDetail;
    @BindView(R.id.tvPrivateMessage)
    TextView tvPrivateMessage;

    @BindView(R.id.flMap)
    FrameLayout flMap;
    @BindView(R.id.llSubscribe)
    LinearLayout llSubscribe;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.ivEmpty)
    ImageView ivEmpty;
    @BindView(R.id.mapOverlay)
    View mapOverlay;
    @Inject
    BlockDialog dialog;

    @BindView(R.id.llMusicContainer)
    LinearLayout llMusicContainer;
    @BindView(R.id.ivMusic)
    ImageView ivMusic;
    @BindView(R.id.tvSound)
    TextView tvSound;
    @BindView(R.id.tvDubs)
    TextView tvDubs;
    @BindView(R.id.cbLike)
    CheckBox cbLike;
    @BindView(R.id.postVideo)
    FloatingActionButton postVideo;
    private String musicPath;
    int subscribers = 0;
    private String musicId;
    private boolean showMap = false;
    private Bus bus = AppController.getBus();
    private Location location;
    private String placeId;
    private GridLayoutManager layoutManager;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_detail);
        unbinder = ButterKnife.bind(this);
        bus.register(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        placeId = getIntent().getStringExtra("placeId");
        musicPath = getIntent().getStringExtra("musicPath");
        musicId = getIntent().getStringExtra("musicId");
        location = (Location) getIntent().getSerializableExtra("latlong");
        llNetworkError.setErrorListner(this);
        presenter.init();
        initilizeMap();

        postObserver.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                presenter.init();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("postRefresh")) {
                presenter.init();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            SupportMapFragment supportMapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void initDetailRecycler() {
        try {
            layoutManager = new GridLayoutManager(this, 3);
            recyclerDetail.setLayoutManager(layoutManager);
            //recyclerDetail.setHasFixedSize(true);
            //recyclerDetail.setNestedScrollingEnabled(false);
            recyclerDetail.addOnScrollListener(recyclerViewOnScrollListener);
            trendingDtlAdapter.setListener(presenter);
            recyclerDetail.setAdapter(trendingDtlAdapter);
            call = getIntent().getStringExtra("call");
            if (call.equals("category")) setTitle(getIntent().getStringExtra("category"));
            presenter.selectType(getIntent());
        } catch (Exception ignored) {
        }
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition =
                            ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    presenter.callApiOnScroll(musicId, firstVisibleItemPosition, visibleItemCount,
                            totalItemCount);
                }
            };

    @Override
    public void showData(ChannelData data, String text, int drawable, int onText, int offText,
                         boolean isChecked) {
        try {

            this.data = data;
            isMine = AppController.getInstance().getUserId().equals(data.getUserId());

            if (drawable != -1) {
                ivImageTop.setImageResource(drawable);
                setTitle(text);
            } else {

                Glide.with(this)
                        .load(Utilities.getModifiedImageLink(data.getChannelImageUrl()))
                        .asBitmap()
                        .centerCrop()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(ivImageTop) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivImageTop.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                setTitle(data.getChannelName());
            }

            channelId = data.getId();
            if (data.getData().isEmpty()) {
                noData();
            } else if (call.equals("channel") || call.equals("profilechannel")) {
                setData(data, onText, offText, isChecked);
                subscribers = data.getTotalSubscribers();
                String subText;
                if (subscribers < 2) {
                    subText = " Subscriber";
                } else {
                    subText = " Subscribers";
                }

                tvSubscriber.setText(subscribers + subText);
            } else if (call.equals("music")) {
                musicPath = data.getMusicData().getPath();
                llSubscribe.setVisibility(View.GONE);
                llMusicContainer.setVisibility(View.VISIBLE);
                //  postVideo.setVisibility(View.VISIBLE);

                Glide.with(this)
                        .load(Utilities.getModifiedImageLink(data.getMusicData().getImageUrl()))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .placeholder(getResources().getDrawable(R.drawable.ic_default))
                        .into(ivMusic);
                tvSound.setText(data.getMusicData().getName());
                int totalVideos = data.getMusicData().getTotalVideos();
                tvDubs.setText(totalVideos + " " + getString(R.string.videos).toLowerCase());
                //cbLike.setChecked(data.getMusicData().isMyFavourite() == 1);
            }
            try {
                tbSubscribe.setTextOff(getString(offText));
                tbSubscribe.setTextOn(getString(onText));
                tbSubscribe.setChecked(isChecked);
            } catch (Exception ignored) {
            }

            if (call.equals("category")) setTitle(getIntent().getStringExtra("category"));
        } catch (NullPointerException ignored) {

        } catch (Exception ignored) {

        }
    }

    @OnClick(R.id.postVideo)
    public void postVideo() {
        downloadFile();
    }

    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) tvTitle.setText(title);
    }

    private void noData() {
        recyclerDetail.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
        switch (call) {
            case "category":
                ivEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_media));
                break;
            case "hashtag":
                ivEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_hashtag));
                break;
            case "location":
                ivEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_location));
                break;
            case "music":
                ivEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_media));
            default:
                ivEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_channel_gray));
                break;
        }
    }

    @Override
    public void invalidateSubsButton(boolean isChecked) {
        data.setSubscribed(isChecked ? 1 : 0);
        String subText;
        if (subscribers < 2) {
            subText = " Subscriber";
        } else {
            subText = " Subscribers";
        }
        if (isChecked) subscribers++;

        tvSubscriber.setText(subscribers + subText);
        tbSubscribe.setChecked(isChecked);
    }

    @Override
    public void invalidateUnSubsButton(boolean isChecked) {
        data.setSubscribed(!isChecked ? 1 : 0);
        String subText;
        if (subscribers < 2) {
            subText = " Subscriber";
        } else {
            subText = " Subscribers";
        }
        if (isChecked && subscribers > 0) subscribers--;

        tvSubscriber.setText(subscribers + subText);
        tbSubscribe.setChecked(!isChecked);
    }

    @Override
    public void applyingFont() {
        try {
            tvTitle.setTypeface(typefaceManager.getSemiboldFont());
            tvSubscriber.setTypeface(typefaceManager.getMediumFont());
            tbSubscribe.setTypeface(typefaceManager.getMediumFont());
        } catch (Exception ignore) {
        }
    }

    @OnClick(R.id.ivBack)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.ivCamera)
    public void camera() {
        SandriosCamera.with(this)
                .setShowPicker(true)
                .setVideoFileSize(20)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                .enableImageCropping(true)
                .launchCamera(this);
    }

    @OnClick(R.id.ivSearch)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profilePicIv)
    public void profile() {
        startActivity(new Intent(TrendingDetail.this, ProfileActivity.class));
    }

    @OnClick(R.id.tbSubscribe)
    public void subscribe() {
        if (tbSubscribe.isChecked()) {
            presenter.subscribeChannel(channelId);
        } else {
            presenter.unSubscribeChannel(channelId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showMessage(String msg, int msgId) {
    }

    @Override
    public void itemClick(int position, View view, List<Data> data) {
        String transitionName = getString(R.string.transition);
        Intent intent = new Intent(this, SocialDetailActivity.class);
        intent.putExtra("data", data.get(position));
        intent.putExtra("dataList", (Serializable) data);
        intent.putExtra("position", position);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, transitionName);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void tbSubscribeVisibility(boolean isVisible) {
        tbSubscribe.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void llSubscribeVisibility(boolean isVisible) {
        llSubscribe.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void mapVisibility(boolean isVisible) {
        showMap = isVisible;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(BuildConfig.MAP_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(BuildConfig.MAP_BUNDLE_KEY, mapViewBundle);
        }
        //   mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  mapView.onStop();
    }

    @Override
    protected void onPause() {
        //  mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //    mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) unbinder.unbind();
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setData(ChannelData data, int onText, int offText, boolean isChecked) {
        boolean isPrivate = data.getPrivate();
        tvPrivateMessage.setText(data.isSubscribed() == 2 ? getString(R.string.already_requested) : "");
        tbSubscribe.setVisibility(isMine ? View.GONE : View.VISIBLE);
        recyclerDetail.setVisibility((isPrivate && !isMine) ? View.GONE : View.VISIBLE);
        tvPrivateMessage.setVisibility(isPrivate && !isMine ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onComplete(CameraOutputModel model) {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(Constants.Post.PATH, model.getPath());
        intent.putExtra(Constants.Post.TYPE,
                model.getType() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
        startActivity(intent);
    }

    @Override
    public void reload() {
        presenter.init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        flMap.setVisibility(showMap ? View.VISIBLE : View.GONE);
        if (location != null) setLocation(location);
    }

    @Override
    public void setLocation(Location location) {
        googleMap.setMinZoomPreference(15);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mapOverlay.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + latLng.latitude + "," + latLng.longitude + " ( )"));
            startActivity(intent);
        });
    }

    private void downloadFile() {

        try {
            String filePath = musicPath;

            URL url = new URL(musicPath);
            InputStream is = url.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            String filename = filePath.substring(filePath.lastIndexOf('/') + 1);
            FileOutputStream fos =
                    new FileOutputStream(new File(getExternalFilesDir(null) + "/" + filename));

            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);

                //downloadedSize += length;
                // update the progressbar //
                //view.progress(downloadedSize);
            }
            //close the output stream when complete //
            fos.close();

            //start camera activity
            startActivity(new Intent(this, DeeparFiltersTabCameraActivity.class).putExtra("audio",
                    getExternalFilesDir(null) + "/" + filename)
                    .putExtra("name", filename)
                    .putExtra("isRecord", true)
                    .putExtra("musicId", musicId));
            finish();
        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    String showError(final String err) {
        Log.e("Error", err);
        return err;
    }
}
