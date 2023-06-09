package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * An on-the-wire representation of an MQTT PUBREL message.
 */
public class MqttPubRel extends MqttPersistableWireMessage {

    /**
     * Createa a pubrel message based on a pubrec
     * @param pubRec
     */
    public MqttPubRel(MqttPubRec pubRec) {
        super(MqttWireMessage.MESSAGE_TYPE_PUBREL);
        this.setMessageId(pubRec.getMessageId());
    }

    /**
     * Creates a pubrel based on a pubrel set of bytes read fro the network
     * @param info
     * @param data
     * @throws IOException
     */
    public MqttPubRel(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_PUBREL);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }

    protected byte getMessageInfo() {
        return (byte)( 2 | (this.duplicate?8:0));
    }

    public String toString() {
        return super.toString() + " msgId " + msgId;
    }

}
