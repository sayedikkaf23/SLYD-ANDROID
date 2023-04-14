package chat.hola.com.app.dubly;

/**
 * <h1>ClickListner</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public interface ClickListner {
    void play(int position, boolean isPlaying);

    void dubWithIt(String name, int position);

    void like(int position, boolean flag);
}
