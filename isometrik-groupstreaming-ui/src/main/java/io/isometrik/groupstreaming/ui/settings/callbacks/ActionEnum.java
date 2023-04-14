package io.isometrik.groupstreaming.ui.settings.callbacks;

public enum ActionEnum {

  /**
   * Chat messages visibility toggles action type enum.
   */
  ChatMessagesVisibilityToggleAction(0),
  /**
   * Control buttons visibility toggles action type enum.
   */
  ControlButtonsVisibilityToggleAction(1);

  private final int value;

  ActionEnum(int value) {
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