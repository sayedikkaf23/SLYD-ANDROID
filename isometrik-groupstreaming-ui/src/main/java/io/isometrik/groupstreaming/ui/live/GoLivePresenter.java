package io.isometrik.groupstreaming.ui.live;

import com.cloudinary.android.callback.ErrorInfo;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.utils.ImageUtil;
import io.isometrik.groupstreaming.ui.utils.UploadImageResult;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class GoLivePresenter implements GoLiveContract.Presenter {

  GoLivePresenter(GoLiveContract.View goLiveView) {
    this.goLiveView = goLiveView;
  }

  private GoLiveContract.View goLiveView;
  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link GoLiveContract.Presenter#requestImageUpload(String)}
   */
  @Override
  public void requestImageUpload(String path) {

    UploadImageResult uploadImageResult = new UploadImageResult() {
      @SuppressWarnings("rawtypes")
      @Override
      public void uploadSuccess(String requestId, Map resultData) {
        goLiveView.onImageUploadResult((String) resultData.get("url"));
      }

      @Override
      public void uploadError(String requestId, ErrorInfo error) {

        goLiveView.onImageUploadError(error.getDescription());
      }
    };

    ImageUtil.requestUploadImage(path, true, null, uploadImageResult);
  }

  /**
   * {@link GoLiveContract.Presenter#deleteImage(File)}
   */
  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      imageFile.delete();
    }
  }

  /**
   * {@link GoLiveContract.Presenter#startBroadcast(String, String, boolean)}
   */
  @Override
  public void startBroadcast(String streamDescription, String streamImageUrl, boolean isPublic) {

    UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

    isometrik.startStream(new StartStreamQuery.Builder().setStreamDescription(streamDescription)
        .setCreatedBy(userSession.getUserId())
        .setStreamImage(streamImageUrl)
        .setPublic(isPublic)
        .setMembers(new ArrayList<>())
        .build(), (var1, var2) -> {

      if (var1 != null) {

        goLiveView.onBroadcastStarted(var1.getStreamId(), streamDescription, streamImageUrl,
            new ArrayList<>(), var1.getStartTime(), userSession.getUserId());
      } else {

        goLiveView.onError(var2.getErrorMessage());
      }
    });
  }
}
