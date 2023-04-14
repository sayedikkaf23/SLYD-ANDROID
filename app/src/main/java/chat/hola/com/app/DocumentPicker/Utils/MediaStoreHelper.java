package chat.hola.com.app.DocumentPicker.Utils;

/**
 * Created by moda on 22/08/17.
 */

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import chat.hola.com.app.DocumentPicker.Cursors.DocScannerTask;
import chat.hola.com.app.DocumentPicker.Cursors.LoaderCallbacks.FileResultCallback;
import chat.hola.com.app.DocumentPicker.Cursors.LoaderCallbacks.PhotoDirLoaderCallbacks;
import chat.hola.com.app.DocumentPicker.FilePickerConst;
import chat.hola.com.app.DocumentPicker.Models.Document;
import chat.hola.com.app.DocumentPicker.Models.PhotoDirectory;


public class MediaStoreHelper {

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
        if (activity.getSupportLoaderManager().getLoader(FilePickerConst.MEDIA_TYPE_IMAGE) != null)
            activity.getSupportLoaderManager().restartLoader(FilePickerConst.MEDIA_TYPE_IMAGE, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
        else
            activity.getSupportLoaderManager().initLoader(FilePickerConst.MEDIA_TYPE_IMAGE, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    public static void getVideoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
        if (activity.getSupportLoaderManager().getLoader(FilePickerConst.MEDIA_TYPE_VIDEO) != null)
            activity.getSupportLoaderManager().restartLoader(FilePickerConst.MEDIA_TYPE_VIDEO, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
        else
            activity.getSupportLoaderManager().initLoader(FilePickerConst.MEDIA_TYPE_VIDEO, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    public static void getDocs(FragmentActivity activity, FileResultCallback<Document> fileResultCallback) {
        new DocScannerTask(activity, fileResultCallback).execute();
    }
}