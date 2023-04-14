package chat.hola.com.app.Utilities;

import android.app.Activity;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 9/18/2018.
 */
public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//        if (ApiOnServer.PORT_API.equals(ApiOnServer.PORT_API_LIVE)){
//            Intent intent = new Intent(activity, MainActivity.class);
//            intent.putExtra("crash", true);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(AppController.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            AlarmManager mgr = (AlarmManager) AppController.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
//            activity.finish();
//            System.exit(2);
//        }
    }
}
