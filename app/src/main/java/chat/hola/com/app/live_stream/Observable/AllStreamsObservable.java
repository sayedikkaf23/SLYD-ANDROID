package chat.hola.com.app.live_stream.Observable;

import java.util.ArrayList;

import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by moda on 12/19/2018.
 */
public class AllStreamsObservable extends Observable<AllStreamsData> {

    private Observer<? super AllStreamsData> observer;
    private static AllStreamsObservable observebleClass;
    private ArrayList<Observer<? super AllStreamsData>> myListObserver = new ArrayList<>();

    public static AllStreamsObservable getInstance() {
        if (observebleClass == null) {
            observebleClass = new AllStreamsObservable();

            return observebleClass;
        } else {
            return observebleClass;
        }
    }

    @Override
    protected void subscribeActual(Observer<? super AllStreamsData> observer) {
        this.observer = observer;

        if (!myListObserver.contains(observer)) {
            myListObserver.add(observer);

        }
    }

    public void removeObserver(Observer<? super AllStreamsData> observer) {

        if (myListObserver!=null) {
            myListObserver.remove(observer);

        }

    }

    public void emitAllStreamsData(AllStreamsData allStreamsData) {

        for (int i = 0; i < myListObserver.size(); i++) {
            Observer<? super AllStreamsData> tempobserver = myListObserver.get(i);
            tempobserver.onNext(allStreamsData);
            tempobserver.onComplete();
        }

    }
}
