package io.isometrik.gs.response.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse leave member result of a member leaving the stream group query
 * LeaveMemberQuery{@link io.isometrik.gs.builder.member.LeaveMemberQuery}.
 *
 * @see io.isometrik.gs.builder.member.LeaveMemberQuery
 */
public class LeaveMemberResult implements Serializable {

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
