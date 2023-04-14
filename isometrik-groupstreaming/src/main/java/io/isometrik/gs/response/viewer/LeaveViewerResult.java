package io.isometrik.gs.response.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse leave viewer result of leave as a viewer from the stream group query
 * LeaveViewerQuery{@link io.isometrik.gs.builder.viewer.LeaveViewerQuery}.
 *
 * @see io.isometrik.gs.builder.viewer.LeaveViewerQuery
 */
public class LeaveViewerResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}
