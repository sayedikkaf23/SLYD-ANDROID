package io.isometrik.gs.builder.subscription;

/**
 * Query builder class for creating the request for add subscription for fetching stream start and
 * stop event s for a user.
 */
public class AddSubscriptionQuery {

  private boolean streamStartEvent;
  private String userId;

  private AddSubscriptionQuery(Builder builder) {
    this.streamStartEvent = builder.streamStartChannel;
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the AddSubscriptionQuery.
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
     * @param streamStartChannel the type of stream presence event for which to subscribe for-stream
     * start or stream stop event
     * @return the AddSubscriptionQuery.Builder{@link io.isometrik.gs.builder.subscription.AddSubscriptionQuery.Builder} instance
     * @see io.isometrik.gs.builder.subscription.AddSubscriptionQuery.Builder
     */
    public Builder setStreamStartChannel(boolean streamStartChannel) {
      this.streamStartChannel = streamStartChannel;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user for whom to add the subscription for stream presence events
     * @return the AddSubscriptionQuery.Builder{@link io.isometrik.gs.builder.subscription.AddSubscriptionQuery.Builder} instance
     * @see io.isometrik.gs.builder.subscription.AddSubscriptionQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build add subscription query.
     *
     * @return the AddSubscriptionQuery{@link io.isometrik.gs.builder.subscription.AddSubscriptionQuery} instance
     * @see io.isometrik.gs.builder.subscription.AddSubscriptionQuery
     */
    public AddSubscriptionQuery build() {
      return new AddSubscriptionQuery(this);
    }
  }

  /**
   * Gets stream start event.
   *
   * @return the type of stream presence event for which to subscribe for-stream start or stream stop event
   */
  public boolean getStreamStartEvent() {
    return streamStartEvent;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user for whom to add the subscription for stream presence events
   */
  public String getUserId() {
    return userId;
  }
}
