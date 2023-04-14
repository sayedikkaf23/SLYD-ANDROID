package io.isometrik.groupstreaming.ui.profile;

import com.cloudinary.android.callback.ErrorInfo;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.ImageUtil;
import io.isometrik.groupstreaming.ui.utils.UploadImageResult;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.UpdateUserQuery;
import java.io.File;
import java.util.Map;

/**
 * The edit user presenter to update the user details.Contains helper methods to locally validate
 * the user details, upload user profile pic image and to delete the image from local storage after
 * image upload.
 * It implements EditUserContract.Presenter{@link EditUserContract.Presenter}
 *
 * @see EditUserContract.Presenter
 */
public class EditUserPresenter implements EditUserContract.Presenter {

  /**
   * Instantiates a new edit user presenter.
   */
  EditUserPresenter(EditUserContract.View editUserView) {
    this.editUserView = editUserView;
  }

  private EditUserContract.View editUserView;
  private boolean updatingUser;
  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link EditUserContract.Presenter#requestUserDetailsUpdate(String, String, String, String)}
   */
  @Override
  public void requestUserDetailsUpdate(String userId, String userName, String userIdentifier,
      String userProfilePicUrl) {
    if (!updatingUser) {
      updatingUser = true;

      UpdateUserQuery.Builder builder = new UpdateUserQuery.Builder().setUserId(userId);
      if (userIdentifier != null) {
        builder.setUserIdentifier(userIdentifier);
      }
      if (userName != null) {
        builder.setUserName(userName);
      }
      if (userProfilePicUrl != null) {
        builder.setUserProfilePic(userProfilePicUrl);
      }

      isometrik.updateUser(builder.build(), (var1, var2) -> {
        updatingUser = false;

        UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

        if (var1 != null) {
          if (userId != null) {
            userSession.setUserName(userName);
          }
          if (userIdentifier != null) {
            userSession.setUserIdentifier(userIdentifier);
          }
          if (userProfilePicUrl != null) {
            userSession.setUserProfilePic(userProfilePicUrl);
          }
          editUserView.onUserDetailsUpdated(userProfilePicUrl);
        } else {

          editUserView.onError(var2.getErrorMessage());
        }
      });
    }
  }

  /**
   * {@link EditUserContract.Presenter#requestImageUpload(String)}
   */
  @Override
  public void requestImageUpload(String imagePath) {
    UploadImageResult uploadImageResult = new UploadImageResult() {
      @Override
      public void uploadSuccess(String requestId, Map resultData) {
        editUserView.onImageUploadResult((String) resultData.get("url"));
      }

      @Override
      public void uploadError(String requestId, ErrorInfo error) {

        editUserView.onImageUploadError(error.getDescription());
      }
    };

    ImageUtil.requestUploadImage(imagePath, false, null, uploadImageResult);
  }

  /**
   * {@link EditUserContract.Presenter#validateUserDetails(String, String, File, boolean)}
   */
  @Override
  public void validateUserDetails(String userName, String userIdentifier, File file,
      boolean profilePicUpdated) {
    String errorMessage = null;

    if (userName.isEmpty()) {
      errorMessage =
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_username);
    } else if (userIdentifier.isEmpty()) {
      errorMessage =
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_user_identifier);
    } else {
      if (profilePicUpdated) {
        if (file == null || !file.exists()) {
          errorMessage =
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_user_image);
        }
      }
    }
    editUserView.onUserDetailsValidationResult(errorMessage);
  }

  /**
   * {@link EditUserContract.Presenter#deleteImage(File)}
   */
  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      imageFile.delete();
    }
  }
}
