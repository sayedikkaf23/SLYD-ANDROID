package chat.hola.com.app.postshare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import chat.hola.com.app.DublyCamera.overlay.OverlayConfig;
import com.ezcall.android.R;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Utilities {
  private static boolean downloadRunning;
  private static String imageFolderPath = null;
  private static String videoFolderPath = null;
  private static String duetFolderPath = null;

  private static String getPath(Context context, boolean duet, int type) {

    if (duet) {

      if (duetFolderPath == null) {
        final File imageFolder;

        imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
            .getString(R.string.app_name) + "/DownloadedMedia/Duet");

        if (!imageFolder.exists() || !imageFolder.isDirectory()) {

          //noinspection ResultOfMethodCallIgnored
          imageFolder.mkdirs();
        }

        duetFolderPath = imageFolder.getAbsolutePath();
      }
      return duetFolderPath;
    } else {

      if (type == 0 || type == 2) {
        if (imageFolderPath == null) {
          final File imageFolder;
          if (Environment.getExternalStorageState().

              equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              imageFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                  + "/"
                  + context.getResources().getString(R.string.app_name)
                  + "/DownloadedMedia/");
            } else {
              imageFolder = new File(
                  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                      + "/"
                      + context.getResources().getString(R.string.app_name));
            }
          } else {

            imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
                .getString(R.string.app_name) + "/DownloadedMedia/");
          }

          if (!imageFolder.exists() || !imageFolder.isDirectory()) {

            //noinspection ResultOfMethodCallIgnored
            imageFolder.mkdirs();
          }

          imageFolderPath = imageFolder.getAbsolutePath();
        }
        return imageFolderPath;
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
  }

  public static boolean copyMediaWithWatermark(Context context, String mediaUrl) {

    try {
      ClipboardManager clipboard =
          (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData clip = ClipData.newPlainText("Copied Text", prepareUrlForDownload(mediaUrl));
      clipboard.setPrimaryClip(clip);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static void downloadMediaWithWatermark(ProgressBar progressBar, String mediaUrl,
      DownloadMediaResult downloadResult, String fileName, Context context, int mediaType) {
    mediaUrl = prepareUrlForDownload(mediaUrl);
    downloadMedia(progressBar, mediaUrl, downloadResult, fileName, context, false, mediaType);
  }

  public static void downloadMediaAsGifWithWatermark(ProgressBar progressBar, String mediaUrl,
      DownloadMediaResult downloadResult, String fileName, Context context) {
    mediaUrl = prepareUrlForDownload(mediaUrl);
    mediaUrl = mediaUrl.replace(".mp4", ".gif").replace(".mov", ".gif");
    downloadMedia(progressBar, mediaUrl, downloadResult, fileName, context, false, 2);
  }

  private static String prepareUrlForDownload(String mediaUrl) {

    return mediaUrl.replace("upload/", "upload/" + WatermarkConfig.WATERMARK_CONFIG);
  }

  public static void downloadMediaForDuet(ProgressBar progressBar, String mediaUrl,
      DownloadMediaResult downloadResult, String fileName, Context context) {

    downloadMedia(progressBar, mediaUrl, downloadResult, fileName, context, true, -1);
  }

  private static void downloadMedia(ProgressBar progressBar, String remoteUrl,
      DownloadMediaResult downloadResult, String fileName, Context context, boolean duet,
      int type) {

    if (!downloadRunning) {
      new AsyncTask<String, String, String>() {
        @Override
        protected void onPreExecute() {
          super.onPreExecute();
          progressBar.setVisibility(View.VISIBLE);
          downloadRunning = true;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
          progressBar.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected String doInBackground(String... f_url) {
          try {

            String folderPath = getPath(context, duet, type);

            int downloadedSize;
            URL url = new URL(f_url[0]);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            // getting file length
            int contentLength = urlConnection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(new File(folderPath + "/" + fileName));

            byte[] data = new byte[1024];

            long total = 0;

            while ((downloadedSize = input.read(data)) != -1) {
              total += downloadedSize;
              // publishing the progress....
              // After this onProgressUpdate will be called
              publishProgress("" + (int) ((total * 100) / contentLength));

              // writing data to file
              output.write(data, 0, downloadedSize);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            if (!duet) {

              if (type == 1) {
                //video post
                if (!OverlayConfig.OVERLAY_REQUIRED) {

                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AndroidQMediaUtil.addMediaToGallery(fileName, type, context,
                        new File(folderPath + "/" + fileName),
                        context.getResources().getString(R.string.app_name));
                  } else {
                    MediaScannerConnection.scanFile(context,
                        new String[] { folderPath + "/" + fileName }, null, null);
                  }
                }
              } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                  AndroidQMediaUtil.addMediaToGallery(fileName, type, context,
                      new File(folderPath + "/" + fileName),
                      context.getResources().getString(R.string.app_name));
                } else {
                  MediaScannerConnection.scanFile(context,
                      new String[] { folderPath + "/" + fileName }, null, null);
                }
              }
            }
            downloadResult.downloadResult(null, folderPath + "/" + fileName, duet);
          } catch (final MalformedURLException e) {
            downloadResult.downloadResult("Error : MalformedURLException ", null, duet);
          } catch (final IOException e) {
            downloadResult.downloadResult(
                "Error : IOException(Not supported for posts in expired CDN accounts) ", null,
                duet);
          } catch (final Exception e) {
            downloadResult.downloadResult("Error : Please check your internet connection ", null,
                duet);
          }
          return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
          // dismiss the dialog after the file was downloaded
          progressBar.setVisibility(View.GONE);
          downloadRunning = false;
        }
      }.

          execute(remoteUrl);
    } else {
      downloadResult.downloadResult("Already downloading media!!", null, duet);
    }
  }
}
