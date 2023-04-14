package chat.hola.com.app.models;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>ConnectionObserver</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/24/2018.
 */

public class ConnectionObserver extends Observable<NetworkConnector> {
    private Observer<? super NetworkConnector> connectorObserver;


    public void isConnected(NetworkConnector networkConnector) {
        if (connectorObserver != null) {
            connectorObserver.onNext(networkConnector);
            connectorObserver.onComplete();
        }
    }

    @Override
    protected void subscribeActual(Observer<? super NetworkConnector> observer) {
        connectorObserver = observer;
    }
}
