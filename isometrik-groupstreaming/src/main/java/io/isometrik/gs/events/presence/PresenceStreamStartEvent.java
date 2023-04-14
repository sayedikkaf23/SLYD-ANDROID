package io.isometrik.gs.events.presence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * The class containing presence stream start event details, which is triggered if user is
 * subscribed to stream start events irrespective of whether he is member of the stream group or
 * not.
 */
public class PresenceStreamStartEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("streamDescription")
  @Expose
  private String streamDescription;

  @SerializedName("streamImage")
  @Expose
  private String streamImage;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  @SerializedName("initiatorIdentifier")
  @Expose
  private String initiatorIdentifier;

  @SerializedName("initiatorImage")
  @Expose
  private String initiatorImage;

  @SerializedName("members")
  @Expose
  private List<StreamMember> members;

  @SerializedName("isPublic")
  @Expose
  private boolean isPublic;

  /**
   * The class containing details of the Stream member.
   */
  public static class StreamMember {

    @SerializedName("member_id")
    @Expose
    public String memberId;

    @SerializedName("member_name")
    @Expose
    public String memberName;

    @SerializedName("member_identifier")
    @Expose
    public String memberIdentifier;

    @SerializedName("is_admin")
    @Expose
    public boolean isAdmin;

    /**
     * Gets member id.
     *
     * @return the member id
     */
    public String getMemberId() {
      return memberId;
    }

    /**
     * Gets member name.
     *
     * @return the member name
     */
    public String getMemberName() {
      return memberName;
    }

    /**
     * Gets member identifier.
     *
     * @return the member identifier
     */
    public String getMemberIdentifier() {
      return memberIdentifier;
    }

    /**
     * Gets admin.
     *
     * @return whether given member is the admin of the stream group
     */
    public boolean getAdmin() {
      return isAdmin;
    }
  }

  /**
   * Gets action.
   *
   * @return the action specifying the stream start event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group created
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which stream group was created
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets members count.
   *
   * @return the members count of the stream group
   */
  public int getMembersCount() {
    return membersCount;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of creator of the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of creator of the stream group
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets initiator identifier.
   *
   * @return the identifier of creator of the stream group
   */
  public String getInitiatorIdentifier() {
    return initiatorIdentifier;
  }

  /**
   * Gets initiator image.
   *
   * @return the image of creator of the stream group
   */
  public String getInitiatorImage() {
    return initiatorImage;
  }

  /**
   * Gets members.
   *
   * @return the list of members of the stream group
   */
  public List<StreamMember> getMembers() {
    return members;
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
   * Gets stream image.
   *
   * @return the image of the stream group
   */
  public String getStreamImage() {
    return streamImage;
  }

  /**
   * Gets whether the stream is public.
   *
   * @return whether stream is public
   */
  public boolean isPublic() {
    return isPublic;
  }
}