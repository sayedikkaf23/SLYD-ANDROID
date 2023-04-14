package io.isometrik.groupstreaming.ui.profile;

import java.io.File;

/**
 * The interface create user contract containing interfaces Presenter and View to be implemented
 * by the
 * CreateUserPresenter{@link CreateUserPresenter} and
 * CreateUserActivity{@link CreateUserActivity} respectively.
 *
 * @see CreateUserPresenter
 * @see CreateUserActivity
 */
public interface CreateUserContract {

  /**
   * The interface CreateUserContract.Presenter to be implemented by CreateUserPresenter{@link
   * CreateUserPresenter}
   *
   * @see CreateUserPresenter
   */
  interface Presenter {

    /**
     * Request create user.
     *
     * @param userName the user name of the user to be created
     * @param userIdentifier the user identifier of the user to be created
     * @param userProfilePicUrl the user profile pic url of the user to be created
     */
    void requestCreateUser(String userName, String userIdentifier, String userProfilePicUrl);

    /**
     * Request image upload.
     *
     * @param imagePath the local path of the image to be uploaded
     */
    void requestImageUpload(String imagePath);

    /**
     * Validate user details.
     *
     * @param userName the user name to be validated locally
     * @param userIdentifier the user identifier to be validated locally
     * @param file the file containing user image to be validated locally
     */
    void validateUserDetails(String userName, String userIdentifier, File file);

    /**
     * Delete image.
     *
     * @param file the file to be deleted
     */
    void deleteImage(File file);
  }

  /**
   * The interface CreateUserContract.View to be implemented by CreateUserActivity{@link
   * CreateUserActivity}
   *
   * @see CreateUserActivity
   */
  interface View {

    /**
     * On user created successfully.
     */
    void onUserCreated();

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
