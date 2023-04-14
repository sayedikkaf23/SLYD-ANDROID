package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


public abstract class MqttPersistableWireMessage extends MqttWireMessage
        implements MqttPersistable {

    public MqttPersistableWireMessage(byte type) {
        super(type);
    }

    public byte[] getHeaderBytes() throws MqttPersistenceException {
        try {
            return getHeader();
        }
        catch (MqttException ex) {
            throw new MqttPersistenceException(ex.getCause());
        }
    }

    public int getHeaderLength() throws MqttPersistenceException {
        return getHeaderBytes().length;
    }

    public int getHeaderOffset() throws MqttPersistenceException {
        return 0;
    }

//	public String getKey() throws MqttPersistenceException {
//		return new Integer(getMessageId()).toString();
//	}

    public byte[] getPayloadBytes() throws MqttPersistenceException {
        try {
            return getPayload();
        }
        catch (MqttException ex) {
            throw new MqttPersistenceException(ex.getCause());
        }
    }

    public int getPayloadLength() throws MqttPersistenceException {
        return 0;
    }

    public int getPayloadOffset() throws MqttPersistenceException {
        return 0;
    }

}