package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * An on-the-wire representation of an MQTT PUBCOMP message.
 */
public class MqttPubComp extends MqttAck {
    public MqttPubComp(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        dis.close();
    }

    public MqttPubComp(MqttPublish publish) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
        this.msgId = publish.getMessageId();
    }

    public MqttPubComp(int msgId) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBCOMP);
        this.msgId = msgId;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
