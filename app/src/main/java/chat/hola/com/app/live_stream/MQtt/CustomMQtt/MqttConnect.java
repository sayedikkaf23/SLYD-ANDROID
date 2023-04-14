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
 * An on-the-wire representation of an MQTT CONNECT message.
 */
public class MqttConnect extends MqttWireMessage {

    public static final String KEY = "Con";

    private String clientId;
    private boolean cleanSession;
    private MqttMessage willMessage;
    private String userName;
    private char[] password;
    private int keepAliveInterval;
    private String willDestination;
    private int MqttVersion;

    /**
     * Constructor for an on the wire MQTT connect message
     *
     * @param info
     * @param data
     * @throws IOException
     * @throws MqttException
     */
    public MqttConnect(byte info, byte[] data) throws IOException, MqttException {
        super(MqttWireMessage.MESSAGE_TYPE_CONNECT);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        String protocol_name = decodeUTF8(dis);
        int protocol_version = dis.readByte();
        byte connect_flags = dis.readByte();
        keepAliveInterval = dis.readUnsignedShort();
        clientId = decodeUTF8(dis);
        dis.close();
    }

    public MqttConnect(String clientId, int MqttVersion, boolean cleanSession, int keepAliveInterval, String userName, char[] password, MqttMessage willMessage, String willDestination) {
        super(MqttWireMessage.MESSAGE_TYPE_CONNECT);
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.keepAliveInterval = keepAliveInterval;
        this.userName = userName;
        this.password = password;
        this.willMessage = willMessage;
        this.willDestination = willDestination;
        this.MqttVersion = MqttVersion;
    }

    public String toString() {
        String rc = super.toString();
        rc += " clientId " + clientId + " keepAliveInterval " + keepAliveInterval;
        return rc;
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            if (MqttVersion == 3) {
                encodeUTF8(dos,"MQIsdp");
            }
            else if (MqttVersion == 4) {
                encodeUTF8(dos,"MQTT");
            }
            dos.write(MqttVersion);

            byte connectFlags = 0;

            if (cleanSession) {
                connectFlags |= 0x02;
            }

            if (willMessage != null ) {
                connectFlags |= 0x04;
                connectFlags |= (willMessage.getQos()<<3);
                if (willMessage.isRetained()) {
                    connectFlags |= 0x20;
                }
            }

            if (userName != null) {
                connectFlags |= 0x80;
                if (password != null) {
                    connectFlags |= 0x40;
                }
            }
            dos.write(connectFlags);
            dos.writeShort(keepAliveInterval);
            dos.flush();
            return baos.toByteArray();
        } catch(IOException ioe) {
            throw new MqttException(ioe);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            encodeUTF8(dos,clientId);

            if (willMessage != null) {
                encodeUTF8(dos,willDestination);
                dos.writeShort(willMessage.getPayload().length);
                dos.write(willMessage.getPayload());
            }

            if (userName != null) {
                encodeUTF8(dos,userName);
                if (password != null) {
                    encodeUTF8(dos,new String(password));
                }
            }
            dos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new MqttException(ex);
        }
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
