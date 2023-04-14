package io.isometrik.gs.response.error;

/**
 * The type Isometrik error builder.
 */
public final class IsometrikErrorBuilder {

  // Error Codes

  /**
   * Account id missing
   */
  private static final int IMERR_ACCOUNTID_MISSING = 101;

  /**
   * The constant IMERROBJ_ACCOUNTID_MISSING.
   */
  public static final IsometrikError IMERROBJ_ACCOUNTID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_ACCOUNTID_MISSING)
          .setErrorMessage("Account id not configured.")
          .setRemoteError(false)
          .build();

  /**
   * Project id missing
   */
  private static final int IMERR_PROJECTID_MISSING = 102;

  /**
   * The constant IMERROBJ_PROJECTID_MISSING.
   */
  public static final IsometrikError IMERROBJ_PROJECTID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PROJECTID_MISSING)
          .setErrorMessage("Project id not configured.")
          .setRemoteError(false)
          .build();
  /**
   * Keyset id missing
   */
  private static final int IMERR_KEYSETID_MISSING = 103;

  /**
   * The constant IMERROBJ_KEYSETID_MISSING.
   */
  public static final IsometrikError IMERROBJ_KEYSETID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_KEYSETID_MISSING)
          .setErrorMessage("Keyset id not configured.")
          .setRemoteError(false)
          .build();
  /**
   * License key missing
   */
  private static final int IMERR_LICENSE_KEY_MISSING = 104;

  /**
   * The constant IMERROBJ_LICENSE_KEY_MISSING.
   */
  public static final IsometrikError IMERROBJ_LICENSE_KEY_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_LICENSE_KEY_MISSING)
          .setErrorMessage("License key not configured.")
          .setRemoteError(false)
          .build();

  /**
   * Stream id missing
   */
  private static final int IMERR_STREAMID_MISSING = 105;

  /**
   * The constant IMERROBJ_STREAMID_MISSING.
   */
  public static final IsometrikError IMERROBJ_STREAMID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAMID_MISSING)
          .setErrorMessage("Stream id is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Member id missing
   */
  private static final int IMERR_MEMBERID_MISSING = 106;

  /**
   * The constant IMERROBJ_MEMBERID_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEMBERID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBERID_MISSING)
          .setErrorMessage("Member id is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Viewer id missing
   */
  private static final int IMERR_VIEWERID_MISSING = 107;

  /**
   * The constant IMERROBJ_VIEWERID_MISSING.
   */
  public static final IsometrikError IMERROBJ_VIEWERID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_VIEWERID_MISSING)
          .setErrorMessage("Viewer id is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Initiator id missing
   */
  private static final int IMERR_INITIATORID_MISSING = 108;

  /**
   * The constant IMERROBJ_INITIATORID_MISSING.
   */
  public static final IsometrikError IMERROBJ_INITIATORID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_INITIATORID_MISSING)
          .setErrorMessage("Initiator id is missing.")
          .setRemoteError(false)
          .build();

  /**
   * User id missing
   */
  private static final int IMERR_USERID_MISSING = 109;

  /**
   * The constant IMERROBJ_USERID_MISSING.
   */
  public static final IsometrikError IMERROBJ_USERID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USERID_MISSING)
          .setErrorMessage("User id is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Sender id missing
   */
  private static final int IMERR_SENDERID_MISSING = 110;

  /**
   * The constant IMERROBJ_SENDERID_MISSING.
   */
  public static final IsometrikError IMERROBJ_SENDERID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDERID_MISSING)
          .setErrorMessage("Sender id is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Sender name missing
   */
  private static final int IMERR_SENDER_NAME_MISSING = 111;

  /**
   * The constant IMERROBJ_SENDER_NAME_MISSING.
   */
  public static final IsometrikError IMERROBJ_SENDER_NAME_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDER_NAME_MISSING)
          .setErrorMessage("Sender name is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Sender identifier missing
   */
  private static final int IMERR_SENDER_IDENTIFIER_MISSING = 112;

  /**
   * The constant IMERROBJ_SENDER_IDENTIFIER_MISSING.
   */
  public static final IsometrikError IMERROBJ_SENDER_IDENTIFIER_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDER_IDENTIFIER_MISSING)
          .setErrorMessage("Sender identifier is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Sender image missing
   */
  private static final int IMERR_SENDER_IMAGE_MISSING = 113;

  /**
   * The constant IMERROBJ_SENDER_IMAGE_MISSING.
   */
  public static final IsometrikError IMERROBJ_SENDER_IMAGE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDER_IMAGE_MISSING)
          .setErrorMessage("Sender image is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Message missing
   */
  private static final int IMERR_MESSAGE_MISSING = 114;

  /**
   * The constant IMERROBJ_MESSAGE_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_MISSING)
          .setErrorMessage("Message is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Message type invalid value
   */
  private static final int IMERR_MESSAGE_TYPE_INVALID_VALUE = 115;

  /**
   * The constant IMERROBJ_MESSAGE_TYPE_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_MESSAGE_TYPE_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_TYPE_INVALID_VALUE)
          .setErrorMessage("Message type invalid value.")
          .setRemoteError(false)
          .build();

  /**
   * Created by missing
   */
  private static final int IMERR_CREATED_BY_MISSING = 116;

  /**
   * The constant IMERROBJ_CREATED_BY_MISSING.
   */
  public static final IsometrikError IMERROBJ_CREATED_BY_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CREATED_BY_MISSING)
          .setErrorMessage("Created by is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Stream image is missing
   */
  private static final int IMERR_STREAM_IMAGE_MISSING = 117;

  /**
   * The constant IMERROBJ_STREAM_IMAGE_MISSING.
   */
  public static final IsometrikError IMERROBJ_STREAM_IMAGE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_IMAGE_MISSING)
          .setErrorMessage("Stream image is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Stream description missing
   */
  private static final int IMERR_STREAM_DESCRIPTION_MISSING = 118;

  /**
   * The constant IMERROBJ_STREAM_DESCRIPTION_MISSING.
   */
  public static final IsometrikError IMERROBJ_STREAM_DESCRIPTION_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_DESCRIPTION_MISSING)
          .setErrorMessage("Stream description is missing.")
          .setRemoteError(false)
          .build();

  /**
   * Stream members missing
   */
  private static final int IMERR_STREAM_MEMBERS_MISSING = 119;

  /**
   * The constant IMERROBJ_STREAM_MEMBERS_MISSING.
   */
  public static final IsometrikError IMERROBJ_STREAM_MEMBERS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_MEMBERS_MISSING)
          .setErrorMessage("Stream members are missing.")
          .setRemoteError(false)
          .build();

  /**
   * User name missing
   */
  private static final int IMERR_USERNAME_MISSING = 120;

  /**
   * The constant IMERROBJ_USERNAME_MISSING.
   */
  public static final IsometrikError IMERROBJ_USERNAME_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USERNAME_MISSING)
          .setErrorMessage("Username is missing.")
          .setRemoteError(false)
          .build();

  /**
   * User profile pic missing
   */
  private static final int IMERR_USER_PROFILEPIC_MISSING = 121;

  /**
   * The constant IMERROBJ_USER_PROFILEPIC_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_PROFILEPIC_MISSING = new IsometrikError.Builder()
      .setIsometrikErrorCode(IMERR_USER_PROFILEPIC_MISSING)
      .setErrorMessage("User profile pic is missing.")
      .setRemoteError(false)
      .build();

  /**
   * User identifier missing
   */
  private static final int IMERR_USER_IDENTIFIER_MISSING = 122;

  /**
   * The constant IMERROBJ_USER_IDENTIFIER_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_IDENTIFIER_MISSING = new IsometrikError.Builder()
      .setIsometrikErrorCode(IMERR_USER_IDENTIFIER_MISSING)
      .setErrorMessage("User identifier is missing.")
      .setRemoteError(false)
      .build();

  /**
   * Count invalid value
   */
  private static final int IMERR_COUNT_INVALID_VALUE = 123;

  /**
   * The constant IMERROBJ_COUNT_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_COUNT_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_COUNT_INVALID_VALUE)
          .setErrorMessage("Count has invalid value.")
          .setRemoteError(false)
          .build();

  /**
   * Page token invalid value
   */
  private static final int IMERR_PAGE_TOKEN_INVALID_VALUE = 124;

  /**
   * The constant IMERROBJ_PAGE_TOKEN_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_PAGE_TOKEN_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PAGE_TOKEN_INVALID_VALUE)
          .setErrorMessage("Page token has invalid value.")
          .setRemoteError(false)
          .build();

  /**
   * User name invalid value
   */
  private static final int IMERR_USERNAME_INVALID_VALUE = 125;

  /**
   * The constant IMERROBJ_USERNAME_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_USERNAME_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USERNAME_INVALID_VALUE)
          .setErrorMessage("Username has invalid value.")
          .setRemoteError(false)
          .build();

  /**
   * User profile invalid value
   */
  private static final int IMERR_USER_PROFILEPIC_INVALID_VALUE = 126;

  /**
   * The constant IMERROBJ_USER_PROFILEPIC_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_USER_PROFILEPIC_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PROFILEPIC_INVALID_VALUE)
          .setErrorMessage("User profile pic has invalid value.")
          .setRemoteError(false)
          .build();

  /**
   * User identifier invalid value
   */
  private static final int IMERR_USER_IDENTIFIER_INVALID_VALUE = 127;

  /**
   * The constant IMERROBJ_USER_IDENTIFIER_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_USER_IDENTIFIER_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IDENTIFIER_INVALID_VALUE)
          .setErrorMessage("User identifier has invalid value.")
          .setRemoteError(false)
          .build();

  /**
   * Not Found
   */
  private static final int IMERR_NOT_FOUND = 128;

  /**
   * Forbidden
   */
  private static final int IMERR_FORBIDDEN = 129;

  /**
   * Service unavailable
   */
  private static final int IMERR_SERVICE_UNAVAILABLE = 130;

  /**
   * Conflict
   */
  private static final int IMERR_CONFLICT = 131;

  /**
   * Unprocessable entity
   */
  private static final int IMERR_UNPROCESSABLE_ENTITY = 132;

  /**
   * Bad gateway
   */
  private static final int IMERR_BAD_GATEWAY = 133;

  /**
   * Internal server error
   */

  private static final int IMERR_INTERNAL_SERVER_ERROR = 134;

  /**
   * Parsing error
   */

  private static final int IMERR_PARSING_ERROR = 135;

  /**
   * Network error
   */

  private static final int IMERR_NETWORK_ERROR = 136;

  /**
   * Bad Request error
   */

  private static final int IMERR_BAD_REQUEST_ERROR = 137;

  /**
   * ClientId missing
   */
  private static final int IMERR_CLIENT_ID_MISSING = 138;

  /**
   * The constant IMERROBJ_CLIENT_ID_MISSING.
   */
  public static final IsometrikError IMERROBJ_CLIENT_ID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CLIENT_ID_MISSING)
          .setErrorMessage("Client id not configured.")
          .setRemoteError(false)
          .build();

  /**
   * Is public missing
   */
  private static final int IMERR_IS_PUBLIC_MISSING = 139;

  /**
   * The constant IMERROBJ_IS_PUBLIC_MISSING.
   */
  public static final IsometrikError IMERROBJ_IS_PUBLIC_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_IS_PUBLIC_MISSING)
          .setErrorMessage("Is public stream missing.")
          .setRemoteError(false)
          .build();

  /**
   * Invalid value of streamType
   */
  private static final int IMERR_STREAM_TYPE_INVALID_VALUE = 140;

  /**
   * The constant IMERROBJ_STREAM_TYPE_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_STREAM_TYPE_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_TYPE_INVALID_VALUE)
          .setErrorMessage("Is public stream missing.")
          .setRemoteError(false)
          .build();


  /**
   * Missing value of streamType
   */
  private static final int IMERR_STREAM_TYPE_MISSING = 141;

  /**
   * The constant IMERROBJ_STREAM_TYPE_MISSING.
   */
  public static final IsometrikError IMERROBJ_STREAM_TYPE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_TYPE_MISSING)
          .setErrorMessage("Stream type missing.")
          .setRemoteError(false)
          .build();

  /**
   * Gets imerr not found.
   *
   * @return the imerr not found
   */
  static int getImerrNotFound() {
    return IMERR_NOT_FOUND;
  }

  /**
   * Gets imerr forbidden.
   *
   * @return the imerr forbidden
   */
  static int getImerrForbidden() {
    return IMERR_FORBIDDEN;
  }

  /**
   * Gets imerr service unavailable.
   *
   * @return the imerr service unavailable
   */
  static int getImerrServiceUnavailable() {
    return IMERR_SERVICE_UNAVAILABLE;
  }

  /**
   * Gets imerr conflict.
   *
   * @return the imerr conflict
   */
  static int getImerrConflict() {
    return IMERR_CONFLICT;
  }

  /**
   * Gets imerr unprocessable entity.
   *
   * @return the imerr unprocessable entity
   */
  static int getImerrUnprocessableEntity() {
    return IMERR_UNPROCESSABLE_ENTITY;
  }

  /**
   * Gets imerr bad gateway.
   *
   * @return the imerr bad gateway
   */
  static int getImerrBadGateway() {
    return IMERR_BAD_GATEWAY;
  }

  /**
   * Gets imerr internal server error.
   *
   * @return the imerr internal server error
   */
  static int getImerrInternalServerError() {
    return IMERR_INTERNAL_SERVER_ERROR;
  }

  /**
   * Gets imerr parsing error.
   *
   * @return the imerr parsing error
   */
  static int getImerrParsingError() {
    return IMERR_PARSING_ERROR;
  }

  /**
   * Gets imerr network error.
   *
   * @return the imerr network error
   */
  static int getImerrNetworkError() {
    return IMERR_NETWORK_ERROR;
  }

  /**
   * Gets imerr bad request error.
   *
   * @return the imerr bad request error
   */
  static int getImerrBadRequestError() {
    return IMERR_BAD_REQUEST_ERROR;
  }
}
