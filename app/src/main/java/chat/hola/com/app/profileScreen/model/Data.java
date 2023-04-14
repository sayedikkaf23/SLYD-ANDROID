package chat.hola.com.app.profileScreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 3/2/2018.
 */

public class Data implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("registeredOn")
    @Expose
    private String registeredOn;
    @SerializedName("private")
    @Expose
    private String _private;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("following")
    @Expose
    private String following;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("postsCount")
    @Expose
    private String postsCount;
    @SerializedName("subscribeUserCount")
    @Expose
    private String subscribeUserCount;
    @SerializedName("isBlocked")
    @Expose
    private Integer block = 1;

    @SerializedName("followStatus")
    @Expose
    private Integer followStatus = 1;

    @SerializedName("qrCode")
    @Expose
    private String qrCode;

    @SerializedName("verified")
    @Expose
    private Verified verified;

    @SerializedName("friendStatusCode")
    @Expose
    private Integer friendStatusCode = 1;

    @SerializedName("isStar")
    @Expose
    private Boolean isStar;

    @SerializedName("starRequest")
    @Expose
    private chat.hola.com.app.request_star_profile.model.Data starRequest;
    @SerializedName("subscriptionAmount")
    @Expose
    private Long subscriptionAmount;
    @SerializedName("isSubscribe")
    @Expose
    private boolean isSubscribe;
    @SerializedName("isSubscriptionCancelled")
    @Expose
    private boolean isSubscriptionCancelled;

    @SerializedName("subscriptionDetails")
    @Expose
    private SubscriptionDetail subscriptionDetails;

    @SerializedName("wallet")
    @Expose
    private Wallet wallet;

    @SerializedName("subscribeEndDate")
    @Expose
    private String subscribeEndDate;

    @SerializedName("profileCoverImage")
    @Expose
    private String profileCoverImage;

    @SerializedName("referralCode")
    @Expose
    private String referralCode;

    @SerializedName("businessProfile")
    private List<BusinessProfile> businessProfiles;

    @SerializedName("isActiveBusinessProfile")
    @Expose
    private boolean isActiveBussinessProfile;
    @SerializedName("isBusinessProfileApproved")
    @Expose
    private boolean isBusinessProfileApproved;
    @SerializedName("AppWillGetInPercentage")
    @Expose
    private Double AppWillGetInPercentage;
    @SerializedName("coinValue")
    @Expose
    private Double coinValue;
    @SerializedName("userType")
    @Expose
    private int userType; //1-Regular, 9-Business, 10-Resellers(verified)
    @SerializedName("userTypeText")
    @Expose
    private String userTypeText;
    @SerializedName("isVerifiedEmail")
    @Expose
    private boolean isVerifiedEmail;
    @SerializedName("isVerifiedNumber")
    @Expose
    private boolean isVerifiedNumber;
    @SerializedName("currency")
    @Expose
    private chat.hola.com.app.models.Currency currency ;
    @SerializedName("isKYCApproved")
    @Expose
    private boolean isKYCApproved;

    public boolean isKYCApproved() {
        return isKYCApproved;
    }

    public void setKYCApproved(boolean KYCApproved) {
        isKYCApproved = KYCApproved;
    }

    public boolean isVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        isVerifiedEmail = verifiedEmail;
    }

    public boolean isVerifiedNumber() {
        return isVerifiedNumber;
    }

    public void setVerifiedNumber(boolean verifiedNumber) {
        isVerifiedNumber = verifiedNumber;
    }

    public chat.hola.com.app.models.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(chat.hola.com.app.models.Currency currency) {
        this.currency = currency;
    }

    public String get_private() {
        return _private;
    }

    public void set_private(String _private) {
        this._private = _private;
    }

    public Boolean getStar() {
        return isStar;
    }

    public void setStar(Boolean star) {
        isStar = star;
    }

    public boolean isBusinessProfileApproved() {
        return isBusinessProfileApproved;
    }

    public void setBusinessProfileApproved(boolean businessProfileApproved) {
        isBusinessProfileApproved = businessProfileApproved;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getPrivate() {
        return _private;
    }

    public void setPrivate(String _private) {
        this._private = _private;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(String postsCount) {
        this.postsCount = postsCount;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Verified getVerified() {
        return verified;
    }

    public void setVerified(Verified verified) {
        this.verified = verified;
    }

    public Integer getFriendStatusCode() {
        return friendStatusCode;
    }

    public void setFriendStatusCode(Integer friendStatusCode) {
        this.friendStatusCode = friendStatusCode;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public chat.hola.com.app.request_star_profile.model.Data getStarRequest() {
        return starRequest;
    }

    public void setStarRequest(chat.hola.com.app.request_star_profile.model.Data starRequest) {
        this.starRequest = starRequest;
    }

    public String getProfileCoverImage() {
        return profileCoverImage;
    }

    public void setProfileCoverImage(String profileCoverImage) {
        this.profileCoverImage = profileCoverImage;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public List<BusinessProfile> getBusinessProfiles() {
        return businessProfiles;
    }

    public void setBusinessProfiles(List<BusinessProfile> businessProfiles) {
        this.businessProfiles = businessProfiles;
    }

    public boolean isActiveBussinessProfile() {
        return isActiveBussinessProfile;
    }

    public void setActiveBussinessProfile(boolean activeBussinessProfile) {
        isActiveBussinessProfile = activeBussinessProfile;
    }

    public class BusinessProfile implements Serializable {
        @SerializedName("businessUniqueId")
        @Expose
        private String businessUniqueId;
        @SerializedName("businessProfilePic")
        @Expose
        private String businessProfilePic;
        @SerializedName("businessProfileCoverImage")
        @Expose
        private String businessProfileCoverImage;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("websiteURL")
        @Expose
        private String website = "";
        @SerializedName("businessBio")
        @Expose
        private String businessBio = "";
        @SerializedName("businessUsername")
        @Expose
        private String businessUserName;
        @SerializedName("businessName")
        @Expose
        private String businessName;
        @SerializedName("businessCategory")
        @Expose
        private String businessCategory;
        @SerializedName("privateAccount")
        @Expose
        private Boolean privateAccount;
        @SerializedName("businessCategoryId")
        @Expose
        private String bussinessId;
        @SerializedName("statusText")
        @Expose
        private String statusText;
        @SerializedName("statusCode")
        @Expose
        private Integer statusCode;
        @SerializedName("phone")
        @Expose
        private Phone phone;
        @SerializedName("email")
        @Expose
        private Email email;
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

        public String getBusinessUniqueId() {
            return businessUniqueId;
        }

        public void setBusinessUniqueId(String businessUniqueId) {
            this.businessUniqueId = businessUniqueId;
        }

        public String getBusinessUserName() {
            return businessUserName;
        }

        public void setBusinessUserName(String businessUserName) {
            this.businessUserName = businessUserName;
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

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public Email getEmail() {
            return email;
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getBusinessCategory() {
            return businessCategory;
        }

        public void setBusinessCategory(String businessCategory) {
            this.businessCategory = businessCategory;
        }

        public Boolean getPrivateAccount() {
            return privateAccount;
        }

        public void setPrivateAccount(Boolean privateAccount) {
            this.privateAccount = privateAccount;
        }

        public String getBussinessId() {
            return bussinessId;
        }

        public void setBussinessId(String bussinessId) {
            this.bussinessId = bussinessId;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public Phone getPhone() {
            return phone;
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public String getBusinessBio() {
            return businessBio;
        }

        public void setBusinessBio(String businessBio) {
            this.businessBio = businessBio;
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

        public class Phone implements Serializable {
            @SerializedName("number")
            @Expose
            private String number;
            @SerializedName("countryCode")
            @Expose
            private String countryCode;
            @SerializedName("verified")
            @Expose
            private Integer verified;

            @SerializedName("isVisible")
            @Expose
            private Integer isVisible;

            public Integer getVisible() {
                return isVisible;
            }

            public void setVisible(Integer visible) {
                isVisible = visible;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public Integer getVerified() {
                return verified;
            }

            public void setVerified(Integer verified) {
                this.verified = verified;
            }
        }

    }

    public Long getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Long subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public SubscriptionDetail getSubscriptionDetails() {
        return subscriptionDetails;
    }

    public void setSubscriptionDetails(SubscriptionDetail subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    public Double getAppWillGetInPercentage() {
        return AppWillGetInPercentage;
    }

    public void setAppWillGetInPercentage(Double appWillGetInPercentage) {
        AppWillGetInPercentage = appWillGetInPercentage;
    }

    public Double getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(Double coinValue) {
        this.coinValue = coinValue;
    }

    public String getSubscribeUserCount() {
        return subscribeUserCount;
    }

    public void setSubscribeUserCount(String subscribeUserCount) {
        this.subscribeUserCount = subscribeUserCount;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserTypeText() {
        return userTypeText;
    }

    public void setUserTypeText(String userTypeText) {
        this.userTypeText = userTypeText;
    }

    public String getSubscribeEndDate() {
        return subscribeEndDate;
    }

    public void setSubscribeEndDate(String subscribeEndDate) {
        this.subscribeEndDate = subscribeEndDate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean isSubscriptionCancelled() {
        return isSubscriptionCancelled;
    }

    public void setSubscriptionCancelled(boolean subscriptionCancelled) {
        isSubscriptionCancelled = subscriptionCancelled;
    }
}
