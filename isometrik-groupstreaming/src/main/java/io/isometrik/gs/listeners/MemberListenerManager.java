package io.isometrik.gs.listeners;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The class containing methods to add or remove listeners for the MemberEventCallback{@link
 * io.isometrik.gs.callbacks.MemberEventCallback } and to
 * broadcast the MemberAddEvent{@link io.isometrik.gs.events.member.MemberAddEvent},
 * MemberLeaveEvent{@link
 * io.isometrik.gs.events.member.MemberLeaveEvent}, MemberRemoveEvent{@link
 * io.isometrik.gs.events.member.MemberRemoveEvent}, MemberTimeoutEvent{@link
 * io.isometrik.gs.events.member.MemberTimeoutEvent},
 * NoPublisherLiveEvent{@link io.isometrik.gs.events.member.NoPublisherLiveEvent},
 * PublishStartEvent{@link
 * io.isometrik.gs.events.member.PublishStartEvent} or PublishStopEvent{@link
 * io.isometrik.gs.events.member.PublishStopEvent} events to all the registered
 * listeners.
 *
 * @see io.isometrik.gs.callbacks.MemberEventCallback
 * @see io.isometrik.gs.events.member.MemberAddEvent
 * @see io.isometrik.gs.events.member.MemberLeaveEvent
 * @see io.isometrik.gs.events.member.MemberRemoveEvent
 * @see io.isometrik.gs.events.member.MemberTimeoutEvent
 * @see io.isometrik.gs.events.member.NoPublisherLiveEvent
 * @see io.isometrik.gs.events.member.PublishStartEvent
 * @see io.isometrik.gs.events.member.PublishStopEvent
 */
public class MemberListenerManager {

  private final List<MemberEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Member listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public MemberListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the MemberEventCallback{@link io.isometrik.gs.callbacks.MemberEventCallback}
   * listener to be added
   * @see io.isometrik.gs.callbacks.MemberEventCallback
   */
  public void addListener(MemberEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the MemberEventCallback{@link io.isometrik.gs.callbacks.MemberEventCallback}
   * listener to be removed
   * @see io.isometrik.gs.callbacks.MemberEventCallback
   */
  public void removeListener(MemberEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of MemberEventCallback{@link io.isometrik.gs.callbacks.MemberEventCallback}
   * listeners currently registered
   * @see io.isometrik.gs.callbacks.MemberEventCallback
   */

  private List<MemberEventCallback> getListeners() {
    List<MemberEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a MemberAddEvent to listeners.
   *
   * @param memberAddEvent MemberAddEvent{@link io.isometrik.gs.events.member.MemberAddEvent} which
   * will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.MemberAddEvent
   */
  public void announce(MemberAddEvent memberAddEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberAdded(this.isometrik, memberAddEvent);
    }
  }

  /**
   * announce a MemberLeaveEvent to listeners.
   *
   * @param memberLeaveEvent MemberLeaveEvent{@link io.isometrik.gs.events.member.MemberLeaveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.MemberLeaveEvent
   */
  public void announce(MemberLeaveEvent memberLeaveEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberLeft(this.isometrik, memberLeaveEvent);
    }
  }

  /**
   * announce a MemberRemoveEvent to listeners.
   *
   * @param memberRemoveEvent MemberRemoveEvent{@link io.isometrik.gs.events.member.MemberRemoveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.MemberRemoveEvent
   */
  public void announce(MemberRemoveEvent memberRemoveEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberRemoved(this.isometrik, memberRemoveEvent);
    }
  }

  /**
   * announce a MemberTimeoutEvent to listeners.
   *
   * @param memberTimeoutEvent MemberTimeoutEvent{@link io.isometrik.gs.events.member.MemberTimeoutEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.MemberTimeoutEvent
   */
  public void announce(MemberTimeoutEvent memberTimeoutEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberTimedOut(this.isometrik, memberTimeoutEvent);
    }
  }

  /**
   * announce a PublishStartEvent to listeners.
   *
   * @param publishStartEvent PublishStartEvent{@link io.isometrik.gs.events.member.PublishStartEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.PublishStartEvent
   */
  public void announce(PublishStartEvent publishStartEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberPublishStarted(this.isometrik, publishStartEvent);
    }
  }

  /**
   * announce a PublishStopEvent to listeners.
   *
   * @param publishStopEvent PublishStopEvent{@link @see io.isometrik.gs.events.member.PublishStopEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.PublishStopEvent
   */
  public void announce(PublishStopEvent publishStopEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.memberPublishStopped(this.isometrik, publishStopEvent);
    }
  }

  /**
   * announce a NoPublisherLiveEvent to listeners.
   *
   * @param noPublisherLiveEvent NoPublisherLiveEvent{@link io.isometrik.gs.events.member.NoPublisherLiveEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.gs.events.member.NoPublisherLiveEvent
   */
  public void announce(NoPublisherLiveEvent noPublisherLiveEvent) {
    for (MemberEventCallback memberEventCallback : getListeners()) {
      memberEventCallback.noMemberPublishing(this.isometrik, noPublisherLiveEvent);
    }
  }
}
