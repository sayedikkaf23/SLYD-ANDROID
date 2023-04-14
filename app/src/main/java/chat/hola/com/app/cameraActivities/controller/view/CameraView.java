package chat.hola.com.app.cameraActivities.controller.view;

import android.app.Activity;
import android.view.View;

import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.utils.Size;

/**
 * Created by Arpit Gandhi on 7/6/16.
 */
public interface CameraView {

    Activity getActivity();

    void updateCameraPreview(Size size, View cameraPreview);

    void updateUiForMediaAction(@CameraConfiguration.MediaAction int mediaAction);

    void updateCameraSwitcher(int numberOfCameras);

    void onPhotoTaken();

    void onVideoRecordStart(int width, int height);

    void onVideoRecordStop();

    void releaseCameraPreview();

}
