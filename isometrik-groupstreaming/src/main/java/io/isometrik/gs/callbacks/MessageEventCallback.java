package io.isometrik.gs.callbacks;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class containing message event callbacks,when a message has been received or message
 * has been removed.
 */
public abstract class MessageEventCallback {

  /**
   * Message added.
   *
   * @param isometrik the isometrik instance
   * @param messageAddEvent the message add event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.message.MessageAddEvent
   */
  public abstract void messageAdded(@NotNull Isometrik isometrik,
      @NotNull MessageAddEvent messageAddEvent);

  /**
   * Message removed.
   *
   * @param isometrik the isometrik instance
   * @param messageRemoveEvent the message remove event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.message.MessageRemoveEvent
   */
  public abstract void messageRemoved(@NotNull Isometrik isometrik,
      @NotNull MessageRemoveEvent messageRemoveEvent);
}
