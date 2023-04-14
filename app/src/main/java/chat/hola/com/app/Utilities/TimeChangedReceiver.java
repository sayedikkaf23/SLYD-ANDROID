package chat.hola.com.app.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import chat.hola.com.app.AppController;


/**
 * Created by moda on 13/07/17.
 */


/**
 * For handling the time changes
 */
public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        AppController.getInstance().getCurrentTime();
        //Do whatever you need to
    }

}