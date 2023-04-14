package io.isometrik.gs.response.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse remove viewer result of the remove viewer from the stream group query
 * RemoveViewerQuery{@link io.isometrik.gs.builder.viewer.RemoveViewerQuery}.
 *
 * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery
 */
public class RemoveViewerResult implements Serializable {
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
