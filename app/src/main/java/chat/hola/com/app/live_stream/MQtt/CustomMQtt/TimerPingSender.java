package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Default ping sender implementation
 *
 * <p>This class implements the pinger interface
 * allowing applications to send ping packet to server every keep alive interval.
 * </p>
 *
 * @see org.eclipse.paho.client.mqttv3.MqttPingSender
 */
public class TimerPingSender implements MqttPingSender {
    private static final String CLASS_NAME = TimerPingSender.class.getName();
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT,CLASS_NAME);

    private ClientComms comms;
    private Timer timer;

    public void init(ClientComms comms) {
        if (comms == null) {
            throw new IllegalArgumentException("ClientComms cannot be null.");
        }
        this.comms = comms;
    }

    public void start() {
        final String methodName = "start";
        String clientid = comms.getClient().getClientId();

        //@Trace 659=start timer for client:{0}
        log.fine(CLASS_NAME, methodName, "659", new Object[]{clientid});

        timer = new Timer("MQTT Ping: " + clientid);
        //Check ping after first keep alive interval.
        timer.schedule(new PingTask(), comms.getKeepAlive());
    }

    public void stop() {
        final String methodName = "stop";
        //@Trace 661=stop
        log.fine(CLASS_NAME, methodName, "661", null);
        if(timer != null){
            timer.cancel();
        }
    }

    public void schedule(long delayInMilliseconds) {
        timer.schedule(new PingTask(), delayInMilliseconds);
    }

    private class PingTask extends TimerTask {
        private static final String methodName = "PingTask.run";

        public void run() {
            //@Trace 660=Check schedule at {0}
            log.fine(CLASS_NAME, methodName, "660", new Object[]{new Long(System.currentTimeMillis())});
            comms.checkForActivity();
        }
    }
}
