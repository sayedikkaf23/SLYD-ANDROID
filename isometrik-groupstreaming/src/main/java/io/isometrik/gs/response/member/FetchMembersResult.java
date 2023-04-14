package io.isometrik.gs.response.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch members result of fetching members list in a stream group query
 * FetchMembersQuery{@link io.isometrik.gs.builder.member.FetchMembersQuery}.
 *
 * @see io.isometrik.gs.builder.member.FetchMembersQuery
 */
public class FetchMembersResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("members")
  @Expose
  private ArrayList<StreamMember> streamMembers;

  /**
   * The class containing details of the stream group members.
   */
  public static class StreamMember {

    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("memberName")
    @Expose
    private String memberName;
    @SerializedName("memberIdentifier")
    @Expose
    private String memberIdentifier;
    @SerializedName("memberProfilePic")
    @Expose
    private String memberProfilePic;

    @SerializedName("isPublishing")
    @Expose
    private boolean isPublishing;
    @SerializedName("isAdmin")
    @Expose
    private boolean isAdmin;
    @SerializedName("joinTime")
    @Expose
    private long joinTime;

    /**
     * Gets member id.
     *
     * @return the id of the member
     */
    public String getMemberId() {
      return memberId;
    }

    /**
     * Gets member name.
     *
     * @return the name of the member
     */
    public String getMemberName() {
      return memberName;
    }

    /**
     * Gets member identifier.
     *
     * @return the identifier of the member
     */
    public String getMemberIdentifier() {
      return memberIdentifier;
    }

    /**
     * Gets member profile pic.
     *
     * @return the profile pic of the member
     */
    public String getMemberProfilePic() {
      return memberProfilePic;
    }

    /**
     * Gets publishing.
     *
     * @return whether the given member is publishing or not
     */
    public boolean getPublishing() {
      return isPublishing;
    }

    /**
     * Gets admin.
     *
     * @return whether the given member is creator of the stream group
     */
    public boolean getAdmin() {
      return isAdmin;
    }

    /**
     * Gets join time.
     *
     * @return time at which given member started publishing otherwise -1
     */
    public long getJoinTime() {
      return joinTime;
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
   * Gets stream members.
   *
   * @return the list of the stream group members
   * @see io.isometrik.gs.response.member.FetchMembersResult.StreamMember
   */
  public ArrayList<StreamMember> getStreamMembers() {
    return streamMembers;
  }
}
