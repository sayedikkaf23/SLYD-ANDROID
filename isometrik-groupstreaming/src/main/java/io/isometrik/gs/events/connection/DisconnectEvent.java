package io.isometrik.gs.events.connection;

/**
 * The class containing disconnect event details.
 */
public class DisconnectEvent {

  private Throwable cause;

  /**
   * Instantiates a new Disconnect event.
   *
   * @param cause the cause of the connection drop
   */
  public DisconnectEvent(Throwable cause) {
    this.cause = cause;
  }

  /**
   * Gets cause.
   *
   * @return the cause of the connection drop
   */
  public Throwable getCause() {
    return cause;
  }

}
