package chat.hola.com.app.dublycategory.fagments;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.dublycategory.DubCategoryActivity;
import chat.hola.com.app.dublycategory.modules.DubListAdapter;
import chat.hola.com.app.dublycategory.modules.ViewHolder;
import com.ezcall.android.R;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/19/2018.
 */

public class DubFragment extends DaggerFragment
    implements DubFragmentContract.View, SwipeRefreshLayout.OnRefreshListener {
  @BindView(R.id.rvDubList)
  RecyclerView rvDubList;
  @BindView(R.id.swipe)
  SwipeRefreshLayout swipe;

  @Inject
  DubListAdapter adapter;
  //private String call;

  @Inject
  DubFragmentPresenter presenter;
  private LinearLayoutManager layoutManager;
  private int position = 0;
  ProgressBar pb;
  Dialog dialog;
  TextView cur_val;
  private MediaPlayer player;
  @Inject
  BlockDialog dialog1;

  private CountDownTimer cTimer;

  @Override
  public void userBlocked() {
    dialog1.show();
  }

  @Inject
  public DubFragment() {
    // Required empty public constructor
  }

  public void getPosition(int position) {
    this.position = position;
  }

  @Override
  public void onStop() {
    try {
      if (player != null) {
        if (player.isPlaying()) {
          try {

            player.stop();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        player.release();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      if (cTimer != null) {
        cTimer.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    super.onStop();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dub, container, false);
    ButterKnife.bind(this, rootView);
    presenter.setView(this);
    layoutManager = new LinearLayoutManager(getActivity());
    rvDubList.setLayoutManager(layoutManager);
    adapter.setListener(presenter.getPresenter());
    rvDubList.setAdapter(adapter);

    rvDubList.addOnScrollListener(recyclerViewOnScrollListener);
    //rvDubList.addItemDecoration(new DividerItemDecoration(rvDubList.getContext(), DividerItemDecoration.VERTICAL));

    presenter.loadData(0, 20);
    presenter.getData(position);
    // Initialize a new media player instance

    player = new MediaPlayer();
    swipe.setOnRefreshListener(this);
    return rootView;
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
          int[] firstVisibleItemPositions = new int[20];
          int firstVisibleItemPosition =
              ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
          presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
      };

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
  public void setMax(Integer max) {
    new Runnable() {
      public void run() {
        pb.setMax(max);
      }
    };
  }

  @Override
  public void startedDownload() {
    showProgress();
    if (player != null) {
      try {
        player.stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    presenter.startedDownload();
  }

  @Override
  public void progress(int downloadedSize) {
    if(pb!=null)
    pb.setProgress(downloadedSize);
  }

  @Override
  public void finishedDownload(String path, String name, String musicId) {
    dialog.dismiss();

    if (getActivity() != null) {
      ((DubCategoryActivity) (getActivity())).returnSelectedDubSound(musicId, path, name);
    }
  }

  @Override
  public void play(String audio, boolean play, int position, String duration) {
    if (player != null) {
      try {
        if (player.isPlaying()) {
          try {
            player.stop();
            player.reset();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        player.release();
        player = null;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    try {
      if (cTimer != null) {
        cTimer.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (play) {

      try {
        if (player == null) {

          player = new MediaPlayer();
        }

        audio = audio.substring(0, audio.length() - 3) + "mp3";

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(AppController.getInstance(), Uri.parse(audio));

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          @Override
          public void onPrepared(MediaPlayer player) {
            // TODO Auto-generated method stub

            player.start();

            displaySoundDuration(position, duration);
          }
        });

        player.prepareAsync();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void isLoading(boolean isLoading) {
    if (!isLoading && swipe != null && swipe.isRefreshing()) swipe.setRefreshing(false);
  }

  @Override
  public void reload() {

  }

  void showProgress() {
    dialog = new Dialog(getContext());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.myprogressdialog);
    dialog.setTitle("Download Progress");

    cur_val = (TextView) dialog.findViewById(R.id.tv1);
    cur_val.setText(getString(R.string.downloading));
    dialog.show();

    pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
    pb.setProgress(0);
    pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
  }

  @Override
  public void onRefresh() {
    presenter.loadData(0, 20);
  }

  private void displaySoundDuration(int position, String time) {
    //time format-    //mm:ss
    ViewHolder viewHolder = (ViewHolder) rvDubList.findViewHolderForAdapterPosition(position);

    try {

      if (viewHolder != null) {

        String[] units = time.split(":"); //will break the string up into an array
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        int duration = (60 * minutes + seconds) * 1000; //add up our values
        //int duration = (60 * minutes + seconds+1) * 1000; //add up our values
        viewHolder.tvPlaybackTime.setText(getString(R.string.string_298));
        cTimer = new CountDownTimer(duration, 1000) {

          public void onTick(long millisUntilFinished) {
            long milliSec = (duration - millisUntilFinished) / 1000;

            long sec = milliSec % 60;
            long min = milliSec / 60;
            //if (min < 10) {
            //  if (sec < 10) {
            //    viewHolder.tvPlaybackTime.setText("0" + min + ":0" + sec);
            //  } else {
            //    viewHolder.tvPlaybackTime.setText("0" + min + ":" + sec);
            //  }
            //} else {
            if (sec < 10) {
              viewHolder.tvPlaybackTime.setText(min + getString(R.string.string_299) + sec);
            } else {
              viewHolder.tvPlaybackTime.setText(min + getString(R.string.semicolon) + sec);
            }
            //}
          }

          @Override
          public void onFinish() {
            //cTimer.start();
            viewHolder.tvPlaybackTime.setText(time);
          }
        };

        cTimer.start();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
