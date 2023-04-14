package chat.hola.com.app.socialDetail.video_manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.volokh.danylo.video_player_manager.Config;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 11/30/2018.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    public static boolean isNetworkConnected(Context ctx) {
        boolean isNetworkConnected;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        isNetworkConnected = ni != null;
        //if (SHOW_LOGS) Log.v(TAG, "isNetworkConnected, " + isNetworkConnected);
        return isNetworkConnected;
    }
}
