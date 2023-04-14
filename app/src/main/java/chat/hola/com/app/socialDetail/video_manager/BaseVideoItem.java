package chat.hola.com.app.socialDetail.video_manager;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.socialDetail.ViewHolder;
import com.ezcall.android.R;
import com.volokh.danylo.video_player_manager.manager.VideoItem;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.CurrentItemMetaData;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.visibility_utils.items.ListItem;
import com.volokh.danylo.visibility_utils.utils.Config;
import java.util.HashMap;

/**
 * <h1>BaseVideoItem</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 11/30/2018.
 */
public abstract class BaseVideoItem implements VideoItem, ListItem {
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = BaseVideoItem.class.getSimpleName();
    /**
     * An object that is filled with values when {@link #getVisibilityPercents} method is called.
     * This object is local visible rect filled by {@link android.view.View#getLocalVisibleRect}
     */
    private final Rect mCurrentViewRect = new Rect();
    private final VideoPlayerManager<MetaData> mVideoPlayerManager;

    protected BaseVideoItem(VideoPlayerManager<MetaData> videoPlayerManager) {
        mVideoPlayerManager = videoPlayerManager;
    }

    /**
     * This method needs to be called when created/recycled view is updated.
     * Call it in
     * 1. {@link android.widget.ListAdapter#getView(int, View, ViewGroup)}
     * 2. {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     */
    public abstract void update(int position, ViewHolder view,
        VideoPlayerManager videoPlayerManager, HashMap<String, Boolean> mTogglePositions);

    /**
     * When this item becomes active we start playback on the video in this item
     */
    @Override
    public void setActive(View newActiveView, int newActiveViewPosition,boolean newPost) {
        try {
            ViewHolder viewHolder = (ViewHolder) newActiveView.getTag();
            viewHolder.ivPlay.setVisibility(View.GONE);
            playNewVideo(new CurrentItemMetaData(newActiveViewPosition, newActiveView), viewHolder.mPlayer, mVideoPlayerManager, viewHolder.ivRecord,newPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When this item becomes inactive we stop playback on the video in this item.
     */
    @Override
    public void deactivate(View currentView, int position) {
        try {
            stopPlayback(mVideoPlayerManager);
        } catch (Exception ignored) {
        }
    }

    public View createView(ViewGroup parent, int screenHeight, int screenWidth) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_detail, parent, false);

        final ViewHolder videoViewHolder = new ViewHolder(view, null);
        view.setTag(videoViewHolder);
        videoViewHolder.llFabContainer.bringToFront();
        ViewCompat.setTranslationZ(videoViewHolder.llFabContainer, 20);

        videoViewHolder.mPlayer.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
            @Override
            public void onVideoSizeChangedMainThread(int width, int height) {
            }

            @Override
            public void onVideoPreparedMainThread() {
                // When video is prepared it's about to start playback. So we hide the cover
//                videoViewHolder.mCover.setVisibility(View.INVISIBLE);
//                try {
//                    fadeOutAnimation(videoViewHolder.mCover, 10);
//                } catch (Exception e) {
//                    videoViewHolder.mCover.setVisibility(View.INVISIBLE);
//                }


            }

            @Override
            public void onVideoRenderingStartedMainThread() {

                // When video is prepared it's about to start playback. So we hide the cover
//                videoViewHolder.mCover.setVisibility(View.INVISIBLE);
                try {
                    fadeOutAnimation(videoViewHolder.mCover, 10);
                } catch (Exception e) {
                    videoViewHolder.mCover.setVisibility(View.INVISIBLE);
                }


            }


            @Override
            public void onVideoCompletionMainThread() {


            }

            @Override
            public void onErrorMainThread(int what, int extra) {


                new SimpleMainThreadMediaPlayerListener() {
                    @Override
                    public void onErrorMainThread(int what, int extra) {
                        Log.d(TAG, "onErrorMainThread");
                        mVideoPlayerManager.resetMediaPlayer();
                    }
                };
            }

            @Override
            public void onBufferingUpdateMainThread(int percent) {

            }

            @Override
            public void onVideoStoppedMainThread() {
                // Show the cover when video stopped
//                videoViewHolder.mCover.setVisibility(View.VISIBLE);

                try {
                    fadeInAnimation(videoViewHolder.mCover, 10);
                } catch (Exception e) {
                    videoViewHolder.mCover.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    /**
     * This method calculates visibility percentage of currentView.
     * This method works correctly when currentView is smaller then it's enclosure.
     *
     * @param currentView - view which visibility should be calculated
     * @return currentView visibility percents
     */
    @Override
    public int getVisibilityPercents(View currentView) {
        //if (SHOW_LOGS) Log.v(TAG, ">> getVisibilityPercents currentView " + currentView);

        int percents = 100;
        try {
            currentView.getLocalVisibleRect(mCurrentViewRect);
            //if (SHOW_LOGS)
            //    Log.v(TAG, "getVisibilityPercents mCurrentViewRect top " + mCurrentViewRect.top + ", left " + mCurrentViewRect.left + ", bottom " + mCurrentViewRect.bottom + ", right " + mCurrentViewRect.right);

            int height = currentView.getHeight();
            //if (SHOW_LOGS) Log.v(TAG, "getVisibilityPercents height " + height);

            if (viewIsPartiallyHiddenTop()) {
                // view is partially hidden behind the top edge
                percents = (height - mCurrentViewRect.top) * 100 / height;
            } else if (viewIsPartiallyHiddenBottom(height)) {
                percents = mCurrentViewRect.bottom * 100 / height;
            }

            //if (SHOW_LOGS) Log.v(TAG, "<< getVisibilityPercents, percents " + percents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return percents;
    }


    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }


    private static void fadeOutAnimation(final View view, final int animationDuration) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(animationDuration);
        fadeOut.setDuration(animationDuration);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeOut);
    }

    private static void fadeInAnimation(final View view, final int animationDuration) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(animationDuration);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeIn);
    }
}
