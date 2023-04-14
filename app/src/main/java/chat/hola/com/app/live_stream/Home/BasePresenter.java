package chat.hola.com.app.live_stream.Home;

/**
 * <h>BasePresenter</h>
 * Created by moda on 1/29/2018.
 */

public interface BasePresenter<T>
{
    /**
     * <h2>attachView</h2>
     * Binds presenter with a view when resumed. The PricePresenter will perform initialization here.
     * @param view the view associated with this presenter
     */
    void attachView(T view);

    /**
     * <h2>detachView</h2>
     * Drops the reference to the view when destroyed
     */
    void detachView();
}
