package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the StreamEventCallback{@link
 * io.isometrik.gs.callbacks.StreamEventCallback } and to
 * broadcast the StreamStartEvent{@link io.isometrik.gs.events.stream.StreamStartEvent} or
 * StreamStopEvent{@link
 * io.isometrik.gs.events.stream.StreamStopEvent} events to all the registered
 * listeners.
 *
 * @see io.isometrik.gs.callbacks.StreamEventCallback
 * @see io.isometrik.gs.events.stream.StreamStartEvent
 * @see io.isometrik.gs.events.stream.StreamStopEvent
 */
public class StreamListenerManager {

  private final List<StreamEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Stream listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public StreamListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the StreamEventCallback{@link io.isometrik.gs.callbacks.StreamEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.StreamEventCallback
   */
  public void addListener(StreamEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the StreamEventCallback{@link io.isometrik.gs.callbacks.StreamEventCallback}
   * listener to be removed
   * @see io.isometrik.gs.callbacks.StreamEventCallback
   */
  public void removeListener(StreamEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of StreamEventCallback{@link io.isometrik.gs.callbacks.StreamEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.StreamEventCallback
   */
  private List<StreamEventCallback> getListeners() {
    List<StreamEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a StreamStartEvent to listeners.
   *
   * @param streamStartEvent StreamStartEvent{@link io.isometrik.gs.events.stream.StreamStartEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.stream.StreamStartEvent
   */
  public void announce(StreamStartEvent streamStartEvent) {
    for (StreamEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStarted(this.isometrik, streamStartEvent);
    }
  }

  /**
   * announce a StreamStopEvent to listeners.
   *
   * @param streamStopEvent StreamStopEvent{@link io.isometrik.gs.events.stream.StreamStopEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.stream.StreamStopEvent
   */
  public void announce(StreamStopEvent streamStopEvent) {
    for (StreamEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStopped(this.isometrik, streamStopEvent);
    }
  }
}
