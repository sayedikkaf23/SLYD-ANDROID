package chat.hola.com.app.live_stream.Observable;


import chat.hola.com.app.live_stream.ResponcePojo.StreamRestartEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 02 August 2019
 */
public class ParticularStreamRestartEventObservable extends Observable<StreamRestartEvent> {

    private Observer<? super StreamRestartEvent> observer;
    private static ParticularStreamRestartEventObservable observebleClass;

    public static ParticularStreamRestartEventObservable getInstance() {
        if (observebleClass == null) {
            observebleClass = new ParticularStreamRestartEventObservable();

            return observebleClass;
        } else {
            return observebleClass;
        }
    }

    @Override
    protected void subscribeActual(Observer<? super StreamRestartEvent> observer) {
        this.observer = observer;
    }

    public void emitStreamRestartEvent(StreamRestartEvent streamRestartEvent) {

        observer.onNext(streamRestartEvent);
        observer.onComplete();
    }
}
