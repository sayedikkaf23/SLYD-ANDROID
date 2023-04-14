package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;


/**
 * An on-the-wire representation of an MQTT DISCONNECT message.
 */
public class MqttDisconnect extends MqttWireMessage {
    public static final String KEY="Disc";

    public MqttDisconnect() {
        super(MqttWireMessage.MESSAGE_TYPE_DISCONNECT);
    }

    public MqttDisconnect(byte info, byte[] variableHeader) throws IOException {
        super(org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage.MESSAGE_TYPE_DISCONNECT);
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    /**
     * Returns whether or not this message needs to include a message ID.
     */
    public boolean isMessageIdRequired() {
        return false;
    }

    public String getKey() {
        return KEY;
    }
}
