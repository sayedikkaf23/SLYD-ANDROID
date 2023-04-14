package chat.hola.com.app.home.stories.model;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>StoryObserver</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/8/2018.
 */

public class StoryObserver extends Observable<StoryPost> {
    Observer<? super StoryPost> observer;


    @Override
    protected void subscribeActual(Observer<? super StoryPost> observer) {
        this.observer = observer;
    }

    public void postData(StoryPost storyData) {
        if (observer != null) {
            if (storyData.isSuccess()) {
                observer.onNext(storyData);
                observer.onComplete();
            } else {
                observer.onError(new Throwable("error"));
            }
        }
    }
}
