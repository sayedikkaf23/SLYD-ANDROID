package io.isometrik.gs.rtcengine.ar;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static io.isometrik.gs.rtcengine.ar.FiltersConfig.getFilesDirectory;

public class ZipUtils {

  private static boolean downloadRunning;

  public static void downloadEffects(int effectType, DownloadZipResult downloadZipResult,
      ProgressBar progressBar) {
    switch (effectType) {

      case 0: {
        //deepar
        downloadFilters(progressBar, FiltersConfig.DEEPAR_EFFECTS, downloadZipResult,
            FiltersConfig.DEEPAR_FILENAME);
        break;
      }
    }
  }

  private static void downloadFilters(ProgressBar progressBar, String remoteUrl,
      DownloadZipResult downloadResult, String fileName) {

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
            OutputStream output =
                new FileOutputStream(new File(FiltersConfig.getFilesDirectory() + "/" + fileName));

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
            downloadResult.downloadResult(null, FiltersConfig.getFilesDirectory() + "/" + fileName);
          } catch (final MalformedURLException e) {
            e.printStackTrace();
            downloadResult.downloadResult("Error : MalformedURLException ", null);
          } catch (final IOException e) {
            e.printStackTrace();
            downloadResult.downloadResult("Error : IOException ", null);
          } catch (final Exception e) {
            e.printStackTrace();
            downloadResult.downloadResult("Error : Please check your internet connection ", null);
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
      downloadResult.downloadResult("Already downloading filters!!", null);
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void extractZip(String zipFile, int effectType,
      DownloadZipResult downloadZipResult) {
    String targetLocation = "";
    switch (effectType) {

      case 0: {
        //deepar
        targetLocation = getFilesDirectory() + FiltersConfig.DEEPAR_DIRECTORY;

        break;
      }
    }
    //create target location folder if not exist
    File fmd = new File(targetLocation);
    fmd.mkdirs();
    InputStream fin;
    try {
      fin = new FileInputStream(zipFile);
      ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fin));
      ZipEntry ze;

      while ((ze = zin.getNextEntry()) != null) {

        // Need to create directories if not exists, or
        // it will generate an Exception...
        if (ze.isDirectory()) {
          fmd = new File(targetLocation + ze.getName());
          fmd.mkdirs();
        } else {
          byte[] buffer = new byte[65536];
          int count;

          FileOutputStream fout = new FileOutputStream(targetLocation + ze.getName());
          BufferedOutputStream bufout = new BufferedOutputStream(fout);
          while ((count = zin.read(buffer)) != -1) {
            bufout.write(buffer, 0, count);
          }
          //out.flush();//flush it......
          bufout.close();
          zin.closeEntry();
          fout.close();
        }
      }
      zin.close();
      //To delete the zip file after extracting assets
      new File(zipFile).delete();
      FiltersConfig.setFiltersDownloadedAlready();
      downloadZipResult.zipExtractResult(null);
    } catch (IOException e) {
      e.printStackTrace();
      downloadZipResult.zipExtractResult("");
    }
  }
}
