package io.isometrik.gs.response.error;

import io.isometrik.gs.utils.MapUtils;

/**
 * The type Base response.
 */
public class BaseResponse {

  /**
   * Handle 400 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @param errorResponse the error response
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle400responseCode(IsometrikError.Builder isometrikErrorBuilder,
      ErrorResponse errorResponse) {

    isometrikErrorBuilder = isometrikErrorBuilder.setErrorMessage(errorResponse.getErrorMessage()).
        setIsometrikErrorCode(IsometrikErrorBuilder.getImerrBadRequestError());

    return isometrikErrorBuilder;
  }

  /**
   * Handle 403 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @param errorResponse the error response
   * @param hasErrorCode the has error code
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle403responseCode(IsometrikError.Builder isometrikErrorBuilder,
      ErrorResponse errorResponse, boolean hasErrorCode) {

    if (hasErrorCode) {
      isometrikErrorBuilder = isometrikErrorBuilder.setRemoteErrorCode(errorResponse.getErrorCode())
          .setErrorMessage(errorResponse.getErrorMessage())
          .setIsometrikErrorCode(IsometrikErrorBuilder.getImerrForbidden());
    } else {
      isometrikErrorBuilder = isometrikErrorBuilder.setErrorMessage(errorResponse.getErrorMessage())
          .setIsometrikErrorCode(IsometrikErrorBuilder.getImerrForbidden());
    }
    return isometrikErrorBuilder;
  }

  /**
   * Handle 404 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @param errorResponse the error response
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle404responseCode(IsometrikError.Builder isometrikErrorBuilder,
      ErrorResponse errorResponse) {

    isometrikErrorBuilder = isometrikErrorBuilder.setRemoteErrorCode(errorResponse.getErrorCode())
        .setErrorMessage(errorResponse.getErrorMessage())
        .setIsometrikErrorCode(IsometrikErrorBuilder.getImerrNotFound());

    return isometrikErrorBuilder;
  }

  /**
   * Handle 409 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @param errorResponse the error response
   * @param hasErrorCode the has error code
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle409responseCode(IsometrikError.Builder isometrikErrorBuilder,
      ErrorResponse errorResponse, boolean hasErrorCode) {

    if (hasErrorCode) {
      isometrikErrorBuilder = isometrikErrorBuilder.setRemoteErrorCode(errorResponse.getErrorCode())
          .setErrorMessage(errorResponse.getErrorMessage())
          .setIsometrikErrorCode(IsometrikErrorBuilder.getImerrConflict());
    } else {
      isometrikErrorBuilder = isometrikErrorBuilder.setErrorMessage(errorResponse.getErrorMessage())
          .setIsometrikErrorCode(IsometrikErrorBuilder.getImerrConflict());
    }
    return isometrikErrorBuilder;
  }

  /**
   * Handle 422 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @param errorResponse the error response
   * @return isometrik error . builder
   */
  public IsometrikError.Builder handle422responseCode(IsometrikError.Builder isometrikErrorBuilder,
      ErrorResponse errorResponse) {

    String message = MapUtils.mapToString(errorResponse.getErrors());

    isometrikErrorBuilder = isometrikErrorBuilder.setErrorMessage(message).
        setIsometrikErrorCode(IsometrikErrorBuilder.getImerrUnprocessableEntity());

    return isometrikErrorBuilder;
  }

  /**
   * Handle 500 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle500responseCode(
      IsometrikError.Builder isometrikErrorBuilder) {

    isometrikErrorBuilder = isometrikErrorBuilder.
        setIsometrikErrorCode(IsometrikErrorBuilder.getImerrInternalServerError());

    return isometrikErrorBuilder;
  }

  /**
   * Handle 502 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle502responseCode(
      IsometrikError.Builder isometrikErrorBuilder) {

    isometrikErrorBuilder = isometrikErrorBuilder.
        setIsometrikErrorCode(IsometrikErrorBuilder.getImerrBadGateway());

    return isometrikErrorBuilder;
  }

  /**
   * Handle 503 response code isometrik error . builder.
   *
   * @param isometrikErrorBuilder the isometrik error builder
   * @param errorResponse the error response
   * @return the isometrik error . builder
   */
  public IsometrikError.Builder handle503responseCode(IsometrikError.Builder isometrikErrorBuilder,
      ErrorResponse errorResponse) {

    isometrikErrorBuilder = isometrikErrorBuilder.setErrorMessage(errorResponse.getErrorMessage()).
        setIsometrikErrorCode(IsometrikErrorBuilder.getImerrServiceUnavailable());

    return isometrikErrorBuilder;
  }

  /**
   * Handle network error isometrik error.
   *
   * @param throwable the throwable
   * @return the isometrik error
   */
  public IsometrikError handleNetworkError(Throwable throwable) {
    return new IsometrikError.Builder().setIsometrikErrorCode(
        IsometrikErrorBuilder.getImerrNetworkError())
        .setErrorMessage(throwable.getMessage())
        .setRemoteError(true)
        .build();
  }

  /**
   * Handle parsing error isometrik error.
   *
   * @param throwable the throwable
   * @return the isometrik error
   */
  public IsometrikError handleParsingError(Throwable throwable) {

    return new IsometrikError.Builder().setIsometrikErrorCode(
        IsometrikErrorBuilder.getImerrParsingError())
        .setErrorMessage(throwable.getMessage())
        .setRemoteError(true)
        .build();
  }


}
