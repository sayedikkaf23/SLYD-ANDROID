package io.isometrik.groupstreaming.ui.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * The type Time util.
 */
public class TimeUtil {

  /**
   * Gets duration string.
   *
   * @param seconds the seconds
   * @return the duration string
   */
  public static String getDurationString(long seconds) {

    if (seconds < 0
        || seconds
        > 2000000)//there is an codec problem and duration is not set correctly,so display meaningfull string
    {
      seconds = 0;
    }
    long hours = seconds / 3600;
    long minutes = (seconds % 3600) / 60;
    seconds = seconds % 60;

    if (hours == 0) {
      return twoDigitString(minutes) + " : " + twoDigitString(seconds);
    } else {
      return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(
          seconds);
    }
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

  /**
   * Gets duration.
   *
   * @param startTime the start time
   * @return the duration
   */
  public static long getDuration(long startTime) {

    long currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

    long duration = currentTime - startTime - Constants.TIME_CORRECTION;
    duration = duration / 1000;

    duration = (duration < 0) ? 0 : duration;
    return duration;
  }
}
