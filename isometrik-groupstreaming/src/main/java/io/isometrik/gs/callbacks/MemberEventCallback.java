package io.isometrik.gs.callbacks;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the member event callbacks when a member is added,removed from a stream
 * group, member join or leave a stream group or updates the publishing status in a stream group,
 * member timed out due to connection being severed and when stream is offline due to none of the
 * publisher being live anymore.
 */
public abstract class MemberEventCallback {

  /**
   * Member added.
   *
   * @param isometrik the isometrik instance
   * @param memberAddEvent the member add event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.MemberAddEvent
   */
  public abstract void memberAdded(@NotNull Isometrik isometrik,
      @NotNull MemberAddEvent memberAddEvent);

  /**
   * Member left.
   *
   * @param isometrik the isometrik instance
   * @param memberLeaveEvent the member leave event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.MemberLeaveEvent
   */
  public abstract void memberLeft(@NotNull Isometrik isometrik,
      @NotNull MemberLeaveEvent memberLeaveEvent);

  /**
   * Member removed.
   *
   * @param isometrik the isometrik instance
   * @param memberRemoveEvent the member remove event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.MemberRemoveEvent
   */
  public abstract void memberRemoved(@NotNull Isometrik isometrik,
      @NotNull MemberRemoveEvent memberRemoveEvent);

  /**
   * Member timed out.
   *
   * @param isometrik the isometrik instance
   * @param memberTimeoutEvent the member timeout event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.MemberTimeoutEvent
   */
  public abstract void memberTimedOut(@NotNull Isometrik isometrik,
      @NotNull MemberTimeoutEvent memberTimeoutEvent);

  /**
   * Member publish started.
   *
   * @param isometrik the isometrik instance
   * @param publishStartEvent the publish start event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.PublishStartEvent
   */
  public abstract void memberPublishStarted(@NotNull Isometrik isometrik,
      @NotNull PublishStartEvent publishStartEvent);

  /**
   * Member publish stopped.
   *
   * @param isometrik the isometrik instance
   * @param publishStopEvent the publish stop event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.PublishStopEvent
   */
  public abstract void memberPublishStopped(@NotNull Isometrik isometrik,
      @NotNull PublishStopEvent publishStopEvent);

  /**
   * No member publishing.
   *
   * @param isometrik the isometrik instance
   * @param noPublisherLiveEvent the no publisher live event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.member.NoPublisherLiveEvent
   */
  public abstract void noMemberPublishing(@NotNull Isometrik isometrik,
      @NotNull NoPublisherLiveEvent noPublisherLiveEvent);
}
