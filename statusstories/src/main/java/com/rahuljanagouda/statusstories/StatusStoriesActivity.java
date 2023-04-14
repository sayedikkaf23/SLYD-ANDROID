package com.rahuljanagouda.statusstories;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.rahuljanagouda.statusstories.glideProgressBar.ProgressTarget;
import java.util.Locale;

public class StatusStoriesActivity extends AppCompatActivity
    implements StoryStatusView.UserInteractionListener {

  public static final String STATUS_RESOURCES_KEY = "statusStoriesResources";
  public static final String STATUS_DURATION_KEY = "statusStoriesDuration";
  public static final String STATUS_DURATIONS_ARRAY_KEY = "statusStoriesDurations";
  public static final String STATUS_MEDIA_TYPE = "statusStoriesMediaType";
  public static final String STATUS_STORY_ID = "statusStoryId";
  public static final String STATUS_IS_MY_STORY = "isMyStory";
  public static final String STATUS_VIEW_COUNT = "statusViewCount";
  public static final String STATUS_VIEWER_LIST = "statusViewerList";
  public static final String IS_IMMERSIVE_KEY = "isImmersive";
  public static final String IS_CACHING_ENABLED_KEY = "isCaching";
  public static final String IS_TEXT_PROGRESS_ENABLED_KEY = "isText";

  private static StoryStatusView storyStatusView;
  private ImageView image;
  private int counter = 0;

  private String[] statusResources, mediaType, storyIds, viewCounts;
  //    private long[] statusResourcesDuration;
  private long statusDuration;
  private boolean isImmersive = true;
  private boolean isCaching = true;
  private static boolean isTextEnabled = true;
  private ProgressTarget<String, Bitmap> target;
  private VideoView videoView;
  private long[] statusResourcesDuration;
  int stopPosition;
  private TextView tVAddCaption;
  private boolean isMyStory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_status_stories);

    statusResources = getIntent().getStringArrayExtra(STATUS_RESOURCES_KEY);
    statusDuration = getIntent().getLongExtra(STATUS_DURATION_KEY, 3000L);
    statusResourcesDuration = getIntent().getLongArrayExtra(STATUS_DURATIONS_ARRAY_KEY);
    mediaType = getIntent().getStringArrayExtra(STATUS_MEDIA_TYPE);
    storyIds = getIntent().getStringArrayExtra(STATUS_STORY_ID);
    isImmersive = getIntent().getBooleanExtra(IS_IMMERSIVE_KEY, true);
    isCaching = getIntent().getBooleanExtra(IS_CACHING_ENABLED_KEY, true);
    isTextEnabled = getIntent().getBooleanExtra(IS_TEXT_PROGRESS_ENABLED_KEY, true);

    ProgressBar imageProgressBar = findViewById(R.id.imageProgressBar);
    TextView textView = findViewById(R.id.textView);
    image = findViewById(R.id.image);
    videoView = findViewById(R.id.videoView);
    tVAddCaption = findViewById(R.id.tVAddCaption);

    storyStatusView = findViewById(R.id.storiesStatus);

    // or
    storyStatusView.setStoriesCountWithDurations(statusResourcesDuration);
    storyStatusView.setUserInteractionListener(this);
    storyStatusView.playStories();

    target = new MyProgressTarget<>(new BitmapImageViewTarget(image), imageProgressBar, textView);
    image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        storyStatusView.skip();
      }
    });

    pauseStoryView();
    if (!mediaType[counter].equals("2")) {
      image.setVisibility(View.VISIBLE);
      videoView.setVisibility(View.GONE);

      setImageView(image, statusResources[counter]);
    } else {
      videoView.setVisibility(View.VISIBLE);
      image.setVisibility(View.GONE);
      setVideoView(videoView, statusResources[counter], counter);

      //videoView.setVideoPath(statusResources[counter]);
      //videoView.start();

    }

    // bind reverse view
    findViewById(R.id.reverse).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        storyStatusView.reverse();
      }
    });

    // bind skip view
    findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        storyStatusView.skip();
      }
    });

    findViewById(R.id.actions).setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {

        if (mediaType[counter].equals("2")) return false;

        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
          storyStatusView.pause();
        }
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
          storyStatusView.resume();
        }
        return true;
      }
    });
  }

  private void setVideoView(final VideoView videoView, final String statusResource,
      final int counter) {

    pauseStoryView();
    videoView.setVideoPath(statusResource);
    videoView.start();

    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
      @Override
      public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
          case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {

            storyStatusView.resume();
            return true;
          }
          case MediaPlayer.MEDIA_INFO_BUFFERING_START: {

            storyStatusView.pause();
            return true;
          }
          case MediaPlayer.MEDIA_INFO_BUFFERING_END: {

            storyStatusView.pause();
            return true;
          }
        }
        return false;
      }
    });

    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {

      }
    });
  }

  private void setImageView(ImageView image, String statusResource) {

    Glide.with(image.getContext())
        .load(statusResource)
        .asBitmap()
        .centerCrop()
        .skipMemoryCache(!isCaching)
        .listener(new RequestListener<String, Bitmap>() {
          @Override
          public boolean onException(Exception e, String model, Target<Bitmap> target,
              boolean isFirstResource) {
            return false;
          }

          @Override
          public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
              boolean isFromMemoryCache, boolean isFirstResource) {
            storyStatusView.resume();
            return false;
          }
        })
        .into(image);
  }

  @Override
  public void onNext() {

    pauseStoryView();
    ++counter;
    if (!mediaType[counter].equals("2")) {
      image.setVisibility(View.VISIBLE);
      videoView.setVisibility(View.GONE);
      setImageView(image, statusResources[counter]);
    } else {
      videoView.setVisibility(View.VISIBLE);
      image.setVisibility(View.GONE);
      setVideoView(videoView, statusResources[counter], counter);
      //videoView.setVideoPath(statusResources[counter]);
      //videoView.start();
    }
  }

  @Override
  public void onPrev() {

    if (counter - 1 < 0) return;
    pauseStoryView();
    --counter;
    if (!mediaType[counter].equals("2")) {
      image.setVisibility(View.VISIBLE);
      videoView.setVisibility(View.GONE);
      setImageView(image, statusResources[counter]);
    } else {
      videoView.setVisibility(View.VISIBLE);
      image.setVisibility(View.GONE);
      setVideoView(videoView, statusResources[counter], counter);
      //videoView.setVideoPath(statusResources[counter]);
      //videoView.start();
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (isImmersive && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
      if (hasFocus) {
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
      }
    }
  }

  @Override
  public void onComplete() {
    finish();
  }

  @Override
  protected void onDestroy() {
    // Very important !
    storyStatusView.destroy();
    super.onDestroy();
  }

  /**
   * Demonstrates 3 different ways of showing the progress:
   * <ul>
   * <li>Update a full fledged progress bar</li>
   * <li>Update a text view to display size/percentage</li>
   * <li>Update the placeholder via Drawable.level</li>
   * </ul>
   * This last one is tricky: the placeholder that Glide sets can be used as a progress drawable
   * without any extra Views in the view hierarchy if it supports levels via <code>usesLevel="true"</code>
   * or <code>level-list</code>.
   *
   * @param <Z> automatically match any real Glide target so it can be used flexibly without
   * reimplementing.
   */
  @SuppressLint("SetTextI18n") // text set only for debugging
  private static class MyProgressTarget<Z> extends ProgressTarget<String, Z> {
    private final TextView text;
    private final ProgressBar progress;

    public MyProgressTarget(Target<Z> target, ProgressBar progress, TextView text) {
      super(target);
      this.progress = progress;
      this.text = text;
    }

    @Override
    public float getGranualityPercentage() {
      return 0.1f; // this matches the format string for #text below
    }

    @Override
    protected void onConnecting() {
      progress.setIndeterminate(true);
      progress.setVisibility(View.INVISIBLE);

      if (isTextEnabled) {
        text.setVisibility(View.VISIBLE);
        text.setText("connecting");
      } else {
        text.setVisibility(View.INVISIBLE);
      }
      storyStatusView.pause();
    }

    @Override
    protected void onDownloading(long bytesRead, long expectedLength) {
      progress.setIndeterminate(false);
      progress.setProgress((int) (100 * bytesRead / expectedLength));

      if (isTextEnabled) {
        text.setVisibility(View.VISIBLE);
        text.setText(String.format(Locale.ROOT, "downloading %.2f/%.2f MB %.1f%%", bytesRead / 1e6,
            expectedLength / 1e6, 100f * bytesRead / expectedLength));
      } else {
        text.setVisibility(View.INVISIBLE);
      }

      storyStatusView.pause();
    }

    @Override
    protected void onDownloaded() {
      progress.setIndeterminate(true);
      if (isTextEnabled) {
        text.setVisibility(View.VISIBLE);
        text.setText("decoding and transforming");
      } else {
        text.setVisibility(View.INVISIBLE);
      }

      storyStatusView.pause();
    }

    @Override
    protected void onDelivered() {
      progress.setVisibility(View.INVISIBLE);
      text.setVisibility(View.INVISIBLE);
      storyStatusView.resume();
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
      storyStatusView.pause();
      super.onLoadFailed(e, errorDrawable);
    }
  }

  public boolean isVideo(String url) {
    return url.contains(".mp4");
  }

  public void pauseStoryView() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        storyStatusView.pause();
      }
    });
  }
}
