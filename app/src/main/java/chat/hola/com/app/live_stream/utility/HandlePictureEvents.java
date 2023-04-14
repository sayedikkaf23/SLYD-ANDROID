package chat.hola.com.app.live_stream.utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.ezcall.android.BuildConfig;

import java.io.File;
import java.io.IOException;

import chat.hola.com.app.Utilities.ApiOnServer;

import static android.os.Build.VERSION_CODES.N;


/**
 * <h>HandlePictureEvents</h>
 * this class open the popup for the option to take the image
 * after it takes the the, it crops the image
 * and then upload it to amazon
 * Created by moda on 8/17/2017.
 */

public class HandlePictureEvents {
    public File newFile;
    private Activity mContext;

    private Fragment fragment = null;


    public HandlePictureEvents(Activity mContext) {
        this.mContext = mContext;

    }


    /**
     * <h1>captureImage</h1>
     * This method is got called, when user chooses to take photos from camera.
     */
    public void captureImage() {
        String state;
        try {
            Uri newProfileImageUri;

            state = Environment.getExternalStorageState();
            String takenNewImage = "stream-" + String.valueOf(System.nanoTime()) + ".png";

            String dirName;

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                dirName = mContext.getExternalFilesDir(null)   + "/" + ApiOnServer.APP_NAME;


            } else {
                dirName = mContext.getFilesDir() + "/" + ApiOnServer.APP_NAME;

            }
            File folder = new File(dirName);
            folder.mkdirs();

            newFile = new File(dirName, takenNewImage);

            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= N)
                newProfileImageUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);

            if (fragment != null)
                fragment.startActivityForResult(intent, 0);
            else
                mContext.startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
