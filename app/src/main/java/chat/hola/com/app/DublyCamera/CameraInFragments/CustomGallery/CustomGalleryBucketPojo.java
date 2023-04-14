package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;

import java.util.ArrayList;

/**
 * Created by moda on 09/05/17.
 */

public class CustomGalleryBucketPojo {
    private ArrayList<CustomGalleryMediaItemPojo> list_data;
    private ArrayList<String> bucket_name_list;

    public ArrayList<CustomGalleryMediaItemPojo> getList_data() {
        return list_data;
    }

    public void setList_data(ArrayList<CustomGalleryMediaItemPojo> list_data) {
        this.list_data = list_data;
    }

    public ArrayList<String> getBucket_name_list() {
        return bucket_name_list;
    }

    public void setBucket_name_list(ArrayList<String> bucket_name_list) {
        this.bucket_name_list = bucket_name_list;
    }
}
