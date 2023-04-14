package io.isometrik.groupstreaming.ui.streamdetails;

/**
 * The interface stream details containing interfaces Presenter and View to be implemented
 * by the
 * StreamDetailsPresenter{@link StreamDetailsPresenter} and
 * StreamDetailsActivity{@link StreamDetailsActivity} respectively.
 *
 * @see StreamDetailsPresenter
 * @see StreamDetailsActivity
 */
public interface StreamDetailsContract {

  /**
   * The interface StreamDetailsContract.Presenter to be implemented by StreamDetailsPresenter{@link
   * StreamDetailsPresenter}
   *
   * @see StreamDetailsPresenter
   */
  interface Presenter {

    /**
     * Initialize.
     *
     * @param streamId the stream id
     */
    void initialize(String streamId);

    /**
     * Register connection event listener.
     */
    void registerConnectionEventListener();

    /**
     * Unregister connection event listener.
     */
    void unregisterConnectionEventListener();

    /**
     * Register streams event listener.
     */
    void registerStreamsEventListener();

    /**
     * Unregister streams event listener.
     */
    void unregisterStreamsEventListener();

    /**
     * Register stream members event listener.
     */
    void registerStreamMembersEventListener();

    /**
     * Unregister stream members event listener.
     */
    void unregisterStreamMembersEventListener();

    /**
     * Register stream viewers event listener.
     */
    void registerStreamViewersEventListener();

    /**
     * Unregister stream viewers event listener.
     */
    void unregisterStreamViewersEventListener();
  }

  /**
   * The interface StreamDetailsContract.View to be implemented by StreamDetailsActivity{@link
   * StreamDetailsActivity}
   *
   * @see StreamDetailsActivity
   */
  interface View {

    /**
     * On stream offline.
     *
     * @param message the message
     * @param dialogType the dialog type
     */
    void onStreamOffline(String message, int dialogType);

    /**
     * Connection state changed.
     *
     * @param connected whether connection to receive realtime events has been made or broken
     */
    void connectionStateChanged(boolean connected);
  }
}
