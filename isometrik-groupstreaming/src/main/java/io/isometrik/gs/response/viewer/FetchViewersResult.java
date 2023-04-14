package io.isometrik.gs.response.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch viewers result of the fetch list of viewers in the stream group query
 * FetchViewersQuery{@link io.isometrik.gs.builder.viewer.FetchViewersQuery}.
 *
 * @see io.isometrik.gs.builder.viewer.FetchViewersQuery
 */
public class FetchViewersResult implements Serializable {

  @SerializedName("viewers")
  @Expose
  private ArrayList<StreamViewer> streamViewers;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the viewers in the stream group.
   */
  public static class StreamViewer {

    @SerializedName("viewerId")
    @Expose
    private String viewerId;
    @SerializedName("viewerName")
    @Expose
    private String viewerName;
    @SerializedName("viewerIdentifier")
    @Expose
    private String viewerIdentifier;
    @SerializedName("viewerProfilePic")
    @Expose
    private String viewerProfilePic;
    @SerializedName("joinTime")
    @Expose
    private long joinTime;

    /**
     * Gets viewer id.
     *
     * @return the id of the viewer
     */
    public String getViewerId() {
      return viewerId;
    }

    /**
     * Gets viewer name.
     *
     * @return the name of the viewer
     */
    public String getViewerName() {
      return viewerName;
    }

    /**
     * Gets viewer identifier.
     *
     * @return the identifier of the viewer
     */
    public String getViewerIdentifier() {
      return viewerIdentifier;
    }

    /**
     * Gets viewer profile pic.
     *
     * @return the profile pic of the viewer
     */
    public String getViewerProfilePic() {
      return viewerProfilePic;
    }

    /**
     * Gets join time.
     *
     * @return the time at which viewer joined the stream group
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
   * Gets stream viewers.
   *
   * @return the list of viewers in the stream group
   * @see io.isometrik.gs.response.viewer.FetchViewersResult.StreamViewer
   */
  public ArrayList<StreamViewer> getStreamViewers() {
    return streamViewers;
  }
}
