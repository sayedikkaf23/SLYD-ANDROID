package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>Business</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class Business implements Serializable {
    @SerializedName("businessName")
    @Expose
    private String businessName;
    @SerializedName("businessPostType")
    @Expose
    private String businessPostType;
    @SerializedName("businessPostTypeLabel")
    @Expose
    private String businessPostTypeLabel;
    @SerializedName("businessPrice")
    @Expose
    private String businessPrice;
    @SerializedName("businessUrl")
    @Expose
    private String businessUrl;
    @SerializedName("businessCurrency")
    @Expose
    private String businessCurrency;
    @SerializedName("businessButtonText")
    @Expose
    private String businessButtonText;
    @SerializedName("businessButtonColor")
    @Expose
    private String businessButtonColor;
    @SerializedName("businessProfilePic")
    @Expose
    private String businessProfilePic;
    @SerializedName("businessProfileCoverImage")
    @Expose
    private String businessProfileCoverImage;

    public String getBusinessPostType() {
        return businessPostType;
    }

    public void setBusinessPostType(String businessPostType) {
        this.businessPostType = businessPostType;
    }

    public String getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(String businessPrice) {
        this.businessPrice = businessPrice;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    public String getBusinessCurrency() {
        return businessCurrency;
    }

    public void setBusinessCurrency(String businessCurrency) {
        this.businessCurrency = businessCurrency;
    }

    public String getBusinessButtonText() {
        return businessButtonText;
    }

    public void setBusinessButtonText(String businessButtonText) {
        this.businessButtonText = businessButtonText;
    }

    public String getBusinessButtonColor() {
        return businessButtonColor;
    }

    public void setBusinessButtonColor(String businessButtonColor) {
        this.businessButtonColor = businessButtonColor;
    }

    public String getBusinessPostTypeLabel() {
        return businessPostTypeLabel;
    }

    public void setBusinessPostTypeLabel(String businessPostTypeLabel) {
        this.businessPostTypeLabel = businessPostTypeLabel;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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
