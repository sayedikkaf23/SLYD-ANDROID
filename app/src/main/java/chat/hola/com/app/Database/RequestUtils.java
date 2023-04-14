package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */



import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.CredentialAuthorizer;
import com.couchbase.lite.util.Log;
import com.couchbase.lite.util.URIUtils;

import java.net.URL;

import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


class RequestUtils {
   public final static String TAG = Log.TAG_SYNC;

   /*package*/
   static Request.Builder preemptivelySetAuthCredentials(
           Request.Builder builder, URL url, Authenticator authenticator) {
       boolean isUrlBased = false;
       String userInfo = url.getUserInfo();
       if (userInfo != null) {
           isUrlBased = true;
       } else {
           if (authenticator != null && authenticator instanceof CredentialAuthorizer) {
               userInfo = ((CredentialAuthorizer) authenticator).authUserInfo();
           }
       }
       return preemptivelySetAuthCredentials(builder, userInfo, isUrlBased);
   }

   // Handling authentication
   // https://github.com/square/okhttp/wiki/Recipes#handling-authentication
   /*package*/
   private  static Request.Builder preemptivelySetAuthCredentials(
           Request.Builder builder, String userInfo, boolean isUrlBased) {

       if (userInfo == null)
           return builder;

       if (!userInfo.contains(":") || ":".equals(userInfo.trim())) {
           Log.w(TAG, "RemoteRequest Unable to parse user info, not setting credentials");
           return builder;
       }

       String[] userInfoElements = userInfo.split(":");
       String username = isUrlBased ? URIUtils.decode(userInfoElements[0]) : userInfoElements[0];
       String password = "";
       if (userInfoElements.length >= 2)
           password = isUrlBased ? URIUtils.decode(userInfoElements[1]) : userInfoElements[1];
       String credential = Credentials.basic(username, password);
       return builder.addHeader("Authorization", credential);
   }

   static void closeResponseBody(Response response) {
       if (response != null) {
           ResponseBody body = response.body();
           if (body != null)
               body.close();
       }
   }
}