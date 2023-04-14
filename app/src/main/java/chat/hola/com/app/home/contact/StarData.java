package chat.hola.com.app.home.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StarData implements Serializable {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("categorieId")
    @Expose
    private String catrgoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("starUserEmail")
    @Expose
    private String starUserEmail;
    @SerializedName("starUserPhoneNumber")
    @Expose
    private String starUserPhoneNumber;
    @SerializedName("starUserIdProof")
    @Expose
    private String starUserIdProof;
    @SerializedName("starUserKnownBy")
    @Expose
    private String starUserKnownBy;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("starUserProfileStatusText")
    @Expose
    private String starUserProfileStatusText;
    @SerializedName("starUserProfileStatusCode")
    @Expose
    private Integer starUserProfileStatusCode;
    @SerializedName("approvalDate")
    @Expose
    private String approvalDate;
    @SerializedName("isNumberVisible")
    @Expose
    private Boolean isNumberVisible;
    @SerializedName("isEmailVisible")
    @Expose
    private Boolean isEmailVisible;
    @SerializedName("isChatVisible")
    @Expose
    private Boolean isChatVisible;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategorieId() {
        return catrgoryId;
    }

    public void setCategorieId(String catrgoryId) {
        this.catrgoryId = catrgoryId;
    }

    public String getStarUserEmail() {
        return starUserEmail;
    }

    public void setStarUserEmail(String starUserEmail) {
        this.starUserEmail = starUserEmail;
    }

    public String getStarUserPhoneNumber() {
        return starUserPhoneNumber;
    }

    public void setStarUserPhoneNumber(String starUserPhoneNumber) {
        this.starUserPhoneNumber = starUserPhoneNumber;
    }

    public String getStarUserIdProof() {
        return starUserIdProof;
    }

    public void setStarUserIdProof(String starUserIdProof) {
        this.starUserIdProof = starUserIdProof;
    }

    public String getStarUserKnownBy() {
        return starUserKnownBy;
    }

    public void setStarUserKnownBy(String starUserKnownBy) {
        this.starUserKnownBy = starUserKnownBy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStarUserProfileStatusText() {
        return starUserProfileStatusText;
    }

    public void setStarUserProfileStatusText(String starUserProfileStatusText) {
        this.starUserProfileStatusText = starUserProfileStatusText;
    }

    public Integer getStarUserProfileStatusCode() {
        return starUserProfileStatusCode;
    }

    public void setStarUserProfileStatusCode(Integer starUserProfileStatusCode) {
        this.starUserProfileStatusCode = starUserProfileStatusCode;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Boolean getIsNumberVisible() {
        return isNumberVisible;
    }

    public void setIsNumberVisible(Boolean isNumberVisible) {
        this.isNumberVisible = isNumberVisible;
    }

    public Boolean getIsEmailVisible() {
        return isEmailVisible;
    }

    public void setIsEmailVisible(Boolean isEmailVisible) {
        this.isEmailVisible = isEmailVisible;
    }

    public Boolean getIsChatVisible() {
        return isChatVisible;
    }

    public void setIsChatVisible(Boolean isChatVisible) {
        this.isChatVisible = isChatVisible;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}