package io.isometrik.gs.enums;

/**
 * The enum defining the stream type.
 */

public enum IMStreamType {
  /**
   * Public and private streams combined
   */
  IMAllStreams(0),
  /**
   * Public streams
   */
  IMPublicStreams(1),
  /**
   * Private streams
   */
  IMPrivateStreams(2);

  private final int value;

  IMStreamType(int value) {
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