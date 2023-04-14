package chat.hola.com.app.postshare;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@TargetApi(29)
public class AndroidQMediaUtil {

  public static void addMediaToGallery(String mediaFileName, int type, Context context,
      File downloadedFile, String appName) {

    ContentValues contentValues;
    contentValues = new ContentValues();
    if (type == 0) {
      //images or gi
      contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
          Environment.DIRECTORY_PICTURES + "/" + appName);
      contentValues.put(MediaStore.Images.Media.TITLE, mediaFileName);
      contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
    } else if (type == 1) {
      //video
      contentValues.put(MediaStore.Video.Media.RELATIVE_PATH,
          Environment.DIRECTORY_MOVIES + "/" + appName);
      contentValues.put(MediaStore.Video.Media.TITLE, mediaFileName);
      contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
      contentValues.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Video.Media.IS_PENDING, 1);
    } else {
      //gif
      contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
          Environment.DIRECTORY_PICTURES + "/" + appName);
      contentValues.put(MediaStore.Images.Media.TITLE, mediaFileName);
      contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
      contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
    }
    ContentResolver resolver = context.getContentResolver();
    Uri collection;

    if(type==1) {
      collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
    }else {
      collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
    }
      Uri mediaUri = resolver.insert(collection, contentValues);

      ParcelFileDescriptor pfd;

      try {
        pfd = context.getContentResolver().openFileDescriptor(mediaUri, "w");

        FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());

        // get the already saved media as fileinputstream

        FileInputStream in = new FileInputStream(downloadedFile);

        byte[] buf = new byte[8192];
        int len;
        while ((len = in.read(buf)) > 0) {

          out.write(buf, 0, len);
        }

        out.close();
        in.close();
        pfd.close();
      } catch (Exception e) {

        e.printStackTrace();
      }

      contentValues.clear();
      deleteFileFromDisk(downloadedFile.getAbsolutePath());
      MediaScannerConnection.scanFile(context, new String[]{downloadedFile.getAbsolutePath()},
              null, null);

      if(type==1) {
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
      }else {
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
      }
      context.getContentResolver().update(mediaUri, contentValues, null, null);
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
}
