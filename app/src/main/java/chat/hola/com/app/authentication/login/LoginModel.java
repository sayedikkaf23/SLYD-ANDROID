package chat.hola.com.app.authentication.login;

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

public class LoginModel {

    @Inject
    SessionManager sessionManager;
    CouchDbController db;

    @Inject
    public LoginModel() {
        db = AppController.getInstance().getDbController();
    }

    void setData(Login.LoginResponse response) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userImageUrl", response.getProfilePic() == null ? "" : response.getProfilePic());
            map.put("userName", response.getUserName());
            map.put("firstName", response.getFirstName());
            map.put("lastName", response.getLastName());
            map.put("userId", response.getUserId());
            map.put("private", response.get_private());
            map.put("socialStatus", "");
            map.put("userIdentifier", response.getUserName());
            map.put("apiToken", response.getToken());
            sessionManager.setRefreshToken(response.getRefreshToken());

            AppController.getInstance().getSharedPreferences().edit().putString("token", response.getToken()).apply();
            map.put("userLoginType", 1);
            map.put("excludedFilterIds", new ArrayList<Integer>());
            if (!db.checkUserDocExists(AppController.getInstance().getIndexDocId(), response.getUserId())) {
                String userDocId = db.createUserInformationDocument(map);
                db.addToIndexDocument(AppController.getInstance().getIndexDocId(), response.getUserId(), userDocId);

            } else {
                db.updateUserDetails(db.getUserDocId(response.getUserId(), AppController.getInstance().getIndexDocId()), map);
            }

            db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(), response.getUserId(), 1, true);
            String name =response.getFirstName();

            if(response.getLastName()!=null&&!response.getLastName().isEmpty()){
                name= name+" "+response.getLastName();
            }

            AppController.getInstance().setSignedIn(true, response.getUserId(), name, response.getUserName() ,
                response.getProfilePic(), 1, response.getAccountId(), response.getProjectId(),
                response.getKeysetId(), response.getLicenseKey(), response.getRtcAppId(),
                response.getArFiltersAppId(),response.getGroupCallStreamId());  AppController.getInstance().setSignStatusChanged(true);

            String topic = "/topics/" + response.getUserId();
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            OneSignal.setExternalUserId(response.getOneSignalId(), new UserIdUpdateHandler());

            sessionManager.setUserName(response.getUserName());
            sessionManager.setFirstName(response.getFirstName());
            sessionManager.setLastsName(response.getLastName());
//            sessionManager.setFacebookAccessToken(AccessToken.getCurrentAccessToken());
            sessionManager.setUserProfilePic(response.getProfilePic(),true);
            sessionManager.setQrCode(response.getQrCode());

//            LiveStream stream = response.getStream();
            String countryCode = response.getCountryCode();
            String phone = response.getPhoneNumber();

          //  sessionManager.setFcmTopic(stream.getFcmTopic());
            sessionManager.setUserId(response.getUserId());
            sessionManager.setEmail(response.getEmail());
            sessionManager.setCountryCode(countryCode);
            sessionManager.setMobileNumber(phone.replace(countryCode, ""));

            sessionManager.setCurrencySymbol(response.getCurrency().getSymbol());
            sessionManager.setCurrency(response.getCurrency().getCurrency());
            sessionManager.setCountryName(response.getCountry());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
