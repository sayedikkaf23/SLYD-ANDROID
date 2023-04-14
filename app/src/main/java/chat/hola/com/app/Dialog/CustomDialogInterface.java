package chat.hola.com.app.Dialog;

/**
 * Created by ankit on 18/4/18.
 */

public interface CustomDialogInterface {

    void show();

    void dismiss();

    void setProgress(int progress);

    void setTitle(String title);

    void enableTryAgain();
}
