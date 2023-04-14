package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch streams result of fetching the stream groups list query
 * FetchStreamsQuery{@link io.isometrik.gs.builder.stream.FetchStreamsQuery}.
 *
 * @see io.isometrik.gs.builder.stream.FetchStreamsQuery
 */
public class FetchStreamsResult implements Serializable {

  @SerializedName("streams")
  @Expose
  private ArrayList<Stream> streams;

  @SerializedName("pageToken")
  @Expose
  private String pageToken;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the stream group.
   */
  public static class Stream {

    @SerializedName("streamid")
    @Expose
    private String streamId;
    @SerializedName("stream_image")
    @Expose
    private String streamImage;
    @SerializedName("stream_description")
    @Expose
    private String streamDescription;

    @SerializedName("members")
    @Expose
    private int membersCount;
    @SerializedName("viewers")
    @Expose
    private int viewersCount;
    @SerializedName("membersPublishing")
    @Expose
    private int publishersCount;

    @SerializedName("is_public")
    @Expose
    private boolean isPublic;
    @SerializedName("memberIds")
    @Expose
    private ArrayList<String> memberIds;

    @SerializedName("created_by")
    @Expose
    private String initiatorId;

    @SerializedName("initiator_name")
    @Expose
    private String initiatorName;

    @SerializedName("initiator_identifier")
    @Expose
    private String initiatorIdentifier;

    @SerializedName("initiator_image")
    @Expose
    private String initiatorImage;

    @SerializedName("start_time")
    @Expose
    private long startTime;

    /**
     * Gets stream id.
     *
     * @return the id of the stream group
     */
    public String getStreamId() {
      return streamId;
    }

    /**
     * Gets stream image.
     *
     * @return the image of the stream group
     */
    public String getStreamImage() {
      return streamImage;
    }

    /**
     * Gets stream description.
     *
     * @return the description of the stream group
     */
    public String getStreamDescription() {
      return streamDescription;
    }

    /**
     * Gets members count.
     *
     * @return the number of members
     */
    public int getMembersCount() {
      return membersCount;
    }

    /**
     * Gets viewers count.
     *
     * @return the number of viewers
     */
    public int getViewersCount() {
      return viewersCount;
    }

    /**
     * Gets publishers count.
     *
     * @return the count of publishers currently publishing
     */
    public int getPublishersCount() {
      return publishersCount;
    }

    /**
     * Gets start time.
     *
     * @return the time at which stream group was created
     */
    public long getStartTime() {
      return startTime;
    }

    /**
     * Gets member ids.
     *
     * @return the ids of the stream group members
     */
    public ArrayList<String> getMemberIds() {
      return memberIds;
    }

    /**
     * Gets initiator id.
     *
     * @return the id of the stream group creator
     */
    public String getInitiatorId() {
      return initiatorId;
    }

    /**
     * Gets initiator name.
     *
     * @return the name of the stream group creator
     */
    public String getInitiatorName() {
      return initiatorName;
    }

    /**
     * Gets initiator identifier.
     *
     * @return the identifier of the stream group creator
     */
    public String getInitiatorIdentifier() {
      return initiatorIdentifier;
    }

    /**
     * Gets initiator image.
     *
     * @return the image of the stream group creator
     */
    public String getInitiatorImage() {
      return initiatorImage;
    }

    /**
     * Gets whether given stream is public.
     *
     * @return whether given stream is public
     */
    public boolean isPublic() {
      return isPublic;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets streams.
   *
   * @return the list of stream groups
   * @see io.isometrik.gs.response.stream.FetchStreamsResult.Stream
   */
  public ArrayList<Stream> getStreams() {
    return streams;
  }

  /**
   * Gets page token.
   *
   * @return the page token to be used for pagination in fetching list of stream groups
   */
  public String getPageToken() {
    return pageToken;
  }
}
