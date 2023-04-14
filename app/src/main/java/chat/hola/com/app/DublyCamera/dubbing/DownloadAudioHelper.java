package chat.hola.com.app.DublyCamera.dubbing;

import android.os.AsyncTask;
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

public class DownloadAudioHelper {

  private static boolean downloadRunning;

  public static void downloadAudio(ProgressBar progressBar, String remoteUrl,
      DownloadResult downloadResult, String fileName) {

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

            // Output stream to write file
            OutputStream output = new FileOutputStream(
                new File(AppController.getInstance().getExternalFilesDir(null) + "/" + fileName));

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

            downloadResult.downloadResult(null, fileName,
                AppController.getInstance().getExternalFilesDir(null) + "/" + fileName);

          } catch (final MalformedURLException e) {
            downloadResult.downloadResult("Error : MalformedURLException ", null, null);
          } catch (final IOException e) {
            downloadResult.downloadResult("Error : IOException(Not supported for posts in expired CDN accounts) ", null, null);
          } catch (final Exception e) {
            downloadResult.downloadResult("Error : Please check your internet connection ", null,
                null);
          }
           return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
          // dismiss the dialog after the file was downloaded
          progressBar.setVisibility(View.GONE);
          downloadRunning = false;
          //returnResult(file_url);
        }
      }.execute(remoteUrl);
    } else {
      downloadResult.downloadResult("Already preparing audio!!", null, null);
    }
  }
}
