package chat.hola.com.app.ContentObserver;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import chat.hola.com.app.AppController;


/**
 * Created by moda on 20/07/17.
 */

public class ContactAddedOrUpdatedObserver extends ContentObserver {



    public ContactAddedOrUpdatedObserver(Handler handler) {
        super(handler);
    }


    @Override
    public void onChange(boolean selfChange,Uri uri) {



      AppController.getInstance().checkContactAddedOrUpdated();

    }


}