package chat.hola.com.app.post.model;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by DELL on 3/26/2018.
 */

public class UploadObserver extends Observable<UploadHolder> {
    Observer<? super UploadHolder> observer;

    @Override
    protected void subscribeActual(Observer<? super UploadHolder> observer) {
        this.observer = observer;
    }

    public void postData(UploadHolder uploadHolder) {
        if (observer != null) {
            if (uploadHolder.isSuccess()) {
                observer.onNext(uploadHolder);
                observer.onComplete();
            } else {
                observer.onError(new Throwable(uploadHolder.getMsg()));
            }
        }
    }
}
