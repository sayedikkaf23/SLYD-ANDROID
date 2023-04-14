package io.isometrik.gs;

import android.content.Context;
import io.isometrik.gs.enums.IMLogVerbosity;
import io.isometrik.gs.enums.IMRealtimeEventsVerbosity;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.rtcengine.ar.FiltersConfig;

/**
 * The type Im configuration.
 */
public class IMConfiguration {

  private static final int REQUEST_TIMEOUT = 10;
  private static final int CONNECT_TIMEOUT = 5;

  private Context context;
  /**
   * License Key provided by Isometrik.
   */
  private String licenseKey;
  //public void setLicenseKey(String licenseKey) {
  //  this.licenseKey = licenseKey;
  //}

  /**
   * Account Id provided by Isometrik.
   */
  private String accountId;
  //public void setAccountId(String accountId) {
  //  this.accountId = accountId;
  //}

  /**
   * Project Id provided by Isometrik.
   */
  private String projectId;
  //public void setProjectId(String projectId) {
  //  this.projectId = projectId;
  //}

  /**
   * Keyset Id provided by Isometrik.
   */
  private String keysetId;
  //public void setKeysetId(String keysetId) {
  //  this.keysetId = keysetId;
  //}

  private String arFiltersKey;
  private boolean arEnabled;
  /**
   * For RtcAppPreferences
   */
  private String rtcAppId;
  private boolean enableDetailedRtcStats;
  private int mirrorLocalIndex;
  private int mirrorRemoteIndex;
  private int mirrorEncodeIndex;

  private String clientId;
  /**
   * If proxies are forcefully caching requests, set to true to allow the client to randomize the
   * subdomain.
   * This configuration is not supported if custom origin is enabled.
   */
  private boolean cacheBusting;

  /**
   * set to true to switch the client to HTTPS:// based communications.
   */

  private boolean secure;
  /**
   * toggle to enable verbose logging.
   */

  private IMLogVerbosity logVerbosity;

  /**
   * toggle to enable verbose logging.
   */

  private IMRealtimeEventsVerbosity realtimeEventsVerbosity;

  /**
   * Stores the maximum number of seconds which the client should wait for connection before timing
   * out.
   */
  private int connectTimeout;

  /**
   * Reference on number of seconds which is used by client during operations to
   * check whether response potentially failed with 'timeout' or not.
   */
  private int requestTimeout;

  /**
   * Initialize the IMConfiguration with default values
   *
   * @param context the context
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param licenseKey the license key
   * @param rtcAppId the rtc app id
   * @param arFiltersKey the ar filters key
   */
  public IMConfiguration(Context context, String accountId, String projectId, String keysetId,
      String licenseKey, String rtcAppId, String arFiltersKey) {

    requestTimeout = REQUEST_TIMEOUT;

    connectTimeout = CONNECT_TIMEOUT;

    logVerbosity = IMLogVerbosity.NONE;

    realtimeEventsVerbosity = IMRealtimeEventsVerbosity.NONE;

    cacheBusting = false;
    secure = true;
    this.context = context;

    this.licenseKey = licenseKey;
    this.accountId = accountId;
    this.projectId = projectId;
    this.keysetId = keysetId;
    this.rtcAppId = rtcAppId;
    this.arFiltersKey = arFiltersKey;
    enableDetailedRtcStats = false;
    mirrorLocalIndex = 0;
    mirrorRemoteIndex = 0;
    mirrorEncodeIndex = 0;

    FiltersConfig.setFilesDirectory(context.getFilesDir().getAbsolutePath());
    FiltersConfig.setSharedPreferences(
        context.getSharedPreferences("defaultPreferences", Context.MODE_PRIVATE));
    //FiltersConfig.setSharedPreferences(
    //    context.getSharedPreferences("isometrikPreferences", Context.MODE_PRIVATE));
  }

  /**
   * Gets context.
   *
   * @return the context
   */
  Context getContext() {
    return context;
  }

  /**
   * Gets rtc app id.
   *
   * @return the rtc app id
   */
  String getRtcAppId() {
    return rtcAppId;
  }

  /**
   * Is enable rtc stats boolean.
   *
   * @return the boolean
   */
  public boolean isEnableDetailedRtcStats() {
    return enableDetailedRtcStats;
  }

  /**
   * Gets mirror local index.
   *
   * @return the mirror local index
   */
  public int getMirrorLocalIndex() {
    return mirrorLocalIndex;
  }

  /**
   * Gets mirror remote index.
   *
   * @return the mirror remote index
   */
  public int getMirrorRemoteIndex() {
    return mirrorRemoteIndex;
  }

  /**
   * Gets mirror encode index.
   *
   * @return the mirror encode index
   */
  public int getMirrorEncodeIndex() {
    return mirrorEncodeIndex;
  }

  /**
   * Is cache busting boolean.
   *
   * @return the boolean
   */
  public boolean isCacheBusting() {
    return cacheBusting;
  }

  /**
   * Is secure boolean.
   *
   * @return the boolean
   */
  public boolean isSecure() {
    return secure;
  }

  /**
   * Gets log verbosity.
   *
   * @return the log verbosity
   */
  public IMLogVerbosity getLogVerbosity() {
    return logVerbosity;
  }

  /**
   * Gets remote message verbosity.
   *
   * @return the remote message verbosity
   */
  public IMRealtimeEventsVerbosity getRealtimeEventsVerbosity() {
    return realtimeEventsVerbosity;
  }

  /**
   * Gets connect timeout.
   *
   * @return the connect timeout
   */
  public int getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Gets request timeout.
   *
   * @return the request timeout
   */
  public int getRequestTimeout() {
    return requestTimeout;
  }

  /**
   * Gets license key.
   *
   * @return the license key
   */
  public String getLicenseKey() {
    return licenseKey;
  }

  /**
   * Gets account id.
   *
   * @return the account id
   */
  public String getAccountId() {
    return accountId;
  }

  /**
   * Gets project id.
   *
   * @return the project id
   */
  public String getProjectId() {
    return projectId;
  }

  /**
   * Gets keyset id.
   *
   * @return the keyset id
   */
  public String getKeysetId() {
    return keysetId;
  }

  /**
   * Gets client id.
   *
   * @return the client id
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets client id.
   *
   * @param clientId the client id
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Sets enable rtc stats.
   *
   * @param enableDetailedRtcStats the enable rtc stats
   */
  public void setEnableDetailedRtcStats(boolean enableDetailedRtcStats) {
    this.enableDetailedRtcStats = enableDetailedRtcStats;
  }

  /**
   * Sets mirror local index.
   *
   * @param mirrorLocalIndex the mirror local index
   */
  public void setMirrorLocalIndex(int mirrorLocalIndex) {
    this.mirrorLocalIndex = mirrorLocalIndex;
  }

  /**
   * Sets mirror remote index.
   *
   * @param mirrorRemoteIndex the mirror remote index
   */
  public void setMirrorRemoteIndex(int mirrorRemoteIndex) {
    this.mirrorRemoteIndex = mirrorRemoteIndex;
  }

  /**
   * Sets mirror encode index.
   *
   * @param mirrorEncodeIndex the mirror encode index
   */
  public void setMirrorEncodeIndex(int mirrorEncodeIndex) {
    this.mirrorEncodeIndex = mirrorEncodeIndex;
  }

  /**
   * Sets ar enabled.
   *
   * @param arEnabled the ar enabled
   */
  public void setArEnabled(boolean arEnabled) {
    this.arEnabled = arEnabled;
  }

  /**
   * Sets download required.
   *
   * @param arFiltersDownloadRequired whether download of AR filters required
   */
  public void setArFiltersDownloadRequired(boolean arFiltersDownloadRequired) {
    FiltersConfig.setDownloadRequired(arFiltersDownloadRequired);
  }

  /**
   * Gets ar enabled.
   *
   * @return the ar enabled
   */
  public boolean getAREnabled() {
    return arEnabled;
  }

  /**
   * Gets ar filters key.
   *
   * @return the ar filters key
   */
  public String getARFiltersKey() {
    return arFiltersKey;
  }

  /**
   * Sets cache busting.
   *
   * @param cacheBusting the cache busting
   */
  public void setCacheBusting(boolean cacheBusting) {
    this.cacheBusting = cacheBusting;
  }

  /**
   * Sets log verbosity.
   *
   * @param logVerbosity the log verbosity
   */
  public void setLogVerbosity(IMLogVerbosity logVerbosity) {
    this.logVerbosity = logVerbosity;
  }

  /**
   * Sets connect timeout.
   *
   * @param connectTimeout the connect timeout
   */
  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  /**
   * Sets request timeout.
   *
   * @param requestTimeout the request timeout
   */
  public void setRequestTimeout(int requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  /**
   * Sets realtime events verbosity.
   *
   * @param realtimeEventsVerbosity the realtime events verbosity
   */
  public void setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity realtimeEventsVerbosity) {
    this.realtimeEventsVerbosity = realtimeEventsVerbosity;
  }

  /**
   * Validate configuration isometrik error.
   *
   * @return the isometrik error
   */
  IsometrikError validateConfiguration() {

    if (accountId == null || accountId.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_ACCOUNTID_MISSING;
    } else if (projectId == null || projectId.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_PROJECTID_MISSING;
    } else if (keysetId == null || keysetId.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_KEYSETID_MISSING;
    } else if (licenseKey == null || licenseKey.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
    } else {
      return null;
    }
  }

  /**
   * Validate connection configuration isometrik error.
   *
   * @return the isometrik error
   */
  IsometrikError validateConnectionConfiguration() {

    IsometrikError isometrikError = validateConfiguration();

    if (isometrikError == null) {
      if (clientId == null || clientId.isEmpty()) {

        return IsometrikErrorBuilder.IMERROBJ_CLIENT_ID_MISSING;
      } else {
        return null;
      }
    } else {
      return isometrikError;
    }
  }
}
