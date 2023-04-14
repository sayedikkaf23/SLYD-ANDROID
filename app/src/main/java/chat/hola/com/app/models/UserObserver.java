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
 * @since 4/22/2018.
 */

public class UserObserver {
    private ConnectableObservable<Integer> connectableObservable;
    private ObservableEmitter<Integer> emitor;

    @SuppressLint("CheckResult")
    public UserObserver() {
        Observable<Integer> observable = Observable.create(e -> emitor = e);
        connectableObservable = observable.publish();
        connectableObservable.share();
        connectableObservable.replay();
        connectableObservable.connect();
    }

    public ConnectableObservable<Integer> getObservable() {
        return connectableObservable;
    }

    public void postData(Integer value) {
        if (emitor != null) {
            emitor.onNext(value);
        }
    }

}
