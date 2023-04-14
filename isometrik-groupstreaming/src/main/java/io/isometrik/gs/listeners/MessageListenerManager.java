package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the MessageEventCallback{@link
 * io.isometrik.gs.callbacks.MessageEventCallback } and to
 * broadcast the MessageAddEvent{@link io.isometrik.gs.events.message.MessageAddEvent} or
 * MessageRemoveEvent{@link
 * io.isometrik.gs.events.message.MessageRemoveEvent} events to all the registered
 * listeners.
 *
 * @see io.isometrik.gs.callbacks.MessageEventCallback
 * @see io.isometrik.gs.events.message.MessageAddEvent
 * @see io.isometrik.gs.events.message.MessageRemoveEvent
 */
public class MessageListenerManager {

  private final List<MessageEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Message listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public MessageListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the MessageEventCallback{@link io.isometrik.gs.callbacks.MessageEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.MessageEventCallback
   */
  public void addListener(MessageEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the MessageEventCallback{@link io.isometrik.gs.callbacks.MessageEventCallback}
   * listener to be removed
   * @see io.isometrik.gs.callbacks.MessageEventCallback
   */
  public void removeListener(MessageEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of MessageEventCallback{@link io.isometrik.gs.callbacks.MessageEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.MessageEventCallback
   */
  private List<MessageEventCallback> getListeners() {
    List<MessageEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a MessageAddEvent to listeners.
   *
   * @param messageAddEvent MessageAddEvent{@link io.isometrik.gs.events.message.MessageAddEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.message.MessageAddEvent
   */
  public void announce(MessageAddEvent messageAddEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageAdded(this.isometrik, messageAddEvent);
    }
  }

  /**
   * announce a MessageRemoveEvent to listeners.
   *
   * @param messageRemoveEvent MessageRemoveEvent{@link io.isometrik.gs.events.message.MessageRemoveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.message.MessageRemoveEvent
   */
  public void announce(MessageRemoveEvent messageRemoveEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageRemoved(this.isometrik, messageRemoveEvent);
    }
  }
}
