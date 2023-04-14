package chat.hola.com.app.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ankit on 18/4/18.
 */

public class MediaDownloader {
    private String mediaUrl = "";
    private Context context;
    private MediaDownloadCallback callback = null;
    private int progress = 0;
    private int oldProgress = 0;

    public MediaDownloader(Context context){
        this.context = context;
    }

    public interface MediaDownloadCallback{
        void onComplete(String mediaPath);
        void onProgress(int progress);
        void onError(String error);
    }

    public void setMediaDownloadCallback(MediaDownloadCallback callback){
        this.callback = callback;
    }

    public void start(@NonNull String mediaUrl){
        progress = 0;
        this.mediaUrl = mediaUrl;
        new DownloadVidTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , new String[]{mediaUrl});
    }

    /*
     * Get the file of the required type ie. Image or video.*/
    private File getFile(String isVideo) throws IOException {
        File file;
        long time = System.currentTimeMillis();
        String temp_video = "media" + time;
        String suffix;
        if (isVideo.equalsIgnoreCase("1")) {
            suffix = ".mp4";
        } else {
            suffix = ".jpg";
        }
        file = File.createTempFile(temp_video,suffix, context.getExternalFilesDir(null)  );
        //file = new File("0", "test.jpeg");
        return file;
    }

    private class DownloadVidTask extends AsyncTask<String, Integer, String> {
        private boolean isError = false;

        protected String doInBackground(String... param) {
            isError = false;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            File file = null;
            String url = param[0];
            long size = 0;
            long downloaded = 0;
            String details = "";
            RandomAccessFile accessFile;
            try {
                if(url.contains("mp4"))
                    file = getFile("1");
                else
                    file = getFile("0");
                //accessFile = new RandomAccessFile(file,"rw");
                URL url_path = new URL(url);
                URLConnection connection = url_path.openConnection();
                connection.connect();
                connection.getContentType();
                size = connection.getContentLength();
                inputStream = new BufferedInputStream(url_path.openStream());
                outputStream = new FileOutputStream(file);
                byte data[] = new byte[1024];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    downloaded+=count;
                    progress = (int) ((downloaded /(float)size)*100);
                    if(oldProgress < progress){
                        oldProgress = progress;
                        publishProgress(new Integer[]{progress});
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                isError = true;
                details = "Error on loading file.";
            } finally {
                try {
                    assert outputStream != null;
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    isError = true;
                    details = "Error on loading file.";
                }
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    isError = true;
                    details = "Error on loading file.";
                }
            }
            if (file.length() > 12582912) {
                isError = true;
                details = "File size is more then 12MB.";
            }
            return isError ? details : file.getPath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(callback != null)
                callback.onProgress(values[0]);
        }

        protected void onPostExecute(String result) {
            if (isError) {
                if (callback != null)
                    callback.onError(result);
            } else {
                if (callback != null)
                    callback.onComplete(result);
            }
        }
    }
}
