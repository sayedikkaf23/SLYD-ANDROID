package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * An on-the-wire representation of an MQTT PUBACK message.
 */
public class MqttPubAck extends MqttAck {
    public MqttPubAck(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_PUBACK);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        dis.close();
    }

    public MqttPubAck(MqttPublish publish) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBACK);
        msgId = publish.getMessageId();
    }

    public MqttPubAck(int messageId) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBACK);
        msgId = messageId;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
