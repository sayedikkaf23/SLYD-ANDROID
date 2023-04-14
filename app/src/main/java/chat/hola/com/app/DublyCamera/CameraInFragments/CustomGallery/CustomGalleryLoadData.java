package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.AsyncTaskLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by moda on 09/05/17.
 */

public class CustomGalleryLoadData extends AsyncTaskLoader<CustomGalleryBucketPojo> {
    private ArrayList<CustomGalleryMediaItemPojo> list_data;
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_JPEG = ".jpeg";
    private static final String EXTENSION_PNG = ".png";
    private ArrayList<String> bucket_name_list;

    public CustomGalleryLoadData(Context context) {
        super(context);
        list_data = new ArrayList<>();
        bucket_name_list = new ArrayList<>();
    }

    @Override
    public CustomGalleryBucketPojo loadInBackground() {
        list_data.clear();
        bucket_name_list.clear();

        /*
         * Loading the video from the external storage*/
        String[] video_pro = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_TAKEN};
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        getDataFromMDB(uri, video_pro, null);
        /*
         * Loading the video from the internal storage*/
        Uri uri_video_interal = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
        getDataFromMDB(uri_video_interal, video_pro, null);
        /*
         * Loading the Image from the internal storage*/
        String[] image_pro = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN};
        Uri uri_Image_interal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        getDataFromMDB(uri_Image_interal, image_pro, null);
        /*
         * Loading the Image from the External storage*/
        Uri uri_Image_external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        getDataFromMDB(uri_Image_external, image_pro, null);

        /*
         * Sorting the element according to the date*/
//        Collections.sort(list_data);
        Collections.sort(bucket_name_list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1 != null && s2 != null)
                    return s1.compareToIgnoreCase(s2);
                else
                    return 0;
            }
        });
        CustomGalleryBucketPojo data = new CustomGalleryBucketPojo();
        data.setList_data(list_data);
        data.setBucket_name_list(bucket_name_list);
        return data;
    }

    /**
     * <h2>getDataFromMDB</h2>
     * <p>
     *
     * </p>
     */
    private void getDataFromMDB(Uri uri, String projection[], String selection) {
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, null, MediaStore.Video.Media.DATE_TAKEN + " DESC");
        assert cursor != null;
        int data_columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int date_columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        int bukket_columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if ((new File(cursor.getString(0))).length() > 10)
                addItem_List(cursor.getString(data_columnIndex), cursor.getString(date_columnIndex), cursor.getString(bukket_columnIndex));
            cursor.moveToNext();
        }
        cursor.close();
    }

    /**
     * <h2>addItem_List</h2>
     * <p>
     * Adding the item list to the .
     * </P>
     */
    private void addItem_List(String path, String created_date, String bukket_name) {


        if (bukket_name != null && bukket_name.equals("Filters")) {
            return;
        }
        CustomGalleryMediaItemPojo data_item;
        if (path.toLowerCase().endsWith(EXTENSION_JPG) || path.toLowerCase().endsWith(EXTENSION_JPEG) || path.toLowerCase().endsWith(EXTENSION_PNG)) {
            data_item = new CustomGalleryMediaItemPojo();
            data_item.setCreated_date(created_date);
            data_item.setPath(path);
            data_item.setBukket_name(bukket_name);
            data_item.setVideo(false);
            list_data.add(data_item);
        } else {
            data_item = new CustomGalleryMediaItemPojo();
            data_item.setPath(path);
            data_item.setCreated_date(created_date);
            data_item.setBukket_name(bukket_name);
            data_item.setVideo(true);
            list_data.add(data_item);
        }
        if (!bucket_name_list.contains(bukket_name)) {
            bucket_name_list.add(bukket_name);
        }

    }
}
