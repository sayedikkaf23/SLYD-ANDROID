package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse delete message result of deleting a message from the stream group query
 * DeleteMessageQuery{@link io.isometrik.gs.builder.message.DeleteMessageQuery}.
 *
 * @see io.isometrik.gs.builder.message.DeleteMessageQuery
 */
public class DeleteMessageResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("deletedAt")
  @Expose
  private long deletedAt;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets deleted at.
   *
   * @return the time at which message was deleted
   */
  public long getDeletedAt() {
    return deletedAt;
  }
}
