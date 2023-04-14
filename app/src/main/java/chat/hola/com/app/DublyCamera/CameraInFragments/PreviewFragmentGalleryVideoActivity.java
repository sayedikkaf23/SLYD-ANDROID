package chat.hola.com.app.DublyCamera.CameraInFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.DublyCamera.DublyFilterAdapter;
import chat.hola.com.app.DublyCamera.DublyFilterModel;
import chat.hola.com.app.DublyCamera.Filter;
import chat.hola.com.app.DublyCamera.FilterAdapter;
import chat.hola.com.app.DublyCamera.FilterType;
import chat.hola.com.app.DublyCamera.GlBitmapOverlayFilter;
import chat.hola.com.app.DublyCamera.MovieWrapperView;
import chat.hola.com.app.DublyCamera.ProgressBarAdapter;
import chat.hola.com.app.DublyCamera.ProgressBarModel;
import chat.hola.com.app.DublyCamera.QtFastStart;
import chat.hola.com.app.DublyCamera.gpuv.composer.GPUMp4Composer;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlFilterGroup;
import chat.hola.com.app.DublyCamera.gpuv.player.GPUPlayerView;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.OnSwipeTouchListener;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.post.model.Post;
import com.ezcall.android.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PreviewFragmentGalleryVideoActivity extends AppCompatActivity
    implements FilterAdapter.SelectFilterListner {

  private static final String TAG = PreviewFragmentGalleryVideoActivity.class.getSimpleName();

  @BindView(R.id.bottom_sheet)
  View bottomSheet;
  @BindView(R.id.rvFilters)
  RecyclerView rvFilters;
  @BindView(R.id.eT_caption)
  EditText eT_caption;
  @BindView(R.id.scrollView)
  ScrollView scrollView;

  @BindView(R.id.rvProgressBars)
  RecyclerView rvProgressBars;

  @BindView(R.id.flContainer)
  FrameLayout flContainer;

  private ProgressDialog progressDialog;

  private BottomSheetBehavior behavior;
  private View view_filter;
  private MediaPlayer player;

  private int currentlyPlayingVideoPosition = 0;

  private String musicId, audioFile;
  private String path, type;

  private ArrayList<String> recordedVideoFiles;
  private ArrayList<Integer> recordedVideoDurations;

  private List<Filter> filterImages;
  private FilterAdapter filterAdapter;
  private int filter = 0;

  private String folderPath;
  private String appName;

  //For the ability to add effects to the video being played
  private GPUPlayerView gpuPlayerView;
  private SimpleExoPlayer exoPlayer;
  private ExtractorMediaSource.Factory factory;
  private MovieWrapperView movieWrapper;

  private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
  private int screenWidth = displayMetrics.widthPixels;

  private boolean notFirstTime = false;

  //To contain the detail of each video segment
  private ArrayList<ProgressBarModel> progressBarModels;
  private ProgressBarAdapter progressBarAdapter;

  private boolean specificSegmentSelected = false;
  private int cumulativeMusicLengthInMs;

  private boolean filterEnabled;

  private int scaledVideoHeight, scaledVideoWidth;
  /**
   * For ability to add the filters dynamically
   */

  private ArrayList<DublyFilterModel> filterItems;
  private DublyFilterAdapter dublyFilterAdapter;
  private View mFiltersView;
  private android.app.AlertDialog mFilterDialog;

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery_video_preview);
    ButterKnife.bind(this);

    if (!chat.hola.com.app.DublyCamera.ResultHolder.getType().equals("video")) {
      //video
      supportFinishAfterTransition();
    }

    int videoHeight = getIntent().getIntExtra("videoHeight", 10);
    int videoWidth = getIntent().getIntExtra("videoWidth", 10);

    scaledVideoHeight = ((screenWidth * videoHeight) / (videoWidth == 0 ? 1 : videoWidth));

    scaledVideoWidth = screenWidth;
    ViewGroup.LayoutParams params = flContainer.getLayoutParams();

    params.height = scaledVideoHeight;

    flContainer.setLayoutParams(params);

    appName = "Demo";//getResources().getString(R.string.app_name);

    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      imageFolder = new File(getExternalFilesDir(null)
          + "/"
          + getResources().getString(R.string.app_name)
          + "/Media/");
    } else {

      imageFolder =
          new File(getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
    }
    if (!imageFolder.exists() && !imageFolder.isDirectory()) imageFolder.mkdirs();

    movieWrapper = findViewById(R.id.layout_movie_wrapper);

    params = movieWrapper.getLayoutParams();

    params.height = scaledVideoHeight;

    movieWrapper.setLayoutParams(params);

    folderPath = imageFolder.getAbsolutePath();

    recordedVideoFiles = getIntent().getStringArrayListExtra("videoArray");
    recordedVideoDurations = getIntent().getIntegerArrayListExtra("durationArray");

    musicId = getIntent().getStringExtra("musicId");
    audioFile = getIntent().getStringExtra("audio");
    scrollView.setVisibility(
        chat.hola.com.app.DublyCamera.ResultHolder.getCall().equals("story") ? VISIBLE : GONE);

    setUpSimpleExoPlayer();
    setUpGlPlayerView();
    setupFilterView();

    prepareVideoPlaybackFiles(getIntent().getIntExtra("maximumDuration", 60));
    type = Constants.Post.VIDEO;

    path = recordedVideoFiles.get(0);

    filterImages = new ArrayList<>();
    filterImages.add(new Filter(R.drawable.filter0, "", true));
    filterImages.add(new Filter(R.drawable.filter1, "#507FF489", false));
    filterImages.add(new Filter(R.drawable.filter2, "#50dc070e", false));
    filterImages.add(new Filter(R.drawable.filter3, "#503362c1", false));
    filterImages.add(new Filter(R.drawable.filter4, "#50c13378", false));
    filterImages.add(new Filter(R.drawable.filter5, "#5033c144", false));
    filterImages.add(new Filter(R.drawable.filter6, "#50712989", false));

    filterAdapter = new FilterAdapter(this, filterImages, this);
    rvFilters.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvFilters.setAdapter(filterAdapter);
    createVideoColorFilter();
    startPlayback();

    behavior = BottomSheetBehavior.from(bottomSheet);

    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  private Player.EventListener playerEventListener = new Player.EventListener() {
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
      if (playbackState == Player.STATE_ENDED) {

        if (recordedVideoFiles.size() > 0) {

          if (specificSegmentSelected) {

            //Looping through selected video

            int position = fetchPositionOfCurrentlyPlayingVideo();

            addMediaToBePlayed(progressBarModels.get(position).getVideoPath(),
                progressBarModels.get(position).getCumulativeMusicLengthInMs(),
                progressBarModels.get(position));
          } else {
            if (currentlyPlayingVideoPosition < (recordedVideoFiles.size() - 1)) {
              currentlyPlayingVideoPosition += 1;
              int position = fetchPositionOfProgressBarItem(
                  recordedVideoFiles.get(currentlyPlayingVideoPosition));
              addMediaToBePlayed(progressBarModels.get(position).getVideoPath(),
                  progressBarModels.get(position).getCumulativeMusicLengthInMs(),
                  progressBarModels.get(position));
            } else {
              if (player != null) {

                player.pause();
                player.seekTo(0);
                player.start();
              }
              currentlyPlayingVideoPosition = 0;
              addMediaToBePlayed(recordedVideoFiles.get(0), 0, progressBarModels.get(0));
            }
          }
        }
      }
    }
  };

  @Override
  protected void onStop() {

    try {

      releasePlayer();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      if (player != null) {

        if (player.isPlaying()) {

          try {
            player.stop();
          } catch (IllegalStateException e) {
            e.printStackTrace();
          }
        }
        player.release();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      cancelProgressDialog();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnClick(R.id.ivNext)
  public void next() {

    if (checkIfFiltersAppliedOnAnyVideoSegment()) {

      progressDialog = new ProgressDialog(PreviewFragmentGalleryVideoActivity.this);
      progressDialog.setCancelable(false);
      progressDialog.setIndeterminate(false);
      progressDialog.setMax(100);
      progressDialog.setProgress(0);
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progressDialog.show();

      applyVideoFilters();
    } else {
      startNextActivity();
    }
  }

  @OnClick(R.id.ibFilter)
  public void filters() {
    if (filterEnabled) {
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    } else {
      Toast.makeText(PreviewFragmentGalleryVideoActivity.this,
          getString(R.string.select_segment_colorfilter), Toast.LENGTH_SHORT).show();
    }
  }

  @OnClick(R.id.back_button)
  public void back() {
    chat.hola.com.app.DublyCamera.ResultHolder.dispose();
    super.onBackPressed();
  }

  private void createVideoColorFilter() {

    view_filter = findViewById(R.id.swipe_for_filter_video);

    ViewGroup.LayoutParams params = view_filter.getLayoutParams();

    params.height = scaledVideoHeight;

    view_filter.setLayoutParams(params);

    initializeViewForColorFiltersVideo();
  }

  private void initializeViewForColorFiltersVideo() {

    view_filter.setOnTouchListener(
        new OnSwipeTouchListener(PreviewFragmentGalleryVideoActivity.this) {

          public void onSwipeRight() {

            if (filterEnabled) {
              filter++;

              if (filter > 6) {
                filter = 0;
              }

              switchFilterToVideo(filter);
            } else {

              Toast.makeText(PreviewFragmentGalleryVideoActivity.this,
                  getString(R.string.select_segment_colorfilter), Toast.LENGTH_SHORT).show();
            }
          }

          public void onSwipeLeft() {
            if (filterEnabled) {
              filter--;

              if (filter < 0) {
                filter = 6;
              }

              switchFilterToVideo(filter);
            } else {
              Toast.makeText(PreviewFragmentGalleryVideoActivity.this,
                  getString(R.string.select_segment_colorfilter), Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  private void switchFilterToVideo(int filter) {
    for (int i = 0; i < filterImages.size(); i++) {
      filterImages.get(i).setSelected(i == filter);
    }
    filterAdapter.notifyDataSetChanged();

    //Since the color in the final merged video was looking very light,so actual color filter being applied is 3 shades darker(alpha)
    String filterColor = "";

    switch (filter) {
      case 0:
        view_filter.setBackgroundColor(Color.TRANSPARENT);
        filterColor = "";
        break;
      case 1:
        view_filter.setBackgroundColor(Color.parseColor("#507FF489"));
        filterColor = "#807FF489";//1
        break;
      case 2:
        view_filter.setBackgroundColor(Color.parseColor("#50dc070e"));
        filterColor = "#80dc070e";//6
        break;
      case 3:
        view_filter.setBackgroundColor(Color.parseColor("#503362c1"));
        filterColor = "#803362c1"; //2
        break;
      case 4:
        view_filter.setBackgroundColor(Color.parseColor("#50c13378"));
        filterColor = "#80c13378";//5
        break;
      case 5:
        view_filter.setBackgroundColor(Color.parseColor("#5033c144"));
        filterColor = "#8033c144";//4
        break;
      case 6:
        view_filter.setBackgroundColor(Color.parseColor("#50712989"));
        filterColor = "#80712989";//3
        break;
    }
    updateColorFilterSelectedStatus(filterColor);
  }

  @Override
  public void onFilterSelected(int position) {

    filter = position;
    switchFilterToVideo(position);
  }

  private long getDuration(Uri file) {
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(this, file);
    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    long timeInMillisec = Long.parseLong(time);
    retriever.release();
    return timeInMillisec;
  }

  private void startNextActivity() {

    disableBackNavigationCameraActivity();

    Intent intent;
    switch (chat.hola.com.app.DublyCamera.ResultHolder.getCall()) {
      case "story":
        chat.hola.com.app.post.model.PostData postData =
            new chat.hola.com.app.post.model.PostData();
        Post post = new Post();
        PostDb db = new PostDb(this);

        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);
        post.setPathForCloudinary(path);
        post.setTypeForCloudinary(type);
        post.setId(ts);
        post.setFiles(recordedVideoFiles);
        post.setAudioFile(audioFile);
        post.setStory(true);
        if (type.equals(Constants.Post.VIDEO)) post.setDub(true);
        if (musicId != null) post.setMusicId(musicId);

        post.setPrivateStory(false);
        Bundle bundle = new Bundle();
        if (!eT_caption.getText().toString().trim().isEmpty()) {
          bundle.putString("caption", eT_caption.getText().toString().trim());
        }
        if (!type.equals("image")) {
          bundle.putString("duration",
              String.valueOf(getDuration(Uri.parse(recordedVideoFiles.get(0)))));
        }

        postData.setUserId(AppController.getInstance().getUserId());
        postData.setData(new Gson().toJson(post));
        postData.setStatus(0);
        postData.setId(ts);

        postData.setMerged(false);
        db.addData(postData);

        try {

          AppController.getInstance().addNewPost(postData);
        } catch (Exception e) {
          e.printStackTrace();
        }

        break;

      default:
        intent = new Intent(this, PostActivity.class);
        intent.putExtra(Constants.Post.PATH, path);
        intent.putExtra(Constants.Post.TYPE, type);
        intent.putExtra("videoArray", recordedVideoFiles);
        intent.putExtra("audio", audioFile);

        if (musicId != null) intent.putExtra("musicId", musicId);
        startActivity(intent);
    }
    supportFinishAfterTransition();
  }

  private void setUpSimpleExoPlayer() {

    TrackSelector trackSelector = new DefaultTrackSelector();

    // Measures bandwidth during playback. Can be null if not required.
    DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory =
        new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"),
            defaultBandwidthMeter);

    factory = new ExtractorMediaSource.Factory(dataSourceFactory);

    // SimpleExoPlayer
    exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
    // Prepare the player with the source.

    exoPlayer.addListener(playerEventListener);
  }

  private void addMediaToBePlayed(String url, int durationInMs, ProgressBarModel progressBarModel) {

    if (exoPlayer == null) {

      setUpSimpleExoPlayer();
    }

    if (gpuPlayerView == null) {

      setUpGlPlayerView();
    }

    MediaSource mediaSource = factory.createMediaSource(Uri.parse(url));

    exoPlayer.prepare(mediaSource);
    exoPlayer.setPlayWhenReady(true);

    if (progressBarModel.isFilterSelected()) {

      gpuPlayerView.setGlFilter(FilterType.createGlFilter(progressBarModel.getSelectedGlFilter(),
          PreviewFragmentGalleryVideoActivity.this));
    } else {

      gpuPlayerView.setGlFilter(FilterType.createGlFilter(filterItems.get(0).getFilterType(),
          PreviewFragmentGalleryVideoActivity.this));
    }

    if (progressBarModel.getFilterColor().isEmpty()) {

      switchFilterToVideo(0);
    } else {

      switchFilterToVideo(
          fetchColorFilterPosition(Color.parseColor(progressBarModel.getFilterColor())));
    }

    if (player != null) {

      if (specificSegmentSelected) {
        player.pause();
        player.seekTo(durationInMs);
        player.start();
      }
    }
  }

  private void setUpGlPlayerView() {
    gpuPlayerView = new GPUPlayerView(this);
    gpuPlayerView.setSimpleExoPlayer(exoPlayer);
    gpuPlayerView.setLayoutParams(
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    (movieWrapper).addView(gpuPlayerView);
    gpuPlayerView.onResume();
  }

  private void releasePlayer() {

    if (gpuPlayerView != null) {
      gpuPlayerView.onPause();
    }
    (movieWrapper).removeAllViews();
    gpuPlayerView = null;

    if (exoPlayer != null) {
      try {
        exoPlayer.stop();
      } catch (IllegalStateException e) {
        e.printStackTrace();
      }
      exoPlayer.release();
    }
    exoPlayer = null;
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (notFirstTime) {

      startPlayback();
    } else {
      notFirstTime = true;
    }
  }

  private void deselectAllVideoFilters(ProgressBarModel progressBarModel) {

    DublyFilterModel dublyFilterModel;
    for (int i = 0; i < filterItems.size(); i++) {

      dublyFilterModel = filterItems.get(i);

      if (progressBarModel == null) {
        dublyFilterModel.setSelected(false);
      } else {

        if (progressBarModel.getSelectedGlFilter() == dublyFilterModel.getFilterType()) {
          dublyFilterModel.setSelected(true);
        } else {
          dublyFilterModel.setSelected(false);
        }
      }
      filterItems.set(i, dublyFilterModel);
    }

    dublyFilterAdapter.notifyDataSetChanged();
  }

  private void selectCurrentFilter(int position) {
    DublyFilterModel dublyFilterModel;
    for (int i = 0; i < filterItems.size(); i++) {

      dublyFilterModel = filterItems.get(i);
      if (position == i) {
        dublyFilterModel.setSelected(true);
      } else {
        dublyFilterModel.setSelected(false);
      }
      filterItems.set(i, dublyFilterModel);
    }

    dublyFilterAdapter.notifyDataSetChanged();
  }

  private void prepareFilterItems() {

    final List<FilterType> filtersList = FilterType.createFilterList();

    DublyFilterModel dublyFilterModel;
    for (int i = 0; i < filtersList.size(); i++) {

      dublyFilterModel = new DublyFilterModel();
      dublyFilterModel.setFilterId(filtersList.get(i));
      dublyFilterModel.setFilterName(filtersList.get(i).name());
      dublyFilterModel.setSelected(false);
      filterItems.add(dublyFilterModel);
    }
  }

  //TODO
  //Have to replace progressdialog by customview,as set progress doesnt work with spinner style,but shows message,and with horizontal style message doesn't show but setprogress works
  //

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void applyVideoFilter(GlFilter glFilter, String sourceVideoPath, int positionOfVideo,
      boolean firstVideo, Bitmap bitmap, boolean filterSelected, String filterColor) {

    if (filterSelected) {
      if (!filterColor.isEmpty()) {
        //Color has been selected

        GlBitmapOverlayFilter glBitmapOverlaySample = new GlBitmapOverlayFilter(bitmap);

        //                glBitmapOverlaySample.setFrameSize(screenWidth, screenHeight);
        glBitmapOverlaySample.setFrameSize(scaledVideoWidth, scaledVideoHeight);

        glFilter = new GlFilterGroup(glFilter, glBitmapOverlaySample);
      }
    } else {

      GlBitmapOverlayFilter glBitmapOverlaySample = new GlBitmapOverlayFilter(bitmap);

      //            glBitmapOverlaySample.setFrameSize(screenWidth, screenHeight);
      glBitmapOverlaySample.setFrameSize(scaledVideoWidth, scaledVideoHeight);
      glFilter = glBitmapOverlaySample;
    }

    if (progressDialog != null && progressDialog.isShowing()) {

      if (firstVideo) {
        progressDialog.setMessage(getResources().getString(R.string.processing_video));
      } else {

        runOnUiThread(() -> {
          progressDialog.setProgress(0);

          progressDialog.setMessage(
              getResources().getString(R.string.processing_segment) + " " + (positionOfVideo + 1));
        });
      }
    }
    String outputVideoPath;

    File outputFile = new File(folderPath, System.currentTimeMillis() + appName + ".mp4");

    if (!outputFile.exists()) {
      try {
        outputFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    outputVideoPath = outputFile.getAbsolutePath();

    new GPUMp4Composer(sourceVideoPath, outputVideoPath)

        //                .size(screenWidth, screenHeight)
        .size(scaledVideoWidth, scaledVideoHeight).filter(glFilter)

        .listener(new GPUMp4Composer.Listener() {
          @Override
          public void onProgress(double progress) {

            if (progressDialog != null && progressDialog.isShowing()) {

              runOnUiThread(() ->

                  progressDialog.setProgress((int) (progress * 100))

              );
            }
          }

          @Override
          public void onCompleted() {

            if (progressDialog != null && progressDialog.isShowing()) {

              runOnUiThread(() -> progressDialog.setProgress(100));
            }
            deleteFileFromDisk(sourceVideoPath);

            handleApplyFilterResult(outputVideoPath, positionOfVideo);
          }

          @Override
          public void onCanceled() {

          }

          @Override
          public void onFailed(Exception exception) {

            deleteFileFromDisk(outputVideoPath);

            handleApplyFilterResult(null, positionOfVideo);
          }
        }).start();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void deleteFileFromDisk(String filePath) {

    File file = new File(filePath);

    if (file.exists()) {

      file.delete();
    }
  }

  private void handleApplyFilterResult(String outputFilePath, int positionOfVideo) {

    if (outputFilePath != null) {

      recordedVideoFiles.set(positionOfVideo, makeMp4FilesStreamable(outputFilePath));
    }
    positionOfVideo = positionOfVideo + 1;

    if (positionOfVideo < recordedVideoFiles.size()) {

      int positionOfProgressBarItem =
          fetchPositionOfProgressBarItem(recordedVideoFiles.get(positionOfVideo));

      ProgressBarModel progressBarModel = progressBarModels.get(positionOfProgressBarItem);

      if (progressBarModel.isFilterSelected() || !progressBarModel.getFilterColor().isEmpty()) {
        processVideo(progressBarModel, positionOfVideo);
      } else {
        handleApplyFilterResult(null, positionOfVideo);
      }
    } else {

      cancelProgressDialog();
      startNextActivity();
    }
  }

  private void cancelProgressDialog() {

    if (progressDialog != null && progressDialog.isShowing()) {
      runOnUiThread(() -> progressDialog.cancel());
    }
  }

  private void disableBackNavigationCameraActivity() {

    Bus bus = AppController.getBus();
    try {
      JSONObject obj = new JSONObject();

      obj.put("eventName", "killCameraActivity");
      bus.post(obj);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void startPlayback() {
    try {

      if (audioFile != null) {
        player = MediaPlayer.create(PreviewFragmentGalleryVideoActivity.this, Uri.parse(audioFile));
        player.setLooping(false);
        player.start();
      }
      if (!specificSegmentSelected) {
        currentlyPlayingVideoPosition = 0;
        addMediaToBePlayed(progressBarModels.get(0).getVideoPath(), 0, progressBarModels.get(0));
      } else {
        int position =
            fetchPositionOfProgressBarItem(recordedVideoFiles.get(currentlyPlayingVideoPosition));
        addMediaToBePlayed(progressBarModels.get(position).getVideoPath(),
            progressBarModels.get(position).getCumulativeMusicLengthInMs(),
            progressBarModels.get(position));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private String makeMp4FilesStreamable(String inputFilePath) {
    String path = inputFilePath;
    File input = new File(inputFilePath); // Your input file

    File output =
        new File(folderPath, System.currentTimeMillis() + appName + ".mp4"); // Your output file
    try {
      if (!output.exists()) {// if there is no output file we'll create one
        output.createNewFile();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      boolean ret = QtFastStart.fastStart(input, output);

      if (ret) {
        deleteFileFromDisk(inputFilePath);

        path = output.getAbsolutePath();
      }
    } catch (QtFastStart.MalformedFileException | IOException | QtFastStart.UnsupportedFileException e1) {
      e1.printStackTrace();
    }

    return path;
  }

  private void prepareVideoPlaybackFiles(int maximumDuration) {

    progressBarModels = new ArrayList<>();

    progressBarAdapter = new ProgressBarAdapter(this, progressBarModels);
    rvProgressBars.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
    rvProgressBars.suppressLayout(true);
    rvProgressBars.setAdapter(progressBarAdapter);
    ProgressBarModel progressBarModel;

    float multiplier = screenWidth / (maximumDuration == 0 ? 1 : maximumDuration);

    for (int i = 0; i < recordedVideoFiles.size(); i++) {
      if (i != 0) {
        cumulativeMusicLengthInMs += recordedVideoDurations.get(i - 1) * 1000;
      }
      progressBarModel = new ProgressBarModel();

      progressBarModel.setFilterColor("");
      progressBarModel.setFilterSelected(false);
      progressBarModel.setSelectedGlFilter(null);
      progressBarModel.setPlaying(false);
      progressBarModel.setProgressViewWidth((int) (recordedVideoDurations.get(i) * multiplier));
      progressBarModel.setVideoItem(true);
      progressBarModel.setCumulativeMusicLengthInMs(cumulativeMusicLengthInMs);
      progressBarModel.setVideoPath(recordedVideoFiles.get(i));
      progressBarModels.add(progressBarModel);

      //            progressBarModel = new ProgressBarModel();
      //            progressBarModel.setVideoItem(false);
      //
      //            progressBarModels.add(progressBarModel);

    }

    progressBarAdapter.notifyDataSetChanged();

    rvProgressBars.addOnItemTouchListener(
        new RecyclerItemClickListener(PreviewFragmentGalleryVideoActivity.this, rvProgressBars,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                ProgressBarModel progressBarModel = progressBarModels.get(position);

                if (progressBarModel != null) {
                  if (progressBarModel.isVideoItem()) {

                    if (progressBarModel.isPlaying()) {
                      //To check if the specific segment has been selected

                      specificSegmentSelected = false;
                      disableUIStatusOfPlaying();

                      filterEnabled = false;

                      //                            if (filtersList.getVisibility() == VISIBLE) {
                      //                                filtersList.setVisibility(GONE);
                      //
                      //
                      //                            }

                      if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                      }
                    } else {
                      specificSegmentSelected = true;
                      //Have to clear the currently selected color or video filter if any

                      if (progressBarModel.isFilterSelected()) {
                        deselectAllVideoFilters(progressBarModel);
                      } else {
                        deselectAllVideoFilters(null);
                      }
                      updateUIStatusToPlaying(position);

                      filterEnabled = true;
                    }
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, final int position) {

              }
            }));
  }

  private void updateUIStatusToPlaying(int position) {

    ProgressBarModel progressBarModel;
    for (int j = 0; j < progressBarModels.size(); j++) {
      progressBarModel = progressBarModels.get(j);
      if (position == j) {
        progressBarModel.setPlaying(true);
        currentlyPlayingVideoPosition =
            fetchPositionOfRecordedFile(progressBarModel.getVideoPath());

        addMediaToBePlayed(progressBarModel.getVideoPath(),
            progressBarModel.getCumulativeMusicLengthInMs(), progressBarModel);
      } else {
        progressBarModel.setPlaying(false);
      }
      progressBarModels.set(j, progressBarModel);
    }

    progressBarAdapter.notifyDataSetChanged();
  }

  private void disableUIStatusOfPlaying() {

    ProgressBarModel progressBarModel;
    for (int i = 0; i < progressBarModels.size(); i++) {
      progressBarModel = progressBarModels.get(i);
      progressBarModel.setPlaying(false);
      progressBarModels.set(i, progressBarModel);
    }

    progressBarAdapter.notifyDataSetChanged();
  }

  private int fetchPositionOfRecordedFile(String path) {

    for (int j = 0; j < recordedVideoFiles.size(); j++) {

      if (recordedVideoFiles.get(j).equals(path)) {

        return j;
      }
    }

    return 0;
  }

  private int fetchPositionOfProgressBarItem(String path) {

    for (int j = 0; j < progressBarModels.size(); j++) {

      if (progressBarModels.get(j).isVideoItem()) {
        if (progressBarModels.get(j).getVideoPath().equals(path)) {

          return j;
        }
      }
    }
    return 0;
  }

  private int fetchPositionOfCurrentlyPlayingVideo() {

    for (int j = 0; j < progressBarModels.size(); j++) {

      if (progressBarModels.get(j).isVideoItem()) {
        if (progressBarModels.get(j).isPlaying()) {

          return j;
        }
      }
    }
    return 0;
  }

  private int fetchColorFilterPosition(int filterColor) {

    if (filterColor == Color.TRANSPARENT) {

      return 0;
    } else if (filterColor == Color.parseColor("#807FF489")) {

      return 1;
    } else if (filterColor == Color.parseColor("#80dc070e")) {

      return 2;
    } else if (filterColor == Color.parseColor("#803362c1")) {

      return 3;
    } else if (filterColor == Color.parseColor("#80c13378")) {

      return 4;
    } else if (filterColor == Color.parseColor("#8033c144")) {

      return 5;
    } else if (filterColor == Color.parseColor("#80712989")) {

      return 6;
    }
    return 0;
  }

  private void updateFilterSelectedStatus(boolean filterSelected, FilterType selectedGlFilter) {

    //No need to call notifyDatasetChanged(),as UI doesn't needs to be updated
    if (filterSelected) {

      for (int j = 0; j < progressBarModels.size(); j++) {

        if (progressBarModels.get(j).isPlaying()) {

          ProgressBarModel progressBarModel = progressBarModels.get(j);

          progressBarModel.setFilterSelected(true);
          progressBarModel.setSelectedGlFilter(selectedGlFilter);
          progressBarModels.set(j, progressBarModel);
          break;
        }
      }
    } else {

      for (int j = 0; j < progressBarModels.size(); j++) {

        if (progressBarModels.get(j).isPlaying()) {
          ProgressBarModel progressBarModel = progressBarModels.get(j);

          progressBarModel.setFilterSelected(false);
          progressBarModel.setSelectedGlFilter(null);
          progressBarModels.set(j, progressBarModel);
          break;
        }
      }
    }
  }

  private void updateColorFilterSelectedStatus(String filterColor) {
    for (int j = 0; j < progressBarModels.size(); j++) {

      if (progressBarModels.get(j).isPlaying()) {

        ProgressBarModel progressBarModel = progressBarModels.get(j);

        progressBarModel.setFilterColor(filterColor);

        progressBarModels.set(j, progressBarModel);
        break;
      }
    }
  }

  private boolean checkIfFiltersAppliedOnAnyVideoSegment() {

    for (int j = 0; j < progressBarModels.size(); j++) {

      if ((progressBarModels.get(j).isVideoItem()) && (progressBarModels.get(j).isFilterSelected()
          || !progressBarModels.get(j).getFilterColor().isEmpty())) {

        return true;
      }
    }
    return false;
  }

  private void applyVideoFilters() {

    ProgressBarModel progressBarModel;

    for (int j = 0; j < progressBarModels.size(); j++) {

      if ((progressBarModels.get(j).isVideoItem()) && (progressBarModels.get(j).isFilterSelected()
          || !progressBarModels.get(j).getFilterColor().isEmpty())) {

        progressBarModel = progressBarModels.get(j);

        int positionOfVideo = fetchPositionOfRecordedFile(progressBarModel.getVideoPath());

        processVideo(progressBarModel, positionOfVideo);

        break;
      }
    }
  }

  private void processVideo(ProgressBarModel progressBarModel, int positionOfVideo) {
    if (progressBarModel.isFilterSelected()) {

      if (progressBarModel.getFilterColor().isEmpty()) {
        applyVideoFilter(FilterType.createGlFilter(progressBarModel.getSelectedGlFilter(),
            PreviewFragmentGalleryVideoActivity.this), progressBarModel.getVideoPath(),
            positionOfVideo, true, null, true, "");
      } else {

        //Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Bitmap bitmap =
            Bitmap.createBitmap(screenWidth, scaledVideoHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor(progressBarModel.getFilterColor()));

        applyVideoFilter(FilterType.createGlFilter(progressBarModel.getSelectedGlFilter(),
            PreviewFragmentGalleryVideoActivity.this), progressBarModel.getVideoPath(),
            positionOfVideo, true, bitmap, true, progressBarModel.getFilterColor());
      }
    } else {
      if (progressBarModel.getFilterColor().isEmpty()) {
        applyVideoFilter(null, progressBarModel.getVideoPath(), positionOfVideo, true, null, false,
            "");
      } else {

        // Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Bitmap bitmap =
            Bitmap.createBitmap(screenWidth, scaledVideoHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor(progressBarModel.getFilterColor()));

        applyVideoFilter(null, progressBarModel.getVideoPath(), positionOfVideo, true, bitmap,
            false, progressBarModel.getFilterColor());
      }
    }
  }

  /**
   * Filters
   */

  private void setupFilterView() {

    mFiltersView = LayoutInflater.from(this).inflate(R.layout.dialog_preview_filters, null);
    RecyclerView filtersList = mFiltersView.findViewById(R.id.rvFilters);

    mFilterDialog = new android.app.AlertDialog.Builder(this).create();
    mFilterDialog.setOnCancelListener(DialogInterface::dismiss);

    filtersList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    filterItems = new ArrayList<>();

    prepareFilterItems();

    dublyFilterAdapter = new DublyFilterAdapter(this, filterItems,true);

    filtersList.setAdapter(dublyFilterAdapter);
    filtersList.addOnItemTouchListener(
        new RecyclerItemClickListener(PreviewFragmentGalleryVideoActivity.this, filtersList,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (gpuPlayerView != null) {
                  DublyFilterModel filterItem = filterItems.get(position);

                  if (filterItem != null) {

                    if (filterItem.isSelected()) {
                      gpuPlayerView.setGlFilter(
                          FilterType.createGlFilter(filterItems.get(0).getFilterType(),
                              PreviewFragmentGalleryVideoActivity.this));

                      deselectAllVideoFilters(null);

                      updateFilterSelectedStatus(false, null);
                    } else {

                      gpuPlayerView.setGlFilter(
                          FilterType.createGlFilter(filterItem.getFilterType(),
                              PreviewFragmentGalleryVideoActivity.this));
                      selectCurrentFilter(position);

                      updateFilterSelectedStatus(true, filterItem.getFilterType());
                    }
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, final int position) {

              }
            }));
  }

  /**
   * Update filters visibility.
   */
  @OnClick(R.id.selectFilters)
  public void updateFiltersVisibility() {
    if (filterEnabled) {
      showCaptureDialogView(mFilterDialog, mFiltersView);
    } else {

      Toast.makeText(PreviewFragmentGalleryVideoActivity.this,
          getString(R.string.select_segment_videofilter), Toast.LENGTH_SHORT).show();
    }
  }

  /*
   * Show window
   * */
  private void showCaptureDialogView(android.app.AlertDialog dialog, View view) {

    dialog.show();
    dialog.setContentView(view);
    dialog.setCanceledOnTouchOutside(true);
    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    dialog.getWindow().setGravity(Gravity.BOTTOM);
    params.dimAmount = 0.0f;
    dialog.getWindow().setAttributes(params);
    dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
    dialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
  }
}