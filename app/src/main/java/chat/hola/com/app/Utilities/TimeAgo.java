package chat.hola.com.app.Utilities;

import android.text.format.DateUtils;

/**
 * Created by DELL on 4/12/2018.
 */

public class TimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

//    public static String getTimeAgo(long time) {
//        return (DateUtils.getRelativeTimeSpanString(time).equals("0 minutes ago") ? "just now" : (String) DateUtils.getRelativeTimeSpanString(time));
//    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String lastDay(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "Old Requests";
        }

        final long diff = now - time;
        if (diff < 24 * HOUR_MILLIS) {
            return "New Friends";
        } else if (diff < 72 * HOUR_MILLIS) {
            return "Last Three Days";
        } else if (diff < 120 * HOUR_MILLIS) {
            return "Last Five Days";
        } else if (diff < 192 * HOUR_MILLIS) {
            return "Last Week";
        } else {
            return "Old Requests";
        }
    }
}
