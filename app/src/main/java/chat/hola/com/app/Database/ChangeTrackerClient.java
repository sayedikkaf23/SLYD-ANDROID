package chat.hola.com.app.Database;

/**
 * Created by moda on 07/01/17.
 */


import com.couchbase.lite.internal.InterfaceAudience;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * @exclude
 */
@InterfaceAudience.Private
interface ChangeTrackerClient {
    OkHttpClient getOkHttpClient();

    void changeTrackerReceivedChange(Map<String, Object> change);

    void changeTrackerStopped(ChangeTracker tracker);

    void changeTrackerFinished(ChangeTracker tracker);

    void changeTrackerCaughtUp();
}
