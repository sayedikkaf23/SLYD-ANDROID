package chat.hola.com.app.live_stream.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.ezcall.android.R;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.live_stream.Home.dummy.DummyContent;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.live_stream.Home.stream.StreamActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.RTMPStreamBroadcasterActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.WebRTCStreamBroadcasterActivity;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.live_stream.utility.AppPermissionsRunTime;
import chat.hola.com.app.live_stream.utility.HandlePictureEvents;
import chat.hola.com.app.live_stream.utility.UploadAmazonS3;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by moda on 11/20/2018.
 */
public class StreamingMainActivity extends DaggerAppCompatActivity
        implements StreamingMainActivityPresenterContract.ViewMain,
        ProfileFragment.OnFragmentInteractionListener,
        BroadcastersFragment.OnListFragmentInteractionListener {

    @BindView(R.id.ivLiveBroadcasters)
    ImageView ivLiveBroadcasters;
    @BindView(R.id.ivStartBroadcast)
    ImageView ivStartBroadcast;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;

    @Inject
    StreamingMainActivityPresenterContract.presenterMain presenterMain;
    @Inject
    BroadcastersFragment liveBroadCastorsFragment;
    @Inject
    ProfileFragment profileFragment;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager manager;

    @Inject
    AlertProgress alertProgress;

    public static final String RTMP_BASE_URL = ApiOnServer.RTMP_BASE_URL;
    private FragmentManager fragmentManager;

    private String streamName = "rtmpstream-";

    private String streamType;
    private String thumbnailUrl = "";

    private HandlePictureEvents handlePicEvent;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_streaming_main);
        ButterKnife.bind(this);
        initializeFragments();
        mqttManager.connectMQttClient(manager.getUserId());
    }

    private void initializeFragments() {
        try {
            fragmentManager = getSupportFragmentManager();
            presenterMain.onFragmentTransition("HOME", fragmentManager, liveBroadCastorsFragment,
                    profileFragment, R.id.frameLayoutContainer);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        handlePicEvent = new HandlePictureEvents(StreamingMainActivity.this);
    }

    @OnClick({R.id.ivLiveBroadcasters, R.id.ivStartBroadcast, R.id.ivProfile})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ivLiveBroadcasters:
                presenterMain.onFragmentTransition("HOME", fragmentManager, liveBroadCastorsFragment,
                        profileFragment, R.id.frameLayoutContainer);
                break;
            case R.id.ivProfile:
                presenterMain.onFragmentTransition("PROFILE", fragmentManager, liveBroadCastorsFragment,
                        profileFragment, R.id.frameLayoutContainer);
                break;
            case R.id.ivStartBroadcast:
                checkForImageCapturePermission();
                break;
        }
    }

    /**
     * <h2>checkPermission</h2>
     * <p>checking for the permission for camera and file storage at run time for
     * build version more than 22
     */
    private void checkForImageCapturePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<AppPermissionsRunTime.MyPermissionConstants> myPermissionConstantsArrayList =
                    new ArrayList<>();
            myPermissionConstantsArrayList.clear();
            myPermissionConstantsArrayList.add(
                    AppPermissionsRunTime.MyPermissionConstants.PERMISSION_CAMERA);
            myPermissionConstantsArrayList.add(
                    AppPermissionsRunTime.MyPermissionConstants.PERMISSION_WRITE_EXTERNAL_STORAGE);
            myPermissionConstantsArrayList.add(
                    AppPermissionsRunTime.MyPermissionConstants.PERMISSION_READ_EXTERNAL_STORAGE);
            myPermissionConstantsArrayList.add(
                    AppPermissionsRunTime.MyPermissionConstants.PERMISSION_RECORD_AUDIO);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

//                    thumbnailUrl =
//                            Constants.AmazonS3.BUCKET + Constants.AmazonS3.THUMBNAIL + "/" + handlePicEvent.newFile.getName();
//                    streamName = "rtmpstream-";
//                    streamName = streamName + System.currentTimeMillis();
//                    streamType = "start";
//
//                    showAlert(getString(R.string.uploading_image));
//
//                    upload.UploadToAmazonS3(Constants.AmazonS3.BUCKET + "/" + Constants.AmazonS3.THUMBNAIL,
//                            handlePicEvent.newFile, new UploadAmazonS3.UploadCallBack() {
//                                @Override
//                                public void sucess(String sucess) {

//                                    hideAlert();

//                                    showAlert(getString(R.string.preparing_stream));
//                                    presenterMain.startLiveBroadcastApi("", streamType, thumbnailUrl, streamName);
//
//                                    try {
//
//                                        //File from the local storage should be deleted,after successfull upload to s3
//
//                                        if (handlePicEvent.newFile.exists())
//                                            handlePicEvent.newFile.delete();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void error(String errorMessage) {
//
//                                    try {
//
//                                        //File from the local storage should be deleted,even if upload to s3 failed
//
//                                        if (handlePicEvent.newFile.exists())
//                                            handlePicEvent.newFile.delete();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    hideAlert();
//
//                                    Toast.makeText(StreamingMainActivity.this,
//                                            getString(R.string.upload_image_failed), Toast.LENGTH_LONG).show();
//                                }
//                            });
                } else {

                    Toast.makeText(this, getString(R.string.image_capture_canceled), Toast.LENGTH_LONG)
                            .show();
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
                        showAlert(getString(R.string.preparing_stream));
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
                        Toast.makeText(StreamingMainActivity.this, getString(R.string.upload_image_failed), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onSuccess(String streamId) {
        hideAlert();
        //Stream prepared successfully
        Intent intent;

        if (ApiOnServer.STREAMING_TYPE == 0) {
            //RTMP Streaming
            intent = new Intent(this, RTMPStreamBroadcasterActivity.class);
        } else {
            //WebRTC Streaming
            intent = new Intent(this, WebRTCStreamBroadcasterActivity.class);
        }
        intent.putExtra("StreamName", streamName);
        intent.putExtra("StreamType", streamType);
        intent.putExtra("StreamId", streamId);
        intent.putExtra("ThumbNail", thumbnailUrl);
        startActivity(intent);
    }

    @Override
    public void showAlert(String message) {
        dialog = alertProgress.getProgressDialog(this, message);
        dialog.show();
    }

    @Override
    public void onError(String message) {
        //Failed to prepare the stream
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

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
