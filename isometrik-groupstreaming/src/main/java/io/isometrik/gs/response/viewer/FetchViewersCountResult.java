package io.isometrik.gs.response.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse fetch viewers count result of the fetch viewers count in the stream group
 * query FetchViewersCountQuery{@link io.isometrik.gs.builder.viewer.FetchViewersCountQuery}.
 *
 * @see io.isometrik.gs.builder.viewer.FetchViewersCountQuery
 */
public class FetchViewersCountResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("numberOfViewers")
  @Expose
  private int numberOfViewers;

  /**
   * Gets number of viewers.
   *
   * @return the number of viewers in the stream group
   */
  public int getNumberOfViewers() {
    return numberOfViewers;
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}
