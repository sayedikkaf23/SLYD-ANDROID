package chat.hola.com.app.Service;

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
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.model.Post;
import chat.hola.com.app.post.model.PostData;
import chat.hola.com.app.post.model.UploadObserver;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.google.gson.Gson;
import dagger.android.DaggerService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import retrofit2.Response;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

//import com.coremedia.iso.IsoFile;
//import com.coremedia.iso.boxes.Container;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
//import com.googlecode.mp4parser.FileDataSourceImpl;
//import com.googlecode.mp4parser.authoring.Movie;
//import com.googlecode.mp4parser.authoring.Track;
//import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
//import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
//import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
//import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

/**
 * Created by DELL on 3/8/2018.
 */

public class PostService extends DaggerService implements UploadCallback {
  private static final String TAG = "PostService";

  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder1, builder2;

  @Inject
  HowdooService service;
  @Inject
  UploadObserver uploadObserver;
  @Inject
  PostDb postDb;

  @Inject
  PostObserver postObserver;
  //    Post post;
  File fileMain, fileMp4, finalFilterFile;
  //    private boolean fbShare = false;
  //    private boolean twitterShare = false;
  //    private boolean instaShare = false;
  //    private boolean isFbShareCompleted = false;
  //    private boolean isTwitterShareCompleted = false;
  public static final String CHANNEL_ONE_ID = "com.howdoo.chat.TWO";
  public static final String CHANNEL_ONE_NAME = "Channel Two";
  SessionApiCall sessionApiCall = new SessionApiCall();

  private Context context;
  private int j;

  private Map<String, Object> postDetails;
  private Map<String, Object> postDetail;
  private List<PostData> posts;
  //    private Bus bus = AppController.getBus();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {

    return null;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    //        Intent intent = new Intent("chat.hola.com.howdoo.Service");
    //        sendBroadcast(rootIntent);
    if (notificationManager != null) {

      if (posts != null) {
        int size = posts.size();
        if (size > 0) {

          for (int i = 0; i < size; i++) {
            notificationManager.cancel(Integer.parseInt(posts.get(i).getId()));
          }
        }
      }
    }
    super.onTaskRemoved(rootIntent);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    //Bundle bundle = intent.getBundleExtra("data");
    context = this;
    postIt();
    return START_NOT_STICKY;
  }

  private void postIt() {

    posts = postDb.getAllData();

    int size = posts.size();

    if (size > 0) {

      postDetails = new HashMap<>();

      for (int i = 0; i < size; i++) {
        j = i;

        Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {

            PostData postData = posts.get(j);
            String data = postData.getData();
            Post post = new Gson().fromJson(data, Post.class);

            ArrayList<String> files = post.getFiles();
            String audioFile = post.getAudioFile();

            //                        fbShare = postData.isFbShare();
            //                        twi tterShare = postData.isTwitterShare();
            //                        instaShare = postData.isInstaShare();

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
            if (post.isDub()) {
              //dub video
              Bitmap bMap = ThumbnailUtils.createVideoThumbnail(files.get(0),
                  MediaStore.Video.Thumbnails.MICRO_KIND);
              contentView.setImageViewBitmap(R.id.image, bMap);
              contentView.setImageViewResource(R.id.play,
                  R.drawable.ic_play_circle_outline_black_24dp);
              contentView.setTextViewText(R.id.title, "Processing video...");
            } else if (post.getTypeForCloudinary().equals(Constants.Post.IMAGE)) {
              //image
              Bitmap bitmap = BitmapFactory.decodeFile(post.getPathForCloudinary());
              contentView.setImageViewBitmap(R.id.image, bitmap);
              contentView.setTextViewText(R.id.title, "Posting...");
            } else {
              // normal video
              Bitmap bMap = ThumbnailUtils.createVideoThumbnail(post.getPathForCloudinary(),
                  MediaStore.Video.Thumbnails.MICRO_KIND);
              contentView.setImageViewBitmap(R.id.image, bMap);
              contentView.setImageViewResource(R.id.play,
                  R.drawable.ic_play_circle_outline_black_24dp);
              contentView.setTextViewText(R.id.title, "Posting...");
            }

            contentView.setProgressBar(R.id.progress, 100, 0, true);

            postDetail = new HashMap<>();

            postDetail.put("contentView", contentView);
            postDetail.put("post", post);

            postDetails.put(post.getId(), postDetail);

            builder1 =
                new NotificationCompat.Builder(context, CHANNEL_ONE_ID).setContent(contentView)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(false)
                    .setOngoing(false);

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder2 = new NotificationCompat.Builder(context, CHANNEL_ONE_ID).setSmallIcon(
                R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setOngoing(false)
                .setSound(soundUri);

            notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME,
                  NotificationManager.IMPORTANCE_HIGH);
              notificationChannel.enableLights(true);
              notificationChannel.setLightColor(Color.RED);
              notificationChannel.setShowBadge(true);
              notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
              notificationManager.createNotificationChannel(notificationChannel);
            }

            if (post.isDub()) {

              if (postData.isMerged()) {

                normalPost(post);
              } else {

                concatVideoCommand(files, audioFile, post.getId(), post.getFilterColor(), postData);
              }
            } else {

              normalPost(post);
            }
            //start notification
            if (notificationManager != null) {
              notificationManager.notify(Integer.parseInt(post.getId()), builder1.build());
            }
          }
        });
        thread.start();
      }
    } else {
      stopSelf();
    }
  }

  private void normalPost(Post post) {

    try {
      String requestId = MediaManager.get()
          .upload(post.getPathForCloudinary())
          .option(Constants.Post.FOLDER, post.isStory() ? Constants.Cloudinary.stories : Constants.Cloudinary.posts)
          .option(Constants.Post.RESOURCE_TYPE, post.getTypeForCloudinary())
          .callback(this)
          .constrain(TimeWindow.immediate())
          .dispatch();

      addRequestIdForPost(post.getId(), requestId);
    } catch (Exception ignored) {

    }
  }

  @Override
  public void onDestroy() {

    //        bus.unregister(this);
    super.onDestroy();
  }

  @Override
  public void onStart(String requestId) {

  }

  @Override
  public void onProgress(String requestId, long bytes, long totalBytes) {

  }

  @Override
  public void onSuccess(String requestId, Map resultData) {
    String type = resultData.get("resource_type").equals("video") ? "1" : "0";
    String url = String.valueOf(resultData.get("url"));

    Map<String, Object> postDetails = fetchPostDetailsFromRequestId(requestId);

    if (postDetails != null) {

      Post post = (Post) postDetails.get("post");

      Map<String, Object> map = new HashMap<>();

      map.put("thumbnailUrl1",
          url.replace("mp4", "jpg"));//url.replace("upload/", "upload/t_media_lib_thumb/"));
      map.put("imageUrl1", url);
      map.put("mediaType1", type);
      map.put("cloudinaryPublicId1", resultData.get("public_id"));
      map.put("imageUrl1Width", String.valueOf(resultData.get("width")));
      map.put("imageUrl1Height", String.valueOf(resultData.get("height")));
      //        if (data != null && !data.trim().equals("")) {
      map.put("musicId", post.getMusicId());
      map.put("hasAudio1", "1");
      map.put("story", post.isStory());
      if (post.getHashTags() != null && !TextUtils.isEmpty(post.getHashTags())) {
        map.put("hashTags", post.getHashTags());
      }

      if (post.getLatitude() != null && !TextUtils.isEmpty(post.getLatitude())) {
        map.put("latitude", Double.parseDouble(post.getLatitude()));
      }

      if (post.getLongitude() != null && !TextUtils.isEmpty(post.getLongitude())) {
        map.put("longitude", Double.parseDouble(post.getLongitude()));
      }

      if (post.getLocation() != null && !TextUtils.isEmpty(post.getLocation())) {
        map.put("location", post.getLocation());
      }

      if (post.getCountrySname() != null && !TextUtils.isEmpty(post.getCountrySname())) {
        map.put("countrySname", post.getCountrySname());
      }

      if (post.getCity() != null && !TextUtils.isEmpty(post.getCity())) {
        map.put("city", post.getCity());
      }

      if (post.getChannelId() != null && !TextUtils.isEmpty(post.getChannelId())) {
        map.put("channelId", post.getChannelId());
      }

      if (post.getCategoryId() != null && !TextUtils.isEmpty(post.getCategoryId())) {
        map.put("categoryId", post.getCategoryId());
      }

      map.put("title", post.getTitle());

      //userTag
      String regexPattern1 = "(@\\w+)";
      Pattern p1 = Pattern.compile(regexPattern1);
      Matcher m1 = p1.matcher(post.getTitle());
      int i = 0;
      ArrayList<String> strings = new ArrayList<>();
      String userTag[] = new String[i + 1];
      while (m1.find()) {
        strings.add(m1.group(1).replace("@", ""));
        // userTag[i++] = m1.group(1).replace("@", "");
      }

      map.put("userTags", strings);
      map.put("isPaid",post.isPaid());
      map.put("postAmount",post.getPostAmount());

      startPost(type, url, (post).getId(), (RemoteViews) postDetails.get("contentView"), map);
    }
  }

  private void startPost(String type, String mediaUrl, String postId, RemoteViews contentView,
      Map<String, Object> map) {

    service.createPost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<Data>>() {
          @Override
          public void onNext(Response<Data> responseBodyResponse) {

            switch (responseBodyResponse.code()) {
              case 200:
                postObserver.postData(true);
                if (notificationManager != null) {
                  contentView.setTextViewText(R.id.title, "Posted successfully");
                  contentView.setProgressBar(R.id.progress, 100, 100, false);
                  notificationManager.notify(Integer.parseInt(postId),
                      builder2.setContent(contentView).build());
                  notificationManager.cancel(Integer.parseInt(postId));
                }

                postDb.delete(postId);
                //                            if (postDb.delete(postId))
                //                                onStartCommand(null, 0, 0);
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
                            startPost(type, mediaUrl, postId, contentView, map);
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
              default:
                stopSelf();
                break;
            }
          }

          @Override
          public void onError(Throwable e) {

            if (notificationManager != null) {
              contentView.setTextViewText(R.id.title, "Failed to post..");
              contentView.setProgressBar(R.id.progress, 100, 100, false);
              notificationManager.notify(Integer.parseInt(postId),
                  builder2.setContent(contentView).build());
              //                            postDb.delete(postId);
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

      Map<String, Object> postDetails = fetchPostDetailsFromRequestId(requestId);
      if (postDetails != null) {

        RemoteViews contentView = (RemoteViews) postDetails.get("contentView");
        contentView.setTextViewText(R.id.title, "Failed to post..");
        contentView.setProgressBar(R.id.progress, 100, 100, false);

        String postId = ((Post) postDetails.get("post")).getId();
        notificationManager.notify(Integer.parseInt(postId),
            builder2.setContent(contentView).build());
        //                postDb.delete(postId);

      }
    }
  }

  @Override
  public void onReschedule(String requestId, ErrorInfo error) {

  }

  //video
  private void concatVideoCommand(ArrayList<String> files, String audioFile, String postId,
      String postFilterColor, PostData originalPostData) {
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder filterComplex = new StringBuilder();
    filterComplex.append("-filter_complex,");

    Log.d(TAG, "concatVideoCommand: " + files.size());
    for (int i = 0; i < files.size(); i++) {
      stringBuilder.append("-i" + "," + files.get(i) + ",");
      filterComplex.append("[")
          .append(i)
          .append(":v")
          .append(i)
          .append("] [")
          .append(i)
          .append(":a")
          .append(i)
          .append("] ");
    }
    filterComplex.append("concat=n=").append(files.size()).append(":v=1:a=1 [v] [a]");
    String[] inputCommand = stringBuilder.toString().split(",");
    String[] filterCommand = filterComplex.toString().split(",");

    //        String filePrefix = "reverse_video";
    //        String fileExtn = ".mp4";
    File f = new File("/storage/emulated/0/Download", System.currentTimeMillis() + ".mp4");
    int fileNo = 0;
    while (f.exists()) {
      fileNo++;
      f = new File("/storage/emulated/0/Download", System.currentTimeMillis() + "V" + ".mp4");
    }
    //        String filePath = f.getAbsolutePath();
    String[] destinationCommand = { "-map", "[v]", "-map", "[a]", f.getAbsolutePath() };
    execFFmpegBinary(combine(inputCommand, filterCommand, destinationCommand), audioFile, postId,
        postFilterColor, f.getAbsolutePath(), originalPostData);
  }

  private void execFFmpegBinary(final String[] command, String audioFile, String postId,
      String postFilterColor, String path, PostData originalPostData) {

    FFmpeg.executeAsync(command, (executionId, returnCode) -> {

      if (returnCode == RETURN_CODE_SUCCESS) {
        //Async command execution completed successfully.
        mergeFiles(path, audioFile, postId, postFilterColor, originalPostData);
      } else if (returnCode == RETURN_CODE_CANCEL) {
        //Async command execution cancelled by user.

      } else {
        //Async command execution failed

      }
    });
  }

  public static String[] combine(String[] arg1, String[] arg2, String[] arg3) {
    String[] result = new String[arg1.length + arg2.length + arg3.length];
    System.arraycopy(arg1, 0, result, 0, arg1.length);
    System.arraycopy(arg2, 0, result, arg1.length, arg2.length);
    System.arraycopy(arg3, 0, result, arg1.length + arg2.length, arg3.length);
    return result;
  }

  //    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
  //        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
  //        long currentSample = 0;
  //        double currentTime = 0;
  //        int i = 0;
  //        for (double timeOfSyncSample : timeOfSyncSamples)
  //
  //        {
  //            timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
  //
  //        }
  //        currentTime += (double) timeOfSyncSamples[i] / (double) track.getTrackMetaData().getTimescale();
  //        currentSample++;
  //
  //        double previous = 0;
  //        for (double timeOfSyncSample : timeOfSyncSamples)
  //
  //        {
  //            if (timeOfSyncSample > cutHere) {
  //                if (next) {
  //                    return timeOfSyncSample;
  //                } else {
  //                    return previous;
  //                }
  //            }
  //            previous = timeOfSyncSample;
  //        }
  //        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
  //
  //    }

  private void mergeFiles(String video, String audioFile, String postId, String postFilterColor,
      PostData originalPostData) {
    //        Handler handler = new Handler();
    //        handler.postDelayed(new Runnable() {
    //            public void run() {
    //
    //
    //                try {
    //                    Movie orig_movie = MovieCreator.build(video);
    //
    //                    AACTrackImpl aacTrack = new AACTrackImpl(new FileDataSourceImpl(audioFile));
    //
    //                    IsoFile isoFile = new IsoFile(video);
    //                    double lengthInSeconds = (double)
    //                            isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
    //                            isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
    //
    //
    //                    Track track = (Track) orig_movie.getTracks().get(0);
    //
    //                    Track audioTrack = (Track) aacTrack;
    //
    //
    //                    double startTime1 = 0;
    //                    double endTime1 = lengthInSeconds;
    //
    //                    boolean timeCorrected = false;
    //
    //                    if (audioTrack.getSyncSamples() != null && audioTrack.getSyncSamples().length > 0) {
    //                        if (timeCorrected) {
    //
    //                            throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
    //                        }
    //                        startTime1 = correctTimeToSyncSample(audioTrack, startTime1, false);
    //                        endTime1 = correctTimeToSyncSample(audioTrack, endTime1, true);
    //                        timeCorrected = true;
    //                    }
    //
    //                    long currentSample = 0;
    //                    double currentTime = 0;
    //                    double lastTime = -1;
    //                    long startSample1 = -1;
    //                    long endSample1 = -1;
    //
    //
    //                    for (int i = 0; i < audioTrack.getSampleDurations().length; i++) {
    //                        long delta = audioTrack.getSampleDurations()[i];
    //
    //                        if (currentTime > lastTime && currentTime <= startTime1) {
    //                            // current sample is still before the new starttime
    //                            startSample1 = currentSample;
    //                        }
    //                        if (currentTime > lastTime && currentTime <= endTime1) {
    //                            // current sample is after the new start time and still before the new endtime
    //                            endSample1 = currentSample;
    //                        }
    //
    //                        lastTime = currentTime;
    //                        currentTime += (double) delta / (double) audioTrack.getTrackMetaData().getTimescale();
    //                        currentSample++;
    //                    }
    //
    //
    //                    CroppedTrack cropperAacTrack = new CroppedTrack(aacTrack, startSample1, endSample1);
    //
    //                    Movie movie = new Movie();
    //                    movie.addTrack(track);
    //                    movie.addTrack(cropperAacTrack);
    //
    //                    Container mp4file = new DefaultMp4Builder().build(movie);
    //
    //                    fileMain = new File("/storage/emulated/0/Download", System.currentTimeMillis() + ".mp4");
    //                    if (!fileMain.exists())
    //                        fileMain.createNewFile();
    //                    mp4file.writeContainer(new FileOutputStream(fileMain).getChannel());
    //                    fileMp4 = new File("/storage/emulated/0/Download", System.currentTimeMillis() + ".mp4");
    //
    //
    //                    String[] comand = {"-y", "-i", fileMain.getAbsolutePath(), "-c", "copy", "-movflags", "+faststart", fileMp4.getAbsolutePath()};
    ////                    String[] comand={"ffmpeg -i",fileMain.getAbsolutePath()," -c copy -movflags +faststart "," -pix_fmt yuv420p",fileMp4.getAbsolutePath()};
    //
    //                    execFFmpegBinaryFinal(comand, postId, postFilterColor, originalPostData);
    //
    //
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }, 15000);
  }

  private void execFFmpegBinaryFinal(final String[] command, String postId, String postFilterColor,
      PostData originalPostData) {

    FFmpeg.executeAsync(command, (executionId, returnCode) -> {

      if (returnCode == RETURN_CODE_SUCCESS) {
        //Async command execution completed successfully.
        finalFilterFile =
            new File("/storage/emulated/0/Download", System.currentTimeMillis() + ".mp4");

        //String[] command = {"-y", "-i", fileMp4.getAbsolutePath(), "-f", "lavfi", "-i", "color=" + postFilterColor + ":s=480x720", "-filter_complex", "blend=shortest=1:all_mode=normal:all_opacity=0.7", "-vcodec", "mpeg4", finalFilterFile.getAbsolutePath()};
        String[] commandNew = {
            "-y", "-i", fileMp4.getAbsolutePath(), "-f", "lavfi", "-i",
            "color=" + postFilterColor + ":s=480x720", "-filter_complex",
            "blend=shortest=1:all_mode=normal:all_opacity=0.7", "-vcodec", "libx264",
            finalFilterFile.getAbsolutePath()
        };
        execFFmpegFilterFinal(commandNew, postId, originalPostData);
      } else if (returnCode == RETURN_CODE_CANCEL) {
        //Async command execution cancelled by user.

      } else {
        //Async command execution failed

      }
    });
  }

  private void execFFmpegFilterFinal(final String[] command, String postId,
      PostData originalPostData) {

    FFmpeg.executeAsync(command, (executionId, returnCode) -> {

      if (returnCode == RETURN_CODE_SUCCESS) {
        //Async command execution completed successfully.
        Map<String, Object> postData = (Map<String, Object>) postDetails.get(postId);

        Post post = (Post) postData.get("post");
        RemoteViews contentView = (RemoteViews) postData.get("contentView");
        post.setPathForCloudinary(finalFilterFile.getAbsolutePath());
        post.setTypeForCloudinary(Constants.Post.VIDEO);

        contentView.setTextViewText(R.id.title, "Posting...");
        contentView.setProgressBar(R.id.progress, 100, 0, true);
        notificationManager.notify(Integer.parseInt(postId),
            builder2.setContent(contentView).build());

        postDb.updateFileMergedStatus(originalPostData);
        normalPost(post);
      } else if (returnCode == RETURN_CODE_CANCEL) {
        //Async command execution cancelled by user.

      } else {
        //Async command execution failed

      }
    });
  }

  @SuppressWarnings("unchecked")

  private void addRequestIdForPost(String postId, String requestId) {

    Map<String, Object> postDetail = (Map<String, Object>) (postDetails.get(postId));

    if (postDetail != null) {

      postDetail.put("requestId", requestId);

      postDetails.put(postId, postDetail);
    }
  }

  @SuppressWarnings("unchecked")

  private Map<String, Object> fetchPostDetailsFromRequestId(String requestId) {

    Map<String, Object> postDetail = null;
    Set<String> keys = postDetails.keySet();
    for (String key : keys) {
      postDetail = (Map<String, Object>) (postDetails.get(key));

      if (postDetail.get("requestId") != null) {

        if (postDetail.get("requestId").equals(requestId)) {

          return postDetail;
        }
      }
    }

    return postDetail;
  }
}
