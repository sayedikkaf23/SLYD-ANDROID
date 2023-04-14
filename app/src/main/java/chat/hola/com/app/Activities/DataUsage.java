package chat.hola.com.app.Activities;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 09/10/17.
 */

public class DataUsage extends AppCompatActivity {


    private Bus bus = AppController.getBus();


    private AppCompatCheckBox cb1, cb2, cb3, cb4;

    private View dialogView;

    private RelativeLayout root;


    private TextView tv3, tv5;
    private SharedPreferences sh;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        root = (RelativeLayout) findViewById(R.id.root);

        RelativeLayout wifi = (RelativeLayout) findViewById(R.id.wifi);

        RelativeLayout mobileData = (RelativeLayout) findViewById(R.id.mobileData);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv5 = (TextView) findViewById(R.id.tv5);


        LayoutInflater inflater = LayoutInflater.from(DataUsage.this);
        dialogView = inflater.inflate(R.layout.auto_download_popup, null);
        cb1 = (AppCompatCheckBox) dialogView.findViewById(R.id.cb1);
        cb2 = (AppCompatCheckBox) dialogView.findViewById(R.id.cb2);
        cb3 = (AppCompatCheckBox) dialogView.findViewById(R.id.cb3);
        cb4 = (AppCompatCheckBox) dialogView.findViewById(R.id.cb4);


        sh = AppController.getInstance().getSharedPreferences();


        updateText(0);
        updateText(1);


        wifi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        showPopup(1);
                                    }
                                }
        );

        mobileData.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {

                                              showPopup(0);
                                          }
                                      }
        );


        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         onBackPressed();

                                     }
                                 }
        );

        Typeface tf = AppController.getInstance().getRegularFont();

        TextView title = (TextView) findViewById(R.id.title);

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tv2 = (TextView) findViewById(R.id.tv2);

        TextView tv4 = (TextView) findViewById(R.id.tv4);

        TextView tv6 = (TextView) findViewById(R.id.tv6);

        tv1.setTypeface(tf, Typeface.NORMAL);
        tv2.setTypeface(tf, Typeface.NORMAL);
        tv3.setTypeface(tf, Typeface.NORMAL);
        tv4.setTypeface(tf, Typeface.NORMAL);
        tv5.setTypeface(tf, Typeface.NORMAL);
        tv6.setTypeface(tf, Typeface.NORMAL);

        title.setTypeface(tf, Typeface.BOLD);
        bus.register(this);
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(DataUsage.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }


    private void showPopup(final int type) {


        AlertDialog.Builder builder =
                new AlertDialog.Builder(DataUsage.this, 0);


        if (dialogView.getParent() != null)
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);

        builder.setView(dialogView);
        if (type == 0) {
            /*
             * MobileData
             *
             */
            cb1.setChecked(sh.getBoolean("mobileAudio", false));
            cb2.setChecked(sh.getBoolean("mobileVideo", false));
            cb3.setChecked(sh.getBoolean("mobilePhoto", false));
            cb4.setChecked(sh.getBoolean("mobileDocument", false));


        } else {

            /*
             * Wifi
             */

            cb1.setChecked(sh.getBoolean("wifiAudio", false));
            cb2.setChecked(sh.getBoolean("wifiVideo", false));
            cb3.setChecked(sh.getBoolean("wifiPhoto", false));
            cb4.setChecked(sh.getBoolean("wifiDocument", false));
        }


        if (type == 1)

            builder.setTitle(R.string.MobileData);
        else
            builder.setTitle(R.string.Wifi);


        builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
                if (type == 0) {
                    /*
                     * Mobile data
                     */

                    if (ActivityCompat.checkSelfPermission(DataUsage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        saveAutoDownloadSettings(0);
                    } else {

                        requestPermission(0);
                    }

                } else {
                    /*
                     * Wifi
                     */
                    if (ActivityCompat.checkSelfPermission(DataUsage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        saveAutoDownloadSettings(1);
                    } else {

                        requestPermission(1);
                    }
                }


            }

        });


        builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.cancel();

                    }
                }

        );
        builder.show();

    }


    private void saveAutoDownloadSettings(int type) {


        if (type == 0) {
            /*
             * Mobile data
             */


            sh.edit().putBoolean("mobileAudio", cb1.isChecked()).apply();
            sh.edit().putBoolean("mobileVideo", cb2.isChecked()).apply();
            sh.edit().putBoolean("mobilePhoto", cb3.isChecked()).apply();
            sh.edit().putBoolean("mobileDocument", cb4.isChecked()).apply();


            updateText(0);
        } else {
            /*
             * Wifi
             */
            sh.edit().putBoolean("wifiAudio", cb1.isChecked()).apply();
            sh.edit().putBoolean("wifiVideo", cb2.isChecked()).apply();
            sh.edit().putBoolean("wifiPhoto", cb3.isChecked()).apply();
            sh.edit().putBoolean("wifiDocument", cb4.isChecked()).apply();
            updateText(1);
        }


    }


    private void requestPermission(final int type) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar snackbar = Snackbar.make(root, R.string.Access,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(DataUsage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            type);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        } else

        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    type);
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                saveAutoDownloadSettings(requestCode);

            } else {


                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }
        } else {


            Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                    Snackbar.LENGTH_SHORT);


            snackbar.show();
            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        }


    }


    private void updateText(int type) {
        String str = "";


        if (type == 0) {

            /*
             *Mobile data
             */


            if (sh.getBoolean("mobileAudio", false)) {

                str = str + ",Audio";

            }

            if (sh.getBoolean("mobileVideo", false)) {

                str = str + ",Videos";
            }

            if (sh.getBoolean("mobilePhoto", false)) {

                str = str + ",Photos";
            }

            if (sh.getBoolean("mobileDocument", false)) {

                str = str + ",Documents";
            }

            if (!str.isEmpty()) {
                tv3.setText(str.substring(1));
            } else {
                tv3.setText(getString(R.string.None));
            }
        } else if (type == 1) {
            /*
             * Wifi
             */


            if (sh.getBoolean("wifiAudio", false)) {

                str = str + ",Audio";

            }

            if (sh.getBoolean("wifiVideo", false)) {

                str = str + ",Videos";
            }

            if (sh.getBoolean("wifiPhoto", false)) {

                str = str + ",Photos";
            }

            if (sh.getBoolean("wifiDocument", false)) {

                str = str + ",Documents";
            }

            if (!str.isEmpty()) {
                tv5.setText(str.substring(1));
            } else {
                tv5.setText(getString(R.string.None));
            }
        }


    }


}
