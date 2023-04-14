package io.isometrik.gs.models.events;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The model for handling the presence events received and broadcasting them to all
 * PresenceEventCallback{@link io.isometrik.gs.callbacks.PresenceEventCallback},
 * StreamEventCallback{@link io.isometrik.gs.callbacks.StreamEventCallback},
 * MemberEventCallback{@link io.isometrik.gs.callbacks.MemberEventCallback} or
 * ViewerEventCallback{@link io.isometrik.gs.callbacks.ViewerEventCallback}listeners listening
 * for PresenceStreamStartEvent{@link io.isometrik.gs.events.presence.PresenceStreamStartEvent} or
 * PresenceStreamStopEvent{@link io.isometrik.gs.events.presence.PresenceStreamStopEvent}
 * , for StreamStartEvent{@link io.isometrik.gs.events.stream.StreamStartEvent} or
 * StreamStopEvent{@link
 * io.isometrik.gs.events.stream.StreamStopEvent}, for NoPublisherLiveEvent{@link
 * io.isometrik.gs.events.member.NoPublisherLiveEvent},
 * MemberAddEvent{@link io.isometrik.gs.events.member.MemberAddEvent}, MemberLeaveEvent{@link
 * io.isometrik.gs.events.member.MemberLeaveEvent}, MemberRemoveEvent{@link
 * io.isometrik.gs.events.member.MemberRemoveEvent}, MemberTimeoutEvent{@link
 * io.isometrik.gs.events.member.MemberTimeoutEvent}, PublishStartEvent{@link
 * io.isometrik.gs.events.member.PublishStartEvent} or PublishStopEvent{@link
 * io.isometrik.gs.events.member.PublishStopEvent}, for ViewerJoinEvent{@link
 * io.isometrik.gs.events.viewer.ViewerJoinEvent}, ViewerLeaveEvent{@link
 * io.isometrik.gs.events.viewer.ViewerLeaveEvent}, ViewerRemoveEvent{@link
 * io.isometrik.gs.events.viewer.ViewerRemoveEvent} or
 * ViewerTimeoutEvent{@link io.isometrik.gs.events.viewer.ViewerTimeoutEvent}.
 *
 * @see io.isometrik.gs.callbacks.PresenceEventCallback
 * @see io.isometrik.gs.events.presence.PresenceStreamStartEvent
 * @see io.isometrik.gs.events.presence.PresenceStreamStopEvent
 * @see io.isometrik.gs.callbacks.StreamEventCallback
 * @see io.isometrik.gs.events.stream.StreamStartEvent
 * @see io.isometrik.gs.events.stream.StreamStopEvent
 * @see io.isometrik.gs.callbacks.MemberEventCallback
 * @see io.isometrik.gs.events.member.NoPublisherLiveEvent
 * @see io.isometrik.gs.events.member.MemberAddEvent
 * @see io.isometrik.gs.events.member.MemberLeaveEvent
 * @see io.isometrik.gs.events.member.MemberRemoveEvent
 * @see io.isometrik.gs.events.member.MemberTimeoutEvent
 * @see io.isometrik.gs.events.member.PublishStartEvent
 * @see io.isometrik.gs.events.member.PublishStopEvent
 * @see io.isometrik.gs.callbacks.ViewerEventCallback
 * @see io.isometrik.gs.events.viewer.ViewerJoinEvent
 * @see io.isometrik.gs.events.viewer.ViewerLeaveEvent
 * @see io.isometrik.gs.events.viewer.ViewerRemoveEvent
 * @see io.isometrik.gs.events.viewer.ViewerTimeoutEvent
 */
public class PresenceEvents {

  /**
   * Handle presence event.
   *
   * @param jsonObject the json object containing details of the presence event for presence stream
   * start/stop,stream start/stop, member add/remove/leave/timeout, publish start/stop, no publisher
   * live, viewer join/remove/leave/timeout events.
   * @param isometrikInstance the isometrik instance
   * @throws JSONException the json exception
   */
  public void handlePresenceEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
      throws JSONException {

    String action = jsonObject.getString("action");

    switch (action) {

      case "streamStartPresence":

        isometrikInstance.getPresenceListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), PresenceStreamStartEvent.class));
        break;
      case "streamStopPresence":

        isometrikInstance.getPresenceListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), PresenceStreamStopEvent.class));
        break;

      case "streamStarted":

        isometrikInstance.getStreamListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), StreamStartEvent.class));
        break;
      case "streamStopped":

        isometrikInstance.getStreamListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), StreamStopEvent.class));
        break;
      case "streamOffline":

        isometrikInstance.getMemberListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), NoPublisherLiveEvent.class));
        break;
      case "memberAdded":

        isometrikInstance.getMemberListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), MemberAddEvent.class));
        break;
      case "memberLeft":

        isometrikInstance.getMemberListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MemberLeaveEvent.class));
        break;
      case "memberRemoved":

        isometrikInstance.getMemberListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MemberRemoveEvent.class));
        break;
      case "publisherTimeout":

        isometrikInstance.getMemberListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MemberTimeoutEvent.class));
        break;
      case "publishStarted":

        isometrikInstance.getMemberListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), PublishStartEvent.class));
        break;
      case "publishStopped":

        isometrikInstance.getMemberListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), PublishStopEvent.class));
        break;
      case "viewerJoined":

        isometrikInstance.getViewerListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), ViewerJoinEvent.class));
        break;
      case "viewerLeft":

        isometrikInstance.getViewerListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), ViewerLeaveEvent.class));
        break;
      case "viewerRemoved":

        isometrikInstance.getViewerListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), ViewerRemoveEvent.class));
        break;

      case "viewerTimeout":

        isometrikInstance.getViewerListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), ViewerTimeoutEvent.class));
        break;

      case "copublishRequestAccepted":

        isometrikInstance.getCopublishListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), CopublishRequestAcceptEvent.class));
        break;
      case "copublishRequestAdded":

        isometrikInstance.getCopublishListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), CopublishRequestAddEvent.class));
        break;
      case "copublishRequestDenied":

        isometrikInstance.getCopublishListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), CopublishRequestDenyEvent.class));
        break;
      case "copublishRequestRemoved":

        isometrikInstance.getCopublishListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), CopublishRequestRemoveEvent.class));
        break;

      case "profileSwitched":

        isometrikInstance.getCopublishListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), CopublishRequestSwitchProfileEvent.class));
        break;
    }
  }
}
