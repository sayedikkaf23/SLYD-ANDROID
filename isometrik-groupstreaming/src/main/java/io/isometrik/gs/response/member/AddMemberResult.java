package io.isometrik.gs.response.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse add member result of adding a member to a stream group query
 * AddMemberQuery{@link io.isometrik.gs.builder.member.AddMemberQuery}.
 *
 * @see io.isometrik.gs.builder.member.AddMemberQuery
 */
public class AddMemberResult implements Serializable {

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
