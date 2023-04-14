package chat.hola.com.app.poststory;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.model.StoryObserver;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.models.SessionObserver;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.squareup.otto.Bus;
import dagger.android.DaggerService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

/**
 * <h1>StoryService</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/2018.
 */

public class StoryService extends DaggerService implements UploadCallback {
  private static final String CHANNEL_ONE_ID = "story_post";
  private static final CharSequence CHANNEL_ONE_NAME = "story";
  private String requestId;
  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder1, builder2;
  private RemoteViews contentView;
  private int notificationId = 121;
  private Object isPrivate = false;
  private String path;
  @Inject
  HowdooService service;
  @Inject
  StoryObserver storyObserver;
  private String duration;
  private String caption = "";
  private static Bus bus = AppController.getBus();
  String cloudinaryMediaType;
  SessionApiCall sessionApiCall = new SessionApiCall();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    if (notificationManager != null) {
      notificationManager.cancel(
          notificationId);//re(Integer.parseInt(post.getId()), builder2.build());
    }
    super.onTaskRemoved(rootIntent);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Bundle bundle = intent.getBundleExtra("data");
    isPrivate = bundle.getBoolean("isPrivate");

    contentView = new RemoteViews(getPackageName(), R.layout.custom_push);

    if (bundle.getInt("type") == 0) {
      cloudinaryMediaType = "image";
      Bitmap bitmap = BitmapFactory.decodeFile(bundle.getString("path"));
      contentView.setImageViewBitmap(R.id.image, bitmap);
    } else {
      cloudinaryMediaType = "video";
      Bitmap bMap = ThumbnailUtils.createVideoThumbnail(bundle.getString("path"),
          MediaStore.Video.Thumbnails.MICRO_KIND);
      contentView.setImageViewBitmap(R.id.image, bMap);
      contentView.setImageViewResource(R.id.play, R.drawable.ic_play_circle_outline_black_24dp);
    }
    contentView.setTextViewText(R.id.title, "Posting story...");
    contentView.setProgressBar(R.id.progress, 100, 0, true);

    builder1 = new NotificationCompat.Builder(this, CHANNEL_ONE_ID).setContent(contentView)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
        .setAutoCancel(false)
        .setOngoing(false);

    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    builder2 = new NotificationCompat.Builder(this, CHANNEL_ONE_ID).setContent(contentView)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
        .setAutoCancel(true)
        .setOngoing(false)
        .setSound(soundUri);

    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    NotificationChannel notificationChannel;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME,
          notificationManager.IMPORTANCE_HIGH);
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(Color.RED);
      notificationChannel.setShowBadge(true);
      notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
      notificationManager.createNotificationChannel(notificationChannel);
    }
    path = bundle.getString("path");
    duration = bundle.getString("duration");
    caption = bundle.getString("caption");
    try {
      requestId = MediaManager.get()
          .upload(path)
          .option("folder", "stories")
          .option(Constants.Post.RESOURCE_TYPE, cloudinaryMediaType)
          .callback(this)
          .constrain(TimeWindow.immediate())
          .dispatch();
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
    return START_NOT_STICKY;
  }

  @Override
  public void onStart(String requestId) {
    if (notificationManager != null) {
      notificationManager.notify(notificationId, builder1.build());
    }
  }

  @Override
  public void onProgress(String requestId, long bytes, long totalBytes) {
    Double progress = (double) bytes / totalBytes;
  }

  @Override
  public void onSuccess(String requestId, Map resultData) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("type", resultData.get("resource_type").equals("video") ? "2" : "1");
    parameters.put("urlPath", resultData.get(Constants.Post.URL));
    parameters.put("thumbnail", resultData.get(Constants.Post.URL));
    parameters.put("isPrivate", isPrivate);
    if (duration != null) parameters.put("duration", duration);
    if (caption != null && !caption.isEmpty()) parameters.put("caption", caption);

    service.postStory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, parameters)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<StoryPost>>() {
          @Override
          public void onNext(Response<StoryPost> response) {
            switch (response.code()) {
              case 200:
                if (notificationManager != null) {
                  contentView.setTextViewText(R.id.title, "Story posted successfully");
                  contentView.setProgressBar(R.id.progress, 100, 100, false);
                  notificationManager.notify(notificationId, builder2.build());
                }

                response.body().setSuccess(true);
                storyObserver.postData(response.body());

                JSONObject obj = new JSONObject();
                try {
                  obj.put("eventName", "myStoryUpdate");
                } catch (JSONException e) {
                  e.printStackTrace();
                }
                bus.post(obj);
                //uploadSuccessListner.uploaded();
                break;
              case 406:
                SessionObserver sessionObserver = new SessionObserver();
                sessionObserver.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                      @Override
                      public void onNext(Boolean flag) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                            try {
                              MediaManager.get()
                                  .upload(path)
                                  .option("folder", "stories")
                                  .option(Constants.Post.RESOURCE_TYPE, cloudinaryMediaType)
                                  .callback(StoryService.this)
                                  .constrain(TimeWindow.immediate())
                                  .dispatch();
                            } catch (Exception ignored) {
                              ignored.printStackTrace();
                            }
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
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            if (notificationManager != null) {
              contentView.setTextViewText(R.id.title, "Failed to post story, please try again");
              contentView.setProgressBar(R.id.progress, 100, 100, false);
              notificationManager.notify(notificationId, builder2.build());
            }
          }

          @Override
          public void onComplete() {

          }
        });
  }

  @Override
  public void onError(String requestId, ErrorInfo error) {

    if (notificationManager != null) {
      contentView.setTextViewText(R.id.title, error.toString());
      contentView.setProgressBar(R.id.progress, 100, 100, false);
      notificationManager.notify(notificationId, builder2.build());
    }
  }

  @Override
  public void onReschedule(String requestId, ErrorInfo error) {

  }
}
