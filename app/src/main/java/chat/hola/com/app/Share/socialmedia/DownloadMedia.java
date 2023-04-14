package chat.hola.com.app.Share.socialmedia;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import chat.hola.com.app.AppController;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadMedia {
  private static boolean downloadRunning;

  public static void startMediaDownload(ProgressBar progressBar, String remoteUrl,
      DownloadMediaToShareResult downloadResult, String fileName, boolean isImage, boolean shareOnFacebook) {

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
            int downloadedSize;
            URL url = new URL(f_url[0]);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            // getting file length
            int contentLength = urlConnection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            String folderPath = AppController.getInstance().getFilesDir() + "/share/";
            File shareFolder = new File(folderPath);
            if (!shareFolder.exists()) {
              shareFolder.mkdir();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(new File(folderPath + fileName));

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
            downloadResult.downloadResult(null, folderPath + fileName, isImage, shareOnFacebook);
          } catch (final MalformedURLException e) {
            e.printStackTrace();
            downloadResult.downloadResult("Error : MalformedURLException ", null, isImage,
                shareOnFacebook);
          } catch (final IOException e) {
            e.printStackTrace();
            downloadResult.downloadResult("Error : IOException ", null, isImage, shareOnFacebook);
          } catch (final Exception e) {
            e.printStackTrace();
            downloadResult.downloadResult("Error : Please check your internet connection ", null,
                isImage, shareOnFacebook);
          }

          return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
          // dismiss the dialog after the file was downloaded
          progressBar.setVisibility(View.GONE);
          downloadRunning = false;
        }
      }.execute(remoteUrl);
    } else {
      downloadResult.downloadResult(
          "Already downloading a file to share, please try again in sometime!!", null, isImage,
          shareOnFacebook);
    }
  }
}
