package chat.hola.com.app.live_stream.Observable;

import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by moda on 12/19/2018.
 */
public class ParticularStreamPresenceEventObservable extends Observable<StreamPresenceEvent> {

    private Observer<? super StreamPresenceEvent> observer;
    private static ParticularStreamPresenceEventObservable observebleClass;

    public static ParticularStreamPresenceEventObservable getInstance() {
        if (observebleClass == null) {
            observebleClass = new ParticularStreamPresenceEventObservable();

            return observebleClass;
        } else {
            return observebleClass;
        }
    }

    @Override
    protected void subscribeActual(Observer<? super StreamPresenceEvent> observer) {
        this.observer = observer;
    }

    public void emitStreamPresenceEvent(StreamPresenceEvent streamPresenceEvent) {

        observer.onNext(streamPresenceEvent);
        observer.onComplete();
    }
}
