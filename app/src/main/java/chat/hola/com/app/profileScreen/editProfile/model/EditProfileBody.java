package chat.hola.com.app.profileScreen.editProfile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 19/3/18.
 */

public class EditProfileBody {


    @Expose
    @SerializedName("starUserKnownBy")
    String knownAs;

    @Expose
    @SerializedName("profilePic")
    String imgUrl;

    @Expose
    @SerializedName("firstName")
    String firstName;

    @Expose
    @SerializedName("lastName")
    String lastName;

    @Expose
    @SerializedName("userName")
    String userName;

    @Expose
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("email")
    String email;

    @Expose
    @SerializedName("private")
    Integer _private;

    @Expose
    @SerializedName("profileCoverImage")
    String profileCoverImage;

    @Expose
    @SerializedName("BusinessName")
    String businessName;
    @Expose
    @SerializedName("BusinessAddress")
    String businessAddress;
    @Expose
    @SerializedName("BusinessWebsite")
    String businessWebsite;
    @Expose
    @SerializedName("bussinessEmail")
    String businessEmail;
    @Expose
    @SerializedName("bussinessPhone")
    String businessPhone;
    @Expose
    @SerializedName("bussinessCountryCode")
    String businessCountryCode;
    @Expose
    @SerializedName("bussinessCategoryId")
    String businessCategoryId;
    @Expose
    @SerializedName("bussinessBio")
    String businessBio;
    @SerializedName("businessStreet")
    @Expose
    private String businessStreet;
    @SerializedName("businessCity")
    @Expose
    private String businessCity;
    @SerializedName("businessZipCode")
    @Expose
    private String businessZipCode;
    @SerializedName("businessLat")
    @Expose
    private String businessLat;
    @SerializedName("businessLng")
    @Expose
    private String businessLng;
    @SerializedName("businessProfilePic")
    @Expose
    private String businessProfilePic;
    @SerializedName("businessProfileCoverImage")
    @Expose
    private String businessProfileCoverImage;
    @SerializedName("businessUniqueId")
    @Expose
    private String businessUniqueId;

    public String getBusinessUniqueId() {
        return businessUniqueId;
    }

    public void setBusinessUniqueId(String businessUniqueId) {
        this.businessUniqueId = businessUniqueId;
    }

    public String getBusinessStreet() {
        return businessStreet;
    }

    public void setBusinessStreet(String businessStreet) {
        this.businessStreet = businessStreet;
    }

    public String getBusinessCity() {
        return businessCity;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    public String getBusinessZipCode() {
        return businessZipCode;
    }

    public void setBusinessZipCode(String businessZipCode) {
        this.businessZipCode = businessZipCode;
    }

    public String getBusinessLat() {
        return businessLat;
    }

    public void setBusinessLat(String businessLat) {
        this.businessLat = businessLat;
    }

    public String getBusinessLng() {
        return businessLng;
    }

    public void setBusinessLng(String businessLng) {
        this.businessLng = businessLng;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String businessWebsite) {
        this.businessWebsite = businessWebsite;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessCountryCode() {
        return businessCountryCode;
    }

    public void setBusinessCountryCode(String businessCountryCode) {
        this.businessCountryCode = businessCountryCode;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessBio() {
        return businessBio;
    }

    public void setBusinessBio(String businessBio) {
        this.businessBio = businessBio;
    }

    //    public void setImgPath(String imgPath) {
//        this.imgPath = imgPath;
//    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }


//    public String getImgPath() {
//        return imgPath;
//    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public Integer get_private() {
        return _private;
    }

    public void set_private(Integer _private) {
        this._private = _private;
    }

    public void setProfileCoverImage(String profileCoverImage) {
        this.profileCoverImage = profileCoverImage;
    }

    public String getProfileCoverImage() {
        return profileCoverImage;
    }

    public String getKnownAs() {
        return knownAs;
    }

    public void setKnownAs(String knownAs) {
        this.knownAs = knownAs;
    }

    public String getBusinessProfilePic() {
        return businessProfilePic;
    }

    public void setBusinessProfilePic(String businessProfilePic) {
        this.businessProfilePic = businessProfilePic;
    }

    public String getBusinessProfileCoverImage() {
        return businessProfileCoverImage;
    }

    public void setBusinessProfileCoverImage(String businessProfileCoverImage) {
        this.businessProfileCoverImage = businessProfileCoverImage;
    }
}
