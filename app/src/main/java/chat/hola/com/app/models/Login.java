package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Login implements Serializable {

    @SerializedName("code")
    @Expose(deserialize = true, serialize = true)
    int code;

    @SerializedName("otp")
    @Expose(deserialize = true, serialize = true)
    String otp;

    @SerializedName("countryName")
    @Expose(deserialize = true, serialize = true)
    String country;

    @SerializedName("deviceId")
    @Expose(deserialize = true, serialize = true)
    String deviceId;

    @SerializedName("password")
    @Expose(deserialize = true, serialize = true)
    String password;

    @SerializedName("userName")
    @Expose(deserialize = true, serialize = true)
    String userName;

    @SerializedName(value = "phoneNumber", alternate = "number")
    @Expose(deserialize = true, serialize = true)
    String phoneNumber;

    @SerializedName("countryCode")
    @Expose(deserialize = true, serialize = true)
    String countryCode;

    @SerializedName("deviceName")
    @Expose(deserialize = true, serialize = true)
    String deviceName;

    @SerializedName("deviceOs")
    @Expose(deserialize = true, serialize = true)
    String deviceOs;

    @SerializedName("modelNumber")
    @Expose(deserialize = true, serialize = true)
    String modelNumber;

    @SerializedName("deviceType")
    @Expose(deserialize = true, serialize = true)
    String deviceType;

    @SerializedName("appVersion")
    @Expose(deserialize = true, serialize = true)
    String appVersion;


    @SerializedName("type")
    @Expose(deserialize = true, serialize = true)
    Integer type;


    @SerializedName("message")
    @Expose(deserialize = true, serialize = true)
    String message;

    @SerializedName(value = "response", alternate = "data")
    @Expose(deserialize = true, serialize = true)
    LoginResponse response;

    @SerializedName("isSignUp")
    @Expose(deserialize = true, serialize = true)
    Boolean isSignUp = false;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIgnUp() {
        return isSignUp;
    }

    public void setIgnUp(Boolean isSignUp) {
        this.isSignUp = isSignUp;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public LoginResponse getResponse() {
        return response;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponse(LoginResponse response) {
        this.response = response;
    }

    public Boolean getSignUp() {
        return isSignUp;
    }

    public void setSignUp(Boolean signUp) {
        isSignUp = signUp;
    }


    public static class LoginResponse implements Serializable {
        //-------------------------
        /**
         * For Isometrik Group streaming
         */

        @SerializedName("accountId")
        @Expose
        String accountId;

        @SerializedName("projectId")
        @Expose
        String projectId;

        @SerializedName("keysetId")
        @Expose
        String keysetId;

        @SerializedName("rtcAppId")
        @Expose
        String rtcAppId;

        @SerializedName("arFiltersAppId")
        @Expose
        String arFiltersAppId;

        @SerializedName("licenseKey")
        @Expose
        String licenseKey;

        @SerializedName("groupCallStreamId")
        @Expose
        String groupCallStreamId;
        //-------------------------

        @SerializedName("countryName")
        @Expose(deserialize = true, serialize = true)
        String country;

        @SerializedName("currencySymbol")
        @Expose(deserialize = true, serialize = true)
        String currencySymbol;

        @SerializedName("currency")
        @Expose(deserialize = true, serialize = true)
        Currency currency;

        @SerializedName("isActiveBusinessProfile")
        boolean isActiveBusinessProfile;

        @SerializedName("businessProfile")
        List<Business> business;

        @SerializedName("userId")
        @Expose(deserialize = true, serialize = true)
        String userId;

        @SerializedName("oneSignalId")
        @Expose(deserialize = true, serialize = true)
        String oneSignalId;

        @SerializedName("email")
        @Expose(deserialize = true, serialize = true)
        String email;

        @SerializedName("number")
        @Expose(deserialize = true, serialize = true)
        String phoneNumber;

        @SerializedName(value = "token", alternate = "accessToken")
        @Expose(deserialize = true, serialize = true)
        String token;

        @SerializedName("countryCode")
        @Expose(deserialize = true, serialize = true)
        String countryCode;

        @SerializedName("private")
        @Expose(deserialize = true, serialize = true)
        int _private;

        @SerializedName("qrCode")
        @Expose(deserialize = true, serialize = true)
        String qrCode;

        @SerializedName("profilePic")
        @Expose(deserialize = true, serialize = true)
        String profilePic;

        @SerializedName("userName")
        @Expose(deserialize = true, serialize = true)
        String userName;

        @SerializedName("firstName")
        @Expose(deserialize = true, serialize = true)
        String firstName;

        @SerializedName("lastName")
        @Expose(deserialize = true, serialize = true)
        String lastName;

        @SerializedName("socialStatus")
        @Expose(deserialize = true, serialize = true)
        String socialStatus;

        @SerializedName("profileVideo")
        @Expose(deserialize = true, serialize = true)
        String profileVideo;

        @SerializedName("profileVideoThumbnail")
        @Expose(deserialize = true, serialize = true)
        String profileVideoThumbnail;

        @SerializedName("authToken")
        @Expose(deserialize = true, serialize = true)
        String authToken;

        @SerializedName("willTopic")
        @Expose(deserialize = true, serialize = true)
        String willTopic;

        @SerializedName("stream")
        @Expose(deserialize = true, serialize = true)
        LiveStream stream;

        public String getAccountId() {
            return accountId;
        }

        public String getProjectId() {
            return projectId;
        }

        public String getKeysetId() {
            return keysetId;
        }

        public String getRtcAppId() {
            return rtcAppId;
        }

        public String getArFiltersAppId() {
            return arFiltersAppId;
        }

        public String getLicenseKey() {
            return licenseKey;
        }

        public String getGroupCallStreamId() {
            return groupCallStreamId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCurrencySymbol() {
            return currencySymbol;
        }

        public void setCurrencySymbol(String currencySymbol) {
            this.currencySymbol = currencySymbol;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        public boolean isActiveBusinessProfile() {
            return isActiveBusinessProfile;
        }

        public void setActiveBusinessProfile(boolean activeBusinessProfile) {
            isActiveBusinessProfile = activeBusinessProfile;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        @SerializedName("refreshToken")
        @Expose(deserialize = true, serialize = true)
        String refreshToken;

        public List<Business> getBusiness() {
            return business;
        }

        public void setBusiness(List<Business> business) {
            this.business = business;
        }

        public LiveStream getStream() {
            return stream;
        }

        public void setStream(LiveStream stream) {
            this.stream = stream;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getUserId() {
            return userId;
        }

        public String getOneSignalId() {
            return oneSignalId;
        }

        public String getToken() {
            return token;
        }

        public int get_private() {
            return _private;
        }

        public String getQrCode() {
            return qrCode;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public String getUserName() {
            return userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getProfileVideo() {
            return profileVideo;
        }

        public String getProfileVideoThumbnail() {
            return profileVideoThumbnail;
        }

        public String getAuthToken() {
            return authToken;
        }

        public String getWillTopic() {
            return willTopic;
        }

        public String getSocialStatus() {
            return socialStatus;
        }

        public void setSocialStatus(String socialStatus) {
            this.socialStatus = socialStatus;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
