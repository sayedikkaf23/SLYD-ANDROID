package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */



/**
 * Represents an object used to send ping packet to MQTT broker
 * every keep alive interval.
 */
public interface MqttPingSender {

    /**
     * Initial method. Pass interal state of current client in.

     */
    void init(ClientComms comms);

    /**
     * Start ping sender. It will be called after connection is success.
     */
    void start();

    /**
     * Stop ping sender. It is called if there is any errors or connection shutdowns.
     */
    void stop();

    /**
     * Schedule next ping in certain delay.

     */
    void schedule(long delayInMilliseconds);

}
