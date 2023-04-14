package io.isometrik.gs.response.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse add viewer result of joining the stream group as viewer query
 * AddViewerQuery{@link io.isometrik.gs.builder.viewer.AddViewerQuery}.
 *
 * @see io.isometrik.gs.builder.viewer.AddViewerQuery
 */
public class AddViewerResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("numberOfViewers")
  @Expose
  private int numberOfViewers;
  @SerializedName("rtcToken")
  @Expose
  private String rtcToken;

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

  /**
   * Gets rtc token.
   *
   * @return the rtc token received
   */
  public String getRtcToken() {
    return rtcToken;
  }
}
