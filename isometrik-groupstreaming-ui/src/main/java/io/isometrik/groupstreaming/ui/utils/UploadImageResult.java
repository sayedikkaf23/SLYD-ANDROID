package io.isometrik.groupstreaming.ui.utils;

import com.cloudinary.android.callback.ErrorInfo;
import java.util.Map;

/**
 * The type Upload image result.
 */
public abstract class UploadImageResult {

  /**
   * Upload success.
   *
   * @param requestId the request id
   * @param resultData the result data
   */
  @SuppressWarnings("rawtypes")
  public abstract void uploadSuccess(String requestId, Map resultData);

  /**
   * Upload error.
   *
   * @param requestId the request id
   * @param error the error
   */
  public abstract void uploadError(String requestId, ErrorInfo error);
}
