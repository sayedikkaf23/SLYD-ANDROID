package chat.hola.com.app.Utilities;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

import chat.hola.com.app.AppController;


/**
 * Created by moda on 13/07/17.
 */


/*
 * To keep the device awake when it is booting
 */
public class BootCompletedIntentReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.

  /*
         * For the case when phone was rebooted with the app being already started
         */




        AppController.getInstance().createMQttConnection(AppController.getInstance().getUserId(),true);

    }
}