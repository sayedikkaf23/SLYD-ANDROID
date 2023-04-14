package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.response.error.IsometrikError;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the ConnectionEventCallback{@link
 * io.isometrik.gs.callbacks.ConnectionEventCallback} and to
 * broadcast the ConnectEvent{@link io.isometrik.gs.events.connection.ConnectEvent},
 * DisconnectEvent{@link io.isometrik.gs.events.connection.DisconnectEvent } or
 * IsometrikError{@link
 * io.isometrik.gs.response.error.IsometrikError} events to all the registered listeners.
 *
 * @see io.isometrik.gs.callbacks.ConnectionEventCallback
 * @see io.isometrik.gs.events.connection.ConnectEvent
 * @see io.isometrik.gs.events.connection.DisconnectEvent
 * @see io.isometrik.gs.response.error.IsometrikError
 */
public class ConnectionListenerManager {
  private final List<ConnectionEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Connection listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public ConnectionListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ConnectionEventCallback{@link io.isometrik.gs.callbacks.ConnectionEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.ConnectionEventCallback
   */
  public void addListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ConnectionEventCallback{@link io.isometrik.gs.callbacks.ConnectionEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.ConnectionEventCallback
   */
  public void removeListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ConnectionEventCallback{@link io.isometrik.gs.callbacks.ConnectionEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.ConnectionEventCallback
   */
  private List<ConnectionEventCallback> getListeners() {
    List<ConnectionEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ConnectEvent to listeners.
   *
   * @param connectEvent ConnectEvent{@link io.isometrik.gs.events.connection.ConnectEvent} which
   * will be broadcast to listeners.
   * @see io.isometrik.gs.events.connection.ConnectEvent
   */
  public void announce(ConnectEvent connectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connected(this.isometrik, connectEvent);
    }
  }

  /**
   * announce a DisconnectEvent to listeners.
   *
   * @param disconnectEvent DisconnectEvent{@link io.isometrik.gs.events.connection.DisconnectEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.connection.DisconnectEvent
   */
  public void announce(DisconnectEvent disconnectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.disconnected(this.isometrik, disconnectEvent);
    }
  }

  /**
   * announce a ConnectionFailedEvent to listeners.
   *
   * @param connectionFailedEvent ConnectionFailedEvent{@link io.isometrik.gs.events.connection.ConnectionFailedEvent}
   * which
   * will be broadcast to listeners.
   * @see io.isometrik.gs.events.connection.ConnectionFailedEvent
   */
  public void announce(ConnectionFailedEvent connectionFailedEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.failedToConnect(this.isometrik, connectionFailedEvent);
    }
  }

  /**
   * announce a IsometrikError to listeners.
   *
   * @param isometrikError IsometrikError{@link io.isometrik.gs.response.error.IsometrikError} which
   * will be broadcast to listeners.
   * @see io.isometrik.gs.response.error.IsometrikError
   */
  public void announce(IsometrikError isometrikError) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connectionFailed(this.isometrik, isometrikError);
    }
  }
}
