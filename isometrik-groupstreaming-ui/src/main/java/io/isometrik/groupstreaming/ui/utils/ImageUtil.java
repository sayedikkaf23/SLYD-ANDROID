package io.isometrik.groupstreaming.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import io.isometrik.groupstreaming.ui.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * The type Image util.
 */
public class ImageUtil {

  /**
   * Request upload image.
   *
   * @param imagePath the image path
   * @param streamImage the stream image
   * @param publicId the public id
   * @param uploadImageResult the upload image result
   */
  public static void requestUploadImage(String imagePath, boolean streamImage, String publicId,
      UploadImageResult uploadImageResult) {
    MediaManager.get()
        .upload(imagePath)
        .option("resource_type", "image")
        .option("folder", streamImage ? "streams" : "users")
        //.option("public_id", publicId)
        .option("overwrite", true)
        .callback(new UploadCallback() {
          @Override
          public void onStart(String requestId) {
            //TODO nothing

          }

          @Override
          public void onProgress(String requestId, long bytes, long totalBytes) {
            //TODO nothing

          }

          @Override
          public void onSuccess(String requestId, Map resultData) {
            uploadImageResult.uploadSuccess(requestId, resultData);
          }

          @Override
          public void onError(String requestId, ErrorInfo error) {
            uploadImageResult.uploadError(requestId, error);
          }

          @Override
          public void onReschedule(String requestId, ErrorInfo error) {
            uploadImageResult.uploadError(requestId, error);
          }
        })
        .dispatch();
  }

  /**
   * Create image file file.
   *
   * @param fileName the file name
   * @param streamImage the stream image
   * @param context the context
   * @return the file
   * @throws IOException the io exception
   */
  public static File createImageFile(String fileName, boolean streamImage, Context context)
      throws IOException {

    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

      imageFolder = new File(context.getExternalFilesDir(null) + "/" + context.getResources()
          .getString(R.string.ism_app_name) + "/" + (streamImage ? "streams" : "users"));
    } else {

      imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
          .getString(R.string.ism_app_name) + "/" + (streamImage ? "streams" : "users"));
    }

    if (!imageFolder.exists() || !imageFolder.isDirectory()) {

      imageFolder.mkdirs();
    }

    return File.createTempFile(fileName,  /* prefix */
        ".jpg",         /* suffix */
        imageFolder      /* directory */);
  }

  public static String saveCapturedBitmap(Bitmap capturedBitmap, Context context) {

    OutputStream fOutputStream;

    try {
      File file = createImageFile(String.valueOf(System.currentTimeMillis()), false, context);

      fOutputStream = new FileOutputStream(file);

      capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

      fOutputStream.flush();
      fOutputStream.close();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
