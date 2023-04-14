package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the CopublishEventCallback{@link
 * io.isometrik.gs.callbacks.CopublishEventCallback} and to
 * broadcast the CopublishRequestAcceptEvent{@link io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent},
 * CopublishRequestAddEvent{@link io.isometrik.gs.events.copublish.CopublishRequestAddEvent },
 * CopublishRequestDenyEvent{@link io.isometrik.gs.events.copublish.CopublishRequestDenyEvent },
 * CopublishRequestRemoveEvent{@link io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent
 * },
 * CopublishRequestSwitchProfileEvent{@link io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent
 * } or
 * IsometrikError{@link
 * io.isometrik.gs.response.error.IsometrikError} events to all the registered listeners.
 *
 * @see io.isometrik.gs.callbacks.CopublishEventCallback
 * @see io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent
 * @see io.isometrik.gs.events.copublish.CopublishRequestAddEvent
 * @see io.isometrik.gs.events.copublish.CopublishRequestDenyEvent
 * @see io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent
 * @see io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent
 * @see io.isometrik.gs.response.error.IsometrikError
 */
public class CopublishListenerManager {

  private final List<CopublishEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new copublish listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public CopublishListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the CopublishEventCallback{@link io.isometrik.gs.callbacks.CopublishEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.CopublishEventCallback
   */
  public void addListener(CopublishEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the CopublishEventCallback{@link io.isometrik.gs.callbacks.CopublishEventCallback}
   * listener to be removed
   * @see io.isometrik.gs.callbacks.CopublishEventCallback
   */
  public void removeListener(CopublishEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of CopublishEventCallback{@link io.isometrik.gs.callbacks.CopublishEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.CopublishEventCallback
   */
  private List<CopublishEventCallback> getListeners() {
    List<CopublishEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a CopublishRequestAcceptEvent to listeners.
   *
   * @param copublishRequestAcceptEvent CopublishRequestAcceptEvent{@link
   * io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent
   */
  public void announce(CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestAccepted(this.isometrik, copublishRequestAcceptEvent);
    }
  }

  /**
   * announce a CopublishRequestAddEvent to listeners.
   *
   * @param copublishRequestAddEvent CopublishRequestAddEvent{@link
   * io.isometrik.gs.events.copublish.CopublishRequestAddEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.copublish.CopublishRequestAddEvent
   */
  public void announce(CopublishRequestAddEvent copublishRequestAddEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestAdded(this.isometrik, copublishRequestAddEvent);
    }
  }

  /**
   * announce a CopublishRequestDenyEvent to listeners.
   *
   * @param copublishRequestDenyEvent CopublishRequestDenyEvent{@link
   * io.isometrik.gs.events.copublish.CopublishRequestDenyEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.copublish.CopublishRequestDenyEvent
   */
  public void announce(CopublishRequestDenyEvent copublishRequestDenyEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestDenied(this.isometrik, copublishRequestDenyEvent);
    }
  }

  /**
   * announce a CopublishRequestRemoveEvent to listeners.
   *
   * @param copublishRequestRemoveEvent CopublishRequestRemoveEvent{@link
   * io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent
   */
  public void announce(CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestRemoved(this.isometrik, copublishRequestRemoveEvent);
    }
  }

  /**
   * announce a CopublishRequestSwitchProfileEvent to listeners.
   *
   * @param copublishRequestSwitchProfileEvent CopublishRequestSwitchProfileEvent{@link
   * io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent
   */
  public void announce(CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.switchProfile(this.isometrik, copublishRequestSwitchProfileEvent);
    }
  }
}
