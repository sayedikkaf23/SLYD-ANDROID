package chat.hola.com.app.AppStateChange;

/*
 * Created by moda on 23/02/17.
 */

import androidx.annotation.NonNull;

public interface AppStateMonitor {

    void start();

    void stop();

    void addListener(@NonNull AppStateListener appStateListener);

    void removeListener(@NonNull AppStateListener appStateListener);

    boolean isAppInForeground();

    boolean isAppInBackground();
}