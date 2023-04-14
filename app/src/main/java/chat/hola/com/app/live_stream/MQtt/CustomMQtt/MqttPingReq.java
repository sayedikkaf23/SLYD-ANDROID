package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

/**
 * An on-the-wire representation of an MQTT PINGREQ message.
 */
public class MqttPingReq extends MqttWireMessage {
    public static final String KEY = "Ping";

    public MqttPingReq() {
        super(MqttWireMessage.MESSAGE_TYPE_PINGREQ);
    }

    public MqttPingReq(byte info, byte[] variableHeader) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_PINGREQ);
    }

    /**
     * Returns <code>false</code> as message IDs are not required for MQTT
     * PINGREQ messages.
     */
    public boolean isMessageIdRequired() {
        return false;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    protected byte getMessageInfo() {
        return 0;
    }

    public String getKey() {
        return KEY;
    }
}

