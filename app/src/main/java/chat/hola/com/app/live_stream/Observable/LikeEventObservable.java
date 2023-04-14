package chat.hola.com.app.live_stream.Observable;

import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class LikeEventObservable extends Observable<LikeEvent> {

    private Observer<? super LikeEvent> observer;
    private static LikeEventObservable observebleClass;

    public static LikeEventObservable getInstance() {
        if (observebleClass == null) {
            observebleClass = new LikeEventObservable();

            return observebleClass;
        } else {
            return observebleClass;
        }
    }

    @Override
    protected void subscribeActual(Observer<? super LikeEvent> observer) {
        this.observer = observer;
    }

    public void emitLikeEvent(LikeEvent likeEvent) {

        observer.onNext(likeEvent);
        observer.onComplete();
    }
}
