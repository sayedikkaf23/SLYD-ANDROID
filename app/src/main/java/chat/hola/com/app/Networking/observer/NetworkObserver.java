package chat.hola.com.app.Networking.observer;

import chat.hola.com.app.Networking.connection.NetworkStateHolder;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by DELL on 3/31/2018.
 */

public class NetworkObserver extends Observable<NetworkStateHolder> {
    private Observer<? super NetworkStateHolder> observer;

    @Override
    protected void subscribeActual(Observer<? super NetworkStateHolder> observer) {
        this.observer = observer;
    }

    public void publishData(NetworkStateHolder data) {
        if (observer != null) {
            observer.onNext(data);
            observer.onComplete();
        }
    }

}