package io.isometrik.gs.enums;

/**
 * The enum defining the message event type.
 */
public enum IMMessageEventType {

  /**
   * isometrik message sent event type.
   */
  IMMessageSentEvent("messageSent"),
  /**
   * isometrik message removed event type.
   */
  IMMessageRemovedEvent("messageRemoved");

  private final String value;

  IMMessageEventType(String value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final String getValue() {
    return this.value;
  }
}
