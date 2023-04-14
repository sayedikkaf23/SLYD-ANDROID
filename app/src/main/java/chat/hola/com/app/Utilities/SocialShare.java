package chat.hola.com.app.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by ankit on 9/3/18.
 */

public class SocialShare {

//    String type = "image*/";
//    String filename = "/myPhoto.jpg";
//    String mediaPath = Environment.getExternalStorageDirectory() + filename;
    public  String INSTA_PACKAGE = "com.instagram.android";
    /*String type = "video*//*";
    String filename = "/myVideo.mp4";
    String mediaPath = Environment.getExternalStorageDirectory() + filename;
    */

    Context context;

    public SocialShare(Context context){
        this.context = context;
    }

    public boolean checkAppinstall(String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public void createInstagramIntent(String type,Uri mediaUri, String mediaPath) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        if(mediaUri == null) {
            File media = new File(mediaPath);
            mediaUri = Uri.fromFile(media);
        }

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, mediaUri);
        share.putExtra(Intent.EXTRA_TEXT, "testing");
        share.setPackage("com.instagram.android");
        // Broadcast the Intent.
        context.startActivity(Intent.createChooser(share, "Share to"));
    }

}
