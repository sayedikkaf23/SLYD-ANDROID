package chat.hola.com.app.DublyCamera.dubbing;

import java.util.Locale;

public class TimeFormatUtil {

  /**
   * Format time string 00: 00: 00.0
   */
  public static String formatUsToString(double totalSecond) {
    int hour = (int) totalSecond / 3600;
    int minute = (int) totalSecond % 3600 / 60;
    double second = totalSecond % 60;
    String timeStr = hour > 0 ? String.format(Locale.US,"%02d:%02d:%04.1f", hour, minute, second)
        : minute > 0 ? String.format(Locale.US,"%02d:%04.1f", minute, second)
            : second > 9 ? String.format(Locale.US,"%4.1f", second) : String.format(Locale.US,"%3.1f", second);
    return timeStr;
  }
}
