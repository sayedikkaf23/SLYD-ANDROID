package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */



public class MqttReceivedMessage extends MqttMessage {

    public void setMessageId(int msgId) {
        super.setId(msgId);
    }

    public int getMessageId() {
        return super.getId();
    }

    // This method exists here to get around the protected visibility of the
    // super class method.
    public void setDuplicate(boolean value) {
        super.setDuplicate(value);
    }
}
