package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */



/**
 * A BufferedMessage contains an MqttWire Message and token
 * it allows both message and token to be buffered when the client
 * is in resting state
 */
public class BufferedMessage {

    private MqttWireMessage message;
    private MqttToken token;

    public BufferedMessage(MqttWireMessage message, MqttToken token) {
        this.message = message;
        this.token = token;
    }

    public MqttWireMessage getMessage() {
        return message;
    }

    public MqttToken getToken() {
        return token;
    }
}
