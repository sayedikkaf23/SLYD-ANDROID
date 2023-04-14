package io.isometrik.gs.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import io.isometrik.gs.Isometrik;

/**
 * The BroadcastReceiver connectivity receiver for receiving changes in the connectivity state.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

  private Isometrik isometrik;

  /**
   * Instantiates a new Connectivity receiver.
   *
   * @param isometrik the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public ConnectivityReceiver(Isometrik isometrik) {
    this.isometrik = isometrik;
  }

  @Override
  public void onReceive(final Context context, final Intent intent) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (cm != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());

        if (networkCapabilities != null && (networkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR))) {

          isometrik.reConnect();
        }
      } else {
        @SuppressWarnings("deprecation")
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //noinspection deprecation
        if (networkInfo != null && networkInfo.isConnected()) {

          isometrik.reConnect();
        }
      }
    }
  }
}