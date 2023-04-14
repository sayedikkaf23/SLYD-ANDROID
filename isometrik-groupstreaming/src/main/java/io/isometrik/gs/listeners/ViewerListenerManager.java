package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the ViewerEventCallback{@link
 * io.isometrik.gs.callbacks.ViewerEventCallback } and to
 * broadcast the ViewerJoinEvent{@link io.isometrik.gs.events.viewer.ViewerJoinEvent},
 * ViewerRemoveEvent{@link io.isometrik.gs.events.viewer.ViewerRemoveEvent},
 * ViewerTimeoutEvent{@link io.isometrik.gs.events.viewer.ViewerTimeoutEvent} or
 * ViewerLeaveEvent{@link
 * io.isometrik.gs.events.viewer.ViewerLeaveEvent} events to all the registered
 * listeners.
 *
 * @see io.isometrik.gs.callbacks.ViewerEventCallback
 * @see io.isometrik.gs.events.viewer.ViewerJoinEvent
 * @see io.isometrik.gs.events.viewer.ViewerLeaveEvent
 * @see io.isometrik.gs.events.viewer.ViewerRemoveEvent
 * @see io.isometrik.gs.events.viewer.ViewerTimeoutEvent
 */
public class ViewerListenerManager {

  private final List<ViewerEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Viewer listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public ViewerListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ViewerEventCallback{@link io.isometrik.gs.callbacks.ViewerEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.ViewerEventCallback
   */
  public void addListener(ViewerEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ViewerEventCallback{@link io.isometrik.gs.callbacks.ViewerEventCallback}
   * listener to be removed
   * @see io.isometrik.gs.callbacks.ViewerEventCallback
   */
  public void removeListener(ViewerEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ViewerEventCallback{@link io.isometrik.gs.callbacks.ViewerEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.ViewerEventCallback
   */
  private List<ViewerEventCallback> getListeners() {
    List<ViewerEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ViewerJoinEvent to listeners.
   *
   * @param viewerJoinEvent ViewerJoinEvent{@link io.isometrik.gs.events.viewer.ViewerJoinEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.viewer.ViewerJoinEvent
   */
  public void announce(ViewerJoinEvent viewerJoinEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerJoined(this.isometrik, viewerJoinEvent);
    }
  }

  /**
   * announce a ViewerLeaveEvent to listeners.
   *
   * @param viewerLeaveEvent ViewerLeaveEvent{@link io.isometrik.gs.events.viewer.ViewerLeaveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.viewer.ViewerLeaveEvent
   */
  public void announce(ViewerLeaveEvent viewerLeaveEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerLeft(this.isometrik, viewerLeaveEvent);
    }
  }

  /**
   * announce a ViewerRemoveEvent to listeners.
   *
   * @param viewerRemoveEvent ViewerRemoveEvent{@link io.isometrik.gs.events.viewer.ViewerRemoveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.viewer.ViewerRemoveEvent
   */
  public void announce(ViewerRemoveEvent viewerRemoveEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerRemoved(this.isometrik, viewerRemoveEvent);
    }
  }

  /**
   * announce a ViewerTimeoutEvent to listeners.
   *
   * @param viewerTimeoutEvent ViewerTimeoutEvent{@link io.isometrik.gs.events.viewer.ViewerTimeoutEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.viewer.ViewerTimeoutEvent
   */
  public void announce(ViewerTimeoutEvent viewerTimeoutEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerTimedOut(this.isometrik, viewerTimeoutEvent);
    }
  }
}
