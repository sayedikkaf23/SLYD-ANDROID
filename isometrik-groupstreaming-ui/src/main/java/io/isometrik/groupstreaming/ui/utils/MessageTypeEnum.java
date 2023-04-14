package io.isometrik.groupstreaming.ui.utils;

/**
 * The enum Message type enum.
 */
public enum MessageTypeEnum {

  /**
   * Normal message message type enum.
   */
  NormalMessage(0),
  /**
   * Heart message message type enum.
   */
  HeartMessage(1),
  /**
   * Gift message message type enum.
   */
  GiftMessage(2),
  /**
   * Removed message message type enum.
   */
  RemovedMessage(3),
  /**
   * Presence message message type enum.
   */
  PresenceMessage(4),

  /**
   * Copublish request message type enum.
   */
  CopublishRequestMessage(5),

  /**
   * Copublish request accepted message type enum.
   */
  CopublishRequestAcceptedMessage(6),

  /**
   * Copublish request action(deleted,switch profile etc) message type enum.
   */
  CopublishRequestActionMessage(7);

  private final int value;

  MessageTypeEnum(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final int getValue() {
    return this.value;
  }
}