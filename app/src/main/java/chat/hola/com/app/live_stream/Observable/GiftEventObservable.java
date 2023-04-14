package chat.hola.com.app.live_stream.Observable;

import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class GiftEventObservable extends Observable<GiftEvent> {

    private Observer<? super GiftEvent> observer;
    private static GiftEventObservable observebleClass;

    public static GiftEventObservable getInstance() {
        if (observebleClass == null) {
            observebleClass = new GiftEventObservable();

            return observebleClass;
        } else {
            return observebleClass;
        }
    }

    @Override
    protected void subscribeActual(Observer<? super GiftEvent> observer) {
        this.observer = observer;
    }

    public void emitGiftEvent(GiftEvent giftEvent) {

        observer.onNext(giftEvent);
        observer.onComplete();
    }
}
