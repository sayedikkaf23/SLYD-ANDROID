package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.http.Body;

public class Register implements Serializable {

    @SerializedName("message")
    @Expose(deserialize = true, serialize = true)
    String message="";

    @SerializedName("userName")
    @Expose(deserialize = true, serialize = true)
    String userName="";

    @SerializedName("dateOfBirth")
    @Expose(deserialize = true, serialize = true)
    String dateOfBirth="";

    @SerializedName("gender")
    @Expose(deserialize = true, serialize = true)
    String gender="";

    @SerializedName("isPrivate")
    @Expose(deserialize = true, serialize = true)
    Boolean isPrivate = false;

    @SerializedName("password")
    @Expose(deserialize = true, serialize = true)
    String pwd="";

    @SerializedName("userReferralCode")
    @Expose(deserialize = true, serialize = true)
    String referralCode="";

    @SerializedName("countryCode")
    @Expose(deserialize = true, serialize = true)
    String countryCode="";

    @SerializedName("number")
    @Expose(deserialize = true, serialize = true)
    String mobileNumber="";

    @SerializedName("profilePic")
    @Expose(deserialize = true, serialize = true)
    String profilePic="";

    @SerializedName("firstName")
    @Expose(deserialize = true, serialize = true)
    String firstName="";

    @SerializedName("lastName")
    @Expose(deserialize = true, serialize = true)
    String lastName="";

    @SerializedName("email")
    @Expose(deserialize = true, serialize = true)
    String emailId="";

    @SerializedName("countryName")
    @Expose(deserialize = true, serialize = true)
    String country="";
    @SerializedName("deviceName")
    @Expose(deserialize = true, serialize = true)
    String deviceName="";
    @SerializedName("deviceOs")
    @Expose(deserialize = true, serialize = true)
    String deviceOs="";
    @SerializedName("modelNumber")
    @Expose(deserialize = true, serialize = true)
    String modelNumber="";
    @SerializedName("deviceType")
    @Expose(deserialize = true, serialize = true)
    String deviceType="";
    @SerializedName("appVersion")
    @Expose(deserialize = true, serialize = true)
    String appVersion="";
    @SerializedName("deviceId")
    @Expose(deserialize = true, serialize = true)
    String deviceId="";
    @SerializedName("googleId")
    @Expose(deserialize = true, serialize = true)
    String googleId="";
    @SerializedName("facebookId")
    @Expose(deserialize = true, serialize = true)
    String facebookId="";
    @SerializedName("appleId")
    @Expose(deserialize = true, serialize = true)
    String appleId="";
    @SerializedName("loginType")
    @Expose(deserialize = true, serialize = true)
    String loginType="";

    @SerializedName("countryCodeName")
    @Expose(deserialize = true, serialize = true)
    String countryCodeName="";

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountryCodeName() {
        return countryCodeName;
    }

    public void setCountryCodeName(String countryCodeName) {
        this.countryCodeName = countryCodeName;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

//    Uri profilePicNew;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String password) {
        this.pwd = password;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getEmail() {
        return emailId;
    }

    public void setEmail(String email) {
        this.emailId = email;
    }

//    public Uri getProfilePicNew() {
//        return profilePicNew;
//    }
//
//    public void setProfilePicNew(Uri profilePicNew) {
//        this.profilePicNew = profilePicNew;
//    }

//    public boolean isUploadProfilePic() {
//        return uploadProfilePic;
//    }
//
//    public void setUploadProfilePic(boolean uploadProfilePic) {
//        this.uploadProfilePic = uploadProfilePic;
//    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
