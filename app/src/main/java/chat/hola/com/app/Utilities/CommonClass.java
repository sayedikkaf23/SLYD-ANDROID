package chat.hola.com.app.Utilities;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Patterns;

/**
 * <h1>{@link CommonClass}</h1>
 * <p>This is utility class used to declare common method in project.</p>
 * @author : Hardik Karkar
 * @since : 23rd May 2019
 *
 */


public class CommonClass {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * <p>This method is used for creating fullname from first name and last name</p>
     * */
    public static String createFullName(String fname,String lname){

        String contactName;

        if(!fname.trim().isEmpty() && !lname.trim().isEmpty()) {
                contactName = fname.substring(0,1).toUpperCase() + fname.substring(1) + " " + lname.substring(0,1).toUpperCase()+lname.substring(1);
        }
        else if (lname.trim().isEmpty() && !fname.trim().isEmpty())
            contactName = fname.substring(0,1).toUpperCase()+fname.substring(1);
        else if (fname.trim().isEmpty() && !lname.trim().isEmpty())
            contactName = lname.substring(0,1).toUpperCase()+lname.substring(1);
        else
            contactName = "";

        return contactName;
    }

    /**
     *<p>This method is used to validate the email address</p>
     * */
    public static boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }


    /**
     * <h>GetDeviceWidth</h>
     * <p>
     *     In this method we used to find the width of the current android device.
     * </p>
     * @param activity the current context
     * @return it returns the width of the device in pixel
     */
    public static int getDeviceWidth(Activity activity)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //int height = metrics.heightPixels;
        //int width = metrics.widthPixels;
        return metrics.widthPixels;
    }

    /**
     * <h>GetDeviceHeight</h>
     * <p>
     *     In this method we used to find the height of the current android device.
     * </p>
     * @param activity the current context
     * @return it returns the height of the device in pixel
     */
    public static int getDeviceHeight(Activity activity)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //int height = metrics.heightPixels;
        //int width = metrics.widthPixels;
        return metrics.heightPixels;
    }

    /**
     * <h>GetDeviceWidth</h>
     * <p>
     *     In this method we used to find the width of the current android device.
     * </p>
     * @param context the current context
     * @return it returns the width of the device in pixel
     */
    public static int getDeviceWidth(Context context)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //int height = metrics.heightPixels;
        //int width = metrics.widthPixels;
        return metrics.widthPixels;
    }

    /**
     *<p>This method is used to validate the email address</p>
     * */
    public static boolean validatingEmail(String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !Patterns.DOMAIN_NAME.matcher(email).matches();
    }
}
