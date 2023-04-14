package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse stop stream result of ending a stream group query StopStreamQuery{@link
 * io.isometrik.gs.builder.stream.StopStreamQuery}.
 *
 * @see io.isometrik.gs.builder.stream.StopStreamQuery
 */
public class StopStreamResult implements Serializable {
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
