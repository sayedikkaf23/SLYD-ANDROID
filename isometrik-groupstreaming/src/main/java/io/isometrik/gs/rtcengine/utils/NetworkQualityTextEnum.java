package io.isometrik.gs.rtcengine.utils;

/**
 * The network quality values text description enum.
 */
public enum NetworkQualityTextEnum {
  UNKNOWN("Unknown"),
  EXCELLENT("Excellent"),
  GOOD("Good"),
  POOR("Poor"),
  BAD("Bad"),
  VBAD("Very bad"),
  DOWN("Down"),
  DETECTING("Detecting");;

  private final String value;

  NetworkQualityTextEnum(String value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return this.value;
  }
}
