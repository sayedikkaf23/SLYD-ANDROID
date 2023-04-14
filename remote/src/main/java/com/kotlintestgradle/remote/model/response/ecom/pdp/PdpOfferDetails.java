package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;

public class PdpOfferDetails implements Parcelable, ValidItem {

  @SerializedName("applicableOnStatus")
  @Expose
  private String applicableOnStatus;

  @SerializedName("images")
  @Expose
  private ImagesDetails images;

  @SerializedName("offerName")
  @Expose
  private OfferName offerName;

  @SerializedName("endDateTimeISO")
  @Expose
  private String endDateTimeISO;

  @SerializedName("endDateTime")
  @Expose
  private String endDateTime;

  @SerializedName("globalClaimCount")
  @Expose
  private String globalClaimCount;

  @SerializedName("startDateTimeISO")
  @Expose
  private String startDateTimeISO;

  @SerializedName("webimages")
  @Expose
  private ImagesDetails webimages;

  @SerializedName("startDateTime")
  @Expose
  private String startDateTime;

  @SerializedName("perUserLimit")
  @Expose
  private String perUserLimit;

  @SerializedName("minimumPurchaseQty")
  @Expose
  private String minimumPurchaseQty;

  @SerializedName("statusString")
  @Expose
  private String statusString;

  @SerializedName("offerId")
  @Expose
  private String offerId;

  @SerializedName("discountType")
  @Expose
  private String discountType;

  @SerializedName("offerFor")
  @Expose
  private String offerFor;

  @SerializedName("discountValue")
  @Expose
  private String discountValue;

  @SerializedName("applicableOn")
  @Expose
  private String applicableOn;

  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("termscond")
  @Expose
  private String termscond;

  public PdpOfferDetails() {
  }

  public PdpOfferDetails(String applicableOnStatus, ImagesDetails images, OfferName offerName,
                         String endDateTimeISO, String endDateTime, String globalClaimCount, String startDateTimeISO,
                         ImagesDetails webimages, String startDateTime, String perUserLimit, String minimumPurchaseQty,
                         String statusString, String offerId, String discountType, String offerFor,
                         String discountValue, String applicableOn, String status) {
    this.applicableOnStatus = applicableOnStatus;
    this.images = images;
    this.offerName = offerName;
    this.endDateTimeISO = endDateTimeISO;
    this.endDateTime = endDateTime;
    this.globalClaimCount = globalClaimCount;
    this.startDateTimeISO = startDateTimeISO;
    this.webimages = webimages;
    this.startDateTime = startDateTime;
    this.perUserLimit = perUserLimit;
    this.minimumPurchaseQty = minimumPurchaseQty;
    this.statusString = statusString;
    this.offerId = offerId;
    this.discountType = discountType;
    this.offerFor = offerFor;
    this.discountValue = discountValue;
    this.applicableOn = applicableOn;
    this.status = status;
  }

  protected PdpOfferDetails(Parcel in) {
    applicableOnStatus = in.readString();
    images = in.readParcelable(ImagesDetails.class.getClassLoader());
    offerName = in.readParcelable(OfferName.class.getClassLoader());
    endDateTimeISO = in.readString();
    endDateTime = in.readString();
    globalClaimCount = in.readString();
    startDateTimeISO = in.readString();
    webimages = in.readParcelable(ImagesDetails.class.getClassLoader());
    startDateTime = in.readString();
    perUserLimit = in.readString();
    minimumPurchaseQty = in.readString();
    statusString = in.readString();
    offerId = in.readString();
    discountType = in.readString();
    offerFor = in.readString();
    discountValue = in.readString();
    applicableOn = in.readString();
    status = in.readString();
    termscond=in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(applicableOnStatus);
    dest.writeParcelable(images, flags);
    dest.writeParcelable(offerName, flags);
    dest.writeString(endDateTimeISO);
    dest.writeString(endDateTime);
    dest.writeString(globalClaimCount);
    dest.writeString(startDateTimeISO);
    dest.writeParcelable(webimages, flags);
    dest.writeString(startDateTime);
    dest.writeString(perUserLimit);
    dest.writeString(minimumPurchaseQty);
    dest.writeString(statusString);
    dest.writeString(offerId);
    dest.writeString(discountType);
    dest.writeString(offerFor);
    dest.writeString(discountValue);
    dest.writeString(applicableOn);
    dest.writeString(status);
    dest.writeString(termscond);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PdpOfferDetails> CREATOR = new Creator<PdpOfferDetails>() {
    @Override
    public PdpOfferDetails createFromParcel(Parcel in) {
      return new PdpOfferDetails(in);
    }

    @Override
    public PdpOfferDetails[] newArray(int size) {
      return new PdpOfferDetails[size];
    }
  };

  public String getApplicableOnStatus() {
    return applicableOnStatus;
  }

  public void setApplicableOnStatus(String applicableOnStatus) {
    this.applicableOnStatus = applicableOnStatus;
  }

  public ImagesDetails getImages() {
    return images;
  }

  public void setImages(ImagesDetails images) {
    this.images = images;
  }

  public OfferName getOfferName() {
    return offerName;
  }

  public void setOfferName(OfferName offerName) {
    this.offerName = offerName;
  }

  public String getEndDateTimeISO() {
    return endDateTimeISO;
  }

  public void setEndDateTimeISO(String endDateTimeISO) {
    this.endDateTimeISO = endDateTimeISO;
  }

  public String getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(String endDateTime) {
    this.endDateTime = endDateTime;
  }

  public String getGlobalClaimCount() {
    return globalClaimCount;
  }

  public void setGlobalClaimCount(String globalClaimCount) {
    this.globalClaimCount = globalClaimCount;
  }

  public String getStartDateTimeISO() {
    return startDateTimeISO;
  }

  public void setStartDateTimeISO(String startDateTimeISO) {
    this.startDateTimeISO = startDateTimeISO;
  }

  public ImagesDetails getWebimages() {
    return webimages;
  }

  public void setWebimages(ImagesDetails webimages) {
    this.webimages = webimages;
  }

  public String getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
  }

  public String getPerUserLimit() {
    return perUserLimit;
  }

  public void setPerUserLimit(String perUserLimit) {
    this.perUserLimit = perUserLimit;
  }

  public String getMinimumPurchaseQty() {
    return minimumPurchaseQty;
  }

  public void setMinimumPurchaseQty(String minimumPurchaseQty) {
    this.minimumPurchaseQty = minimumPurchaseQty;
  }

  public String getStatusString() {
    return statusString;
  }

  public void setStatusString(String statusString) {
    this.statusString = statusString;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public String getDiscountType() {
    return discountType;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public String getOfferFor() {
    return offerFor;
  }

  public void setOfferFor(String offerFor) {
    this.offerFor = offerFor;
  }

  public String getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public String getApplicableOn() {
    return applicableOn;
  }

  public void setApplicableOn(String applicableOn) {
    this.applicableOn = applicableOn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTermscond() {
    return termscond;
  }

  public void setTermscond(String termscond) {
    this.termscond = termscond;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
