package chat.hola.com.app.dubly;

import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import com.ezcall.android.R;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;

/**
 * <h1>DubCategoryActivity</h1>
 * <p>All the Dubs appears on this screen.
 * User can add new Dubs also</p>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class DubsActivity extends DaggerAppCompatActivity implements DubsContract.View {
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  public static int page = 0;
  private int downloadedSize;
  private Unbinder unbinder;

  @Inject
  TypefaceManager typefaceManager;
  @Inject
  SessionManager sessionManager;
  @Inject
  DubsContract.Presenter presenter;
  @Inject
  DubsAdapter adapter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.rvDubList)
  RecyclerView rvDubList;
  @BindView(R.id.tvTbTitle)
  TextView tvTbTitle;
  @BindView(R.id.progressbar)
  ProgressBar progresbar;

  private MediaPlayer player;
  private String categoryId = "";
  private LinearLayoutManager layoutManager;
  ProgressBar pb;
  Dialog dialog;
  TextView cur_val;
  @Inject
  BlockDialog  blockDialog;
  private CountDownTimer cTimer;

  @Override
  public void userBlocked() {
    blockDialog.show();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dublist_activity);
    unbinder = ButterKnife.bind(this);
    //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    categoryId = getIntent().getStringExtra("categoryId");
    String categoryName = getIntent().getStringExtra("categoryName");
    //        call = getIntent().getStringExtra("call");
    if (categoryName != null || !categoryName.isEmpty()) tvTbTitle.setText(categoryName);
    layoutManager = new LinearLayoutManager(this);
    tvTbTitle.setTypeface(typefaceManager.getSemiboldFont());
    rvDubList.setLayoutManager(layoutManager);
    rvDubList.addOnScrollListener(recyclerViewOnScrollListener);
    adapter.setListener(presenter.getPresenter());
    rvDubList.setAdapter(adapter);
    presenter.getDubs(0, PAGE_SIZE, categoryId);

    player = new MediaPlayer();
    toolbarSetup();
  }

  private void toolbarSetup() {
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }
  }

  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    stop();

    setResult(RESULT_CANCELED, new Intent());
    supportFinishAfterTransition();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    super.onDestroy();
  }

  @Override
  public void showMessage(String msg, int msgId) {

  }

  @Override
  public void sessionExpired() {
    if (sessionManager != null) sessionManager.sessionExpired(this);
  }

  @Override
  public void isInternetAvailable(boolean flag) {

  }

  @Override
  public void reload() {
    presenter.getDubs(0, PAGE_SIZE, categoryId);
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
              ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
          presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount,
              categoryId);
        }
      };

  @Override
  public void play(String audio, boolean isPlaying, int position, String duration) {

    stop();
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
    //            }
    //        }).start();
  }

  @Override
  public void progress(int i) {
    if(pb!=null)
    pb.setProgress(i);
  }

  @Override
  public void finishedDownload(String s, String filename, String musicId) {
    progresbar.setVisibility(View.GONE);
    dialog.dismiss();
    stop();

    Intent intent = new Intent();
    intent.putExtra("musicId", musicId);
    intent.putExtra("audio", s);
    intent.putExtra("name", filename);
    setResult(RESULT_OK, intent);
    supportFinishAfterTransition();
  }

  @Override
  protected void onStop() {
    stop();

    super.onStop();
  }

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
    try {
      if (cTimer != null) {
        cTimer.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void startDownload(String url, String musicId, String name) {
    progresbar.setVisibility(View.VISIBLE);
    new Thread(() -> downloadFile(url, musicId, name)).start();
  }

  private void downloadFile(String filePath, String musicId, String name) {

    try {

      URL url = new URL(filePath);
      InputStream is = url.openStream();

      DataInputStream dis = new DataInputStream(is);

      byte[] buffer = new byte[1024];
      int length;

      String filename = filePath.substring(filePath.lastIndexOf('/') + 1);
      FileOutputStream fos = new FileOutputStream(new File(getExternalFilesDir(null) + "/" + name));

      while ((length = dis.read(buffer)) > 0) {
        fos.write(buffer, 0, length);
        downloadedSize += length;
        // update the progressbar //
        progress(downloadedSize);
      }
      //close the output stream when complete //
      fos.close();

      runOnUiThread(
          () -> finishedDownload(getExternalFilesDir(null) + "/" + filename, name, musicId));
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

    return err;
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
            //    if (sec < 10) {
            //        viewHolder.tvPlaybackTime.setText("0" + min + ":0" + sec);
            //    } else {
            //        viewHolder.tvPlaybackTime.setText("0" + min + ":" + sec);
            //    }
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

  void showProgress() {
    dialog = new Dialog(this);
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
}
