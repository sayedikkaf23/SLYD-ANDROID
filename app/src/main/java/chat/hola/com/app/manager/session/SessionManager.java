package chat.hola.com.app.manager.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.widget.Toast;

import chat.hola.com.app.Activities.MainActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Utilities.Constants;

import com.couchbase.lite.android.AndroidContext;
import com.ezcall.android.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import java.util.Objects;

import javax.inject.Inject;

/**
 * <h1>SessionManager</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 2/28/2018.
 */

public class SessionManager {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean isLogout = false;
    private boolean isDelete = false;
    private String cameraCall;

    @SuppressLint("CommitPrefEdits")
    @Inject
    public SessionManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("sessionManager", Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    public String getUserName() {
        return preferences.getString("userName", "");
    }

    public void setUserName(String username) {
        editor.putString("userName", username);
        editor.apply();
    }

    public String getFirstName() {
        return preferences.getString("firstName", "");
    }

    public void setFirstName(String firstName) {
        editor.putString("firstName", firstName);
        editor.apply();
    }

    public String getLastName() {
        return preferences.getString("lastsName", "");
    }

    public void setLastsName(String lastsName) {
        editor.putString("lastsName", lastsName);
        editor.apply();
    }

    public String getUserProfilePic() {
        return preferences.getString("userProfilePic", Constants.PROFILE_PIC_SHAPE);
    }

    public void setUserProfilePic(String url, boolean newImage) {
        AppController.getInstance().setUserImageUrl(url);
        editor.putString("userProfilePic", url);
        editor.apply();
        if (newImage)
            setUserProfilePicUpdateTime();
    }

    public String getUserProfilePicUpdateTime() {

        return preferences.getString("userProfilePicUpdateTime", "0");
    }

    public void setUserProfilePicUpdateTime() {

        editor.putString("userProfilePicUpdateTime", String.valueOf(System.currentTimeMillis()));

        editor.apply();
    }

    public String getAdresses() {
        return preferences.getString("adresses", "");
    }

    public void setAdresses(String adresses) {
        editor.putString("adresses", adresses);
        editor.apply();
    }

    public void clear() {
        editor.clear();
    }

    public void logOut(Context context) {
        isLogout = true;
        sessionExpired(context);
    }

    public void delete(Context context) {
        isDelete = true;
        sessionExpired(context);
    }

    /*
     * Bug title:  we are able to get the call notifications for users even after we are logged out
     * Bug Id: #1699
     * Fix Desc: removeExternalUserId
     * Fix dev: hardik
     * Fix date: 24/5/21
     * */
    public void sessionExpired(Context context) {
        OneSignal.unsubscribeWhenNotificationsAreDisabled(false);
        OneSignal.removeExternalUserId();

        String topic = "/topics/" + AppController.getInstance().getUserId();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        clear();
        AppController.getInstance().setSignedIn(false);
        AppController.getInstance().setContactSynced(false);
        AppController.getInstance().unregisterContactsObserver();
        AppController.getInstance().logOutAndDisconnectMqtt();
        AppController.getInstance().setChatSynced(false);
        setIsStar(false);

        editor.putBoolean("isFacebookLogin", false);
        editor.apply();

        CouchDbController db = new CouchDbController(new AndroidContext(context));
        db.updateIndexDocumentOnSignOut(AppController.getInstance().getIndexDocId());

        //        AccountManager mAccountManager = AccountManager.get(context);
        //        Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        //        if (accounts.length > 0)
        //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        //                mAccountManager.removeAccountExplicitly(accounts[0]);

        String errorText="";
        if (isDelete) {
            errorText = context.getString(R.string.user_deleted);
        } else if (isLogout) {
            errorText = context.getString(R.string.logged_out);
        } else {
            errorText = context.getString(R.string.session_expired);
        }
        Toast.makeText(context, errorText,
                Toast.LENGTH_SHORT).show();

        Intent i1 = new Intent(context, MainActivity.class);
        i1.setAction(Intent.ACTION_MAIN);
        i1.addCategory(Intent.CATEGORY_HOME);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i1.putExtra("caller", "PostActivity");
        context.startActivity(i1);
    }

    public void sessionExpiredFCM(Context context) {
        OneSignal.removeExternalUserId();
        String topic = "/topics/" + AppController.getInstance().getUserId();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        clear();
        AppController.getInstance().setSignedIn(false);
        AppController.getInstance().setContactSynced(false);
        AppController.getInstance().unregisterContactsObserver();
        AppController.getInstance().logOutAndDisconnectMqtt();
        AppController.getInstance().setChatSynced(false);
        setIsStar(false);

        editor.putBoolean("isFacebookLogin", false);
        editor.apply();

        CouchDbController db = new CouchDbController(new AndroidContext(context));
        db.updateIndexDocumentOnSignOut(AppController.getInstance().getIndexDocId());

        //        AccountManager mAccountManager = AccountManager.get(context);
        //        Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        //        if (accounts.length > 0)
        //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        //                mAccountManager.removeAccountExplicitly(accounts[0]);


        if (AppController.getInstance().isForeground()) {

            Intent i1 = new Intent(context, MainActivity.class);
            i1.setAction(Intent.ACTION_MAIN);
            i1.addCategory(Intent.CATEGORY_HOME);
            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i1.putExtra("caller", "PostActivity");
            context.startActivity(i1);
        }
    }

    public boolean isHomeChanged() {
        return preferences.getBoolean("homedata", false);
    }

    public void setHomeChange(boolean b) {
        editor.putBoolean("homedata", b);
        editor.apply();
    }

    public String getCommentCount() {
        return preferences.getString("comments", "0");
    }

    public void setCommentCount(String count) {
        editor.putString("comments", count);
        editor.apply();
    }

    public void setFollowAll(boolean b) {
        editor.putBoolean("isFollowAll", b);
        editor.apply();
    }

    public boolean isFollowAll() {
        return preferences.getBoolean("isFollowAll", false);
    }

    public String getCameraCall() {
        return preferences.getString("cameraCall", cameraCall);
    }

    public void setCameraCall(String cameraCall) {
        editor.putString("cameraCall", cameraCall);
        editor.apply();
    }

    public void setHomeTab(int index) {
        editor.putInt("homeTab", index);
        editor.apply();
    }

    public int getHomeTab() {
        return preferences.getInt("homeTab", 0);
    }

    public void setIsStar(boolean isStar) {
        editor.putBoolean("isStar", isStar).apply();
    }

    public boolean isStar() {
        return preferences.getBoolean("isStar", false);
    }

    public void setCurrentLocation(Location location) {
        editor.putString("latitude", String.valueOf(location.getLatitude())).apply();
        editor.putString("longitude", String.valueOf(location.getLongitude())).apply();
    }

    public Double getLatitude() {
        return Double.parseDouble(Objects.requireNonNull(preferences.getString("latitude", "0")));
    }

    public Double getLongitude() {
        return Double.parseDouble(Objects.requireNonNull(preferences.getString("longitude", "0")));
    }

    public void setCountry(String countryName) {
        editor.putString("country", countryName).apply();
    }

    public String getCountry() {
        return preferences.getString("country", "");
    }

    public void setCountryName(String countryName) {
        editor.putString("countryName", countryName).apply();
    }

    public String getCountryName() {
        return preferences.getString("countryName", "");
    }

    public void setCity(String city) {
        editor.putString("city", city).apply();
    }

    public String getCity() {
        return preferences.getString("city", "");
    }

    public void setIpAdress(String iPaddress) {
        editor.putString("IpAddress", iPaddress).apply();
    }

    public String getIpAdress() {
        return preferences.getString("IpAddress", "");
    }

    public void setFcmTopic(String fcmTopic) {
        editor.putString("fcmTopic", fcmTopic).apply();
    }

    public String getFcmTopic() {
        return preferences.getString("fcmTopic", "");
    }

    public void setUserId(String userId) {
        editor.putString("userId", userId).apply();
    }

    public String getUserId() {
        return preferences.getString("userId", "");
    }

    public void setEmail(String email) {
        editor.putString("email", email).apply();
    }

    public String getEmail() {
        return preferences.getString("email", "");
    }

    public void setCountryCode(String countryCode) {
        editor.putString("countryCode", countryCode).apply();
    }

    public String getCountryCode() {
        return preferences.getString("countryCode", "");
    }

    public void setMobileNumber(String phone) {
        editor.putString("phone", phone).apply();
    }

    public String getMobileNumber() {
        return preferences.getString("phone", "");
    }

    public void setRefreshToken(String token) {
        editor.putString("refreshToken", token).apply();
    }

    public String getRefreshToken() {
        return preferences.getString("refreshToken", "");
    }

    public void setAccessToken(String token) {
        editor.putString("token", token).apply();
    }

    public String getAccessToken() {
        return preferences.getString("token", "");
    }

    public void setGuestToken(String token) {
        editor.putString("guestToken", token).apply();
    }

    public String getGuestToken() {
        return preferences.getString("guestToken", "");
    }

    public void setTokenExpired(Long token) {
        editor.putLong("accessExpireAt", token).apply();
    }

    public Long getTokenExpired() {
        return preferences.getLong("accessExpireAt", 0);
    }

    public void setWalletBalance(String balance) {
        editor.putString("walletBalance", balance).apply();
    }

    public String getWalletBalance() {
        return preferences.getString("walletBalance", "0");
    }

    public void businessProfile(boolean isBusinessProfile) {
        editor.putBoolean("isBusinessProfile", isBusinessProfile).apply();
    }

    public boolean isBusinessProfile() {
        return preferences.getBoolean("isBusinessProfile", false);
    }

    public void setBusinessProfileAvailable(boolean isBusinessProfile) {
        editor.putBoolean("businessProfileAvailable", isBusinessProfile).apply();
    }

    public boolean isBusinessProfileAvailable() {
        return preferences.getBoolean("businessProfileAvailable", false);
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        editor.putString("businessCategoryId", businessCategoryId).apply();
    }

    public String getBusinessCategoryId() {
        return preferences.getString("businessCategoryId", "");
    }

    public void setBusinessProfileApproved(boolean businessProfileApproved) {
        editor.putBoolean("businessProfileApproved", businessProfileApproved).apply();
    }

    public boolean isBusinessProfileApproved() {
        return preferences.getBoolean("businessProfileApproved", false);
    }

    public String getQrCode() {
        return preferences.getString("qrcode", "");
    }

    public void setQrCode(String currency) {
        editor.putString("qrcode", currency);
        editor.apply();
    }

    public String getCurrency() {
        return preferences.getString("currency", "");
    }

    public void setCurrency(String currency) {
        editor.putString("currency", currency);
        editor.apply();
    }

    public String getCurrencySymbol() {
        return preferences.getString("currencySymbol", "");
    }

    public void setCurrencySymbol(String currencySymbol) {
        editor.putString("currencySymbol", currencySymbol);
        editor.apply();
    }

    public String getWalletId() {
        return preferences.getString("walletId", "");
    }

    public void setWalletId(String walletId) {
        editor.putString("walletId", walletId);
        editor.apply();
    }

    public boolean isWalletAvailable() {
        return preferences.getBoolean("isWalletAvailable", false);
    }

    public void setWalletAvailable(boolean isWalletAvailable) {
        editor.putBoolean("isWalletAvailable", isWalletAvailable);
        editor.apply();
    }

    public String getCoinBalance() {
        return preferences.getString("coins", "0");
    }

    public void setCoinBalance(String coins) {
        editor.putString("coins", coins);
        editor.apply();
    }

    public String getCoinWalletId() {
        return preferences.getString("coinWalletId", "");
    }

    public void setCoinWalletId(String walletId) {
        editor.putString("coinWalletId", walletId);
        editor.apply();
    }

    public boolean isAskAutoStart() {
        return preferences.getBoolean("askAutoStart", false);
    }

    public void setAskAutoStart(boolean askAutoStart) {
        editor.putBoolean("askAutoStart", askAutoStart);
        editor.apply();
    }

    /**
     * Isometrik Group streaming
     */

    public void setIsometrikConfigurationKeys(String accountId, String projectId, String keysetId,
                                              String licenseKey, String rtcAppId, String arFiltersAppId, String isometrikUserId) {
        editor.putString("accountId", accountId);
        editor.putString("projectId", projectId);
        editor.putString("keysetId", keysetId);
        editor.putString("licenseKey", licenseKey);
        editor.putString("rtcAppId", rtcAppId);
        editor.putString("arFiltersAppId", arFiltersAppId);
        editor.putString("isometrikUserId", isometrikUserId);
        editor.apply();
    }

    public String getAccountId() {
        return preferences.getString("accountId", null);
    }

    public String getProjectId() {
        return preferences.getString("projectId", null);
    }

    public String getKeysetId() {
        return preferences.getString("keysetId", null);
    }

    public String getLicenseKey() {
        return preferences.getString("licenseKey", null);
    }

    public String getRtcAppId() {
        return preferences.getString("rtcAppId", null);
    }

    public String getArFiltersAppId() {
        return preferences.getString("arFiltersAppId", null);
    }

    public String getIsometrikUserId() {
        return preferences.getString("isometrikUserId", null);
    }

    public boolean isBackgroundRestrictAsk() {
        return preferences.getBoolean("askBackgroundRestriction", false);
    }

    public void setBackgroundRestrictAsk(boolean askAutoStart) {
        editor.putBoolean("askBackgroundRestriction", askAutoStart);
        editor.apply();
    }

    public boolean appliedBusinessProfile() {
        return preferences.getBoolean("appliedBusinessProfile", false);
    }

    public void setAppliedBusinessProfile(boolean appliedBusinessProfile) {
        editor.putBoolean("appliedBusinessProfile", appliedBusinessProfile);
        editor.apply();
    }

    public void setBusinessUniqueId(String businessUniqueId) {
        editor.putString("businessUniqueId", businessUniqueId).apply();
    }

    public String getBusinessUniqueId() {
        return preferences.getString("businessUniqueId", "");
    }

    public String getPgId() {
        return preferences.getString("pgId", "");
    }

    public void setPgId(String pgId) {
        editor.putString("pgId", pgId);
        editor.apply();
    }

    public String getPgName() {
        return preferences.getString("pgName", "");
    }

    public void setPgName(String pgName) {
        editor.putString("pgName", pgName);
        editor.apply();
    }
}
