package chat.hola.com.app.socialDetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.CountFormat;
import chat.hola.com.app.Utilities.OnSwipeTouchListener;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.comment.CommentActivity;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 8/13/2018.
 */
public class PostFragment extends Fragment
    implements TextureView.SurfaceTextureListener, PostContract.View {
  private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR =
      new DecelerateInterpolator();
  private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR =
      new AccelerateInterpolator();

  @BindView(R.id.ivMedia)
  ImageView ivMedia;
  @BindView(R.id.tvUserName)
  TextView tvUserName;
  @BindView(R.id.tvTitle)
  TextView tvTitle;
  @BindView(R.id.tvLocation)
  TextView tvLocation;
  @BindView(R.id.tvCategory)
  TextView tvCategory;
  @BindView(R.id.tvChannel)
  TextView tvChannel;
  @BindView(R.id.ivProfilePic)
  ImageView ivProfilePic;
  @BindView(R.id.ivLike)
  CheckBox ivLike;
  @BindView(R.id.ivComment)
  ImageView ivComment;
  @BindView(R.id.tvLikeCount)
  TextView tvLikeCount;
  @BindView(R.id.tvCommentCount)
  TextView tvCommentCount;
  @BindView(R.id.tvViewCount)
  TextView tvViewCount;
  @BindView(R.id.rlItem)
  RelativeLayout rlItem;
  @BindView(R.id.flMediaContainer)
  RelativeLayout flMediaContainer;
  @BindView(R.id.ibReplay)
  ImageButton ibReplay;
  @BindView(R.id.video)
  TextureView video;
  @BindView(R.id.vBgLike)
  View vBgLike;
  @BindView(R.id.ivLikeIt)
  ImageView ivLikeIt;
  @BindView(R.id.videoContainer)
  RelativeLayout videoContainer;
  @BindView(R.id.ibPlay)
  CheckBox ibPlay;
  @BindView(R.id.tvMusic)
  TextView tvMusic;
  @BindView(R.id.llMusic)
  LinearLayout llMusic;
  @BindView(R.id.tvDivide)
  TextView tvDivide;
  @BindView(R.id.cbFollow)
  CheckBox cbFollow;
  @BindView(R.id.progress)
  ProgressBar progressBar;

  @Inject
  SessionManager sessionManager;

  private PostPresenter presenter;
  private String postId;
  private Data data = null;
  private String userId;
  private String musicId;
  private String categoryId;
  private MediaPlayer mediaPlayer;
  private String path;
  private int likes = 0;
  private String channelId;

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.post_detail, container, false);
    ButterKnife.bind(this, rootView);
    Bundle bundle = this.getArguments();
    presenter = new PostPresenter(getContext());
    if (bundle != null) {
      data = (Data) bundle.getSerializable("data");

      displayData(data);

      flMediaContainer.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
        public void onSingleTap() {
          if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
              mediaPlayer.stop();
            } else {
              play(path);
            }
          }
        }

        public void onDoubleTaps() {
          animatePhotoLike();
        }
      });
    }
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
  }

  // heart animation
  private void animatePhotoLike() {
    vBgLike.setVisibility(View.VISIBLE);
    ivLikeIt.setVisibility(View.VISIBLE);

    vBgLike.setScaleY(0.1f);
    vBgLike.setScaleX(0.1f);
    vBgLike.setAlpha(1f);
    ivLikeIt.setScaleY(0.1f);
    ivLikeIt.setScaleX(0.1f);

    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(vBgLike, "scaleY", 0.1f, 1f);
    bgScaleYAnim.setDuration(200);
    bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(vBgLike, "scaleX", 0.1f, 1f);
    bgScaleXAnim.setDuration(200);
    bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(vBgLike, "alpha", 1f, 0f);
    bgAlphaAnim.setDuration(200);
    bgAlphaAnim.setStartDelay(150);
    bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

    ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(ivLikeIt, "scaleY", 0.1f, 1f);
    imgScaleUpYAnim.setDuration(300);
    imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(ivLikeIt, "scaleX", 0.1f, 1f);
    imgScaleUpXAnim.setDuration(300);
    imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

    ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(ivLikeIt, "scaleY", 1f, 0f);
    imgScaleDownYAnim.setDuration(300);
    imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
    ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(ivLikeIt, "scaleX", 1f, 0f);
    imgScaleDownXAnim.setDuration(300);
    imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim,
        imgScaleUpXAnim);
    animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        resetLikeAnimationState();
      }
    });
    animatorSet.start();
    ivLike.setChecked(true);
  }

  private void resetLikeAnimationState() {
    vBgLike.setVisibility(View.INVISIBLE);
    ivLikeIt.setVisibility(View.INVISIBLE);
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (!isVisibleToUser)   // If we are becoming invisible, then...
    {
      if (mediaPlayer == null) {
        mediaPlayer = new MediaPlayer();
      } else if (mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
      }
    }

    if (isVisibleToUser) // If we are becoming visible, then...
    {
      /*  BUG-ID : DUB-375
       *  BUG-DESC : Auto play does not work, till you tap on the video to play
       * */
      Bundle bundle = this.getArguments();
      data = (Data) bundle.getSerializable("data");

      path =
          data.getImageUrl1();//.replace("upload/", "upload/ar_" + String.valueOf(MainActivity.ratio) + ",c_fill/");
      play(path);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    /*  BUG-ID : DUB-373
     *  BUG-DESC : open the page with video, minimise in android 9 and then open the same page against will show the black page.
     * */
    if (mediaPlayer != null && !mediaPlayer.isPlaying()) play(path);
  }

  @OnClick({ R.id.tvMusic, R.id.llMusic })
  public void music() {
    startActivity(new Intent(getContext(), TrendingDetail.class).putExtra("call", "music")
        .putExtra("musicId", musicId)
        .putExtra("name", data.getDub().getName()));
  }

  @OnClick(R.id.tvLocation)
  public void tvLocation() {
    startActivity(
        new Intent(getContext(), TrendingDetail.class).putExtra("placeId", data.getPlaceId())
            .putExtra("call", "location")
            .putExtra("location", tvLocation.getText().toString())
            .putExtra("latlong", data.getLocation()));
  }

  @OnClick(R.id.tvCategory)
  public void tvCategory() {
    startActivity(new Intent(getContext(), TrendingDetail.class).putExtra("categoryId", categoryId)
        .putExtra("call", "category")
        .putExtra("category", tvCategory.getText().toString()));
  }

  @OnClick(R.id.ivComment)
  public void comment() {
    startActivity(new Intent(getContext(), CommentActivity.class).putExtra("postId", postId));
  }

  @OnClick({ R.id.tvUserName, R.id.ivProfilePic })
  public void profile() {
    startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("userId", userId));
  }

  private void findMatch(SpannableString spanString, Matcher matcher) {
    while (matcher.find()) {
      final String tag = matcher.group(0);
      spanString.setSpan(new TagSpannable(getContext(), tag, R.color.color_white), matcher.start(),
          matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }

  //display data
  public void displayData(Data data) {
    userId = data.getUserId();
    likes = Integer.parseInt(data.getLikesCount());
    postId = data.getPostId();
    Uri uri = Uri.parse(data.getImageUrl1());
    // ivMedia
    Glide.with(getContext())
        .load(Utilities.getModifiedImageLink(uri.toString()))
        .asBitmap()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(ivMedia);

    Glide.with(getContext()).load(data.getProfilepic()).asBitmap().centerCrop()
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        .into(new BitmapImageViewTarget(ivProfilePic) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            ivProfilePic.setImageDrawable(circularBitmapDrawable);
          }
        });

    tvUserName.setText("@" + data.getUsername());
    tvViewCount.setText(CountFormat.format(Long.parseLong(data.getDistinctViews())));
    tvLikeCount.setText(CountFormat.format(Long.parseLong(String.valueOf(likes))));
    tvCommentCount.setText(CountFormat.format(Long.parseLong(data.getCommentCount())));

    cbFollow.setVisibility(
        AppController.getInstance().getUserId().equals(userId) ? View.GONE : View.VISIBLE);
    cbFollow.setChecked(data.getFollowStatus() != 0);

    if (data.getDub() != null) {
      llMusic.setVisibility(View.VISIBLE);
      tvMusic.setText(data.getDub().getName());
      musicId = data.getDub().getId();
    }
    path = null;
    ivLike.setChecked(data.isLiked());
    if (data.getMediaType1() == 1) {
      path = Utilities.getModifiedVideoLink(
          data.getImageUrl1());//.replace("upload/", "upload/ar_" + String.valueOf(MainActivity.ratio) + ",c_fill/");

      videoContainer.setVisibility(View.VISIBLE);
      video.setSurfaceTextureListener(this);
      //            ibPlay.setVisibility(View.VISIBLE);
      //            ibPlay.setOnCheckedChangeListener((compoundButton, b) -> {
      //                if (b) {
      //                    play(path);
      //                    ibPlay.setVisibility(View.GONE);
      //                }
      //            });
    }

    //title
    if (data.getTitle() != null && !data.getTitle().trim().equals("")) {
      tvTitle.setVisibility(View.VISIBLE);
      SpannableString spanString = new SpannableString(data.getTitle());
      Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
      findMatch(spanString, matcher);
      Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
      findMatch(spanString, userMatcher);
      tvTitle.setText(spanString);
      tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //location
    if (data.getPlace() != null && !data.getPlace().trim().equals("")) {
      tvLocation.setVisibility(View.VISIBLE);
      tvLocation.setText(data.getPlace());
    }

    //divide
      if (!data.getChannelName().isEmpty() && !data.getCategoryName().isEmpty()) {
          tvDivide.setVisibility(View.VISIBLE);
      }

    //category
    if (data.getCategoryName() != null && !data.getCategoryName().trim().equals("")) {
      tvCategory.setVisibility(View.VISIBLE);
      tvCategory.setText(data.getCategoryName());
      categoryId = data.getCategoryId();
    }

    //channel
    if (data.getChannelImageUrl() != null) {
      tvChannel.setVisibility(View.VISIBLE);
      tvChannel.setText(data.getChannelName());
      channelId = data.getChannelId();
    }
    //        ivLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    //            @Override
    //            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
    //                new Handler(Looper.getMainLooper()).post(() -> {
    //                    ((SocialDetailActivity) Objects.requireNonNull(getActivity())).like(isChecked, postId);
    //                    if (!ivLike.isChecked()) {
    //                        likes++;
    //                    } else if (likes > 0) {
    //                        likes--;
    //                    }
    //                });
    //                ivLike.setChecked(isChecked);
    //                Log.i("LIKE", "" + likes);
    //
    //                tvLikeCount.setText(CountFormat.format(Long.parseLong(String.valueOf(likes))));
    //            }
    //        });
  }

  @OnClick(R.id.tvChannel)
  public void channel() {
      if (channelId != null) {
          startActivity(new Intent(getContext(), TrendingDetail.class).putExtra("call", "channel")
              .putExtra("channelId", channelId));
      }
  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, int i, int i1) {
    if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
    mediaPlayer.setSurface(new Surface(surface));
  }

  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

  }

  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
    return false;
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

  }

  private void play(String path) {

    try {
      mediaPlayer.reset();
      mediaPlayer.setDataSource(Utilities.getModifiedVideoLink(path));
      mediaPlayer.setLooping(true);
      mediaPlayer.prepare();
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      //            progressBar.setVisibility(View.VISIBLE);
      mediaPlayer.start();
      mediaPlayer.setOnBufferingUpdateListener((mediaPlayer, i) -> {
        if (mediaPlayer.isPlaying()) progressBar.setVisibility(View.GONE);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void liked(boolean flag) {
    //    ivLike.setSelected(flag);
    //        if (flag) likes++;
    //        else if (likes > 0) likes--;
    //        tvLikeCount.setText(CountFormat.format(Long.parseLong(String.valueOf(likes))));
  }

  @Override
  public void deleted() {
  }

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

  }

  @Override
  public void reload() {

  }

//  @OnClick(R.id.ivShare)
//  public void share() {
//    ((SocialDetailActivity) getActivity()).send(0);
//  }

  @OnCheckedChanged(R.id.cbFollow)
  public void onFOllowChanged(CheckBox view, boolean isChecked) {
    new Handler(Looper.getMainLooper()).post(() -> {
      ((SocialDetailActivity) getActivity()).follow(isChecked, postId);
    });
  }

  @OnCheckedChanged(R.id.ivLike)
  public void onCheckedChanged(CheckBox view, boolean isChecked) {
    new Like(isChecked).execute();
  }

  @SuppressLint("StaticFieldLeak")
  private class Like extends AsyncTask<Void, Void, Void> {
    private boolean isChecked;

    public Like(boolean isChecked) {
      this.isChecked = isChecked;
    }

    @Override
    protected void onPreExecute() {
      //start showing progress here
    }

    @Override
    protected Void doInBackground(Void... params) {
      new Handler(Looper.getMainLooper()).post(() -> {
        if (isChecked) {
          likes++;
        } else if (likes > 0) {
          likes--;
        }
      });
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {

      tvLikeCount.setText(CountFormat.format(Long.parseLong(String.valueOf(likes))));
    }
  }
}
