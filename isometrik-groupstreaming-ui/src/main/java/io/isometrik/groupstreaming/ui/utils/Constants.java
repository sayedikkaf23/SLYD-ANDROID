package io.isometrik.groupstreaming.ui.utils;

import javax.crypto.spec.IvParameterSpec;

/**
 * The type Constants.
 */
public class Constants {

  /**
   * The constant USERS_PAGE_SIZE.
   */
  public static final int USERS_PAGE_SIZE = 10;

  /**
   * The constant STREAMS_PAGE_SIZE.
   */
  public static final int STREAMS_PAGE_SIZE = 10;

  /**
   * The constant MESSAGES_PAGE_SIZE.
   */
  public static final int MESSAGES_PAGE_SIZE = 10;

  /**
   * The Time correction.
   */
  static final int TIME_CORRECTION = 1000;

  /**
   * The constant KICKED_OUT_URL.
   */
  public static final String KICKED_OUT_URL =
      "https://res.cloudinary.com/dedibgpdw/image/upload/v1588226685/gifs/cry_cjb9st.gif";

  /**
   * The constant STREAM_OFFLINE_URL.
   */
  public static final String STREAM_OFFLINE_URL =
      "https://res.cloudinary.com/dedibgpdw/image/upload/v1588759141/gifs/disappointed_htkuth.gif";

  /**
   * The constant LIKE_URL.
   */
  public static final String LIKE_URL =
      "https://res.cloudinary.com/dedibgpdw/image/upload/v1589026805/gifs/giphy_cezush.gif";
  //public static final String LIKE_URL =
  //    "https://res.cloudinary.com/dedibgpdw/image/upload/v1589029578/users/svg-transparency-gif-6_kujmnn.gif";
  //public static final String LIKE_URL =
  //    "https://res.cloudinary.com/dedibgpdw/image/upload/v1589029793/users/heart_o3tar9.gif";

  public static final String KEY_ALIAS = "ALIAS";
  public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
  public static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
  public static final String AES_MODE = "AES/GCM/NoPadding";
  public static final String FIXED_IV = "appscripName";
  private final static byte[] IV = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
  public static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(IV);
}
