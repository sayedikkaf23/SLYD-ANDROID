package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */


/**
 * Abstract super-class of all acknowledgement messages.
 */
public abstract class MqttAck extends MqttWireMessage {
    public MqttAck(byte type) {
        super(type);
    }

    protected byte getMessageInfo() {
        return 0;
    }

    /**
     * @return String representation of the wire message
     */
    public String toString() {
        return super.toString() + " msgId " + msgId;
    }
}