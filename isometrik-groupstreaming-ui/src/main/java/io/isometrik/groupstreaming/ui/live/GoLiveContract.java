package io.isometrik.groupstreaming.ui.live;

import java.io.File;
import java.util.ArrayList;

public interface GoLiveContract {

  interface Presenter {
    /**
     * Request image upload.
     *
     * @param imagePath the image path
     */
    void requestImageUpload(String imagePath);

    /**
     * Delete image.
     *
     * @param file the file
     */
    void deleteImage(File file);

    /**
     * Start broadcast.
     *
     * @param streamDescription the stream description
     * @param streamImageUrl the stream image url
     * @param isPublic whether stream type is public or not
     */
    void startBroadcast(String streamDescription, String streamImageUrl, boolean isPublic);
  }

  interface View {
    /**
     * On broadcast started.
     *
     * @param streamId the stream id
     * @param streamDescription the stream description
     * @param streamImageUrl the stream image url
     * @param memberIds the member ids
     * @param startTime the start time
     * @param userId the user id
     */
    void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl,
        ArrayList<String> memberIds, long startTime, String userId);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On image upload result.
     *
     * @param url the url
     */
    void onImageUploadResult(String url);

    /**
     * On image upload error.
     *
     * @param errorMessage the error message containing details of the error encountered while
     * trying to upload image
     */
    void onImageUploadError(String errorMessage);
  }
}
