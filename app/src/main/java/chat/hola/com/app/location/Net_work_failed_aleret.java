package chat.hola.com.app.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * <h1>Net_work_failed_aleret</h1>
 * <p>
 * This class used to show alert which contains the user internet setting aleret.
 * by the help of the dilog class.{@see Dialog}.
 * </P>
 *
 * @author 3Embed.
 * @since 4/4/16.
 */
public class Net_work_failed_aleret {
    private static Net_work_failed_aleret NETWORK_FAIL = new Net_work_failed_aleret();
    private Dialog dialog_net_work_fail;
    private int TYPE_WIFI = 1, TYPE_MOBILE = 2;
    private Activity activity = null;
    private ProgressBar wifyi_progress_bar = null;
    private Internet_connection_Callback internet_connection_callback = null;
    private AppController appControlar_class;

    /**
     * Constructor of the aleret class.
     */
    private Net_work_failed_aleret() {
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static Net_work_failed_aleret getInstance() {
        if (NETWORK_FAIL == null) {
            NETWORK_FAIL = new Net_work_failed_aleret();
            return NETWORK_FAIL;
        } else {
            return NETWORK_FAIL;
        }
    }

    /**
     * <h2>net_work_fail</h2>
     * <p>
     * it is going to open a popup and provide option to the user to set option to set the wifyi and Deta connection.
     * </P>
     */
    public void net_work_fail(final Activity mactivity, Internet_connection_Callback connectioncallback) {

        internet_connection_callback = null;
        internet_connection_callback = connectioncallback;
        activity = mactivity;
        appControlar_class = (AppController) mactivity.getApplicationContext();
        /**
         * Network failed aleret is visible to user.
         */
        if (dialog_net_work_fail != null) {
            if (dialog_net_work_fail.isShowing()) {
                dialog_net_work_fail.dismiss();
            }
        }
        /**
         * Fonts for hum tum*/
        // Typeface robotoRegular =appControlar_class.robotoRegular_fonts();
        // Typeface robotMedium = appControlar_class.robotoMedium_fonts();

        ColorDrawable cd = new ColorDrawable();
        cd.setColor(Color.TRANSPARENT);
        dialog_net_work_fail = new Dialog(mactivity);
        dialog_net_work_fail.getWindow().setBackgroundDrawable(cd);
        dialog_net_work_fail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = mactivity.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.net_work_failed_aleret_layout, null);

        /**
         * Toggle button.*/
        final ToggleButton wifi = (ToggleButton) dialogView.findViewById(R.id.wifyi_button);
        wifi.setText(null);
        wifi.setTextOn(null);
        wifi.setTextOff(null);
        wifi.setChecked(false);

        /**
         * Toggle button.*/
        final ToggleButton data_pack = (ToggleButton) dialogView.findViewById(R.id.data_pack_button);
        data_pack.setText(null);
        data_pack.setTextOn(null);
        data_pack.setTextOff(null);
        data_pack.setChecked(false);

        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /**
                     * True for connect*/
                    connect_Internet(mactivity, TYPE_WIFI, true);
                    data_pack.setChecked(false);

                } else {
                    /**
                     * False for disconnect.*/
                    connect_Internet(mactivity, TYPE_WIFI, false);
                }
            }
        });

        data_pack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    wifi.setChecked(false);
                    connect_Internet(mactivity, TYPE_MOBILE, true);
                } else {
                    /**
                     * dismiss the progress bar.*/
                    if (wifyi_progress_bar != null) {
                        wifyi_progress_bar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_net_work_fail.dismiss();
                internet_connection_callback.onErrorConnection("Canceled !");
            }
        });

        ImageView setting_button = (ImageView) dialogView.findViewById(R.id.setting_button);
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_net_work_fail.dismiss();
                activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });

        /**
         * Wifi progress bar.*/
        wifyi_progress_bar = (ProgressBar) dialogView.findViewById(R.id.wifyi_progress_bar);

        /**
         * Data connection progress bar.*/

        TextView title_header = (TextView) dialogView.findViewById(R.id.title_header);
        //   title_header.setTypeface(robotoRegular);

        TextView query_title = (TextView) dialogView.findViewById(R.id.query_title);
        //   query_title.setTypeface(robotoRegular);

        cancel = (TextView) dialogView.findViewById(R.id.cancel);
        //   cancel.setTypeface(robotoRegular);

        dialog_net_work_fail.setContentView(dialogView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog_net_work_fail.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog_net_work_fail.setCancelable(false);
        dialog_net_work_fail.show();
    }

    /**
     * <h2>connect_Internet</h2>
     * <p>
     * Method call to connect with the internet.
     * </P>
     */
    private void connect_Internet(Activity activity, int connection_type, boolean status) {
        if (connection_type == TYPE_WIFI) {
            if (wifyi_progress_bar != null) {
                wifyi_progress_bar.setVisibility(View.VISIBLE);
            }
            intialize_receiver(activity);
          //  connect_disconnect_the_wifi_network(activity, status);

        } else if (connection_type == TYPE_MOBILE) {
            if (wifyi_progress_bar != null) {
                wifyi_progress_bar.setVisibility(View.VISIBLE);
            }
            intialize_receiver(activity);
            connect_to_the_Mobile_data(activity);
        }
    }

    /**
     * Initializing the broadcast receiver.
     */
    private void intialize_receiver(final Activity activity) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (null != activeNetwork) {
                    switch (activeNetwork.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            internet_connection_callback.onSucessConnection("Wify enable");
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            internet_connection_callback.onSucessConnection("Data enable");
                            break;
                        default:
                            internet_connection_callback.onErrorConnection("Unable to connect.");
                    }

                    /**
                     * removing the aleret.*/
                    if (dialog_net_work_fail != null) {
                        if (dialog_net_work_fail.isShowing()) {
                            dialog_net_work_fail.dismiss();
                        }
                    }

                    if (wifyi_progress_bar != null) {
                        wifyi_progress_bar.setVisibility(View.GONE);
                    }
                    unRegister_network_service(activity, this);
                }

            }
        };
        /**
         * Intent filter.*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * Method to call and switch on and off the wifi.
     */
//    private void connect_disconnect_the_wifi_network(Activity activity, boolean connecting_status) {
//        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(connecting_status);
//    }

    /**
     * Connecting to the mobile data  pack.
     */
    private void connect_to_the_Mobile_data(final Activity activity) {
        activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    }

    /**
     * UnRegistering the broad cast receiver.
     */
    private void unRegister_network_service(Activity activity, BroadcastReceiver broadcastReceiver) {
        /**
         * Setting the broad cast receiver.*/
        activity.unregisterReceiver(broadcastReceiver);
    }

    /**
     * <h2>InternetConnection call back.</h2>
     */
    public interface Internet_connection_Callback {
        void onSucessConnection(String connection_Type);

        void onErrorConnection(String error);
    }
}
