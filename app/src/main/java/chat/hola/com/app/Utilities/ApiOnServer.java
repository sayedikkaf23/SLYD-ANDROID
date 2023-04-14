package chat.hola.com.app.Utilities;

import com.amazonaws.regions.Regions;
import com.ezcall.android.BuildConfig;

/**
 * <h1>ApiOnServer</h1>
 *
 * <p>Contains server folder names, posts, ip address and constants</p>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 20/06/17.
 */
public class ApiOnServer {
    //folders
    public static final boolean DEFAULT_APPRTC_USED = true;
    public static final String APP_NAME = "ezcall";
    public static final String CHAT_RECEIVED_THUMBNAILS_FOLDER = "/" + APP_NAME + "/receivedThumbnails";
    public static final String CHAT_UPLOAD_THUMBNAILS_FOLDER = "/" + APP_NAME + "/upload";
    public static final String MQTT_SERVICE_PATH = "chat.hola.com.app.MQtt.MqttService";
    public static final String MQTT_SERVICE_PATH_LIVE = "chat.hola.com.app.live_stream.MQtt.MqttService";
    public static final String CHAT_DOODLES_FOLDER = "/" + APP_NAME + "/doodles";
    public static final String CHAT_DOWNLOADS_FOLDER = "/" + APP_NAME + "/";
    public static final String IMAGE_CAPTURE_URI = "/" + APP_NAME;

    public static final String HOST_API = BuildConfig.HOST_API;
    public static final String HOST_API_MQTT = BuildConfig.HOST_API_MQTT;
    public static final String TRENDING_HOST_API = BuildConfig.TRENDING_HOST_API;
    public static final String LIVE_STREAM_BASE = BuildConfig.LIVE_STREAM_BASE;
    public static final String COIN_WALLET = BuildConfig.LIVE_STREAM_BASE;
    public static final String TYPE = "https://";

    public static final String MQTT_PORT = BuildConfig.MQTT_PORT;

    //0-RTMP Streaming,1-WebRTC Streaming
    public static final int STREAMING_TYPE = 1;
    public static final String WEBRTC_BASE_URL = BuildConfig.WEBRTC_BASE_URL;

    public static final String CHAT_MULTER_UPLOAD_URL = BuildConfig.CHAT_UPLOAD_URL;
    public static final String RTMP_BASE_URL = "";
    public static final String LIVE_MQTT_PORT = BuildConfig.MQTT_PORT;
    public static final boolean ALLOW_DEFAULT_OTP = true;

    private static final String CHAT_UPLOAD_SERVER_URL = BuildConfig.CHAT_DOWNLOAD_URL;
    public static final String CHAT_UPLOAD_PATH = CHAT_UPLOAD_SERVER_URL + "chatDoc/";
    public static final String PROFILEPIC_UPLOAD_PATH = CHAT_UPLOAD_SERVER_URL + "chatDoc/";
    public static final String DELETE_DOWNLOAD = CHAT_MULTER_UPLOAD_URL + "deleteImage";

    public static final String TRENDING_STICKERS = "http://api.giphy.com/v1/stickers/trending?api_key=" + Constants.GIPHY;
    public static final String TRENDING_GIFS = "http://api.giphy.com/v1/gifs/trending?api_key=" + Constants.GIPHY;
    public static final String GIPHY_APIKEY = "&api_key=" + Constants.GIPHY;
    public static final String SEARCH_STICKERS = "http://api.giphy.com/v1/stickers/search?q=";
    public static final String SEARCH_GIFS = "http://api.giphy.com/v1/gifs/search?q=";

    // sup specific apis
    public static final String SUP_API_MAIN_LINK = TYPE + HOST_API;
    public static final String FRIEND_API = SUP_API_MAIN_LINK + "friend";
    public static final String VERIFY_OTP_EMAIL = SUP_API_MAIN_LINK + "verifyOTPByEmail";
    public static final String REQUEST_OTP = SUP_API_MAIN_LINK + "RequestOTP";
    public static final String REQUEST_OTP_BUSINESS = SUP_API_MAIN_LINK + "businessPhoneVerification";
    public static final String REQUEST_OTP_EDIT = SUP_API_MAIN_LINK + "profile/emailPhoneSend";
    public static final String VERIFY_REFERRAL_CODE = SUP_API_MAIN_LINK + "referralCode";
    public static final String VERIFY_OTP = SUP_API_MAIN_LINK + "verifyOtp";
    public static final String VERIFY_EMAIL = SUP_API_MAIN_LINK + "verifyEmail";

    public static final String REQUEST_OTP_EMAIL = SUP_API_MAIN_LINK + "requestEmailVerification";
    public static final String REQUEST_OTP_EMAIL_BUSINES = SUP_API_MAIN_LINK + "businessEmailVerification";
    public static final String BUSINESS_EMAIL_OTP_VERIFY = SUP_API_MAIN_LINK + "businessEmailOtpVerify";
    public static final String VERIFY_OTP_PHONE_BUSINESS = SUP_API_MAIN_LINK + "business/emailPhoneVerify";
    public static final String VERIFY_OTP_PHONE = SUP_API_MAIN_LINK + "profile/emailPhoneVerify";
    public static final String USER_PROFILE = SUP_API_MAIN_LINK + "profile"/*"User/Profile"*/;
    public static final String OTHER_USER_PROFILE = SUP_API_MAIN_LINK + "profile/member"/*"User/Profile"*/;
    public static final String NEW_TOKEN = SUP_API_MAIN_LINK + "accessToken";
    public static final String WALLET_CHECK = SUP_API_MAIN_LINK + "wallet/check";
    public static final String KYC_CHECK = SUP_API_MAIN_LINK + "verificationStatus";

    // To get the opponent Profile details
    public static final String OPPONENT_PROFILE = SUP_API_MAIN_LINK + "Participant/Profile";
    public static final String USER_STATUS = SUP_API_MAIN_LINK + "User/SocialStatus";
    public static final String SYNC_CONTACTS = ""; //SUP_API_MAIN_LINK + "User/Contacts";
    public static final String GET_FRIENDS = SUP_API_MAIN_LINK + "friend";
    public static final String GET_FOLLWERS_FOLLOWEE = SUP_API_MAIN_LINK + "followersFollowee";
    public static final String FETCH_CHATS = SUP_API_MAIN_LINK + "Chats";

    // fetch the message into the list
    public static final String FETCH_MESSAGES = SUP_API_MAIN_LINK + "Messages";

    // terms and condition url
    public static final String TERMS = "https://configfile.ezcall.app/termsAndConditions.html";

    // call logs
//    public static final String CALLS_LOGS = SUP_API_MAIN_LINK + "CallLogs";
    public static final String CALLS_LOGS = SUP_API_MAIN_LINK + "CallLogs";

    // group chat
    public static final String CREATE_GROUP = SUP_API_MAIN_LINK + "GroupChat";
    public static final String GROUP_MEMBER = SUP_API_MAIN_LINK + "GroupChat/Member";
    public static final String GROUP_BY_MEMBER = SUP_API_MAIN_LINK + "GroupChat/ByMember";
    public static final String DELETE_GROUP = SUP_API_MAIN_LINK + "GroupChat";
    public static final String FETCH_GROUP_MESSAGES = SUP_API_MAIN_LINK + "GroupChat/Messages";
    public static final String FETCH_GROUP_ACKS = SUP_API_MAIN_LINK + "GroupChat";

    //Block user functionality
    public static final String BLOCK_USER = SUP_API_MAIN_LINK + "User/Block";
    public static final String TRANSFER_RESPONSE = SUP_API_MAIN_LINK + "ReceiversResponseToTransfer";
    public static final String TRANSFER_CANCEL = SUP_API_MAIN_LINK + "cancelTransfer";

    public static final String CALLING_BASE = BuildConfig.CALLING_BASE;
    public static final String GET_PARTICIPANT = CALLING_BASE + "call";
    public static final String DISCONNECT_CALL = CALLING_BASE + "call";

    /*
     * GET Request for ICESERVERS
     */

    public static final String GET_ICE_SERVERS = SUP_API_MAIN_LINK + "iceServers";
    public static final String POST_IOS = SUP_API_MAIN_LINK + "iosPush";

    /*
    * Shaktisinh jayaram.mobifyi Hardik Bilven

I need to know that is app name so I can send notification for that

so in post iosPush api will add a new key/field you need to call this api when you signup/login

key : appName
value : doChat || dubly || picoadda
this one
    * */

    public static String BASE_URL = "s3.ap-south-1.amazonaws.com/";
    public static String BUCKET = "";
    public static String POOL_ID = "";
    public static String REGION = "";
    public static String COGNITO_ID = "";
    public static String COGNITO_TOKEN = "";

    public static String KYC_DOCUMENTS = "kyc_documents";
    public static String BUSINESS_COVER_PIC_FOLDER = "business_cover_image";
    public static String BUSINESS_PROFILE_PIC_FOLDER = "business_profile_image";
    public static String PROFILE_PIC_FOLDER = "profile_image";
    public static String COVER_PIC_FOLDER = "background_image";
    public static String THUMBNAIL = "thumbnail";
    public static String LIVESTREAM_THUMBNAIL = "streams/thumbnail";
    public static String STRIPE = "stripe";

    public static String PYTHON_CONTACT_SYNC = TYPE + BuildConfig.TRENDING_HOST_API + "contactSync/";
    public static String MAIN_LINK = "https://api.ezcall.app/v1/";
    public static String HOST_API_2 = "https://api.syld.io/v1/";
}
