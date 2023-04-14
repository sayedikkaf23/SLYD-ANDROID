package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse update stream publishing status result of updating the publishing status of a
 * user in a stream group query UpdateStreamPublishingStatusQuery{@link
 * io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery}.
 *
 * @see io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery
 */
public class UpdateStreamPublishingStatusResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("rtcToken")
  @Expose
  private String rtcToken;

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
