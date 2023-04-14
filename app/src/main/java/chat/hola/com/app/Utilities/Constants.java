package chat.hola.com.app.Utilities;

import android.annotation.SuppressLint;

import com.amazonaws.regions.Regions;
import com.ezcall.android.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author 3Embed
 * @since 2/26/2018.
 */

public interface Constants {

    String EMPTY = "";
    @SuppressLint("ConstantLocale")
    String AUTH = Utilities.getThings();
    String LANGUAGE = Locale.getDefault().getLanguage();
    String TYPE = "type";
    String PATH = "path";
    public static final String PACKAGE_ID = "com.ezcall.android";
    public static final String BOX_COUNT = "boxCount";
    public static final String INVALID_DATE = "Invalid date";

    String DEFAULT_PROFILE_PIC_LINK = "https://res.cloudinary.com/dafszph29/image/upload/c_thumb,w_200,g_face/v1551361911/default/profile_pic.png";//"https://res.cloudinary.com/dqodmo1yc/image/upload/c_thumb,w_200,g_face/v1536126847/default/profile_one.png";
    String IMAGE_QUALITY = "upload/q_80/";
    String PROFILE_PIC_SHAPE = "upload/w_90,h_90,c_thumb,g_face,r_max/";
    int PAGE_SIZE = 20;
    String GIPHY = "fEqiwbWjGephFBkZ5giLZ9IKKrSmrHP8";
    String BUSINESS = "business";
    String STAR = "star";
    CharSequence IMAGE_EXT = ".webp";
    String MP4 = "mp4";
    String APP_TYPE = "user";
    String CURRENCY_CODE = "566";
    String CURRENCY = "USD";
    int SCAN_QR_CODE = 109;
    String FROM = "from";
    String WALLET = "wallet";
    String URL = "url";
    String TITLE = "title";
    String URL_DATA = "url_data";
    String CURRENCY_SYMBOL = "currencySymbol";
    String USER_ID = "userId";
    String DASHBOARD = "dashboard";
    String CALL = "call";
    int AUTOCOMPLETE_REQUEST_CODE = 13432;
    String GUEST_USER = "is_guest_user";
    int AD_IMPRESS_POSITION = 5;
    int FORWARD_MESSAGES_REQ = 771;
    String PAYMENT_GATEWAY = "PLAYSTORE";
    String DYNAMIC_LINK = "https://ezcallapp.page.link";
    String DYNAMIC_LINK_PROFILE = "userprofile";
    String STRIPE_ID = "608fc3dcad6beeb8a78d03e8";
    String STRIPE = "STRIPE";
    String BUSINESS_PROFILE = "business_profile";
    String BUSINESS_COVER = "business_cover";
    String KEY = "key";
    String VALUE = "value";
    int REQ_FORGOT_PASSWORD_EMAIL = 3001;
    //    String WALLET_CURRENCY = "INR";
    String WALLET_COIN = "COIN";
    String USER = "user";
    int EDIT_PROFILE_REQ = 6003;
    String EDIT_PROFILE = "edit_profile";

    interface AmazonS3 {

        String KYC_DOCUMENTS = "kyc_documents";
        String BUSINESS_COVER_PIC_FOLDER = "business_cover_image";
        String BUSINESS_PROFILE_PIC_FOLDER = "business_profile_image";
        String PROFILE_PIC_FOLDER = "profile_image";
        String COVER_PIC_FOLDER = "background_image";
        String THUMBNAIL = "thumbnail";
        String LIVESTREAM_THUMBNAIL = "streams/thumbnail";

        String BASE_URL = "https://s3.ap-south-1.amazonaws.com";
        String BUCKET = "dubly";
        String POOL_ID = "ap-south-1:47ac2716-7f91-4772-a371-d3eadfb589f3";
        Regions REGION = Regions.AP_SOUTH_1;

        String STRIPE = "stripe";
    }

    interface Business {
        String BUSINESS_UNIQUE_ID = "businessUniqueId";
    }

    interface Profile {
        int PROFILE_PIC_SIZE= 100;

        int WIDTH = 400;
        int HEIGHT = 150;
    }

    interface Cloudinary {
        String app_name = "ezcall/";
        String posts = app_name + "posts";
        String profile_image = app_name + "profile_image";
        String cover_image = app_name + "cover_image";
        String qr_code = app_name + "qr_code";
        String livestream = app_name + "livestream";
        String stories = app_name + "stories";
        String channel_image = app_name + "channel_image";
        String others = app_name + "others";
    }

    interface CallAndroid10 {
        int notification_id = 10;
    }

    /*This is used to API request to backend server for get ice servers list and cred's
     * if you using below static cred's(WebRTC) then make it false*/
    boolean requestIceServer = true;

    interface WebRTC {
        String turn80udp = "turn:bturn2.xirsys.com:80?transport=udp";
        String turn3478udp = "turn:bturn2.xirsys.com:3478?transport=udp";
        String turn80tcp = "turn:bturn2.xirsys.com:80?transport=tcp";
        String turn3478tcp = "turn:bturn2.xirsys.com:3478?transport=tcp";
        String turns443tcp = "turns:bturn2.xirsys.com:443?transport=tcp";
        String turns5349tcp = "turns:bturn2.xirsys.com:5349?transport=tcp";

        String userName = "AjxdMUu_pSHgQBzBgc4jTOMcV1Z-_I1ozjQIoggBpNBBToJdD8ERSrluHqLuVR8hAAAAAF5DnDlkdWJseQ==";
        String password = "95331758-4d61-11ea-bd26-9646de0e6ccd";

        String stun = "stun:bturn2.xirsys.com";
    }

    interface SocialFragment {
        String USERID = "userId";
        String DATA = "data";
    }

    interface Verification {
        int EMAIL_VERIFICATION_REQ = 554;
        int NUMBER_VERIFICATION_REQ = 552;
    }

    interface Camera {
        int FILE_SIZE = 40;
    }

    interface Mqtt {
        String EVENT_NAME = "eventName";
    }

    interface DiscoverContact {
        String CONTACT_IDENTIFIRE = "contactIdentifier";
        String CONTACT_PICTURE = "contactPicUrl";
        String CONTACT_NAME = "contactName";
        String CONTACTS = "contacts";
        String PRIVATE = "private";
        String LOCAL_NUMBER = "localNumber";
        String USER_NAME = "userName";
        String ID = "_id";
        String NUMBER = "number";
        String PROFILE_PICTURE = "profilePic";
        String SOCIAL_STATUS = "socialStatus";
        String FOLLOWING = "followStatus";
        String PROFILE_PICTURE_URL = "contactPicUrl";
        String CONTACT_ID = "contactId";
        String TYPE = "type";
        String CONTACT_LOCAL_NUMBER = "contactLocalNumber";
        String CONTACT_LOCAL_ID = "contactLocalId";
        String CONTACT_UID = "contactUid";
        String CONTACT_TYPE = "contactType";
        String CONTACT_STATUS = "contactStatus";
        String PHONE_NUMBER = "phoneNumber";
        String FRIEND_STATUS_CODE = "friendStatusCode";
    }

    interface Google {
        String GOOGLE_API_KEY = "AIzaSyA5B6FVQmeaOFa8EJZjdoW_w3wdxj8c25M";
    }

    interface Post {
        String PATH_FILE = "file://";
        String PATH = "path";
        String TYPE = "type";
        String IMAGE = "image";
        String VIDEO = "video";

        String RESOURCE_TYPE = "resource_type";
        String URL = "url";
        String PUBLIC_ID = "public_id";
        String FOLDER = "folder";
        String LIVE_STREAM = "live_stream";
    }

    interface Transaction {

        String TRIGGER = "trigger";
        String TRANSACTION_ID = "transactionId";
        String NAME = "name";
        String FROM = "from";
        String TO = "to";
        String TRANSACTION_DATE = "transactionDate";
        String IMAGE = "image";
        String AMOUNT = "amount";
        String NOTE = "note";
        String FEE = "fee";
        String DETAIL = "transactionDetail";
        String MODE = "rechargeMode";
        String ACC_NO = "accNo";
        String BANK = "bank";

        String TRANSFER = "transfer";
        String PAYMENT = "payment";
        String TO_AMOUNT = "to_amount";
        String COMMISSION = "commission";

        interface Type {
            int ALL = 0;
            int CREDIT = 1;
            int DEBIT = 2;
            int PENDING = 3;
        }

        interface WithdrawStatus {
            String NEW = "NEW";
            String APPROVED = "APPROVED";
            String REJECTED = "REJECTED";
            String FAILED = "FAILED";
            String CANCELED = "CANCELED";
            String TRANSFERRED = "TRANSFERD";
        }

        interface Trigger {
            String TRANSFER = "transfer";
            String PAYMENT = "payment";
        }
    }

    interface Image {
        int LOAD_FRONT_IMAGE = 1186;
        int CAPTURE_FRONT_IMAGE = 1187;
        int LOAD_BACK_IMAGE = 1188;
        int CAPTURE_BACK_IMAGE = 1189;
        int CAPTURE_IMAGE = 1190;
        int LOAD_IMAGE = 1191;
    }

    interface PostUpdate {
        String HOME_FRAGMENT = "home_fragment";
        String POPULAR_FRAGMENT = "popular_fragment";
        String SOCIAL_DETAIL = "social_detail_activity";
    }

    interface LoginType {
        String GOOGLE = "google";
        String FACEBOOK = "facebook";
        String NORMAL = "normal";
    }

    public static final int ZERO = 0;
    public static final int ORDER_CANCELLED = 3;
    public static final int ORDER_PENDING_CONFIRMATION = 9;

    public static final String SUB_CAT_DATA = "subCat";
    public static final Boolean FALSE = false;
    public static final Boolean TRUE = true;
    public static final int NONE = -1;
    public static final int CANCEL = 41;
    public static final int RIDE_ADDRESS_REQUEST = 1001;
    public static final String ACTION = "action";
    public static final String ADDRESS = "address";
    public static final String IS_DEFAULT = "isDefault";
    public static final int EDIT_TYPE = 1;
    public static final String ADDRESS_LIST_DATA = "addressListData";
    public static final String TOTAL_STORE_COUNT = "totalStoreCount";
    public static final String SELECTED_PRESCRIPTION = "selectedPrescriptions";
    public static final String WHICH_DATE_SELECTED = "whichDateSelected";
    public static final String WHICH_SLOT_SELECTED = "whichSlotSelected";
    public static final String IS_SLOTS_AVAILABLE = "IsSlotsAvailable";
    public static final String IS_FROM_CART = "isFromCart";
    public static final String PROFILE_SCREEN = "profileScreen";
    public static final String CART_AMT = "cartAmt";
    public static final String CART_ID = "cartId";
    public static final String ESTIMATED_PRODUCT_PRICE = "estimatePrice";
    public static final String CART_ITEM = "cartItem";
    public static final String STORE_COUNT = "storeCount";
    public static final String DELIVERY_DISPLAY_TEXT = "DeliveryDisplayText";
    public static final String IS_DELIVER_NOW = "IsDeliveryNow";
    public static final String REQUESTED_TIME_PICKUP = "requestedTimePickup";
    public static final String REQUESTED_TIME = "requestedTime";
    public static final String PICKUP_SLOT_ID = "pickupSlotId";
    public static final String DELIVERY_SLOT_ID = "deliverySlotId";
    public static final String CART_SCREEN = "cartScreen";
    public static final String ORDER_SCREEN = "orderScreen";
    public static final String ADDRESS_SCREEN = "addressScreen";
    public static final String DELIVERY_SCREEN = "deliveryScreen";
    public static final String PRESCRIPTION_SCREEN = "prescriptionScreen";
    public static final String RIDE_ADDRESS_SELECTION = "ride_address_selection";
    public static final int ADDRESS_HOME_TYPE = 1;
    public static final int ADDRESS_WORK_TYPE = 2;
    public static final int ADDRESS_OTHER_TYPE = 3;
    public static final int ZOOM_RANGE = 16;
    public static final int REQUEST_FOR_LOCATION = 111;
    public static final int ADDRESS_REQUEST = 100;
    public static final String LAT = "lat";
    public static final String LONG = "langitude";
    public static final String INDIA_PHONE_CODE = "+91";
    public static final int TEN = 10;
    public static final int FIVE = 5;
    public static final int FOUR = 4;
    public static final int THREE = 3;
    public static final int ADD = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int BUFFERING_TIME = 250;
    public static final int ONE_MINUTE_MILLI_SECOND = 1000;
    public static final String BLACK_COLOUR = "#000000";
    public static final String APP_COLOUR = "#3A3B7F";
    public static final int SIX = 6;
    public static final float HUNDRED = 100.0f;
    public static final float TWO_HUNDRED = 200.0f;
    public static final float THREE_HUNDRED = 300.0f;
    public static final float FOUR_HUNDRED = 400.0f;
    public static final int ONE_FIFTY_FIVE = 155;
    public static final int REVIEW_IMAGE_WIDTH = 120;
    public static final int BOTTOM_SHEET_LOAD_DELAY = 500;
    public static final String GET_IP_ADDRESS = "https://api6.ipify.org/";
    public static final int ADD_CART = 1;
    public static final int DELETE_CART = 3;
    public static final int UPDATE_CART = 2;
    public static final int RETAILER = 1;
    public static final int MULTI_STORE = 2;
    public static final double DEFAULT_LAT_LANG = 0.0;
    public static final int MINUS_ONE = -1;
    public static final String PRODUCT_IMAGE = "productImage";
    public static final String PRODUCT_NAME = "productName";
    public static final String POSITION = "position";
    public static final String MOBILE_IMAGES = "mobileImages";
    public static final int FIFTY = 50;
    public static final String ATTRIBUTES_BOTTOM_SHEET_TAG = "AttributesBottomSheetFragment";
    public static final String SELLER_BOTTOM_SHEET_TAG = "SellerBottomSheetFragment";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String PRODUCT = "Product";
    public static final String PRICE = "price";
    public static final String PRODUCT_COLOUR = "productColor";
    public static final String RATING_DATA = "ratingData";
    public static final String RATING = "rating";
    static final String CERTIFICATE_TYPE = "X.509";
    static final String ALIAS_NAME = "ca";
    static final String REQUESTED_PROTOCOL = "TLS";
    static final int ARRAY_SIZE = 1;
    static final String CERTIFICATION_ERROR = "Unexpected default trust managers:";

    public static final String TOTALRATING_AND_REVIEWS = "totalRatingAndReviews";
    public static final String PEN_COUNT = "penCount";
    public static final int ALL_REVIEWS_CODE = 1;
    public static final int ALL_PREVIEW_CODE = 2;
    public static final String USER_REVIEW_DATA = "userReviewData";
    public static final String SHOPPING_STATUS = "shoppingStatus";
    public static final float POINT_ZERO = 0.0f;
    public static final String SUCCESS_MSG = "Success.";
    public static final String ERROR_MSG = "errorMsg";
    public static final String REVIEW = "review";
    public static final String VIEW_ALL = "View All";
    public static final String REVIEW_CLICKED = "reviewClicked";
    public static final String RATING_CLICKED = "ratingClicked";
    public static final String LIKE_DISLIKE = "likeDislike";
    public static final String VIEW_PAGER_IMAGES = "viewPagerImages";
    public static final String REVIEW_IMAGES = "reviewImages";
    public static final String REVIEW_DATA = "reviewData";
    public static final String GET_RATINGS = "getRatings";
    public static final String ALL_DETAILS_CLICKED = "allDetailsClicked";
    public static final String GET_HILIGHTS = "getHilights";
    public static final String ALL_OFFERS = "allOffers";
    public static final String GET_ATTRIBUTES = "getAttributes";
    public static final String GET_PRESCRIPTION_REQUIRED = "getPrescriptionRequired";
    public static final String GET_SELLERS = "getSellers";
    public static final String GET_DESCRIPTION = "description";
    public static final String GET_ATTRIBUTES_RATING = "getAttributesRating";
    public static final String GET_VARIENT_DATA = "getVarientData";
    public static final String GET_OFFER_DATA = "getOfferData";
    public static final String OUT_OF_STOCK_CLICKED = "outOfStockClicked";
    public static final int TWO_FIFTY = 250;
    public static final int ONE_FIFTY = 150;
    public static final int THREE_FIFTY = 350;
    public static final int FIVE_HUNDRED = 500;
    public static final float POINT_THREE = 0.3f;
    public static final float POINT_SIX = 0.6f;
    public static final float POINT_EIGHT = 0.8f;
    public static final String VALUE_ZERO = "0";
    public static final String PHARMACY_HEADING = "pharmacyHeading";
    public static final String PHARMACY_CONTENT = "pharmacyContent";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String OFFER_PRICE = "offerPrice";
    public static final String SPECIFICATIONS = "Specification";
    public static final String ATTRIBUTES_DATA = "AttributesData";
    public static final String HIGHLIGHT_DATA = "HighLightData";
    public static final String HTML_ATTRIBUTES_DATA = "HtmlAttributesData";
    public static final String HTML_ATTRIBUTES_HEADING = "HtmlAttributesHeading";
    public static final String LINK = "?link=";
    public static final int INSERT = 2;
    public static final int EDIT_NAME = 4;
    public static final int REQUEST_CODE = 7;
    public static final int ECOM_FLOW = 8;
    public static final int RATE_PRODUCT = 0;
    public static final int RATE_SELLER = 1;
    public static final String SELLER_IMAGE = "sellerImage";
    public static final String CAMERA_ITEM = "camera";
    public static final int REQUEST_CODE_PERMISSION_MULTIPLE = 123;
    public static final String IMAGE_SELECT = "image/*";
    public static final int CAMERA_PIC = 1;
    public static final int GALLERY_PIC = 2;
    public static final int DELIVERY_TYPE = 2;
    public static final int CROP_IMAGE = 3;
    public static final String PARENT_FOLDER = "master";
    public static final String IMAGE = "image";
    public static final String IMAGE_DATA = "/ImagesData/";
    public static final String PROVIDER_NAME = ".provider";
    public static final String PNG_FORMAT = ".png";
    public static final String PHOTO_PICKER_TYPE = "image/*";
    public static final String RETURN_DATA = "return-mData";
    public static final int NOUGHT = 24;
    public static final String PROVIDER = ".provider";
    public static final int RATING_TYPE = 1;
    public static final int REVIEW_TYPE = 2;
    public static final int HISTROY_PRODUCT_DETAIL = 3;
    public static final int HISTROY_ORDER_DETAIL = 4;
    public static final String IS_SPIT_PRODUCT = "isSplitProduct";
    public static final String STORE_NAME = "storeName";
    public static final int HISTORY_PRODUCT_DETAIL = 6;


    public static final String LEAVE_A_REVIEW = "Leave A Review ";
    public static final String REVIEW_FOLDER = "productReview";
    public static final int LIMIT = 10;
    public static final int EIGHT = 8;
    public static final int OUT_OF_STOCK_DIALOG_TYPE = 3;
    public static final int FORGOT_PASSWORD_DIALOG_TYPE = 1;
    public static final int EMAIL_SUCC_DIALOG_TYPE = 2;
    public static final int EMAIL_DIALOG_TYPE = 41;
    public static final int PHONE_DIALOG_TYPE = 51;
    public static final int VERSION_UPDATE_DIALOG_TYPE = 5;
    public static final String CONFIRM_ORDER = "confirmOrder";
    public static final String CARD_ID = "cardId";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String PAYMENT_BY_WALLET = "paymentByWallet";
    public static final String PAYMENT = "payment";
    public static final float SIXTEEN_F = 16f;
    public static final String CARD_NUMBER = "cardNumber";
    public static final String WALLET_AMT = "walletAmt";
    public static final String AMOUNT = "amount";
    public static final int THOUSAND = 1000;
    public static final int CARD_PAYMENT_TYPE = 1;
    public static final int CASH_PAYMENT_TYPE = 2;
    public static final int WALLET_PLUS_CARD = 3;
    public static final int WALLET_PLUS_CASH = 4;
    public static final int WALLET_PAYMENT_TYPE = 5;
    public static final int STORE_TYPE = 8;
    public static final int CARD = 4;
    public static final int PAYMENT_TYPE_CARD = 1;
    public static final int PAYMENT_METHOD_REQ_CODE = 1040;
    public static final String EXTRA_NOTES = "extraNotes";
    public static final int EXTRA_NOTES_REQ_CODE = 1520;
    public static final int CONTACT_LESS_DELIVERY_REQ_CODE = 1220;
    public static final String SELECTED_CONTACTLESS_DELIVERY_TYPE = "selectedContactlessDelivery";
    public static final String SELECTED_CONTACTLESS_DELIVERY_TYPE_ID =
            "selectedContactlessDeliveryId";
    public static final int SCHEDULE_DELIVERY_REQ_CODE = 1320;
    public static final int STORAGE_PERMISSION_REQ_CODE = 100;
    public static final String PROMO_CODE = "promoCode";
    public static final String PROMO_CODE_ID = "promoCodeId";
    public static final int APPLY_PROMO = 100;
    public static final int LOGIN = 2;
    public static final int LOGIN_RC = 0;
    public static final int PAYMENT_METHOD_REQUEST = 250;
    public static final int MANGE_ADDRESS_REQUEST = 200;
    public static final int CHANGE_ADDRESS_REQUEST = 150;
    public static final int BILLING_ADDRESS_REQUEST = 300;
    public static final int THIRTY = 30;
    public static final int FOURTY = 40;
    public static final String TIP_AMOUNT = "tipAmount";
    public static final String TIP_CURRENCY = "tipCurrency";
    public static final int TWENTY = 20;
    public static final int FIFTEEN = 15;
    public static final int RADIO_BUTTON_ID = 765419;
    public static final int CENTRAL_DRIVER = 2;
    public static final String DOLLAR_SIGN = "$";
    public static final String ERROR = "error";
    public static final String BRAND_LOGO = "brandLogo";
    public static final int ADD_CARD_RC = 1000;
    public static final int ADD_MONEY_RC = 120;
    public static final int WALLET_RC = 150;
    public static final String WALLET_TYPE = "customer";
    public static final int DEFAULT_VAL = 0;
    public static final int DELETE_ITEM = 2;
    public static final int LOW_TO_HIGH_VAL = 1;
    public static final int HIGH_TO_LOW_VAL = 2;
    public static final int NEWEST_FIRST_VAL = 0;
    public static final int CLEAR_ALL = 1;
    public static final int NEWEST_FIRST = 4;
    public static final int REVIEW_REQUEST_CODE = 50;
    public static final String OPEN_CART = "openCart";
    public static final int CANCEL_ORDER_REQUEST = 5;
    public static final String IS_TO_FINISH = "isToFinish";
    public static final String PRODUCT_ORDER_TYPE = "productOrder";

    String PROFILE_NAME = "profileName";
    String PROFILE_USERNAME = "profileUserName";
    String PROFILE_STATUS = "profileStatus";
    String EMAIL_PHONE_TYPE = "type";
    String PROFILE_DATA = "profileData";
    String BUSINESS_USERNAME = "businessUsername";
    String KNOWN_AS = "known_as";


}
