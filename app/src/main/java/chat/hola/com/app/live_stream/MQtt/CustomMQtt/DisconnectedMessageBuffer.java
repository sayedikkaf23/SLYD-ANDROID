package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import java.util.ArrayList;

public class DisconnectedMessageBuffer implements Runnable {

    private static final String CLASS_NAME = "DisconnectedMessageBuffer";
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    private DisconnectedBufferOptions bufferOpts;
    private ArrayList buffer;
    private Object	bufLock = new Object();  	// Used to synchronise the buffer
    private IDisconnectedBufferCallback callback;

    public DisconnectedMessageBuffer(DisconnectedBufferOptions options){
        this.bufferOpts = options;
        buffer = new ArrayList();
    }

    /**
     * This will add a new message to the offline buffer,
     * if the buffer is full and deleteOldestMessages is enabled
     * then the 0th item in the buffer will be deleted and the
     * new message will be added. If it is not enabled then an
     * MqttException will be thrown.
     * @param message
     * @throws MqttException
     */
    public void putMessage(MqttWireMessage message, MqttToken token) throws MqttException {
        BufferedMessage bufferedMessage = new BufferedMessage(message, token);
        synchronized (bufLock) {
            if(buffer.size() < bufferOpts.getBufferSize()){
                buffer.add(bufferedMessage);
            } else if(bufferOpts.isDeleteOldestMessages() == true){
                buffer.remove(0);
                buffer.add(bufferedMessage);
            }else {
                throw new MqttException(MqttException.REASON_CODE_DISCONNECTED_BUFFER_FULL);
            }
        }
    }

    /**
     * Retrieves a message from the buffer at the given index.
     * @param messageIndex
     * @return
     * @throws MqttException
     */
    public BufferedMessage getMessage(int messageIndex){
        synchronized (bufLock) {
            return((BufferedMessage) buffer.get(messageIndex));
        }
    }


    /**
     * Removes a message from the buffer
     * @param messageIndex
     * @throws MqttException
     */
    public void deleteMessage(int messageIndex){
        synchronized (bufLock) {
            buffer.remove(messageIndex);
        }
    }

    /**
     * Returns the number of messages currently in the buffer
     * @return
     * @throws MqttException
     */
    public int getMessageCount() {
        synchronized (bufLock) {
            return buffer.size();
        }
    }

    /**
     * Flushes the buffer of messages into an open connection
     */
    public void run() {
        final String methodName = "run";
        // @TRACE 516=Restoring all buffered messages.
        log.fine(CLASS_NAME, methodName, "516");
        while(getMessageCount() > 0){
            try {
                BufferedMessage bufferedMessage = getMessage(0);
                callback.publishBufferedMessage(bufferedMessage);
                // Publish was successful, remove message from buffer.
                deleteMessage(0);
            } catch (MqttException ex) {
                // Error occurred attempting to publish buffered message likely because the client is not connected
                // @TRACE 517=Error occured attempting to publish buffered message due to disconnect.
                log.warning(CLASS_NAME, methodName, "517");
                break;
            }
        }
    }

    public void setPublishCallback(IDisconnectedBufferCallback callback) {
        this.callback = callback;
    }

}
