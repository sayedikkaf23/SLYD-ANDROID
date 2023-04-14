package chat.hola.com.app.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import chat.hola.com.app.AppController;


/**
 * Created by moda on 13/07/17.
 */

public class ShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Insert code here


        if (!AppController.getInstance().getSharedPreferences().getBoolean("applicationKilled", true)) {

            if (AppController.getInstance().isActiveOnACall()) {
/*
 *If active on a call and the app is slided off,then have to make myself available
 */


                AppController.getInstance().cutCallOnKillingApp(false);


            }

       //     AppController.getInstance().saveActiveTimers();


            try {
                AppController.getInstance().disconnect();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            AppController.getInstance().setApplicationKilled(true);


        }


    }

}