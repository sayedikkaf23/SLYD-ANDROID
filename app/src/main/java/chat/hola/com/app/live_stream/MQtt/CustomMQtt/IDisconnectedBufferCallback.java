package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */


import org.eclipse.paho.client.mqttv3.MqttException;

public interface IDisconnectedBufferCallback {

    void publishBufferedMessage(BufferedMessage bufferedMessage) throws MqttException;

}
