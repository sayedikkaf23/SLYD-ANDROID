package chat.hola.com.app.Networking.observer;

import chat.hola.com.app.Networking.connection.ContactHolder;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by DELL on 3/31/2018.
 */

public class ContactObserver extends Observable<ContactHolder> {
    private Observer<? super ContactHolder> observer;

    @Override
    protected void subscribeActual(Observer<? super ContactHolder> observer) {
        this.observer = observer;
    }

    public void publishData(ContactHolder data) {
        if (observer != null) {
            observer.onNext(data);
            observer.onComplete();
        }
    }

}