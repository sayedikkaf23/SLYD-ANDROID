package chat.hola.com.app.base;


/**
 * <h1>BaseView</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 31 August 2019
 */
public interface BaseView {

    /**
     * Show message
     *
     * @param message : string message
     */
    void message(String message);

    /**
     * Show message
     *
     * @param message : string id
     */
    void message(int message);

    /**
     * shows loader
     */
    void showLoader();

    /**
     * hides loader
     */
    void hideLoader();
}
