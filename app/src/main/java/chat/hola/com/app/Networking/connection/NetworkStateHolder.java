package chat.hola.com.app.Networking.connection;

/**
 * <h2>NetworkStateHolder</h2>
 * <p>
 * it is holder class for the network service class
 * data provider to carry the network info from the required class.
 * </P>
 *
 * @author 3Embed.
 * @version 1.0.
 */
public class NetworkStateHolder {
    private String message;
    private boolean isConnected;
    private ConnectionType connectionType;
    private boolean prevIsConneccted;

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isPrevIsConneccted() {
        return prevIsConneccted;
    }

    public void setPrevIsConneccted(boolean prevIsConneccted) {
        this.prevIsConneccted = prevIsConneccted;
    }
}
