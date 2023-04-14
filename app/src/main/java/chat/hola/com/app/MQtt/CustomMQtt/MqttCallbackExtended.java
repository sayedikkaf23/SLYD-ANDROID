package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.*;

/**
 * Extension of {@link org.eclipse.paho.client.mqttv3.MqttCallback} to allow new callbacks
 * without breaking the API for existing applications.
 * Classes implementing this interface can be registered on
 * both types of client: {@link IMqttClient#setCallback(org.eclipse.paho.client.mqttv3.MqttCallback)}
 * and {@link IMqttAsyncClient#setCallback(org.eclipse.paho.client.mqttv3.MqttCallback)}
 */
public interface MqttCallbackExtended extends MqttCallback {

    /**
     * Called when the connection to the server is completed successfully.
     * @param reconnect If true, the connection was the result of automatic reconnect.
     * @param serverURI The server URI that the connection was made to.
     */
    void connectComplete(boolean reconnect, String serverURI);

}
