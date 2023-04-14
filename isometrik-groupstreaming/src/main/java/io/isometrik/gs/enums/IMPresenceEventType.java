package io.isometrik.gs.enums;

/**
 * The enum defining the presence event type.
 */
public enum IMPresenceEventType {

  /**
   * isometrik member add event type.
   */
  IMMemberAddEvent("memberAdded"),

  /**
   * isometrik member remove event type.
   */
  IMMemberRemoveEvent("memberRemoved"),

  /**
   * isometrik member leave event type.
   */
  IMMemberLeaveEvent("memberLeft"),

  /**
   * isometrik member timeout event type.
   */
  IMMemberTimeoutEvent("publisherTimeout"),

  /**
   * isometrik viewer join event type.
   */
  IMViewerJoinEvent("viewerJoined"),

  /**
   * isometrik viewer remove event type.
   */
  IMViewerRemoveEvent("viewerRemoved"),

  /**
   * isometrik viewer leave event type.
   */
  IMViewerLeaveEvent("viewerLeft"),

  /**
   * isometrik viewer timeout event type.
   */
  IMViewerTimeoutEvent("viewerTimeout"),

  /**
   * isometrik stream start event type.
   */
  IMStreamStartEvent("streamStarted"),

  /**
   * Im stream stop event im presence event type.
   */
  IMStreamStopEvent("streamStopped"),

  /**
   * Im publish start event im presence event type.
   */
  IMPublishStartEvent("publishStarted"),

  /**
   * Im publish stop event im presence event type.
   */
  IMPublishStopEvent("publishStopped"),

  /**
   * Im no publisher live event im presence event type.
   */
  IMNoPublisherLiveEvent("streamOffline");

  private final String value;

  IMPresenceEventType(String value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return this.value;
  }
}
