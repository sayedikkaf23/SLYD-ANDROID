package chat.hola.com.app.live_stream.Home.stream;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ezcall.android.R;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.WebRTCStreamBroadcasterActivity;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.live_stream.utility.AppPermissionsRunTime;
import chat.hola.com.app.live_stream.utility.HandlePictureEvents;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by moda on 11/20/2018.
 */
public class StreamActivity extends DaggerAppCompatActivity implements StreamContract.View {


    @Inject
    StreamContract.Presenter presenterMain;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager manager;
    @Inject
    AlertProgress alertProgress;


    private String streamName = "rtmpstream-";
    private String streamType;
    private String thumbnailUrl = "";

    private HandlePictureEvents handlePicEvent;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stream);
        ButterKnife.bind(this);
        initializeFragments();
        mqttManager.connectMQttClient(manager.getUserId());
    }

    private void initializeFragments() {
        handlePicEvent = new HandlePictureEvents(StreamActivity.this);
        checkForImageCapturePermission();
    }


    /**
     * <h2>checkPermission</h2>
     * <p>checking for the permission for camera and file storage at run time for
     * build version more than 22
     */
    private void checkForImageCapturePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<AppPermissionsRunTime.MyPermissionConstants> myPermissionConstantsArrayList = new ArrayList<>();
            myPermissionConstantsArrayList.clear();
            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_CAMERA);
            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_WRITE_EXTERNAL_STORAGE);
            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_READ_EXTERNAL_STORAGE);
            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_RECORD_AUDIO);

            if (AppPermissionsRunTime.checkPermission(this, myPermissionConstantsArrayList, 0)) {
                requestImageCapture();
            }
        } else {
            requestImageCapture();
        }
    }

    /**
     * predefined method to check run time permissions list call back
     *
     * @param requestCode   request code
     * @param permissions:  contains the list of requested permissions
     * @param grantResults: contains granted and un granted permissions result list
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionDenied = false;
        switch (requestCode) {
            case 0:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permissionDenied = true;
                        break;
                    }
                }
                if (permissionDenied) {
                    Toast.makeText(this, getString(R.string.app_doesnot_work_without_FilePermisssion),
                            Toast.LENGTH_LONG).show();
                } else {
                    requestImageCapture();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestImageCapture() {
        handlePicEvent.captureImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    uploadImage(handlePicEvent.newFile);
                } else {
                    Toast.makeText(this, getString(R.string.image_capture_canceled), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void uploadImage(File mFileTemp) {
        showAlert(getString(R.string.uploading_image));
        UploadFileAmazonS3 amazonS3 = new UploadFileAmazonS3(this);
        String name = AppController.getInstance().getUserId() + System.currentTimeMillis();
        final String imageUrl = ApiOnServer.BASE_URL + "/" + ApiOnServer.BUCKET + "/" + ApiOnServer.THUMBNAIL + "/" + name;
        Log.d("amazon", "amzonUpload: " + imageUrl);

        thumbnailUrl = imageUrl;
        streamName = name;
        streamType = "start";

        amazonS3.Upload_data(this,ApiOnServer.THUMBNAIL + "/" + name,
                mFileTemp,
                new UploadFileAmazonS3.UploadCallBack() {
                    @Override
                    public void sucess(String success) {
                        presenterMain.startLiveBroadcastApi("", streamType, thumbnailUrl, streamName);
                        try {
                            if (handlePicEvent.newFile.exists())
                                handlePicEvent.newFile.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(String errormsg) {
                        try {
                            if (handlePicEvent.newFile.exists())
                                handlePicEvent.newFile.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideAlert();
                        Toast.makeText(StreamActivity.this, getString(R.string.upload_image_failed), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onSuccess(String streamId) {
        hideAlert();
        //Stream prepared successfully
        Intent intent = new Intent(this, WebRTCStreamBroadcasterActivity.class);
        intent.putExtra("StreamName", streamName);
        intent.putExtra("StreamType", streamType);
        intent.putExtra("StreamId", streamId);
        intent.putExtra("ThumbNail", thumbnailUrl);
        startActivity(intent);
        finish();
    }

    @Override
    public void showAlert(String message) {
        dialog = alertProgress.getProgressDialog(this, message);
        dialog.show();
    }

    @Override
    public void onError(String message) {
        //Failed to prepare the stream
        hideAlert();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //        Utility.checkAndShowNetworkError(this);
    }

    public void hideAlert() {

        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
