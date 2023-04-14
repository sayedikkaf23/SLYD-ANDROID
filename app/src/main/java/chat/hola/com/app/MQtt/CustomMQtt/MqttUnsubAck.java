package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */


import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * An on-the-wire representation of an MQTT UNSUBACK.
 */
public class MqttUnsubAck extends MqttAck {

    public MqttUnsubAck(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_UNSUBACK);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        // Not needed, as the client never encodes an UNSUBACK
        return new byte[0];
    }
}
