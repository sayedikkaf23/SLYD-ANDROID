package chat.hola.com.app.live_stream.MQtt.CustomMQtt;

/**
 * Created by moda on 08/07/17.
 */

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * An <code>MqttOutputStream</code> lets applications write instances of
 * <code>MqttWireMessage</code>.
 */
public class MqttOutputStream extends OutputStream {
    private static final String CLASS_NAME = MqttOutputStream.class.getName();
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);

    private ClientState clientState = null;
    private BufferedOutputStream out;

    public MqttOutputStream(ClientState clientState, OutputStream out) {
        this.clientState = clientState;
        this.out = new BufferedOutputStream(out);
    }

    public void close() throws IOException {
        out.close();
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void write(byte[] b) throws IOException {
        out.write(b);
        clientState.notifySentBytes(b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        clientState.notifySentBytes(len);
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    /**
     * Writes an <code>MqttWireMessage</code> to the stream.
     */
    public void write(MqttWireMessage message) throws IOException, MqttException {
        final String methodName = "write";
        byte[] bytes = message.getHeader();
        byte[] pl = message.getPayload();
//		out.write(message.getHeader());
//		out.write(message.getPayload());
        out.write(bytes,0,bytes.length);
        clientState.notifySentBytes(bytes.length);

        int offset = 0;
        int chunckSize = 1024;
        while (offset < pl.length) {
            int length = Math.min(chunckSize, pl.length - offset);
            out.write(pl, offset, length);
            offset += chunckSize;
            clientState.notifySentBytes(length);
        }

        // @TRACE 500= sent {0}
        log.fine(CLASS_NAME, methodName, "500", new Object[]{message});
    }
}
