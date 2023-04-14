package io.isometrik.gs.callbacks;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class containing viewer event callbacks for when viewer joins or leaves a stream
 * group as audience, or when viewer has been removed from a stream group or timed out due to his
 * connection being severed.
 */
public abstract class ViewerEventCallback {
  /**
   * Viewer joined.
   *
   * @param isometrik the isometrik instance
   * @param viewerJoinEvent the viewer join event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.viewer.ViewerJoinEvent
   */
  public abstract void viewerJoined(@NotNull Isometrik isometrik,
      @NotNull ViewerJoinEvent viewerJoinEvent);

  /**
   * Viewer left.
   *
   * @param isometrik the isometrik instance
   * @param viewerLeaveEvent the viewer leave event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.viewer.ViewerLeaveEvent
   */
  public abstract void viewerLeft(@NotNull Isometrik isometrik,
      @NotNull ViewerLeaveEvent viewerLeaveEvent);

  /**
   * Viewer removed.
   *
   * @param isometrik the isometrik instance
   * @param viewerRemoveEvent the viewer remove event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.viewer.ViewerRemoveEvent
   */
  public abstract void viewerRemoved(@NotNull Isometrik isometrik,
      @NotNull ViewerRemoveEvent viewerRemoveEvent);

  /**
   * Viewer timed out.
   *
   * @param isometrik the isometrik instance
   * @param viewerTimeoutEvent the viewer timeout event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.viewer.ViewerTimeoutEvent
   */
  public abstract void viewerTimedOut(@NotNull Isometrik isometrik,
      @NotNull ViewerTimeoutEvent viewerTimeoutEvent);
}
