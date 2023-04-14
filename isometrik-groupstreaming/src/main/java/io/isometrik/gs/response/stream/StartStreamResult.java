package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse start stream result of creating a stream group query StartStreamQuery{@link
 * io.isometrik.gs.builder.stream.StartStreamQuery}.
 *
 * @see io.isometrik.gs.builder.stream.StartStreamQuery
 */
public class StartStreamResult implements Serializable {

  @SerializedName("streamId")
  @Expose
  private String streamId;
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("startTime")
  @Expose
  private long startTime;
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
   * Gets stream id.
   *
   * @return the id of the stream group
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets start time.
   *
   * @return the time at which stream group was created
   */
  public long getStartTime() {
    return startTime;
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
