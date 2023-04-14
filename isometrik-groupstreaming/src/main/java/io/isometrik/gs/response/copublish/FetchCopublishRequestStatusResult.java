package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse fetch copublish request status result of fetching status of a copublish
 * request in a stream group
 * query
 * FetchCopublishRequestStatusQuery{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery}.
 *
 * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery
 */
public class FetchCopublishRequestStatusResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("pending")
  @Expose
  private boolean pending;

  @SerializedName("accepted")
  @Expose
  private boolean accepted;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Whether copublish request is pending from action.
   *
   * @return the copublish request's pending status
   */
  public boolean isPending() {
    return pending;
  }

  /**
   * Whether copublish request has been accepted or denied.
   *
   * @return the copublish request's acceptance or denial status
   */
  public boolean isAccepted() {
    return accepted;
  }
}