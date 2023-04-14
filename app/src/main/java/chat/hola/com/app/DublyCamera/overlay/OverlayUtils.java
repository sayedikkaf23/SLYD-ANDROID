package chat.hola.com.app.DublyCamera.overlay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.models.Business;
import chat.hola.com.app.postshare.AndroidQMediaUtil;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.ezcall.android.R;
import java.io.File;
import java.io.FileOutputStream;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class OverlayUtils {
  private static String videoFolderPath = null;
  private static String bitmapFolderPath = null;

  public static void addProfileOverlay(int time, String imagePath, String videoPath,
      String outputVideoPath, OverlayProfileInfoResult overlayProfileInfoResult, Context context,
      String fileName) {

    try {
      String[] complexCommand = {
          "-y", "-i", videoPath, "-loop", "1", "-t", String.valueOf(time), "-i", imagePath, "-f",
          "lavfi", "-t", String.valueOf(time), "-i", "aevalsrc=0", "-vsync", "2", "-filter_complex",
          "[0:v] [0:a] [1:v] [2:a] concat=n=2:v=1:a=1 [v] [a]", "-c:v", "libx264", "-preset",
          "ultrafast", "-c:a", "aac", "-strict", "-2", "-map", "[v]", "-map", "[a]", outputVideoPath
      };

      FFmpeg.executeAsync(complexCommand, (executionId, returnCode) -> {

        if (returnCode == RETURN_CODE_SUCCESS) {
          //Async command execution completed successfully.
          deleteFileFromDisk(imagePath);
          deleteFileFromDisk(videoPath);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AndroidQMediaUtil.addMediaToGallery(fileName, 1, context, new File(outputVideoPath),
                context.getResources().getString(R.string.app_name));
          } else {

            MediaScannerConnection.scanFile(context, new String[] { videoPath, outputVideoPath },
                null, null);
          }

          overlayProfileInfoResult.overlayResult(outputVideoPath);
        } else {
          //Async command execution failed
          deleteFileFromDisk(imagePath);
          deleteFileFromDisk(videoPath);
          deleteFileFromDisk(outputVideoPath);
          overlayProfileInfoResult.overlayResult(null);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void deleteFileFromDisk(String filePath) {
    try {
      File file = new File(filePath);

      if (file.exists()) {

        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void prepareOverlay(Context context, Data postTobeShared, String videoPath,
      OverlayProfileInfoResult overlayProfileInfoResult) {
    String userImageUrl, userName;
    Business business = postTobeShared.getBusiness();
    boolean isBusiness =
        business != null && !business.getBusinessPostType().equalsIgnoreCase("regular");

    if (isBusiness) {
      userName = business.getBusinessName();
      userImageUrl = business.getBusinessProfilePic();
    } else {
      boolean isChannel =
          postTobeShared.getChannelId() != null && !postTobeShared.getChannelId().isEmpty();
      userImageUrl =
          isChannel ? postTobeShared.getChannelImageUrl() : postTobeShared.getProfilepic();

      userName = isChannel ? postTobeShared.getChannelName() : postTobeShared.getUsername();
    }

    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (inflater != null) {

      @SuppressLint("InflateParams")
      View profileView = inflater.inflate(R.layout.overlay_downloaded_media, null);
      TextView tvUserName = (TextView) profileView.findViewById(R.id.tvUserName);
      tvUserName.setText(userName);
      AppCompatImageView ivProfile = (AppCompatImageView) profileView.findViewById(R.id.ivProfile);

      try {
        Glide.with(context).load(userImageUrl)

            .asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
          @Override
          public boolean onException(Exception e, String model, Target<Bitmap> target,
              boolean isFirstResource) {
            overlayProfileInfoResult.overlayResult(null);
            return false;
          }

          @Override
          public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
              boolean isFromMemoryCache, boolean isFirstResource) {

            return false;
          }
        }).into(new BitmapImageViewTarget(ivProfile) {
          @Override
          protected void setResource(Bitmap resource) {

            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            ivProfile.setImageDrawable(circularBitmapDrawable);

            Bitmap bitmap = ViewToBitmapUtil.createBitmapFromView(profileView,
                Integer.parseInt(postTobeShared.getImageUrl1Width()),
                Integer.parseInt(postTobeShared.getImageUrl1Height()));

            String imagePath = saveBitmap(context, bitmap);
            if (imagePath != null) {
              String fileName = System.currentTimeMillis() + ".mp4";
              String outputVideoPath = getPath(context, false) + "/" + fileName;
              addProfileOverlay(OverlayConfig.OVERLAY_DURATION, imagePath, videoPath,
                  outputVideoPath, overlayProfileInfoResult, context, fileName);
            } else {
              overlayProfileInfoResult.overlayResult(null);
            }
          }
        });
      } catch (Exception e) {
        overlayProfileInfoResult.overlayResult(null);
      }
    } else {
      overlayProfileInfoResult.overlayResult(null);
    }
  }

  private static String getPath(Context context, boolean bitmap) {

    if (bitmap) {

      if (bitmapFolderPath == null) {
        final File imageFolder;
        imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
            .getString(R.string.app_name) + "/DownloadedMedia/");

        if (!imageFolder.exists() || !imageFolder.isDirectory()) {

          //noinspection ResultOfMethodCallIgnored
          imageFolder.mkdirs();
        }

        bitmapFolderPath = imageFolder.getAbsolutePath();
      }
      return bitmapFolderPath;
    } else {

      if (videoFolderPath == null) {
        final File videoFolder;
        if (Environment.getExternalStorageState().

            equals(Environment.MEDIA_MOUNTED)) {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            videoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                + "/"
                + context.getResources().getString(R.string.app_name)
                + "/DownloadedMedia/");
          } else {
            videoFolder = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                    + "/"
                    + context.getResources().getString(R.string.app_name));
          }
        } else {

          videoFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
              .getString(R.string.app_name) + "/DownloadedMedia/");
        }

        if (!videoFolder.exists() || !videoFolder.isDirectory()) {

          //noinspection ResultOfMethodCallIgnored
          videoFolder.mkdirs();
        }

        videoFolderPath = videoFolder.getAbsolutePath();
      }
      return videoFolderPath;
    }
  }

  private static String saveBitmap(Context context, Bitmap bitmap) {

    File imageBitmap = new File(getPath(context, true), System.currentTimeMillis() + ".jpg");

    try {
      FileOutputStream out = new FileOutputStream(imageBitmap);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
      out.flush();
      out.close();
      return imageBitmap.getAbsolutePath();
    } catch (Exception e) {
      deleteFileFromDisk(imageBitmap.getAbsolutePath());
      return null;
    }
  }
}
