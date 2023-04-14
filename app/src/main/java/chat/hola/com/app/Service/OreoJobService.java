package chat.hola.com.app.Service;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import org.eclipse.paho.client.mqttv3.MqttException;

import chat.hola.com.app.AppController;
import chat.hola.com.app.MQtt.MqttAndroidClient;

/**
 * Created by moda on 05/09/18.
 */
@TargetApi(21)
public class OreoJobService extends JobService {

    private JobParameters mParams;
    private MqttAndroidClient mqttAndroidClient;

    //Assuming it takes maximum 5 seconds for
    private static long mRetryInterval = 2500;


    public OreoJobService() {
    }

    public boolean onStartJob(JobParameters params) {


        this.mParams = params;

        String userId = AppController.getInstance().getUserId();

        if (userId == null) return false;
        if (mqttAndroidClient == null) {


            if (params.getExtras().containsKey("fromJobScheduler")) {

                AppController.getInstance().setApplicationKilled(true);

            }
            mqttAndroidClient = AppController.getInstance().createMQttConnection(userId, false);
        }


        String command = params.getExtras().getString("command");
        if (command != null && command.equals("stop")) {

            try {

                mqttAndroidClient.disconnect();

            } catch (MqttException e) {
                e.printStackTrace();
            }
            this.endJob();
            return false;
        } else {


            if (!mqttAndroidClient.isConnected()) {
                this.connect();
            } else {

                this.scheduleJob((long) MQTT_constants.MQTT_JOB_INTERVAL_MS);
            }


            return true;
        }
    }


    public void scheduleReconnect() {
        //To automatically retry after mRetryInterval

        if (mRetryInterval < 60000L) {
            mRetryInterval = Math.min(mRetryInterval * 2L, 60000L);
        }


        this.scheduleJob(mRetryInterval);
    }


    void endJob() {
        this.jobFinished(this.mParams, false);
    }


    private void connect() {


        AppController.getInstance().connectMqttClient();

        this.scheduleReconnect();

    }


    void scheduleJob(long interval) {


        ComponentName serviceName = new ComponentName(this.getPackageName(), OreoJobService.class.getName());


        PersistableBundle extras = new PersistableBundle();
        extras.putString("command", "start");
        extras.putInt("fromJobScheduler", 1);
        JobInfo jobInfo = (new JobInfo.Builder(MQTT_constants.MQTT_JOB_ID, serviceName)).setExtras(extras).setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setMinimumLatency(interval).setOverrideDeadline(interval).build();
        try {
            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.schedule(jobInfo);
            }
            this.endJob();
        } catch (Exception ignored) {
        }
    }

    public boolean onStopJob(JobParameters params) {
        return false;
    }


}
