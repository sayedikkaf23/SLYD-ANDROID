package chat.hola.com.app.Networking.observer;

import chat.hola.com.app.Networking.connection.SocialShareHolder;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by DELL on 3/31/2018.
 */

public class SocialShareObserver extends Observable<SocialShareHolder> {
    private Observer<? super SocialShareHolder> observer;

    @Override
    protected void subscribeActual(Observer<? super SocialShareHolder> observer) {
        this.observer = observer;
    }

    public void publishData(SocialShareHolder data) {
        if (observer != null) {
            observer.onNext(data);
            observer.onComplete();
        }
    }

}