package chat.hola.com.app.preview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rahuljanagouda.statusstories.StoryStatusView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.home.stories.model.ViewerAdapter;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

import static chat.hola.com.app.preview.PreviewPresenter.page;
import static chat.hola.com.app.preview.PreviewPresenter.pageSize;

/**
 * <h1>PreviewActivity</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/2018.
 */

public class PreviewActivity extends DaggerAppCompatActivity
    implements PreviewContract.View, StoryStatusView.UserInteractionListener {
  private Unbinder unbinder;
  @Inject
  PreviewPresenter presenter;
  @Inject
  BlockDialog dialog;

  public static final String STATUS_RESOURCES_KEY = "statusStoriesResources";
  public static final String STATUS_DURATION_KEY = "statusStoriesDuration";
  public static final String STATUS_DURATIONS_ARRAY_KEY = "statusStoriesDurations";
  public static final String STATUS_MEDIA_TYPE = "statusStoriesMediaType";
  public static final String STATUS_STORY_ID = "statusStoryId";
  public static final String STATUS_IS_MY_STORY = "isMyStory";
  public static final String STATUS_VIEW_COUNT = "statusViewCount";
  public static final String MY_STORY_POSTS = "myStoryPost";
  public static final String ALL_STORY_POST = "allStoryPost";
  public static final String IS_IMMERSIVE_KEY = "isImmersive";
  public static final String IS_CACHING_ENABLED_KEY = "isCaching";
  public static final String IS_TEXT_PROGRESS_ENABLED_KEY = "isText";

  private int counter = 0;
  private boolean isImmersive = true;
  private boolean isCaching = true;
  private ViewerAdapter viewerListAdapter;
  BottomSheetBehavior behavior;

  @BindView(R.id.image)
  ImageView image;
  @BindView(R.id.videoView)
  VideoView videoView;
  @BindView(R.id.tVAddCaption)
  TextView tVAddCaption;
  @BindView(R.id.tV_viewCount)
  TextView tV_viewCount;
  @BindView(R.id.rL_viewCount)
  View rL_viewCount;
  @BindView(R.id.iVOptions)
  ImageView iVOptions;
  @BindView(R.id.coordinatorLayout)
  CoordinatorLayout coordinatorLayout;
  @BindView(R.id.rV_viewerList)
  RecyclerView rV_viewerList;
  @BindView(R.id.storiesStatus)
  StoryStatusView storyStatusView;
  @BindView(R.id.actions)
  LinearLayout actions;
  @BindView(R.id.tV_status)
  TextView tV_status;
  @BindView(R.id.rL_main)
  RelativeLayout rL_main;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.ll_view)
  LinearLayout ll_view;
  @BindView(R.id.iV_eye)
  ImageView iV_eye;

  @BindView(R.id.rLStoryDetail)
  RelativeLayout rLStoryDetail;
  @BindView(R.id.ivProfilePic)
  ImageView ivProfilePic;
  @BindView(R.id.tvUserName)
  TextView tvUserName;
  @BindView(R.id.tvTime)
  TextView tvTime;
  @BindView(R.id.iVBack)
  ImageView iVBack;

  private boolean isMyStory;
  // current user stiores position
  int position;
  // story post of one user
  private List<StoryPost> storyPosts = new ArrayList<>();
  // viewerlist of storiesFetched only when my storiesFetched
  private List<Viewer> viewerList = new ArrayList<Viewer>();
  // storiesFetched of all user
  private List<StoryData> storyData;
  private LinearLayoutManager viewerLayoutManager;

  @Override
  public void showMessage(String msg, int msgId) {

  }

  @Override
  public void sessionExpired() {

  }

  @Override
  public void isInternetAvailable(boolean flag) {

  }

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stories_preview);
    unbinder = ButterKnife.bind(this);

    getIntentData();

    if (isMyStory) {
      rL_viewCount.setVisibility(View.VISIBLE);
      iVOptions.setVisibility(View.GONE);
      viewerListAdapter = new ViewerAdapter(this, viewerList);
      viewerLayoutManager = new LinearLayoutManager(this);
      rV_viewerList.setLayoutManager(viewerLayoutManager);
      rV_viewerList.setAdapter(viewerListAdapter);
      rV_viewerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          int visibleItemCount = viewerLayoutManager.getChildCount();
          int totalItemCount = viewerLayoutManager.getItemCount();
          int firstVisibleItemPosition = viewerLayoutManager.findFirstVisibleItemPosition();
          presenter.viewerListScroll(storyPosts.get(counter).getStoryId(),visibleItemCount,firstVisibleItemPosition,totalItemCount);
        }
      });
    } else {
      rL_viewCount.setVisibility(View.GONE);
      iVOptions.setVisibility(View.GONE);

    }

    behavior = BottomSheetBehavior.from(rL_viewCount);
    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {

        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
          pauseStoryView();
          videoView.pause();
          rL_viewCount.setBackground(getDrawable(R.drawable.rounded_rectangle_white));
          tV_viewCount.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
          iV_eye.setColorFilter(
              ContextCompat.getColor(PreviewActivity.this, R.color.colorPrimaryDark),
              android.graphics.PorterDuff.Mode.MULTIPLY);
          storyStatusView.setVisibility(View.INVISIBLE);
        } else {
          resumeStoryView();
          videoView.start();
          rL_viewCount.setBackground(null);
          tV_viewCount.setTextColor(getResources().getColor(R.color.color_white));
          iV_eye.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.color_white),
              android.graphics.PorterDuff.Mode.MULTIPLY);
          storyStatusView.setVisibility(View.VISIBLE);
        }
        // React to state change
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        // React to dragging events
      }
    });

    //storyStatusView.setStoriesCount(statusResources.length);
    //storyStatusView.setStoryDuration(statusDuration);
    // or
    storyStatusView.setStoriesCountWithDurations(getResourcesDuration());
    storyStatusView.setUserInteractionListener(this);
    storyStatusView.playStories();

    actions.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {

               /* if(mediaType[counter].equals("2"))
                    return false;*/

        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
          pauseStoryView();
          videoView.pause();
        }
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
          resumeStoryView();
          videoView.start();
          behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        return true;
      }
    });

    setViewOnData();

    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        videoView.start();
      }
    });

    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
      @Override
      public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
          case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {

            progressBar.setVisibility(View.GONE);
            resumeStoryView();
            return true;
          }
          case MediaPlayer.MEDIA_INFO_BUFFERING_START: {

            progressBar.setVisibility(View.VISIBLE);
            pauseStoryView();
            return true;
          }
          case MediaPlayer.MEDIA_INFO_BUFFERING_END: {

            progressBar.setVisibility(View.GONE);
            resumeStoryView();
            return true;
          }
        }
        return false;
      }
    });

    iVOptions.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });
  }

  private long[] getResourcesDuration() {
    long[] duarations = new long[storyPosts.size()];

    for (int i = 0; i < storyPosts.size(); i++) {
      if (storyPosts.get(i).getDuration() != null  && !storyPosts.get(i).getDuration().isEmpty() ) {
        duarations[i] = Long.parseLong(storyPosts.get(i).getDuration());
      } else {
        duarations[i] = 3000L;
      }
    }

    return duarations;
  }

  public void getIntentData() {
    isMyStory = getIntent().getBooleanExtra(STATUS_IS_MY_STORY, false);
    storyData = (List<StoryData>) getIntent().getSerializableExtra(ALL_STORY_POST);
    position = getIntent().getIntExtra("position", 0);
    isImmersive = getIntent().getBooleanExtra(IS_IMMERSIVE_KEY, true);
    isCaching = getIntent().getBooleanExtra(IS_CACHING_ENABLED_KEY, true);

    intializeStoryPostList();
  }

  public void intializeStoryPostList() {
    if (isMyStory) {
      storyPosts = (List<StoryPost>) getIntent().getSerializableExtra(MY_STORY_POSTS);
    } else {
      storyPosts = storyData.get(position).getPosts();
    }
  }

  @OnClick(R.id.reverse)
  public void reverseStory() {
    storyStatusView.reverse();
    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
  }

  @OnClick(R.id.skip)
  public void skipStory() {
    storyStatusView.skip();
    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
  }

  @Override
  public void onNext() {
    ++counter;
    setViewOnData();
  }

  @Override
  public void onPrev() {

    if (counter - 1 < 0) return;
    --counter;
    setViewOnData();
  }

  @Override
  public void onComplete() {

    //finish();
    if (!isMyStory) {
      if (position != storyData.size() - 1) {
        position = position + 1;
        Intent a = new Intent(PreviewActivity.this, PreviewActivity.class);
        a.putExtra(PreviewActivity.IS_IMMERSIVE_KEY, false);
        a.putExtra(PreviewActivity.IS_CACHING_ENABLED_KEY, false);
        a.putExtra(PreviewActivity.IS_TEXT_PROGRESS_ENABLED_KEY, false);
        a.putExtra(PreviewActivity.STATUS_IS_MY_STORY, false);
        a.putExtra("position", position);
        a.putExtra(PreviewActivity.ALL_STORY_POST, (Serializable) storyData);
        startActivity(a);
        finish();
      } else {
        finish();
      }
    } else {
      finish();
    }
  }

  public void setViewOnData() {
    pauseStoryView();
    switch (storyPosts.get(counter).getType()) {
      case "1":
        image.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        tV_status.setVisibility(View.GONE);
        rL_main.setBackgroundColor(Color.BLACK);
        setImageView(image, storyPosts.get(counter).getUrlPath(),
            storyPosts.get(counter).getStoryId(), counter);
        break;
      case "2":
        videoView.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        tV_status.setVisibility(View.GONE);
        rL_main.setBackgroundColor(Color.BLACK);
        setVideoView(videoView, storyPosts.get(counter).getUrlPath(),
            storyPosts.get(counter).getStoryId(), counter);
        break;
      case "3":
        image.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        tV_status.setVisibility(View.VISIBLE);
        setStatusView(counter);
        break;
    }

    String pic,userName;

    if(isMyStory) {
      pic = AppController.getInstance().getUserImageUrl();
      userName = AppController.getInstance().getUserName();
    }
    else {
      pic = storyData.get(position).getProfilePic();
      userName = storyData.get(position).getUserName();
    }

    SessionManager sessionManager = new SessionManager(this);
    try {
      if (storyData.get(position).getProfilePic() != null && !storyData.get(position).getProfilePic().isEmpty()) {
        Glide.with(getBaseContext())
                .load(storyData.get(position).getProfilePic())
                .asBitmap()
                .centerCrop()
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .into(new BitmapImageViewTarget(ivProfilePic) {
                  @Override
                  protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfilePic.setImageDrawable(circularBitmapDrawable);
                  }
                });
      } else {
        Utilities.setTextRoundDrawable(getBaseContext(), storyData.get(position).getFirstName(),
                storyData.get(position).getLastName(), ivProfilePic);
      }
    }catch(Exception e){

    }
    tvUserName.setText(userName);
    tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(storyPosts.get(counter).getTimestamp())));

    if (storyPosts.get(counter).getCaption() != null && !storyPosts.get(counter)
        .getCaption()
        .isEmpty()) {
      tVAddCaption.setVisibility(View.VISIBLE);
      tVAddCaption.setText(storyPosts.get(counter).getCaption());
      Linkify.addLinks(tVAddCaption, Linkify.WEB_URLS);
    } else {
      tVAddCaption.setVisibility(View.GONE);
    }
  }

  @OnClick(R.id.iVBack)
  public void onBack(){
    onBackPressed();
  }

  private void setStatusView(int counter) {

    if(isMyStory){
      tV_viewCount.setText(storyPosts.get(counter).getUniqueViewCount());
      presenter.getViewerList(storyPosts.get(counter).getStoryId(),page,pageSize);
    }

    rL_main.setBackgroundColor(Color.parseColor(storyPosts.get(counter).getBackgroundColor()));
    tV_status.setText(storyPosts.get(counter).getStatusMessage());
    tV_status.setTypeface(getFontType());
    Linkify.addLinks(tV_status, Linkify.WEB_URLS);
    resumeStoryView();

    /*
    * Bug Title: Add story-> When we try to see the viewers name this is the behaviour please see the video.
    * Bug Id: DUBAND155
    * Fix Desc: check
    * Fix Dev: Hardik
    * Fix Date: 13/5/21
    * */
    if(!isMyStory)presenter.viewStory(storyPosts.get(counter).getStoryId());
  }

  private Typeface getFontType() {

    switch (storyPosts.get(counter).getFontType()) {
      case "default":
        return Typeface.DEFAULT;
      case "monospace":
        return Typeface.MONOSPACE;
      case "sans_serif":
        return Typeface.SANS_SERIF;
      case "serif":
        return Typeface.SERIF;
      case "default_bold":
        return Typeface.DEFAULT_BOLD;

      default:
        return Typeface.DEFAULT;
    }
  }

  private void setVideoView(final VideoView vV, String statusResource, String storyId,
      int counter) {

    progressBar.setVisibility(View.VISIBLE);

    statusResource = statusResource.replace("/upload", "/upload/c_fit,q_auto");

    if(isMyStory){
      tV_viewCount.setText(storyPosts.get(counter).getUniqueViewCount());
      presenter.getViewerList(storyPosts.get(counter).getStoryId(),page,pageSize);
    }

    //pauseStoryView();

    videoView.setVideoPath(statusResource);
    //videoView.start();
    /*
     * Bug Title: Add story-> When we try to see the viewers name this is the behaviour please see the video.
     * Bug Id: DUBAND155
     * Fix Desc: check
     * Fix Dev: Hardik
     * Fix Date: 13/5/21
     * */
    if(!isMyStory)presenter.viewStory(storyId);
  }

  private void setImageView(ImageView image, String statusResource, String storyId, int counter) {

    progressBar.setVisibility(View.VISIBLE);

    if(isMyStory){
      tV_viewCount.setText(storyPosts.get(counter).getUniqueViewCount());
      presenter.getViewerList(storyPosts.get(counter).getStoryId(),page,pageSize);
    }

    Glide.with(image.getContext())
        .load(statusResource)
        .asBitmap()
        .fitCenter()
        .skipMemoryCache(!isCaching)
        .listener(new RequestListener<String, Bitmap>() {
          @Override
          public boolean onException(Exception e, String model, Target<Bitmap> target,
              boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }

          @Override
          public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
              boolean isFromMemoryCache, boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            resumeStoryView();
            return false;
          }
        })
        .into(image);

    /*
     * Bug Title: Add story-> When we try to see the viewers name this is the behaviour please see the video.
     * Bug Id: DUBAND155
     * Fix Desc: check
     * Fix Dev: Hardik
     * Fix Date: 13/5/21
     * */
    if(!isMyStory)presenter.viewStory(storyId);
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
  protected void onDestroy() {
    // Very important !
    storyStatusView.destroy();
    super.onDestroy();
  }

  @Override
  public void reload() {

  }

  public void pauseStoryView() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        storyStatusView.pause();
      }
    });
  }

  public void resumeStoryView() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        storyStatusView.resume();
      }
    });
  }

  @Override
  public void updateViewerList(String storyId, List<Viewer> data, boolean isClear) {
    if(storyPosts.get(counter).getStoryId().equals(storyId)) {
      /*same story viewer list update otherwise don't*/
      if (isClear)
        viewerList.clear();
      viewerList.addAll(data);
      viewerListAdapter.notifyDataSetChanged();
    }
  }
}
