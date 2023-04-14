package io.isometrik.gs.callbacks;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class containing stream event callbacks, when a stream start or stream stop event
 * has been received,which has given user as the member.
 */
public abstract class StreamEventCallback {
  /**
   * Stream started.
   *
   * @param isometrik the isometrik instance
   * @param streamStartEvent the stream start event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.stream.StreamStartEvent
   */
  public abstract void streamStarted(@NotNull Isometrik isometrik,
      @NotNull StreamStartEvent streamStartEvent);

  /**
   * Stream stopped.
   *
   * @param isometrik the isometrik instance
   * @param streamStopEvent the stream stop event
   * @see io.isometrik.gs.Isometrik
   * @see io.isometrik.gs.events.stream.StreamStopEvent
   */
  public abstract void streamStopped(@NotNull Isometrik isometrik,
      @NotNull StreamStopEvent streamStopEvent);
}
