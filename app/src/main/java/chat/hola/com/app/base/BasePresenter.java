package chat.hola.com.app.base;


/**
 * <h1>BasePresenter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 31 August 2019
 */
public interface BasePresenter<V> {

    /**
     * to attache view
     *
     * @param view : related view
     */
    void attach(V view);

    /**
     * detached view
     */
    void detach();
}
