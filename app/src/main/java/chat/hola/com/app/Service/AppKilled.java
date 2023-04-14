package chat.hola.com.app.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;


import chat.hola.com.app.AppController;
import chat.hola.com.app.MQtt.MqttService;


/**
 * Created by moda on 21/06/17.
 */

public class AppKilled extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public void onTaskRemoved(Intent rootIntent) {


        if (AppController.getInstance().isActiveOnACall()) {
/*
 *If active on a call and the app is slided off,then have to make myself available
 */


            AppController.getInstance().cutCallOnKillingApp(false);


        }

        AppController.getInstance().disconnect();

        AppController.getInstance().setApplicationKilled(true);

        AppController.getInstance().createMQttConnection(AppController.getInstance().getUserId(),true);


        /*
         * For sticky service not restarting on KITKAT devices
         */


        Intent restartService = new Intent(getApplicationContext(),
                MqttService.class);
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);


        stopSelf();
    }
}