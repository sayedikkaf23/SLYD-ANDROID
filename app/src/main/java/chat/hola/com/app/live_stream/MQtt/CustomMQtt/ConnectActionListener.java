package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */


import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


/**
 * <p>This class handles the connection of the AsyncClient to one of the available URLs.</p>
 * <p>The URLs are supplied as either the singleton when the client is created, or as a list in the connect options.</p>
 * <p>This class uses its own onStreamSubscribed and onFailure callbacks in preference to the user supplied callbacks.</p>
 * <p>An attempt is made to connect to each URL in the list until either a connection attempt succeeds or all the URLs have been tried</p>
 * <p>If a connection succeeds then the users token is notified and the users onStreamSubscribed callback is called.</p>
 * <p>If a connection fails then another URL in the list is attempted, otherwise the users token is notified
 * and the users onFailure callback is called</p>
 */
public class ConnectActionListener implements IMqttActionListener {

    private MqttClientPersistence persistence;
    private MqttAsyncClient client;
    private ClientComms comms;
    private MqttConnectOptions options;
    private MqttToken userToken;
    private Object userContext;
    private IMqttActionListener userCallback;
    private int originalMqttVersion;
    private MqttCallbackExtended mqttCallbackExtended;
    private boolean reconnect;

    /**
     * @param persistence
     * @param client
     * @param comms
     * @param options
     * @param userToken
     * @param userContext
     * @param userCallback
     */
    public ConnectActionListener(
            MqttAsyncClient client,
            MqttClientPersistence persistence,
            ClientComms comms,
            MqttConnectOptions options,
            MqttToken userToken,
            Object userContext,
            IMqttActionListener userCallback,
            boolean reconnect) {
        this.persistence = persistence;
        this.client = client;
        this.comms = comms;
        this.options = options;
        this.userToken = userToken;
        this.userContext = userContext;
        this.userCallback = userCallback;
        this.originalMqttVersion = options.getMqttVersion();
        this.reconnect = reconnect;
    }

    /**
     * If the connect succeeded then call the users onStreamSubscribed callback
     *
     * @param token
     */
    public void onSuccess(IMqttToken token) {
        if (originalMqttVersion == MqttConnectOptions.MQTT_VERSION_DEFAULT) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
        }
        userToken.internalTok.markComplete(token.getResponse(), null);
        userToken.internalTok.notifyComplete();
        userToken.internalTok.setClient(this.client); // fix bug 469527 - maybe should be set elsewhere?

        if(reconnect){
            comms.notifyReconnect();
        }

        if (userCallback != null) {
            userToken.setUserContext(userContext);
            userCallback.onSuccess(userToken);
        }

        if(mqttCallbackExtended != null){
            String serverURI = comms.getNetworkModules()[comms.getNetworkModuleIndex()].getServerURI();
            mqttCallbackExtended.connectComplete(reconnect, serverURI);
        }


    }

    /**
     * The connect failed, so try the next URI on the list.
     * If there are no more URIs, then fail the overall connect.
     *
     * @param token
     * @param exception
     */
    public void onFailure(IMqttToken token, Throwable exception) {

        int numberOfURIs = comms.getNetworkModules().length;
        int index = comms.getNetworkModuleIndex();

        if ((index + 1) < numberOfURIs || (originalMqttVersion == MqttConnectOptions.MQTT_VERSION_DEFAULT && options.getMqttVersion() == MqttConnectOptions.MQTT_VERSION_3_1_1)) {

            if (originalMqttVersion == MqttConnectOptions.MQTT_VERSION_DEFAULT) {
                if (options.getMqttVersion() == MqttConnectOptions.MQTT_VERSION_3_1_1) {
                    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                }
                else {
                    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                    comms.setNetworkModuleIndex(index + 1);
                }
            }
            else {
                comms.setNetworkModuleIndex(index + 1);
            }
            try {
                connect();
            }
            catch (MqttPersistenceException e) {
                onFailure(token, e); // try the next URI in the list
            }
        }
        else {
            if (originalMqttVersion == MqttConnectOptions.MQTT_VERSION_DEFAULT) {
                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
            }
            MqttException ex;
            if (exception instanceof MqttException) {
                ex = (MqttException) exception;
            }
            else {
                ex = new MqttException(exception);
            }
            userToken.internalTok.markComplete(null, ex);
            userToken.internalTok.notifyComplete();
            userToken.internalTok.setClient(this.client); // fix bug 469527 - maybe should be set elsewhere?

            if (userCallback != null) {
                userToken.setUserContext(userContext);
                userCallback.onFailure(userToken, exception);
            }
        }
    }

    /**
     * Start the connect processing
     * @throws MqttPersistenceException
     */
    public void connect() throws MqttPersistenceException {
        MqttToken token = new MqttToken(client.getClientId());
        token.setActionCallback(this);
        token.setUserContext(this);

        persistence.open(client.getClientId(), client.getServerURI());

        if (options.isCleanSession()) {
            persistence.clear();
        }

        if (options.getMqttVersion() == MqttConnectOptions.MQTT_VERSION_DEFAULT) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        }

        try {
            comms.connect(options, token);
        }
        catch (MqttException e) {
            onFailure(token, e);
        }
    }

    /**
     * Set the MqttCallbackExtened callback to receive connectComplete callbacks
     * @param mqttCallbackExtended
     */
    public void setMqttCallbackExtended(MqttCallbackExtended mqttCallbackExtended) {
        this.mqttCallbackExtended = mqttCallbackExtended;
    }

}