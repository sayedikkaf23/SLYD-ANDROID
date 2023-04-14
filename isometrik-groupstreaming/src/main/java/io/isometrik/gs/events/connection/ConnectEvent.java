package io.isometrik.gs.events.connection;

/**
 * The class containing connect event details.
 */
public class ConnectEvent {
  private boolean reconnect;
  private String serverURI;

  /**
   * Instantiates a new Connect event.
   *
   * @param reconnect whether the event is a reconnect or a first time connect event
   * @param serverURI the server uri
   */
  public ConnectEvent(boolean reconnect, String serverURI) {
    this.reconnect = reconnect;
    this.serverURI = serverURI;
  }

  /**
   * Is reconnect boolean.
   *
   * @return the connection event is a reconnect or a first time connect event
   */
  public boolean isReconnect() {
    return reconnect;
  }

  /**
   * Gets server uri.
   *
   * @return the server uri
   */
  public String getServerURI() {
    return serverURI;
  }
}
