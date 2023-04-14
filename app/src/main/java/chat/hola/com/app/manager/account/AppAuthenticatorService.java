package chat.hola.com.app.manager.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by DELL on 3/28/2018.
 */

public class AppAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        AppAuthenticator authenticator = new AppAuthenticator(this);
        return authenticator.getIBinder();
    }
}
