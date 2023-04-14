package chat.hola.com.app.Utilities.twitterManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.TwitterApiClient;
//import com.twitter.sdk.android.core.TwitterCore;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.models.Media;
//import com.twitter.sdk.android.core.models.Tweet;
//import com.twitter.sdk.android.core.services.MediaService;
//import com.twitter.sdk.android.core.services.StatusesService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import chat.hola.com.app.Utilities.FileUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by ankit on 11/3/18.
 */

public class TwitterShareManager {

//    private static final String TAG = TwitterShareManager.class.getSimpleName();
//    Callback<Tweet> callback = null;
//
//    public TwitterShareManager() {
//    }
//
//    public void tweet(String tweetText, final Callback<Tweet> callback){
//
//        this.callback = callback;
//
//        TwitterSession session = TwitterCore.getInstance()
//                .getSessionManager().getActiveSession();
//
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
//        StatusesService statusesService =  twitterApiClient.getStatusesService();
//
//        Call<Tweet> tweetCall = statusesService.update
//                (
//                        tweetText,
//                        null,false,
//                        null,null,null,
//                        true,false,null
//                );
//        tweetCall.enqueue(new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
//                Log.w(TAG, "tweet successful: "+result.response.body().toString());
//                if(callback != null)
//                    callback.success(result);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.e(TAG, "tweet failure: "+exception.getMessage() );
//                if(callback != null)
//                    callback.failure(exception);
//            }
//        });
//    }
//
//    private void tweet(String tweetText, String mediaId, final Callback<Tweet> callback){
//
//        this.callback = callback;
//
//        TwitterSession session = TwitterCore.getInstance()
//                .getSessionManager().getActiveSession();
//
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
//        StatusesService statusesService =  twitterApiClient.getStatusesService();
//
//        Call<Tweet> tweetCall = statusesService.update
//                (
//                        tweetText,
//                        null,false,
//                        null,null,null,
//                        true,false,mediaId
//                );
//        tweetCall.enqueue(new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
//                Log.w(TAG, "tweet successful: "+result.response.body().toString());
//                if(callback != null)
//                    callback.success(result);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.e(TAG, "tweet failure: "+exception.getMessage() );
//                if(callback != null)
//                    callback.failure(exception);
//            }
//        });
//    }
//
//    private byte[] getBytesFromBitmap(Bitmap bitmap){
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,70,stream);
//        return stream.toByteArray();
//    }
//
//    //LIMITS image size < 5mb and video < 15mb (5sec to 15sec);
//    public void tweetMedia(final String tweetText, String type, String path, final Callback<Tweet> callback){
//
//        this.callback = callback;
//
//        TwitterSession session = TwitterCore.getInstance()
//                .getSessionManager().getActiveSession();
//
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
//        MediaService mediaService = twitterApiClient.getMediaService();
//
//        File file = new File(path);
//        MediaType mediaType = MediaType.parse(type);
//        RequestBody requestBody = RequestBody.create(mediaType,file);
//        RequestBody requestData = null;
//
//        if(type.contains("image")) {
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            if (bitmap == null)
//                Log.w(TAG, "bitmap can not be null !!");
//            String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);
//            requestData = RequestBody.create(mediaType, imgString);
//        }
//        else {
//            byte[] bytesData;
//            try {
//                bytesData =FileUtil.getFileToByteArray(file.getPath());
//                String vidString = Base64.encodeToString(bytesData,Base64.NO_WRAP);
//                requestData = RequestBody.create(mediaType, vidString);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Call<Media> mediaCall = mediaService.upload
//                (
//                        requestBody,requestData,null
//                );
//
//        mediaCall.enqueue(new Callback<Media>() {
//            @Override
//            public void success(Result<Media> result) {
//                Log.w(TAG, "media upload successful: "+result.data.mediaIdString);
//                tweet(tweetText,result.data.mediaIdString,callback);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.e(TAG, "media upload failed: "+exception.getMessage() );
//            }
//        });
//
//    }


}