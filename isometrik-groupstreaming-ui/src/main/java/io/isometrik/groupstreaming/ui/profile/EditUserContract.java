package io.isometrik.groupstreaming.ui.profile;

import java.io.File;

/**
 * The interface edit user contract containing interfaces Presenter and View to be implemented
 * by the
 * EditUserPresenter{@link EditUserPresenter} and
 * EditUserActivity{@link EditUserActivity} respectively.
 *
 * @see EditUserPresenter
 * @see EditUserActivity
 */
public interface EditUserContract {

  /**
   * The interface EditUserContract.Presenter to be implemented by EditUserPresenter{@link
   * EditUserPresenter}
   *
   * @see EditUserPresenter
   */
  interface Presenter {

    /**
     * Request user details update.
     *
     * @param userId the user id of the user whose details are to be updated
     * @param userName the user name of the user to be updated
     * @param userIdentifier the user identifier of the user to be updated
     * @param userProfilePicUrl the user profile pic url of the user to be updated
     */
    void requestUserDetailsUpdate(String userId, String userName, String userIdentifier,
        String userProfilePicUrl);

    /**
     * Request image upload.
     *
     * @param imagePath the local path of the image to be uploaded
     */
    void requestImageUpload(String imagePath);

    /**
     * Validate user details.
     *
     * @param userName the user name of the user who details are to be updated
     * @param userIdentifier the user identifier of the user who details are to be updated
     * @param file the file containing image of the user who details are to be updated
     * @param profilePicUpdated whether to update the profile pic or not
     */
    void validateUserDetails(String userName, String userIdentifier, File file,
        boolean profilePicUpdated);

    /**
     * Delete image.
     *
     * @param file the file containing image to be deleted from local storage
     */
    void deleteImage(File file);
  }

  /**
   * The interface EditUserContract.View to be implemented by EditUserActivity{@link
   * EditUserActivity}
   *
   * @see EditUserActivity
   */
  interface View {

    /**
     * On user details updated.
     *
     * @param userProfilePicUrl the url of the user profile pic
     */
    void onUserDetailsUpdated(String userProfilePicUrl);

    /**
     * On user details validation result.
     *
     * @param errorMessage the error message containing details of the error in the validation of
     * the user details
     */
    void onUserDetailsValidationResult(String errorMessage);

    /**
     * On image upload result.
     *
     * @param url the url of the image uploaded
     */
    void onImageUploadResult(String url);

    /**
     * On image upload error.
     *
     * @param errorMessage the error message containing details of the error encountered while
     * trying to upload image
     */
    void onImageUploadError(String errorMessage);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
