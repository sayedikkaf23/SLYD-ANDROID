package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch copublish requests result of fetching copublish requests list in a
 * stream group query
 * FetchCopublishRequestsQuery{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery}.
 *
 * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery
 */
public class FetchCopublishRequestsResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("requests")
  @Expose
  private ArrayList<CopublishRequest> copublishRequests;

  /**
   * The class containing details of the copublish requests.
   */
  public static class CopublishRequest {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("userProfilePic")
    @Expose
    private String userProfilePic;

    @SerializedName("pending")
    @Expose
    private boolean pending;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;
    @SerializedName("requestTime")
    @Expose
    private Long requestTime;

    /**
     * Gets user id.
     *
     * @return the id of the user who made the copublish request
     */
    public String getUserId() {
      return userId;
    }

    /**
     * Gets user name.
     *
     * @return the name of the user who made the copublish request
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Gets user identifier.
     *
     * @return the identifier of the user who made the copublish request
     */
    public String getUserIdentifier() {
      return userIdentifier;
    }

    /**
     * Gets user profile pic.
     *
     * @return the profile pic of the user who made the copublish request
     */
    public String getUserProfilePic() {
      return userProfilePic;
    }

    /**
     * Gets join time.
     *
     * @return time at which given user requested copublish, null if request is not pending
     */
    public Long getRequestTime() {
      return requestTime;
    }

    /**
     * Whether copublish request is pending from action.
     *
     * @return the copublish request's pending status
     */
    public boolean isPending() {
      return pending;
    }

    /**
     * Whether copublish request has been accepted or denied.
     *
     * @return the copublish request's acceptance or denial status, null if request is pending
     */
    public Boolean isAccepted() {
      return accepted;
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
   * Gets copublish requests.
   *
   * @return the list of the copublish requests in the stream group
   * @see CopublishRequest
   */
  public ArrayList<CopublishRequest> getCopublishRequests() {
    return copublishRequests;
  }
}