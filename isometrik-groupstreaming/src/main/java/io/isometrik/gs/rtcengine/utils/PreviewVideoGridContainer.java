package io.isometrik.gs.rtcengine.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import io.isometrik.gs.R;
import java.util.ArrayList;
import java.util.List;

public class PreviewVideoGridContainer extends RelativeLayout {
  private static final int MAX_USER = 6;
  private SparseArray<ViewGroup> mUserViewList = new SparseArray<>(MAX_USER);
  private List<Integer> mUidList = new ArrayList<>(MAX_USER);

  /**
   * Instantiates a new Video grid container.
   *
   * @param context the context
   */
  public PreviewVideoGridContainer(Context context) {
    super(context);
  }

  /**
   * Instantiates a new Video grid container.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public PreviewVideoGridContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Instantiates a new Video grid container.
   *
   * @param context the context
   * @param attrs the attrs
   * @param defStyleAttr the def style attr
   */
  public PreviewVideoGridContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * Add user video surface.
   *
   * @param uid the uid
   * @param surface the surface
   * @param isLocal the is local
   */
  public void addUserVideoSurface(int uid, SurfaceView surface, boolean isLocal) {
    if (surface == null) {
      return;
    }

    int id = -1;
    if (isLocal) {
      if (mUidList.contains(0)) {
        mUidList.remove((Integer) 0);
        mUserViewList.remove(0);
      }

      if (mUidList.size() == MAX_USER) {
        mUidList.remove(0);
        mUserViewList.remove(0);
      }
      id = 0;
    } else {
      if (mUidList.contains(uid)) {
        mUidList.remove((Integer) uid);
        mUserViewList.remove(uid);
      }

      if (mUidList.size() < MAX_USER) {
        id = uid;
      }
    }

    if (id == 0) {
      mUidList.add(0, uid);
    } else {
      mUidList.add(uid);
    }

    if (id != -1) {
      mUserViewList.append(uid, createVideoView(uid, surface, isLocal));

      requestGridLayout();
    }
  }

  private ViewGroup createVideoView(int uid, SurfaceView surface, boolean isLocal) {
    RelativeLayout layout = new RelativeLayout(getContext());

    layout.setId(Math.abs(surface.hashCode()));

    RelativeLayout.LayoutParams videoLayoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    layout.addView(surface, videoLayoutParams);

    if (!isLocal) {

      ImageView ivMuteAudio = new ImageView(getContext());
      ivMuteAudio.setId(UserIdGenerator.getViewId(uid + "ivMuteAudio"));

      ImageView ivMuteVideo = new ImageView(getContext());
      ivMuteVideo.setId(UserIdGenerator.getViewId(uid + "ivMuteVideo"));

      RelativeLayout.LayoutParams ivMuteAudioParams =
          new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
      ivMuteAudioParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
      ivMuteAudioParams.setMargins(10, 10, 10, 10);
      ivMuteAudioParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
      ivMuteAudio.setColorFilter(Color.WHITE);
      ivMuteAudio.setImageResource(R.drawable.ism_ic_mic_off);
      ivMuteAudio.setVisibility(INVISIBLE);
      RelativeLayout.LayoutParams ivMuteVideoParams =
          new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
      ivMuteAudioParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
      ivMuteVideoParams.addRule(RelativeLayout.START_OF, ivMuteAudio.getId());
      ivMuteVideoParams.setMargins(10, 10, 10, 10);
      ivMuteVideoParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
      ivMuteVideo.setColorFilter(Color.WHITE);
      ivMuteVideo.setImageResource(R.drawable.ism_ic_videocam_off);
      ivMuteVideo.setVisibility(INVISIBLE);
      layout.addView(ivMuteAudio, ivMuteAudioParams);
      layout.addView(ivMuteVideo, ivMuteVideoParams);
    }

    return layout;
  }

  /**
   * Remove user video.
   *
   * @param uid the uid
   * @param isLocal the is local
   */
  public void removeUserVideo(int uid, boolean isLocal) {
    if (isLocal && mUidList.contains(0)) {
      mUidList.remove((Integer) 0);
      mUserViewList.remove(0);
    } else if (mUidList.contains(uid)) {
      mUidList.remove((Integer) uid);
      mUserViewList.remove(uid);
    }
    requestGridLayout();
  }

  private void requestGridLayout() {
    removeAllViews();
    layout(mUidList.size());
  }

  private void layout(int size) {
    RelativeLayout.LayoutParams[] params = getParams(size);
    for (int i = 0; i < size; i++) {
      addView(mUserViewList.get(mUidList.get(i)), params[i]);
    }
  }

  private RelativeLayout.LayoutParams[] getParams(int size) {
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();

    RelativeLayout.LayoutParams[] array = new RelativeLayout.LayoutParams[size];

    for (int i = 0; i < size; i++) {

      if (i == 0) {

        array[0] =
            new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
      } else if (i == 1) {

        array[0] = new RelativeLayout.LayoutParams(width, height / 2);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[1] = new RelativeLayout.LayoutParams(width, height / 2);
        array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
        array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
      } else if (i == 2) {

        array[0] = new RelativeLayout.LayoutParams(width, height / 2);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[1] = new RelativeLayout.LayoutParams(width / 2, height / 2);
        array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
        array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[2] = new RelativeLayout.LayoutParams(width / 2, height / 2);
        array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
        array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
      } else if (i == 3) {

        array[0] = new RelativeLayout.LayoutParams(width / 2, height / 2);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[1] = new RelativeLayout.LayoutParams(width / 2, height / 2);
        array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());

        array[2] = new RelativeLayout.LayoutParams(width / 2, height / 2);
        array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
        array[2].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[3] = new RelativeLayout.LayoutParams(width / 2, height / 2);
        array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
        array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());
      } else if (i == 4) {

        array[0] = new RelativeLayout.LayoutParams(width, height / 3);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[1] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[1].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());
        array[1].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[2] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[2].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(1)).getId());
        array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());

        array[3] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
        array[3].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[4] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(2)).getId());
        array[4].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(3)).getId());
      } else if (i == 5) {

        array[0] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        array[0].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[1] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[1].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(0)).getId());
        array[1].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        array[2] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[2].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        array[2].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(0)).getId());

        array[3] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[3].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(1)).getId());
        array[3].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(2)).getId());

        array[4] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[4].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(2)).getId());
        array[4].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        array[5] = new RelativeLayout.LayoutParams(width / 2, height / 3);
        array[5].addRule(RelativeLayout.BELOW, mUserViewList.get(mUidList.get(3)).getId());
        array[5].addRule(RelativeLayout.END_OF, mUserViewList.get(mUidList.get(4)).getId());
      }
    }

    return array;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    clearAllVideo();
  }

  private void clearAllVideo() {
    removeAllViews();
    mUserViewList.clear();
    mUidList.clear();
  }

  /**
   * @param uid of the user who audio state updated
   * @param muted whether remote user muted or unmuted audio
   */
  public void updateAudioState(int uid, boolean muted) {
    try {
      new Handler(getContext().getMainLooper()).post(() -> {
        for (int i = 0; i < mUidList.size(); i++) {

          if (mUidList.get(i) == uid) {
            try {

              ViewGroup viewGroup = mUserViewList.get(uid);

              ImageView ivMuteAudio = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(uid + "ivMuteAudio"));
              if (muted) {
                ivMuteAudio.setVisibility(VISIBLE);
              } else {
                ivMuteAudio.setVisibility(INVISIBLE);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            break;
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param uid of the user who video state updated
   * @param muted whether remote user muted or unmuted video
   */
  public void updateVideoState(int uid, boolean muted) {
    try {
      new Handler(getContext().getMainLooper()).post(() -> {
        for (int i = 0; i < mUidList.size(); i++) {

          if (mUidList.get(i) == uid) {
            try {
              ViewGroup viewGroup = mUserViewList.get(uid);

              ImageView ivMuteVideo = ((RelativeLayout) viewGroup).findViewById(
                  UserIdGenerator.getViewId(uid + "ivMuteVideo"));
              if (muted) {

                ivMuteVideo.setVisibility(VISIBLE);
              } else {

                ivMuteVideo.setVisibility(INVISIBLE);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            break;
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void release() {

    mUserViewList = new SparseArray<>(MAX_USER);
    mUidList = new ArrayList<>(MAX_USER);
    removeAllViews();
  }

  public int getNumberOfPublishers() {
    return mUserViewList.size();
  }
}

