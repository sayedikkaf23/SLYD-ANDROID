package chat.hola.com.app.request_star_profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("categorieId")
    @Expose
    private String categorieId;

    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("starUserEmail")
    @Expose
    private String starUserEmail;
    @SerializedName("starUserPhoneNumber")
    @Expose
    private String starUserPhoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("starUserIdProof")
    @Expose
    private String starUserIdProof;
    @SerializedName("starUserKnownBy")
    @Expose
    private String starUserKnownBy;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("starUserProfileStatusText")
    @Expose
    // starUserProfileStatusCode	1,         2,        3,      4
    // starUserProfileStatusText	pending,notApproved,suspend,approved
    private String starUserProfileStatusText;
    @SerializedName("starUserProfileStatusCode")
    @Expose
    private Integer starUserProfileStatusCode;
    @SerializedName("approvalDate")
    @Expose
    private Long approvalDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isEmailVisible")
    @Expose
    private boolean isEmailVisible;
    @SerializedName("isNumberVisible")
    @Expose
    private boolean isNumberVisible;
    @SerializedName("isChatVisible")
    @Expose
    private boolean isChatVisible;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(String categorieId) {
        this.categorieId = categorieId;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
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

    public Long getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Long approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmailVisible() {
        return isEmailVisible;
    }

    public void setEmailVisible(boolean emailVisible) {
        isEmailVisible = emailVisible;
    }

    public boolean isNumberVisible() {
        return isNumberVisible;
    }

    public void setNumberVisible(boolean numberVisible) {
        isNumberVisible = numberVisible;
    }

    public boolean isChatVisible() {
        return isChatVisible;
    }

    public void setChatVisible(boolean chatVisible) {
        isChatVisible = chatVisible;
    }
}
