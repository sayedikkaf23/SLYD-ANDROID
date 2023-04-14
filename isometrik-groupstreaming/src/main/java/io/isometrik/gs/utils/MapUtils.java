package io.isometrik.gs.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * The utility class to convert a map to string.
 */
public class MapUtils {

  /**
   * Converts a map to string.
   *
   * @param map map to be converted to string
   * @return the string result of map converted to string
   */
  @SuppressWarnings("unchecked")
  public static String mapToString(Map<String, Object> map) {
    StringBuilder stringBuilder = new StringBuilder();

    for (String key : map.keySet()) {
      if (stringBuilder.length() > 0) {
        stringBuilder.append("&");
      }
      List<String> value = (List<String>) map.get(key);
      try {
        stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
        stringBuilder.append("=");
        stringBuilder.append(value != null ? URLEncoder.encode(value.get(0), "UTF-8") : "");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("This method requires UTF-8 encoding support", e);
      }
    }

    return stringBuilder.toString();
  }
}
