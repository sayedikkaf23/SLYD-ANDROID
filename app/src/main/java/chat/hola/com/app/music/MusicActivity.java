package chat.hola.com.app.music;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

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
import chat.hola.com.app.Utilities.CountFormat;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>HashtagActivity</h1>
 * <p>Get app list of dubbed music post</p>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/1/20119.
 */

public class MusicActivity extends DaggerAppCompatActivity
        implements MusicContract.View, SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener {

    private Unbinder unbinder;
    public static String musicId;
    public static String categoryId;
    private boolean isShow = true;
    private int scrollRange = -1;
    private String title;
    private String artist;
    private String musicUrl;
    private GridLayoutManager layoutManager;
    private MediaPlayer player;
    private int downloadedSize;
    public static String hashtag;
    private String totalPosts;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    MusicContract.Presenter presenter;
    @Inject
    MusicAdapter adapter;
    @Inject
    BlockDialog dialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivMusicPhoto)
    ImageView ivMusicPhoto;
    //    @BindView(R.id.ibPlay)
//    CheckBox ibPlay;
    @BindView(R.id.tvMusicName)
    TextView tvMusicName;
    @BindView(R.id.tvMusicCount)
    TextView tvMusicCount;
    //    @BindView(R.id.tvAddToFav)
//    TextView tvAddToFav;
    @BindView(R.id.ivBookmark)
    ImageView ivBookmark;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.rvDubList)
    RecyclerView rvDubList;
    @BindView(R.id.btnRecord)
    Button btnRecord;

    @BindView(R.id.htab_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.htab_appbar)
    AppBarLayout appBarLayout;
    //    @BindView(R.id.btnAddToFav)
//    LinearLayout btnAddToFav;
    @BindView(R.id.tvArtist)
    TextView tvArtist;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivBackground)
    ImageView ivBackground;
    String from = "";

    //private boolean titleSetRequired = false;
    //private boolean titleHideRequired = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity);
        unbinder = ButterKnife.bind(this);
        categoryId = getIntent().getStringExtra("categoryId");
        musicId = getIntent().getStringExtra("musicId");
        artist = getIntent().getStringExtra("artist");
        hashtag = getIntent().getStringExtra("hashtag");
        totalPosts = getIntent().getStringExtra("totalPosts");
        from = getIntent().getStringExtra("from");

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (musicId == null) changeTitle(verticalOffset <= -271);
        });
        initialize();
    }

    private void changeTitle(boolean b) {
        runOnUiThread(() -> tvTitle.setText(b ? hashtag : ""));
    }

    private void initialize() {
        //setup toolbar
        toolbarSetup();

//        btnAddToFav.setVisibility(musicId == null ? View.GONE : View.VISIBLE);
//        ibPlay.setVisibility(musicId == null ? View.GONE : View.VISIBLE);

        player = new MediaPlayer();

        //set fonts
        tvMusicName.setTypeface(typefaceManager.getSemiboldFont());
        tvMusicCount.setTypeface(typefaceManager.getRegularFont());
        tvMusicName.setTypeface(typefaceManager.getRegularFont());

        //setup recycler view
        refresh.setOnRefreshListener(this);
        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setItemPrefetchEnabled(true);
        rvDubList.setLayoutManager(layoutManager);
        adapter.setClickListner(presenter.getPresenter());
        rvDubList.addOnScrollListener(recyclerViewOnScrollListener);
        rvDubList.setAdapter(adapter);

        btnRecord.setVisibility(musicId==null ? View.GONE : View.VISIBLE);

        if (musicId != null) {
            loadData();
        } else if (categoryId != null) {
            loadCategoryPost();
        } else {
            loadHashTag(hashtag);
        }

//        rvDubList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    fabRecord.hide();
//                } else {
//                    if (musicId != null) fabRecord.show();
//                }
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
    }

    private void loadHashTag(String hashtag) {
        loading(true);
        presenter.getHashtag(hashtag, 0, Constants.PAGE_SIZE, from);
    }

    //    @OnClick(R.id.fabRecord)
    @OnClick(R.id.btnRecord)
    public void record() {
        startDownload(musicUrl);
    }

//    @OnClick(R.id.ibPlay)
//    public void item() {
//        if (ibPlay.isChecked()) {
//            play(musicUrl);
//        } else {
//            stop();
//        }
//    }

    private void toolbarSetup() {
        setSupportActionBar(toolbar);
        appBarLayout.addOnOffsetChangedListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void loadCategoryPost() {
        loading(true);
        presenter.getCategoryPost(categoryId, 0, Constants.PAGE_SIZE);
    }

    private void loadData() {
        loading(true);
        presenter.getMusicData(musicId, 0, Constants.PAGE_SIZE);
    }

//    @OnClick(R.id.btnAddToFav)
//    public void addToFav() {
//        setToSelect(!ivBookmark.isSelected());
//        presenter.favorite(musicId, ivBookmark.isSelected());
//    }

//    private void setToSelect(boolean isSelected) {
//        ivBookmark.setSelected(isSelected);
//        tvAddToFav.setSelected(isSelected);
//        tvAddToFav.setText(isSelected ? R.string.added_to_fav : R.string.add_to_fav);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        stop();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void loading(boolean isLoading) {
        if (refresh != null) refresh.setRefreshing(isLoading);
    }

    @Override
    public void showDetail(int position, List<Data> data) {
        Intent intent = new Intent(this, SocialDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("dataList", (Serializable) data);
        startActivity(intent);
    }

    @Override
    public void setData(Long totalPosts, Dub data) {
        if (musicId != null && !isFinishing()) {
//            if (fabRecord != null) fabRecord.setVisibility(View.VISIBLE);

            try {
                Glide.with(this)
                        .load(Utilities.getModifiedImageLink(data.getImageUrl()))
                        .asBitmap().centerCrop()
                        .placeholder(R.drawable.ic_default)
                        .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .into(new BitmapImageViewTarget(ivMusicPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivMusicPhoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (IllegalArgumentException ignored) {
            }

            musicUrl = data.getPath();
            title = data.getName();
            tvMusicName.setText(title);
            String count = CountFormat.format(Long.parseLong(String.valueOf(data.getTotalVideos()))) + getString(R.string.space) + (
                    data.getTotalVideos() > 1 ? getResources().getString(R.string.videos).toLowerCase()
                            : getResources().getString(R.string.video).toLowerCase());
            tvMusicCount.setText(count);

            if (artist != null) {
                tvArtist.setVisibility(View.VISIBLE);
                tvArtist.setText(artist);
            }
//            setToSelect(data.isMyFavourite() == 1);
        } else {

            if (!isFinishing()) {
                ivMusicPhoto.setImageDrawable(getDrawable(R.drawable.ic_def_hashtag));
                tvMusicName.setText(hashtag);
                if (this.totalPosts != null) {
                    String count = CountFormat.format(totalPosts) + " " + (totalPosts > 1 ? "Posts" : "Post");
                    tvMusicCount.setText(count);
                } else if (totalPosts != null) {
                    String count = CountFormat.format(totalPosts) + " " + (totalPosts > 1 ? "Posts" : "Post");
                    tvMusicCount.setText(count);
                }
            }
        }
    }

    @Override
    public void setImage(String image) {
        if (image != null && !image.isEmpty()) {
            try {
                Glide.with(this)
                        .load(image)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .fitCenter()
                        .placeholder(getResources().getDrawable(R.drawable.ic_def_hashtag))
                        .into(ivMusicPhoto);

                Glide.with(this)
                        .load(image)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .fitCenter()
                        .into(ivBackground);
            } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msgId == -1 ? msg : getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        if (sessionManager != null) sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
    }

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    public void reload() {
        loadData();
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

                    if (musicId != null) {
                        presenter.callApiOnScroll(musicId, firstVisibleItemPosition, visibleItemCount,
                                totalItemCount, from);
                    } else if (categoryId != null) {
                        presenter.callApiOnScroll(categoryId, firstVisibleItemPosition, visibleItemCount,
                                totalItemCount, from);
                    } else {
                        presenter.callApiOnScroll(hashtag, firstVisibleItemPosition, visibleItemCount,
                                totalItemCount, from);
                    }
                }
            };

    @Override
    public void onRefresh() {
        reload();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
            toolbar.setTitle(title);
            collapsingToolbarLayout.setTitle(title);
            isShow = true;
        } else if (isShow) {
            toolbar.setTitle(" ");
            collapsingToolbarLayout.setTitle(
                    " ");//careful there should a space between double quote otherwise it wont work
            isShow = false;
        }
    }

    //play audio
    private void play(String audio) {
        stop();
        try {
            if (player == null) player = new MediaPlayer();

            audio = audio.substring(0, audio.length() - 3) + "mp3";
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(AppController.getInstance(), Uri.parse(audio));

            player.setOnPreparedListener(player -> player.start());
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //stop playing audio
    private void stop() {
        if (player != null) {
            try {

                if (player.isPlaying()) {
                    player.stop();
                    player.reset();
                }
                player.release();
                player = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startDownload(String url) {
        //        progresbar.setVisibility(View.VISIBLE);
        new Thread(() -> downloadFile(url)).start();
    }

    private void downloadFile(String filePath) {

        try {

            URL url = new URL(filePath);
            InputStream is = url.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            String filename = filePath.substring(filePath.lastIndexOf('/') + 1);
            FileOutputStream fos =
                    new FileOutputStream(new File(getExternalFilesDir(null) + "/" + filename));

            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
                downloadedSize += length;
                // update the progressbar //
                //                progress(downloadedSize);
            }
            //close the output stream when complete //
            fos.close();

            runOnUiThread(() -> finishedDownload(getExternalFilesDir(null) + "/" + filename, title));
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

    private void finishedDownload(String path, String filename) {
        //        progresbar.setVisibility(View.GONE);
        stop();
        Intent intent = new Intent(this, DeeparFiltersTabCameraActivity.class);
        intent.putExtra("musicId", musicId).
                putExtra("audio", path).putExtra("name", filename).putExtra("isRecord", true);
        startActivity(intent);
        finish();
    }

    String showError(final String err) {

        return err;
    }
}
