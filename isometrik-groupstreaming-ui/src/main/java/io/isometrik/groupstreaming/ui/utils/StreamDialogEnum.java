package io.isometrik.groupstreaming.ui.utils;

/**
 * The enum Stream dialog enum.
 */
public enum StreamDialogEnum {

  /**
   * Kicked out stream dialog enum.
   */
  KickedOut(0),

  /**
   * Stream offline stream dialog enum.
   */
  StreamOffline(1);

  private final int value;

  StreamDialogEnum(int value) {
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
