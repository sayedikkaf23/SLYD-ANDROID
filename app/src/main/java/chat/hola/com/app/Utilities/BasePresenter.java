package chat.hola.com.app.Utilities;

/**
 * Created by ankit on 22/2/18.
 */

public interface BasePresenter<T> {
    void attachView(T view);
    void detachView();
}
