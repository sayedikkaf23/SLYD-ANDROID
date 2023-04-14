package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */



import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Provides a mechanism to track the delivery progress of a message.
 *
 * <p>
 * Used to track the the delivery progress of a message when a publish is
 * executed in a non-blocking manner (run in the background)</p>
 *
 * @see org.eclipse.paho.client.mqttv3.MqttToken
 */
public class MqttDeliveryToken extends MqttToken implements IMqttDeliveryToken {


    public MqttDeliveryToken() {
        super();
    }

    public MqttDeliveryToken(String logContext) {
        super(logContext);
    }

    /**
     * Returns the message associated with this token.
     * <p>Until the message has been delivered, the message being delivered will
     * be returned. Once the message has been delivered <code>null</code> will be
     * returned.
     * @return the message associated with this token or null if already delivered.
     * @throws MqttException if there was a problem completing retrieving the message
     */
    public MqttMessage getMessage() throws MqttException {
        return internalTok.getMessage();
    }

    protected void setMessage(MqttMessage msg) {
        internalTok.setMessage(msg);
    }
}
