package io.isometrik.gs.response.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Map;

/**
 * The class to parse the error response.
 */
public class ErrorResponse implements Serializable {
  @SerializedName("error")
  @Expose
  private String errorMessage;

  @SerializedName("errorCode")
  @Expose
  private int errorCode;

  @SerializedName("errors")
  @Expose
  private Map<String, Object> errors;

  /**
   * Gets errors.
   *
   * @return the errors map associated with the failure response
   */
  public Map<String, Object> getErrors() {
    return errors;
  }

  /**
   * Gets error code.
   *
   * @return the error code associated with the failure response
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Gets error message.
   *
   * @return the error message associated with the failure response
   */
  public String getErrorMessage() {
    return errorMessage;
  }
}
