package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse add copublish request result of adding a copublish request in a stream group
 * query
 * AddCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.AddCopublishRequestQuery}.
 *
 * @see io.isometrik.gs.builder.copublish.AddCopublishRequestQuery
 */
public class AddCopublishRequestResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("numberOfRequests")
  @Expose
  private int numberOfRequests;

  /**
   * Gets number of copublish requests.
   *
   * @return the number of copublish requests in the stream group
   */
  public int getNumberOfCopublishRequests() {
    return numberOfRequests;
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