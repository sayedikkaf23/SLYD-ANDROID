package chat.hola.com.app.live_stream.Observable;

import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by moda on 12/19/2018.
 */
public class ParticularStreamChatMessageObservable extends Observable<StreamChatMessage> {

    private Observer<? super StreamChatMessage> observer;
    private static ParticularStreamChatMessageObservable observebleClass;

    public static ParticularStreamChatMessageObservable getInstance() {
        if (observebleClass == null) {
            observebleClass = new ParticularStreamChatMessageObservable();

            return observebleClass;
        } else {
            return observebleClass;
        }
    }

    @Override
    protected void subscribeActual(Observer<? super StreamChatMessage> observer) {
        this.observer = observer;
    }


    public void emitStreamChatMessage(StreamChatMessage streamChatMessage)
    {

        observer.onNext(streamChatMessage);
        observer.onComplete();
    }
}
