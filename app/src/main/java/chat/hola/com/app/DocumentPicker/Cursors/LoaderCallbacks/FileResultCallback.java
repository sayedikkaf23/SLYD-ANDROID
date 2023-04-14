package chat.hola.com.app.DocumentPicker.Cursors.LoaderCallbacks;

/**
 * Created by moda on 22/08/17.
 */

import java.util.List;

public interface FileResultCallback<T> {
    void onResultCallback(List<T> files);
}