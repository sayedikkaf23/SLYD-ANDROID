package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * An on-the-wire representation of an MQTT UNSUBSCRIBE message.
 */
public class MqttUnsubscribe extends MqttWireMessage {

    private String[] names;
    private int count;

    /**
     * Constructs an MqttUnsubscribe
     */
    public MqttUnsubscribe(String[] names) {
        super(MqttWireMessage.MESSAGE_TYPE_UNSUBSCRIBE);
        this.names = names;
    }

    /**
     * Constructor for an on the wire MQTT un-subscribe message
     *
     * @param info
     * @param data
     * @throws IOException
     */
    public MqttUnsubscribe(byte info, byte[] data) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_UNSUBSCRIBE);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        msgId = dis.readUnsignedShort();

        count = 0;
        names = new String[10];
        boolean end = false;
        while (!end) {
            try {
                names[count] = decodeUTF8(dis);
            } catch (Exception e) {
                end = true;
            }
        }
        dis.close();
    }

    /**
     * @return string representation of this un-subscribe packet
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append(" names:[");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("\"" + names[i] + "\"");
        }
        sb.append("]");
        return sb.toString();
    }

    protected byte getMessageInfo() {
        return (byte) (2 | (duplicate ? 8 : 0));
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(msgId);
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (int i=0; i<names.length; i++) {
                encodeUTF8(dos, names[i]);
            }
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isRetryable() {
        return true;
    }
}
