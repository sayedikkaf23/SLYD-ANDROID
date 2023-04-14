package chat.hola.com.app.live_stream.utility;

import java.util.Calendar;
import java.util.TimeZone;

public class TimerHelper {
    public static String getDurationString(long seconds) {

        if (seconds < 0 || seconds > 2000000)//there is an codec problem and duration is not set correctly,so display meaningfull string
            seconds = 0;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if (hours == 0)
            return twoDigitString(minutes) + " : " + twoDigitString(seconds);
        else
            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private static String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public static long getUnixTime()
    {
        long now =  Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

        return (long)(now / 1000);

    }
}
