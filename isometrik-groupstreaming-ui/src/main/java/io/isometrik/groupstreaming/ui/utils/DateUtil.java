package io.isometrik.groupstreaming.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The type Date util.
 */
public class DateUtil {

  /**
   * Gets date.
   *
   * @param timestamp the timestamp
   * @return the date
   */
  public static String getDate(long timestamp) {

    String gmtToLocal = changeStatusDateFromGmtToLocal(String.valueOf(timestamp));

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

    String currentDate = simpleDateFormat.format(System.currentTimeMillis());

    currentDate = currentDate.substring(0, 8);

    String time = getTime(gmtToLocal);
    if (currentDate.equals(gmtToLocal.substring(0, 8))) {

      return "Today " + time;
    } else {

      String date = gmtToLocal.substring(6, 8)
          + "-"
          + gmtToLocal.substring(4, 6)
          + "-"
          + gmtToLocal.substring(0, 4);

      return date + " " + time;
    }
  }

  private static String getTime(String gmtToLocal) {
    return convert24To12Hourformat(
        gmtToLocal.substring(8, 10) + ":" + gmtToLocal.substring(10, 12));
  }

  private static String changeStatusDateFromGmtToLocal(String timestamp) {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

    TimeZone timeZone = TimeZone.getDefault();
    simpleDateFormat.setTimeZone(timeZone);
    return simpleDateFormat.format(new Date(Long.parseLong(timestamp)));
  }

  /*
   * To convert string from the 24 hour format to 12 hour format
   */
  private static String convert24To12Hourformat(String d) {

    String dateIn12HourFormat = null;

    try {
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm", Locale.US);
      final Date date = simpleDateFormat.parse(d);

      dateIn12HourFormat = new SimpleDateFormat("h:mm a", Locale.US).format(date);
    } catch (final ParseException e) {
      e.printStackTrace();
    }

    return dateIn12HourFormat;
  }
}
