package io.isometrik.groupstreaming.ui.streams.grid;

import io.isometrik.groupstreaming.ui.utils.TimeUtil;
import io.isometrik.gs.response.stream.FetchStreamsResult;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The type Streams model.
 */
public class StreamsModel implements Serializable {

  private boolean givenUserIsMember;

  private String streamId;
  private String streamImage;
  private String streamDescription;
  private int membersCount;
  private int viewersCount;
  private int publishersCount;
  private ArrayList<String> memberIds;
  private String initiatorId;
  private long startTime;
  private String initiatorName;
  private String duration;
  private String initiatorImage;
  private String initiatorIdentifier;
  private boolean isPublic;
  private int numberOfTimesAlreadySeen;

  /**
   * Instantiates a new Streams model.
   *
   * @param stream the stream
   * @param givenUserIsMember the given user is member
   */
  public StreamsModel(FetchStreamsResult.Stream stream, boolean givenUserIsMember) {

    streamId = stream.getStreamId();
    streamImage = stream.getStreamImage();
    streamDescription = stream.getStreamDescription();
    membersCount = stream.getMembersCount();
    viewersCount = stream.getViewersCount();
    publishersCount = stream.getPublishersCount();
    initiatorId = stream.getInitiatorId();
    startTime = stream.getStartTime();
    memberIds = stream.getMemberIds();
    initiatorName = stream.getInitiatorName();
    initiatorImage = stream.getInitiatorImage();
    initiatorIdentifier = stream.getInitiatorIdentifier();
    isPublic = stream.isPublic();
    this.givenUserIsMember = givenUserIsMember;
    this.duration = TimeUtil.getDurationString(TimeUtil.getDuration(startTime));
  }

  /**
   * Instantiates a new Streams model.
   *
   * @param streamId the stream id
   * @param streamImage the stream image
   * @param streamDescription the stream description
   * @param membersCount the members count
   * @param viewersCount the viewers count
   * @param publishersCount the publishers count
   * @param initiatorId the initiator id
   * @param initiatorName the initiator name
   * @param initiatorIdentifier the initiator identifier
   * @param initiatorImage the initiator image
   * @param startTime the start time
   * @param memberIds the member ids
   * @param givenUserIsMember the given user is member
   * @param isPublic whether given stream is public or not
   */
  public StreamsModel(String streamId, String streamImage, String streamDescription,
      int membersCount, int viewersCount, int publishersCount, String initiatorId,
      String initiatorName, String initiatorIdentifier, String initiatorImage, long startTime,
      ArrayList<String> memberIds, boolean givenUserIsMember, boolean isPublic) {

    this.streamId = streamId;
    this.streamImage = streamImage;
    this.streamDescription = streamDescription;
    this.membersCount = membersCount;
    this.viewersCount = viewersCount;
    this.publishersCount = publishersCount;
    this.initiatorId = initiatorId;
    this.startTime = startTime;
    this.memberIds = memberIds;
    this.initiatorName = initiatorName;
    this.initiatorIdentifier = initiatorIdentifier;
    this.initiatorImage = initiatorImage;
    this.givenUserIsMember = givenUserIsMember;
    this.duration = TimeUtil.getDurationString(TimeUtil.getDuration(startTime));
    this.isPublic = isPublic;
  }

  /**
   * Gets duration.
   *
   * @return the duration
   */
  public String getDuration() {
    return duration;
  }

  /**
   * Gets stream id.
   *
   * @return the stream id
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets stream image.
   *
   * @return the stream image
   */
  public String getStreamImage() {
    return streamImage;
  }

  /**
   * Gets stream description.
   *
   * @return the stream description
   */
  public String getStreamDescription() {
    return streamDescription;
  }

  /**
   * Gets members count.
   *
   * @return the members count
   */
  public int getMembersCount() {
    return membersCount;
  }

  /**
   * Gets viewers count.
   *
   * @return the viewers count
   */
  public int getViewersCount() {
    return viewersCount;
  }

  /**
   * Gets publishers count.
   *
   * @return the publishers count
   */
  public int getPublishersCount() {
    return publishersCount;
  }

  /**
   * Gets start time.
   *
   * @return the start time
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * Gets initiator name.
   *
   * @return the initiator name
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets member ids.
   *
   * @return the member ids
   */
  public ArrayList<String> getMemberIds() {
    return memberIds;
  }

  /**
   * Gets initiator id.
   *
   * @return the initiator id
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Is given user is member boolean.
   *
   * @return the boolean
   */
  public boolean isGivenUserIsMember() {
    return givenUserIsMember;
  }

  /**
   * Sets members count.
   *
   * @param membersCount the members count
   */
  public void setMembersCount(int membersCount) {
    this.membersCount = membersCount;
  }

  /**
   * Sets viewers count.
   *
   * @param viewersCount the viewers count
   */
  public void setViewersCount(int viewersCount) {
    this.viewersCount = viewersCount;
  }

  /**
   * Sets given user is member.
   *
   * @param givenUserIsMember the given user is member
   */
  public void setGivenUserIsMember(boolean givenUserIsMember) {
    this.givenUserIsMember = givenUserIsMember;
  }

  /**
   * Sets member ids.
   *
   * @param memberIds the member ids
   */
  public void setMemberIds(ArrayList<String> memberIds) {
    this.memberIds = memberIds;
  }

  /**
   * Gets initiator image.
   *
   * @return the image of the user who started the stream group
   */
  public String getInitiatorImage() {
    return initiatorImage;
  }

  /**
   * Gets initiator identifier.
   *
   * @return the identifier of the user who started the stream group
   */
  public String getInitiatorIdentifier() {
    return initiatorIdentifier;
  }

  /**
   * Gets whether given stream is public.
   *
   * @return whether given stream is public
   */
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * For stream preview functionality
   */

  public int getNumberOfTimesAlreadySeen() {
    return numberOfTimesAlreadySeen;
  }

  public void setNumberOfTimesAlreadySeen(int numberOfTimesAlreadySeen) {
    this.numberOfTimesAlreadySeen = numberOfTimesAlreadySeen;
  }
}
