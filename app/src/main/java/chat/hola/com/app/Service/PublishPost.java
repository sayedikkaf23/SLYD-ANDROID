package chat.hola.com.app.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.SocialObserver;
import chat.hola.com.app.post.model.Post;
import chat.hola.com.app.post.model.PostData;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.squareup.otto.Bus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

/**
 * Created by moda on 18/12/18.
 */

public class PublishPost {
    private String orientation;
    private PostData postData;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder2;

    private String mediaPath;
    private HowdooService service;
    private SocialObserver socialShareObserver;

    private PostDb postDb;

    private String appName;
    private PostObserver postObserver;

    private String type;
    //    private boolean fbShare = false;
    //    private boolean twitterShare = false;
    //    private boolean instaShare = false;
    //    private boolean isFbShareCompleted = false;
    //    private boolean isTwitterShareCompleted = false;
    private static final String CHANNEL_ONE_ID = "com.howdoo.chat.TWO";
    private static final String CHANNEL_ONE_NAME = "Channel Two";

    private Map<String, Object> postDetails;

    private List<PostData> posts;

    private static Bus bus = AppController.getBus();

    private String folderPath;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @SuppressWarnings("unchecked")
    public void retryPublishingPosts(HowdooService service, SocialObserver socialShareObserver) {

        this.socialShareObserver = socialShareObserver;
        this.service = service;

        appName =
                ApiOnServer.APP_NAME;//AppController.getInstance().getResources().getString(R.string.app_name);

        final File imageFolder;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            imageFolder = new File(
                    AppController.getInstance().getApplicationContext().getExternalFilesDir(null)
                            + "/"
                            + AppController.getInstance().getResources().getString(R.string.app_name)
                            + "/Media/");
        } else {

            imageFolder = new File(
                    AppController.getInstance().getFilesDir() + "/" + AppController.getInstance()
                            .getResources()
                            .getString(R.string.app_name) + "/Media/");
        }
        if (!imageFolder.exists() && !imageFolder.isDirectory()) imageFolder.mkdirs();

        folderPath = imageFolder.getAbsolutePath();

        postDb = new PostDb(AppController.getInstance());
        postObserver = new PostObserver();
        //        uploadObserver = new UploadObserver();

        new PostAsync().execute();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private class PostAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            try {

                postIt();
            } catch (Exception e) {


                /*
                 * Should disable user from using the app
                 */

            }
            return null;
        }
    }

    private void postIt() {

        posts = postDb.getAllData();

        int size = posts.size();
        postDetails = new HashMap<>();

        if (size > 0) {

            for (int i = 0; i < size; i++) {
                final int j = i;

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        publishPost(posts.get(j));
                    }
                });
                thread.start();
            }
        }
    }

    private void normalPost(Post post) {

        //        Log.d("log1", post.getPathForCloudinary());
        File f = new File(post.getPathForCloudinary());
        //        post.setDuration(String.valueOf(getDuration(Uri.parse(f.getAbsolutePath()))));
        //        Log.d("log1", f.exists() + " " + Integer.parseInt(String.valueOf(f.length() / 1024)));

        //        if (f.exists()){
        //            File from = new File(f.getAbsolutePath(),f.getName());
        //            File to = new File(f.getAbsolutePath(),AppController.getInstance().getUserId()+".jpg");
        //            if(from.exists())
        //                from.renameTo(to);
        //        }

        try {

            mediaPath = post.getPathForCloudinary();
            orientation = "0";
            if (post.getTypeForCloudinary().equals("video")) {
                MediaMetadataRetriever m = new MediaMetadataRetriever();
                m.setDataSource(mediaPath);
                orientation = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            }
            post.setOrientation(Integer.parseInt(orientation));

            String requestId = MediaManager.get()
                    .upload(post.getPathForCloudinary())
                    .option(Constants.Post.FOLDER, post.isStory() ? Constants.Cloudinary.stories : Constants.Cloudinary.posts)
                    .option(Constants.Post.RESOURCE_TYPE, post.getTypeForCloudinary())
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            //                            Double progress = (double) bytes / totalBytes;
                            //                            Log.i("Cloudinary", "onProgress: " + progress);
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            if (post.isStory()) {
                                storyPost(requestId, post, resultData, orientation);
                            } else {
                                mediaPost(requestId, post, resultData, orientation);
                            }
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.d("post", "cloudinary onError: ");
                            if (notificationManager != null) {

                                Map<String, Object> postDetails = fetchPostDetailsFromRequestId(requestId);
                                if (postDetails != null) {

                                    RemoteViews contentView = (RemoteViews) postDetails.get("contentView");
                                    contentView.setTextViewText(R.id.title, "Failed to post..");
                                    contentView.setProgressBar(R.id.progress, 100, 100, false);

                                    String postId = ((Post) postDetails.get("post")).getId();
                                    notificationManager.notify(Integer.parseInt(postId),
                                            builder2.setContent(contentView).build());
                                }

                                boolean deleted = postDb.delete(post.getId());

                                try {
                                    File fdelete = new File(post.getPathForCloudinary());
                                    if (fdelete.exists()) {
                                        fdelete.delete();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }
                    })
                    .constrain(TimeWindow.immediate())
                    .dispatch();

            addRequestIdForPost(post.getId(), requestId);
        } catch (Exception ignored) {
            Log.d("post", "onError: ");
            ignored.printStackTrace();
        }
    }

    private void mediaPost(String requestId, Post post, Map resultData, String orientation) {
        type = resultData.get("resource_type").equals("video") ? "1" : "0";
        String url = String.valueOf(resultData.get("url"));
        String public_id = String.valueOf(resultData.get("public_id"));

        Map<String, Object> postDetails = fetchPostDetailsFromRequestId(requestId);

        if (postDetails != null) {
            Map<String, Object> map = new HashMap<>();

            if (post.getBusinessPostType() != null) {
                map.put("businessPostTypeId", post.getBusinessPostType());
            }
            if (post.getBusinessPrice() != null) map.put("businessPrice", post.getBusinessPrice());
            if (post.getBusinessUrl() != null) map.put("businessUrl", post.getBusinessUrl());
            if (post.getBusinessCurrency() != null) {
                map.put("businessCurrency", post.getBusinessCurrency());
            }
            if (post.getBusinessButtonText() != null) {
                map.put("businessButtonText", post.getBusinessButtonText());
            }
            if (post.getBusinessButtonColor() != null) {
                map.put("businessButtonColor", post.getBusinessButtonColor());
            }

            map.put("thumbnailUrl1",
                    url.replace("mp4", "jpg"));//url.replace("upload/", "upload/t_media_lib_thumb/"));
            map.put("imageUrl1", url);
            map.put("cloudinary_public_id", public_id);
            map.put("mediaType1", type);
            map.put("cloudinaryPublicId1", public_id);
            map.put("imageUrl1Width", String.valueOf(resultData.get("width")));
            map.put("imageUrl1Height", String.valueOf(resultData.get("height")));

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

            if (post.getPlaceId() != null && !TextUtils.isEmpty(post.getPlaceId())) {
                map.put("placeId", post.getPlaceId());
            }

            map.put("title", post.getTitle());
            map.put("orientation", orientation);
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

            map.put("allowDownload", post.isAllowDownload());
            map.put("allowComment", post.isAllowComments());
            map.put("allowDuet", post.isAllowDuet());

            map.put("cityForPost", post.getCityForPost());
            map.put("countryForPost", post.getCountryForPost());
            map.put("latForPost", post.getLatForPost());
            map.put("longForPost", post.getLongForPost());

            map.put("isPaid",post.isPaid());
            map.put("postAmount",post.getPostAmount());

            startPost(type, url, post, (RemoteViews) postDetails.get("contentView"), map,
                    post.getPathForCloudinary());
        }
    }

    private void startPost(String type, String mediaUrl, Post post, RemoteViews contentView,
                           Map<String, Object> map, String path) {

        service.createPost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Data>>() {
                    @Override
                    public void onNext(Response<Data> responseBodyResponse) {

                        switch (responseBodyResponse.code()) {
                            case 200:
                                if (notificationManager != null) {
                                    contentView.setTextViewText(R.id.title, "Posted successfully");
                                    contentView.setProgressBar(R.id.progress, 100, 100, false);
                                    notificationManager.notify(Integer.parseInt(post.getId()),
                                            builder2.setContent(contentView).build());
                                    notificationManager.cancel(Integer.parseInt(post.getId()));
                                }

                                boolean deleted = postDb.delete(post.getId());

                                //                                if (!post.isGallery())
                                //                                    deleteFileFromDisk(path);

                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("type", type);
                                    obj.put("mediaPath", mediaPath);
                                    obj.put("facebook", postData.isFbShare());
                                    obj.put("twitter", postData.isTwitterShare());
                                    obj.put("instagram", postData.isInstaShare());
                                    obj.put("url", mediaUrl);
                                    obj.put("eventName", "postCompleted");
                                    obj.put("data", responseBodyResponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                bus.post(obj);
                                //                                socialShareObserver.postData(new ShareWith(postData.isFbShare(), postData.isTwitterShare(), postData.isInstaShare()));
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
                                                        startPost(type, mediaUrl, post, contentView, map, path);
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
                            contentView.setTextViewText(R.id.title, "Failed to post..");
                            contentView.setProgressBar(R.id.progress, 100, 100, false);
                            notificationManager.notify(Integer.parseInt(post.getId()),
                                    builder2.setContent(contentView).build());
                        }

                        boolean deleted = postDb.delete(post.getId());

                        if (!post.isGallery()) deleteFileFromDisk(path);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //video
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void concatenateVideos(ArrayList<String> files, String audioFile, String postId,
                                   PostData originalPostData) {

        if (files.size() > 1) {

            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder filterComplex = new StringBuilder();
            filterComplex.append("-y,-filter_complex,");

            if (audioFile == null) {
                for (int i = 0; i < files.size(); i++) {

                    stringBuilder.append("-i" + "," + files.get(i) + ",");

                    filterComplex.append("[")
                            .append(i)
                            .append(":v:0")
                            .append("] [")
                            .append(i)
                            .append(":a:0")
                            .append("] ");
                }

                filterComplex.append("concat=n=").append(files.size()).append(":v=1:a=1 [v] [a]");
            } else {
                for (int i = 0; i < files.size(); i++) {

                    stringBuilder.append("-i" + "," + files.get(i) + ",");

                    filterComplex.append("[").append(i).append(":v:0").append("] ");
                }

                filterComplex.append("concat=n=").append(files.size()).append(":v=1 [v]");
            }
            String[] inputCommand = stringBuilder.toString().split(",");
            String[] filterCommand = filterComplex.toString().split(",");

            File f = new File(folderPath, System.currentTimeMillis() + appName + ".mp4");

            if (f.exists()) {

                f = new File(folderPath, System.currentTimeMillis() + appName + "V.mp4");
            }

            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (audioFile == null) {
                //-vsync 2 added after updating ffmpeg to support android 10
                String[] destinationCommand = {
                        "-vsync", "2", "-crf", "29", "-vcodec", "libx264", "-preset", "ultrafast", "-map", "[v]", "-map",
                        "[a]", f.getAbsolutePath()
                };
                ///////
                //String[] destinationCommand = {"-preset", "ultrafast", "-crf", "29", "-map", "[v]", "-map", "[a]", f.getAbsolutePath()};

                execFFmpegToConcatenateVideos(combine(inputCommand, filterCommand, destinationCommand),
                        null, postId, f.getAbsolutePath(), originalPostData, files);
            } else {
                //-vsync 2 added after updating ffmpeg to support android 10
                String[] destinationCommand = {
                        "-vsync", "2", "-crf", "29", "-vcodec", "libx264", "-preset", "ultrafast", "-map", "[v]",
                        f.getAbsolutePath()
                };
                ///////
                //String[] destinationCommand = {"-preset", "ultrafast", "-crf", "29", "-map", "[v]", f.getAbsolutePath()};

                execFFmpegToConcatenateVideos(combine(inputCommand, filterCommand, destinationCommand),
                        audioFile, postId, f.getAbsolutePath(), originalPostData, files);
            }
        } else {

            execFFmpegToConcatenateVideos(null, audioFile, postId, files.get(0), originalPostData, files);
        }
    }

    private void execFFmpegToConcatenateVideos(final String[] command, String audioFile,
                                               String postId, String path, PostData originalPostData, ArrayList<String> files) {

        if (command != null) {

            FFmpeg.executeAsync(command, (executionId, returnCode) -> {

                if (returnCode == RETURN_CODE_SUCCESS) {
                    //Async command execution completed successfully.
                    mergeAudioAndVideoFiles(path, audioFile, postId, originalPostData, files);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    //Async command execution cancelled by user.

                } else {
                    //Async command execution failed

                }
            });
        } else {

            mergeAudioAndVideoFiles(path, audioFile, postId, originalPostData, files);
        }
    }

    private static String[] combine(String[] arg1, String[] arg2, String[] arg3) {
        String[] result = new String[arg1.length + arg2.length + arg3.length];
        System.arraycopy(arg1, 0, result, 0, arg1.length);
        System.arraycopy(arg2, 0, result, arg1.length, arg2.length);
        System.arraycopy(arg3, 0, result, arg1.length + arg2.length, arg3.length);
        return result;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void mergeAudioAndVideoFiles(String video, String audioFile, String postId,
                                         PostData originalPostData, ArrayList<String> files) {

        if (audioFile != null) {

            File fileMp4 = new File(folderPath, System.currentTimeMillis() + appName + ".mp4");

            if (!fileMp4.exists()) {
                try {
                    fileMp4.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String[] complexCommand = {
                    "-y", "-i", video,  "-itsoffset", "0.4",  "-i", audioFile, "-preset", "ultrafast", "-c:v", "copy", "-map",
                    "0:v:0", "-map", "1:a:0", "-shortest", fileMp4.getAbsolutePath()
            };
            execFFmpegToMergeAudioAndVideoFiles(complexCommand, postId, originalPostData,
                    fileMp4.getAbsolutePath(), files);
        } else {

            execFFmpegToMergeAudioAndVideoFiles(null, postId, originalPostData, video, files);
        }
    }

    private void execFFmpegToMergeAudioAndVideoFiles(final String[] command, String postId,
                                                     PostData originalPostData, String path, ArrayList<String> files) {

        if (command != null) {

            FFmpeg.executeAsync(command, (executionId, returnCode) -> {

                if (returnCode == RETURN_CODE_SUCCESS) {
                    //Async command execution completed successfully.
                    publishPostAfterProcessing(postId, path, originalPostData, files);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    //Async command execution cancelled by user.

                } else {
                    //Async command execution failed

                }
            });
        } else {

            publishPostAfterProcessing(postId, path, originalPostData, files);
        }
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

            if (postDetail != null) {
                if (postDetail.get("requestId") != null) {

                    if (postDetail.get("requestId").equals(requestId)) {

                        return postDetail;
                    }
                }
            }
        }

        return postDetail;
    }

    private void publishPost(PostData postData) {
        this.postData = postData;
        String data = postData.getData();
        Post post = new Gson().fromJson(data, Post.class);

        ArrayList<String> files = post.getFiles();
        String audioFile = post.getAudioFile();

        //                        fbShare = postData.isFbShare();
        //                        twi tterShare = postData.isTwitterShare();
        //                        instaShare = postData.isInstaShare();

        RemoteViews contentView =
                new RemoteViews(AppController.getInstance().getPackageName(), R.layout.custom_push);
        if (post.isDub()) {
            //dub video
            Bitmap bMap =
                    ThumbnailUtils.createVideoThumbnail(files.get(0), MediaStore.Video.Thumbnails.MICRO_KIND);
            contentView.setImageViewBitmap(R.id.image, bMap);
            contentView.setImageViewResource(R.id.play, R.drawable.ic_play_circle_outline_black_24dp);

            if (postData.isMerged()) {
                contentView.setTextViewText(R.id.title, "Posting...");
            } else {
                contentView.setTextViewText(R.id.title, "Processing video...");
            }
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
            contentView.setImageViewResource(R.id.play, R.drawable.ic_play_circle_outline_black_24dp);
            contentView.setTextViewText(R.id.title, "Posting...");
        }

        contentView.setProgressBar(R.id.progress, 100, 0, true);

        Map<String, Object> postDetail = new HashMap<>();

        postDetail.put("contentView", contentView);
        postDetail.put("post", post);

        postDetails.put(post.getId(), postDetail);

        NotificationCompat.Builder builder1 =
                new NotificationCompat.Builder(AppController.getInstance(), CHANNEL_ONE_ID).setContent(
                        contentView)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(AppController.getInstance().getResources(),
                                R.mipmap.ic_launcher))
                        .setAutoCancel(false)
                        .setOngoing(false);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder2 =
                new NotificationCompat.Builder(AppController.getInstance(), CHANNEL_ONE_ID).setSmallIcon(
                        R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(AppController.getInstance().getResources(),
                                R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setSound(soundUri);

        notificationManager = (NotificationManager) AppController.getInstance()
                .getSystemService(Context.NOTIFICATION_SERVICE);

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

                concatenateVideos(files, audioFile, post.getId(), postData);
            }
        } else {

            normalPost(post);
        }
        //start notification
        if (notificationManager != null) {
            notificationManager.notify(Integer.parseInt(post.getId()), builder1.build());
        }
    }

    @SuppressWarnings("unchecked")
    private void publishPostAfterProcessing(String postId, String path, PostData originalPostData,
                                            ArrayList<String> files) {

        Map<String, Object> postData = (Map<String, Object>) postDetails.get(postId);

        Post post = (Post) postData.get("post");
        RemoteViews contentView = (RemoteViews) postData.get("contentView");
        post.setPathForCloudinary(path);
        post.setTypeForCloudinary(Constants.Post.VIDEO);

        contentView.setTextViewText(R.id.title, "Posting...");
        contentView.setProgressBar(R.id.progress, 100, 0, true);
        notificationManager.notify(Integer.parseInt(postId), builder2.setContent(contentView).build());

        originalPostData.setData(new Gson().toJson(post));
        postDb.updateFileMergedStatus(originalPostData);

        for (int i = 0; i < files.size(); i++) {

            if (!files.get(i).equals(path)) {
                deleteFileFromDisk(files.get(i));
            }
        }

        normalPost(post);
    }

    private void storyPost(String requestId, Post post, Map resultData, String orientation) {
        Map<String, Object> postDetails = fetchPostDetailsFromRequestId(requestId);
        RemoteViews contentView = (RemoteViews) postDetails.get("contentView");
        Map<String, Object> parameters = new HashMap<>();
        String url = (String) resultData.get(Constants.Post.URL);

        //        if (url != null && !orientation.equals("0"))
        //            url = url.replace("upload/", "upload/a_" + orientation + "/");

        parameters.put("type", resultData.get("resource_type").equals("video") ? "2" : "1");
        parameters.put("urlPath", url);
        parameters.put("thumbnail", url);
        parameters.put("isPrivate", post.isPrivateStory());
        if (post.getDuration() != null) parameters.put("duration", post.getDuration());
        if (post.getCaption() != null && !post.getCaption().isEmpty()) {
            parameters.put("caption", post.getCaption());
        }

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
                                    notificationManager.notify(Integer.parseInt(post.getId()), builder2.build());
                                    notificationManager.cancel(Integer.parseInt(post.getId()));
                                }

                                response.body().setSuccess(true);
                                Log.e("POST-ID", "" + post.getId());
                                boolean deleted = postDb.delete(post.getId());

                                if (!post.isGallery())
                                    deleteFileFromDisk(post.getPathForCloudinary());

                                //      storyObserver.postData(response.body());
                                //uploadSuccessListner.uploaded();

                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("eventName", "myStoryUpdate");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                bus.post(obj);
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
                                                        storyPost(requestId, post, resultData, orientation);
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
                            notificationManager.notify(Integer.parseInt(post.getId()), builder2.build());
                            notificationManager.cancel(Integer.parseInt(post.getId()));
                        }
                        boolean deleted = postDb.delete(post.getId());

                        if (!post.isGallery()) deleteFileFromDisk(post.getPathForCloudinary());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addNewPost(PostData postData, SocialObserver socialShareObserver) {

        this.socialShareObserver = socialShareObserver;
        publishPost(postData);
    }

    public void addNewPost(PostData postData) {

        publishPost(postData);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteFileFromDisk(String filePath) {
        try {
            File file = new File(filePath);

            if (file.exists()) {

                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
