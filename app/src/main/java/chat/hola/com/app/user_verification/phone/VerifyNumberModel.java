package chat.hola.com.app.user_verification.phone;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.notification.UserIdUpdateHandler;

/**
 * <h1>VerifyNumberModel</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 22 August 2019
 */
public class VerifyNumberModel {

    @Inject
    SessionManager sessionManager;

    @Inject
    VerifyNumberModel() {
    }

    boolean setData(Login.LoginResponse response) {

        try {
            CouchDbController db = AppController.getInstance().getDbController();

            Map<String, Object> map = new HashMap<>();
            map.put("userImageUrl", response.getProfilePic());
            map.put("userName", response.getUserName());
            map.put("firstName", response.getFirstName());
            map.put("lastName", response.getLastName());
            map.put("userId", response.getUserId());
            map.put("private", response.get_private());
            map.put("socialStatus", "");
            map.put("userIdentifier", response.getUserName());
            map.put("apiToken", response.getToken());

            AppController.getInstance()
                    .getSharedPreferences()
                    .edit()
                    .putString("token", response.getToken())
                    .apply();
            map.put("userLoginType", 1);
            map.put("excludedFilterIds", new ArrayList<Integer>());
            if (!db.checkUserDocExists(AppController.getInstance().getIndexDocId(),
                    response.getUserId())) {
                String userDocId = db.createUserInformationDocument(map);
                db.addToIndexDocument(AppController.getInstance().getIndexDocId(), response.getUserId(),
                        userDocId);
            } else {
                db.updateUserDetails(
                        db.getUserDocId(response.getUserId(), AppController.getInstance().getIndexDocId()),
                        map);
            }

            db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(),
                    response.getUserId(), 1, true);
            String name =response.getFirstName();

            if(response.getLastName()!=null&&!response.getLastName().isEmpty()){
                name= name+" "+response.getLastName();
            }
            AppController.getInstance()
                .setSignedIn(true, response.getUserId(),
                    name, response.getUserName() ,
                    response.getProfilePic(), 1, response.getAccountId(), response.getProjectId(),
                    response.getKeysetId(), response.getLicenseKey(), response.getRtcAppId(),
                    response.getArFiltersAppId(),response.getGroupCallStreamId());
            AppController.getInstance().setSignStatusChanged(true);

            String topic = "/topics/" + response.getUserId();
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            OneSignal.setExternalUserId(response.getOneSignalId(), new UserIdUpdateHandler());

            String countryCode = response.getCountryCode();
            String phone = response.getPhoneNumber();

            sessionManager.setUserName(response.getUserName());
            sessionManager.setFirstName(response.getFirstName());
            sessionManager.setLastsName(response.getLastName());
            //        sessionManager.setFacebookAccessToken(AccessToken.getCurrentAccessToken());
            sessionManager.setUserProfilePic(response.getProfilePic(), true);
            sessionManager.setQrCode(response.getQrCode());
            sessionManager.setRefreshToken(response.getRefreshToken());
//            LiveStream stream = response.getStream();

            //        sessionManager.setFcmTopic(stream.getFcmTopic());
            sessionManager.setUserId(response.getUserId());
            sessionManager.setEmail(response.getEmail());
            sessionManager.setCountryCode(countryCode);
            sessionManager.setMobileNumber(phone);

            sessionManager.setCurrencySymbol(response.getCurrency().getSymbol());
            sessionManager.setCurrency(response.getCurrency().getCurrency());
            sessionManager.setCountryName(response.getCountry());
            AppController.getInstance().setProfileSaved(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}