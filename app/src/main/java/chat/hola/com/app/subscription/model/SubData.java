package chat.hola.com.app.subscription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("startDate")
    @Expose
    private Double startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("beneficiaryId")
    @Expose
    private String beneficiaryId;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("isSubscriptionCancelled")
    @Expose
    private boolean isSubscriptionCancelled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getStartDate() {
        return startDate;
    }

    public void setStartDate(Double startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isSubscriptionCancelled() {
        return isSubscriptionCancelled;
    }

    public void setSubscriptionCancelled(boolean subscriptionCancelled) {
        isSubscriptionCancelled = subscriptionCancelled;
    }
}