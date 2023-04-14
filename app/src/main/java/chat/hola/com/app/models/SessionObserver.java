package chat.hola.com.app.models;

import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/24/2018.
 */

public class SessionObserver {
    private ConnectableObservable<Boolean> connectableObservable;
    private ObservableEmitter<Boolean> emitor;

    @SuppressLint("CheckResult")
    public SessionObserver() {
        Observable<Boolean> observable = Observable.create(e -> emitor = e);
        connectableObservable = observable.publish();
        connectableObservable.share();
        connectableObservable.replay();
        connectableObservable.connect();
    }

    public ConnectableObservable<Boolean> getObservable() {
        return connectableObservable;
    }

    public void postData(Boolean flag) {
        if (emitor != null) {
            emitor.onNext(flag);
        }
    }

}
