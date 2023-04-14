package chat.hola.com.app.Utilities.twitterManager;

/**
 * Created by ankit on 11/3/18.
 */

public class TwitterManager {

//    private static final String TAG = TwitterManager.class.getSimpleName();
//
//    private static String CONSUMER_KEY = "";
//    private static String CONSUMER_SECRET = "";
//    private static Context context;
//
//
//    public TwitterManager(Context context, String CONSUMER_KEY, String CONSUMER_SECRET) {
//        TwitterManager.CONSUMER_KEY = CONSUMER_KEY;
//        TwitterManager.CONSUMER_SECRET = CONSUMER_SECRET;
//        TwitterManager.context = context;
//        //twitterInit();
//    }
//
//
//    //need to call during this object creation.
//    public static void twitterInit(Context context,String CONSUMER_KEY,String CONSUMER_SECRET) {
//        TwitterConfig config = new TwitterConfig.Builder(context)
//                .logger(new DefaultLogger(Log.DEBUG))
//                .twitterAuthConfig(
//                        new TwitterAuthConfig(
//                                CONSUMER_KEY, CONSUMER_SECRET
//                        ))
//                .debug(true)
//                .build();
//        Twitter.initialize(config);
//    }
//
//    public void twitterLogin(Activity activity, final Callback<TwitterSession> callback){
//        this.callback = callback;
//        Callback<TwitterSession> twitterSessionCallback = new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                callback.success(result);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                callback.failure(exception);
//            }
//        };
//        getTwitterAuthClient().authorize(activity,twitterSessionCallback);
//    }
//
//    public TwitterAuthClient getTwitterAuthClient(){
//        if(twitterAuthClient == null)
//            twitterAuthClient = new TwitterAuthClient();
//        return twitterAuthClient;
//    }
//
//    public boolean isLoggedin(){
//        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
//        if(twitterSession == null)
//            return false;
//        else {
//            TwitterAuthToken authToken = twitterSession.getAuthToken();
//            return !authToken.isExpired();
//        }
//    }
//
//    public void logout(){
//        SessionManager sessionManager = TwitterCore.getInstance().getSessionManager();
//        sessionManager.clearActiveSession();
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == getTwitterAuthClient().getRequestCode()) {
//            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
//        }
//    }


}
