package chat.hola.com.app.Service;

import android.content.Intent;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import chat.hola.com.app.Utilities.twitterManager.TwitterShareManager;
import dagger.android.DaggerIntentService;

/**
 * Created by ankit on 19/4/18.
 */

public class ShareService extends DaggerIntentService {

    private static final String TAG = ShareData.class.getSimpleName();

    private ShareData shareData;


    @Inject
    TwitterShareManager twitterShareManager;


    public ShareService() {
        super("share_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        shareData = (ShareData) intent.getSerializableExtra("share_data");
    }
}
