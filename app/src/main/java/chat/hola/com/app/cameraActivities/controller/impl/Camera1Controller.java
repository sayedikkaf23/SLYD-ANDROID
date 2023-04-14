package chat.hola.com.app.cameraActivities.controller.impl;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.configuration.ConfigurationProvider;
import chat.hola.com.app.cameraActivities.controller.CameraController;
import chat.hola.com.app.cameraActivities.controller.view.CameraView;
import chat.hola.com.app.cameraActivities.manager.CameraManager;
import chat.hola.com.app.cameraActivities.manager.impl.Camera1Manager;
import chat.hola.com.app.cameraActivities.manager.listener.CameraCloseListener;
import chat.hola.com.app.cameraActivities.manager.listener.CameraOpenListener;
import chat.hola.com.app.cameraActivities.manager.listener.CameraPhotoListener;
import chat.hola.com.app.cameraActivities.manager.listener.CameraVideoListener;
import chat.hola.com.app.cameraActivities.ui.view.AutoFitSurfaceView;
import chat.hola.com.app.cameraActivities.utils.CameraHelper;
import chat.hola.com.app.cameraActivities.utils.Size;

import java.io.File;

/**
 * Created by Arpit Gandhi on 7/7/16.
 */

@SuppressWarnings("deprecation")
public class Camera1Controller implements CameraController<Integer>,
        CameraOpenListener<Integer, SurfaceHolder.Callback>, CameraPhotoListener, CameraCloseListener<Integer>, CameraVideoListener {

    private final static String TAG = "Camera1Controller";

    private Integer currentCameraId;
    private ConfigurationProvider configurationProvider;
    private CameraManager<Integer, SurfaceHolder.Callback> cameraManager;
    private CameraView cameraView;

    private File outputFile;

    public Camera1Controller(CameraView cameraView, ConfigurationProvider configurationProvider) {
        this.cameraView = cameraView;
        this.configurationProvider = configurationProvider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        cameraManager = Camera1Manager.getInstance();
        cameraManager.initializeCameraManager(configurationProvider, cameraView.getActivity());
        currentCameraId = cameraManager.getFaceBackCameraId();
    }

    @Override
    public void onResume() {
        cameraManager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPause() {
        cameraManager.closeCamera(null);
    }

    @Override
    public void onDestroy() {
        cameraManager.releaseCameraManager();
    }

    @Override
    public void takePhoto() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), CameraConfiguration.MEDIA_ACTION_PHOTO);
        cameraManager.takePhoto(outputFile, this);
    }

    @Override
    public void startVideoRecord() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), CameraConfiguration.MEDIA_ACTION_VIDEO);
        cameraManager.startVideoRecord(outputFile, this);
    }

    @Override
    public void setFlashMode(@CameraConfiguration.FlashMode int flashMode) {
        cameraManager.setFlashMode(flashMode);
    }

    @Override
    public void stopVideoRecord() {
        cameraManager.stopVideoRecord();
    }

    @Override
    public boolean isVideoRecording() {
        return cameraManager.isVideoRecording();
    }

    @Override
    public void switchCamera(@CameraConfiguration.CameraFace final int cameraFace) {
        currentCameraId = cameraManager.getCurrentCameraId().equals(cameraManager.getFaceFrontCameraId()) ?
                cameraManager.getFaceBackCameraId() : cameraManager.getFaceFrontCameraId();

        cameraManager.closeCamera(this);
    }

    @Override
    public void switchQuality() {
        cameraManager.closeCamera(this);
    }

    @Override
    public int getNumberOfCameras() {
        return cameraManager.getNumberOfCameras();
    }

    @Override
    public int getMediaAction() {
        return configurationProvider.getMediaAction();
    }

    @Override
    public File getOutputFile() {
        return outputFile;
    }

    @Override
    public Integer getCurrentCameraId() {
        return currentCameraId;
    }


    @Override
    public void onCameraOpened(Integer cameraId, Size previewSize, SurfaceHolder.Callback surfaceCallback) {
        cameraView.updateUiForMediaAction(configurationProvider.getMediaAction());
        cameraView.updateCameraPreview(previewSize, new AutoFitSurfaceView(cameraView.getActivity(), surfaceCallback));
        cameraView.updateCameraSwitcher(getNumberOfCameras());
    }

    @Override
    public void onCameraOpenError() {
        Log.e(TAG, "onCameraOpenError");
    }

    @Override
    public void onCameraClosed(Integer closedCameraId) {
        cameraView.releaseCameraPreview();

        cameraManager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPhotoTaken(File photoFile) {
        cameraView.onPhotoTaken();
    }

    @Override
    public void onPhotoTakeError() {
    }

    @Override
    public void onVideoRecordStarted(Size videoSize) {
        cameraView.onVideoRecordStart(videoSize.getWidth(), videoSize.getHeight());
    }

    @Override
    public void onVideoRecordStopped(File videoFile) {
        cameraView.onVideoRecordStop();
    }

    @Override
    public void onVideoRecordError() {

    }

    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }
}
