package chat.hola.com.app.models;

import android.annotation.SuppressLint;

import chat.hola.com.app.Networking.connection.ShareWith;
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

public class SocialObserver {
    private ConnectableObservable<ShareWith> connectableObservable;
    private ObservableEmitter<ShareWith> emitor;

    @SuppressLint("CheckResult")
    public SocialObserver() {
        Observable<ShareWith> observable = Observable.create(e -> emitor = e);
        connectableObservable = observable.publish();
        connectableObservable.share();
        connectableObservable.replay();
        connectableObservable.connect();
    }

    public ConnectableObservable<ShareWith> getObservable() {
        return connectableObservable;
    }

    public void postData(ShareWith data) {
        if (emitor != null) {
            emitor.onNext(data);
        }
    }

}
