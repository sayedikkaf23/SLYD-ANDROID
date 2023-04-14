package io.isometrik.gs.builder.subscription;

/**
 * Query builder class for creating the request for removing subscription for stream start or stop
 * events for a user.
 */
public class RemoveSubscriptionQuery {

  private boolean streamStartEvent;
  private String userId;

  private RemoveSubscriptionQuery(Builder builder) {
    this.streamStartEvent = builder.streamStartChannel;
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the RemoveSubscriptionQuery.
   */
  public static class Builder {
    private boolean streamStartChannel;
    private String userId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream start channel.
     *
     * @param streamStartChannel the type of presence event to unsubscribe from-stream start or
     * stream stop event
     * @return the RemoveSubscriptionQuery.Builder{@link io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery.Builder} instance
     * @see io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery.Builder
     */
    public Builder setStreamStartChannel(boolean streamStartChannel) {
      this.streamStartChannel = streamStartChannel;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user for whom to remove the subscription for stream presence
     * events
     * @return the RemoveSubscriptionQuery.Builder{@link io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery.Builder} instance
     * @see io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build remove subscription query.
     *
     * @return the RemoveSubscriptionQuery{@link io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery} instance
     * @see io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery
     */
    public RemoveSubscriptionQuery build() {
      return new RemoveSubscriptionQuery(this);
    }
  }

  /**
   * Gets stream start event.
   *
   * @return the type of presence event to unsubscribe from-stream start or stream stop event
   */
  public boolean getStreamStartEvent() {
    return streamStartEvent;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user for whom to remove the subscription for stream presence events
   */
  public String getUserId() {
    return userId;
  }
}
