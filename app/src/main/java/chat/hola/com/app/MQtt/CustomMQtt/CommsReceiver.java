package chat.hola.com.app.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Receives MQTT packets from the server.
 */
public class CommsReceiver implements Runnable {
    private static final String CLASS_NAME = CommsReceiver.class.getName();
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);

    private boolean running = false;
    private Object lifecycle = new Object();
    private ClientState clientState = null;
    private ClientComms clientComms = null;
    private MqttInputStream in;
    private CommsTokenStore tokenStore = null;
    private Thread recThread = null;
    private volatile boolean receiving;

    public CommsReceiver(ClientComms clientComms, ClientState clientState, CommsTokenStore tokenStore, InputStream in) {
        this.in = new MqttInputStream(clientState, in);
        this.clientComms = clientComms;
        this.clientState = clientState;
        this.tokenStore = tokenStore;
        log.setResourceName(clientComms.getClient().getClientId());
    }

    /**
     * Starts up the Receiver's thread.
     */
    public void start(String threadName) {
        final String methodName = "start";
        //@TRACE 855=starting
        log.fine(CLASS_NAME, methodName, "855");
        synchronized (lifecycle) {
            if (!running) {
                running = true;
                recThread = new Thread(this, threadName);
                recThread.start();
            }
        }
    }

    /**
     * Stops the Receiver's thread.  This call will block.
     */
    public void stop() {
        final String methodName = "stop";
        synchronized (lifecycle) {
            //@TRACE 850=stopping
            log.fine(CLASS_NAME, methodName, "850");
            if (running) {
                running = false;
                receiving = false;
                if (!Thread.currentThread().equals(recThread)) {
                    try {
                        // Wait for the thread to finish.
                        recThread.join();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
        recThread = null;
        //@TRACE 851=stopped
        log.fine(CLASS_NAME, methodName, "851");
    }

    /**
     * Run loop to receive messages from the server.
     */
    public void run() {
        final String methodName = "run";
        MqttToken token = null;

        while (running && (in != null)) {
            try {
                //@TRACE 852=network read message
                log.fine(CLASS_NAME, methodName, "852");
                receiving = in.available() > 0;
                MqttWireMessage message = in.readMqttWireMessage();
                receiving = false;

                if (message instanceof MqttAck) {
                    token = tokenStore.getToken(message);
                    if (token != null) {
                        synchronized (token) {
                            // Ensure the notify processing is done under a lock on the token
                            // This ensures that the send processing can complete  before the
                            // receive processing starts! ( request and ack and ack processing
                            // can occur before request processing is complete if not!
                            clientState.notifyReceivedAck((MqttAck) message);
                        }
                    } else {
                        // It its an ack and there is no token then something is not right.
                        // An ack should always have a token assoicated with it.
                        throw new MqttException(MqttException.REASON_CODE_UNEXPECTED_ERROR);
                    }
                } else {
                    // A new message has arrived
                    clientState.notifyReceivedMsg(message);
                }
            } catch (MqttException ex) {
                //@TRACE 856=Stopping, MQttException
                log.fine(CLASS_NAME, methodName, "856", null, ex);
                running = false;
                // Token maybe null but that is handled in shutdown
                clientComms.shutdownConnection(token, ex);
            } catch (IOException ioe) {
                //@TRACE 853=Stopping due to IOException
                log.fine(CLASS_NAME, methodName, "853");

                running = false;
                // An EOFException could be raised if the broker processes the
                // DISCONNECT and ends the socket before we complete. As such,
                // only shutdown the connection if we're not already shutting down.
                if (!clientComms.isDisconnecting()) {
                    clientComms.shutdownConnection(token, new MqttException(MqttException.REASON_CODE_CONNECTION_LOST, ioe));
                }
            } finally {
                receiving = false;
            }
        }

        //@TRACE 854=<
        log.fine(CLASS_NAME, methodName, "854");
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the receiving state.
     *
     * @return true if the receiver is receiving data, false otherwise.
     */
    public boolean isReceiving() {
        return receiving;
    }
}
