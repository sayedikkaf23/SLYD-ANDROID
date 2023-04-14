package io.isometrik.groupstreaming.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.enums.IMLogVerbosity;
import io.isometrik.gs.enums.IMRealtimeEventsVerbosity;

/**
 * The IsometrikUiSdk singleton to expose sdk functionality to other modules.
 */
public class IsometrikUiSdk {

  private Isometrik isometrik;
  private UserSession userSession;
  private String applicationId;
  private Context applicationContext;
  private static volatile IsometrikUiSdk isometrikUiSdk;

  /**
   * private constructor.
   */
  private IsometrikUiSdk() {

    //Prevent form the reflection api.
    if (isometrikUiSdk != null) {
      throw new RuntimeException(
          "Use getInstance() method to get the single instance of this class.");
    }
  }

  /**
   * Gets instance.
   *
   * @return the IsometrikUiSdk instance
   */
  public static IsometrikUiSdk getInstance() {
    if (isometrikUiSdk == null) {
      synchronized (IsometrikUiSdk.class) {
        if (isometrikUiSdk == null) {
          isometrikUiSdk = new IsometrikUiSdk();
        }
      }
    }
    return isometrikUiSdk;
  }

  public void sdkInitialize(Context applicationContext) {
    if (applicationContext == null) {
      throw new RuntimeException(
          "Sdk initialization failed as application context cannot be null.");
    } else if (!(applicationContext instanceof Application)) {
      throw new RuntimeException(
          "Sdk initialization failed as context passed in parameter is not application context.");
    }

    this.applicationContext = applicationContext;

    try {
      MediaManager.init(applicationContext);
    } catch (Exception e) {
      e.printStackTrace();
    }
    cacheStreamDialogImages(applicationContext);
  }

  public void createConfiguration(String accountId, String projectId, String keysetId,
      String licenseKey, String rtcAppId, String arFiltersAppId, String applicationId) {

    if (applicationContext == null) {
      throw new RuntimeException("Initialize the sdk before creating configuration.");
    } else if (accountId == null || accountId.isEmpty()) {
      throw new RuntimeException("Pass a valid accountId for isometrik sdk initialization.");
    } else if (projectId == null || projectId.isEmpty()) {
      throw new RuntimeException("Pass a valid projectId for isometrik sdk initialization.");
    } else if (keysetId == null || keysetId.isEmpty()) {
      throw new RuntimeException("Pass a valid keysetId for isometrik sdk initialization.");
    } else if (licenseKey == null || licenseKey.isEmpty()) {
      throw new RuntimeException("Pass a valid licenseKey for isometrik sdk initialization.");
    } else if (rtcAppId == null || rtcAppId.isEmpty()) {
      throw new RuntimeException("Pass a valid rtcAppId for isometrik sdk initialization.");
    } else if (arFiltersAppId == null || arFiltersAppId.isEmpty()) {
      throw new RuntimeException("Pass a valid arFiltersAppId for isometrik sdk initialization.");
    } else if (applicationId == null || applicationId.isEmpty()) {
      throw new RuntimeException("Pass a valid applicationId for isometrik sdk initialization.");
    }

    //Have hardcoded until backend is ready
    //arFiltersAppId =
    //    "2be1b408c6489a3449f235b250004941fcc4f9ebf76e18ff032203aaef5e20f108406b795eee12d4";

    this.applicationId = applicationId;
    IMConfiguration imConfiguration =
        new IMConfiguration(applicationContext, accountId, projectId, keysetId, licenseKey,
            rtcAppId, arFiltersAppId);
    imConfiguration.setLogVerbosity(IMLogVerbosity.NONE);
    imConfiguration.setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity.NONE);
    imConfiguration.setArEnabled(true);
    imConfiguration.setArFiltersDownloadRequired(true);
    /*
     * Auto mirroring
     */
    imConfiguration.setMirrorLocalIndex(0);
    imConfiguration.setMirrorRemoteIndex(0);
    imConfiguration.setEnableDetailedRtcStats(true);
    isometrik = new Isometrik(imConfiguration);
    userSession = new UserSession(applicationContext);
  }

  /**
   * Gets isometrik.
   *
   * @return the isometrik
   */
  public Isometrik getIsometrik() {
    if (isometrik == null) {
      throw new RuntimeException("Create configuration before trying to access isometrik object.");
    }

    return isometrik;
  }

  /**
   * Gets user session.
   *
   * @return the user session
   */
  public UserSession getUserSession() {
    if (userSession == null) {
      throw new RuntimeException(
          "Create configuration before trying to access user session object.");
    }

    return userSession;
  }

  public Context getContext() {
    if (isometrik == null) {
      throw new RuntimeException("Create configuration before trying to access context object.");
    }
    return applicationContext;
  }

  public void onTerminate() {
    if (isometrik == null) {
      throw new RuntimeException("Create configuration before trying to access isometrik object.");
    }
    isometrik.destroy();
  }

  @SuppressLint("CheckResult")
  private static void cacheStreamDialogImages(Context context) {
    try {
      Glide.with(context).load(Constants.KICKED_OUT_URL);
      Glide.with(context).load(Constants.STREAM_OFFLINE_URL);
      Glide.with(context).load(Constants.LIKE_URL);
    } catch (NullPointerException | IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  public String getApplicationId() {
    return applicationId;
  }
}
