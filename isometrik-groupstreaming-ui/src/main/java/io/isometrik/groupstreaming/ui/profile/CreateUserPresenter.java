package io.isometrik.groupstreaming.ui.profile;

import com.cloudinary.android.callback.ErrorInfo;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.ImageUtil;
import io.isometrik.groupstreaming.ui.utils.UploadImageResult;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.AddUserQuery;
import java.io.File;
import java.util.Map;

/**
 * The create user presenter to create a new user.Contains helper methods to locally validate the
 * user details, upload user profile pic image and to delete the image from local storage after
 * image upload.
 * It implements CreateUserContract.Presenter{@link CreateUserContract.Presenter}
 *
 * @see CreateUserContract.Presenter
 */
public class CreateUserPresenter implements CreateUserContract.Presenter {

  /**
   * Instantiates a new create user presenter.
   */
  CreateUserPresenter(CreateUserContract.View createUserView) {
    this.createUserView = createUserView;
  }

  private CreateUserContract.View createUserView;
  private boolean creatingUser;

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link CreateUserContract.Presenter#requestCreateUser(String, String, String)}
   */
  @Override
  public void requestCreateUser(String userName, String userIdentifier, String userProfilePicUrl) {
    if (!creatingUser) {
      creatingUser = true;
      isometrik.addUser(new AddUserQuery.Builder().setUserIdentifier(userIdentifier)
          .setUserName(userName)
          .setUserProfilePic(userProfilePicUrl)
          .build(), (var1, var2) -> {
        creatingUser = false;
        if (var1 != null) {

          IsometrikUiSdk.getInstance()
              .getUserSession()
              .switchUser(var1.getUserId(), userName, userIdentifier, userProfilePicUrl, false);

          createUserView.onUserCreated();
        } else {

          createUserView.onError(var2.getErrorMessage());
        }
      });
    }
  }

  /**
   * {@link CreateUserContract.Presenter#validateUserDetails(String, String, File)}
   */
  @Override
  public void validateUserDetails(String userName, String userIdentifier, File file) {

    String errorMessage = null;

    if (userName.isEmpty()) {
      errorMessage =
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_username);
    } else if (userIdentifier.isEmpty()) {
      errorMessage =
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_user_identifier);
    } else if (file == null || !file.exists()) {
      errorMessage =
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_user_image);
    }
    createUserView.onUserDetailsValidationResult(errorMessage);
  }

  /**
   * {@link CreateUserContract.Presenter#requestImageUpload(String)}
   */
  @Override
  public void requestImageUpload(String path) {

    UploadImageResult uploadImageResult = new UploadImageResult() {
      @Override
      public void uploadSuccess(String requestId, Map resultData) {
        createUserView.onImageUploadResult((String) resultData.get("url"));
      }

      @Override
      public void uploadError(String requestId, ErrorInfo error) {

        createUserView.onImageUploadError(error.getDescription());
      }
    };

    ImageUtil.requestUploadImage(path, false, null, uploadImageResult);
  }

  /**
   * {@link CreateUserContract.Presenter#deleteImage(File)}
   */
  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      imageFile.delete();
    }
  }
}
