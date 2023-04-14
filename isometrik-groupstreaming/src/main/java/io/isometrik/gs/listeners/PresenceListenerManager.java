package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the PresenceEventCallback{@link
 * io.isometrik.gs.callbacks.PresenceEventCallback } and to
 * broadcast the PresenceStreamStartEvent{@link io.isometrik.gs.events.presence.PresenceStreamStartEvent}
 * or
 * PresenceStreamStopEvent{@link
 * io.isometrik.gs.events.presence.PresenceStreamStopEvent} events to all the registered
 * listeners.
 *
 * @see io.isometrik.gs.callbacks.PresenceEventCallback
 * @see io.isometrik.gs.events.presence.PresenceStreamStartEvent
 * @see io.isometrik.gs.events.presence.PresenceStreamStopEvent
 */
public class PresenceListenerManager {

  private final List<PresenceEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Presence listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public PresenceListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the PresenceEventCallback{@link io.isometrik.gs.callbacks.PresenceEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.PresenceEventCallback
   */
  public void addListener(PresenceEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the PresenceEventCallback{@link io.isometrik.gs.callbacks.PresenceEventCallback}
   * listener to be removed
   * @see io.isometrik.gs.callbacks.PresenceEventCallback
   */
  public void removeListener(PresenceEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of PresenceEventCallback{@link io.isometrik.gs.callbacks.PresenceEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.PresenceEventCallback
   */
  private List<PresenceEventCallback> getListeners() {
    List<PresenceEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a PresenceStreamStartEvent to listeners.
   *
   * @param presenceStreamStartEvent PresenceStreamStartEvent{@link io.isometrik.gs.events.presence.PresenceStreamStartEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.presence.PresenceStreamStartEvent
   */
  public void announce(PresenceStreamStartEvent presenceStreamStartEvent) {
    for (PresenceEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStarted(this.isometrik, presenceStreamStartEvent);
    }
  }

  /**
   * announce a PresenceStreamStopEvent to listeners.
   *
   * @param presenceStreamStopEvent PresenceStreamStopEvent{@link io.isometrik.gs.events.presence.PresenceStreamStopEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.presence.PresenceStreamStopEvent
   */
  public void announce(PresenceStreamStopEvent presenceStreamStopEvent) {
    for (PresenceEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStopped(this.isometrik, presenceStreamStopEvent);
    }
  }
}
