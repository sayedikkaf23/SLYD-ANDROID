package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse delete copublish request result of deleting a copublish request in a stream
 * group query
 * DeleteCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery}.
 *
 * @see io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery
 */
public class DeleteCopublishRequestResult implements Serializable {

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