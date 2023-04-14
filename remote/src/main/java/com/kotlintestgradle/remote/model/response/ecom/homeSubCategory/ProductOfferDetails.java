package com.kotlintestgradle.remote.model.response.ecom.homeSubCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.OfferName;

public class ProductOfferDetails implements Parcelable, ValidItem {

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
  private long endDateTimeISO;

  @SerializedName("endDateTime")
  @Expose
  private String endDateTime;

  @SerializedName("globalClaimCount")
  @Expose
  private String globalClaimCount;

  @SerializedName("startDateTimeISO")
  @Expose
  private String startDateTimeISO;

  @SerializedName("startDateTime")
  @Expose
  private String startDateTime;

  @SerializedName("minimumPurchaseQty")
  @Expose
  private String minimumPurchaseQty;

  @SerializedName("perUserLimit")
  @Expose
  private String perUserLimit;

  @SerializedName("statusString")
  @Expose
  private String statusString;

  @SerializedName("offerId")
  @Expose
  private String offerId;

  @SerializedName("discountType")
  @Expose
  private int discountType;

  @SerializedName("offerFor")
  @Expose
  private String offerFor;

  @SerializedName("discountValue")
  @Expose
  private long discountValue;

  @SerializedName("applicableOn")
  @Expose
  private String applicableOn;

  @SerializedName("status")
  @Expose
  private String status;

  public ProductOfferDetails(String applicableOnStatus, ImagesDetails images,
      OfferName offerName, long endDateTimeISO, String endDateTime,
      String globalClaimCount, String startDateTimeISO,
      String startDateTime, String minimumPurchaseQty, String perUserLimit,
      String statusString, String offerId,
      int discountType, String offerFor, long discountValue,
      String applicableOn, String status) {
    this.applicableOnStatus = applicableOnStatus;
    this.images = images;
    this.offerName = offerName;
    this.endDateTimeISO = endDateTimeISO;
    this.endDateTime = endDateTime;
    this.globalClaimCount = globalClaimCount;
    this.startDateTimeISO = startDateTimeISO;
    this.startDateTime = startDateTime;
    this.minimumPurchaseQty = minimumPurchaseQty;
    this.perUserLimit = perUserLimit;
    this.statusString = statusString;
    this.offerId = offerId;
    this.discountType = discountType;
    this.offerFor = offerFor;
    this.discountValue = discountValue;
    this.applicableOn = applicableOn;
    this.status = status;
  }

  protected ProductOfferDetails(Parcel in) {
    applicableOnStatus = in.readString();
    images = in.readParcelable(ImagesDetails.class.getClassLoader());
    endDateTimeISO = in.readLong();
    endDateTime = in.readString();
    globalClaimCount = in.readString();
    startDateTimeISO = in.readString();
    startDateTime = in.readString();
    minimumPurchaseQty = in.readString();
    perUserLimit = in.readString();
    statusString = in.readString();
    offerId = in.readString();
    discountType = in.readInt();
    offerFor = in.readString();
    discountValue = in.readLong();
    applicableOn = in.readString();
    status = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(applicableOnStatus);
    dest.writeParcelable(images, flags);
    dest.writeLong(endDateTimeISO);
    dest.writeString(endDateTime);
    dest.writeString(globalClaimCount);
    dest.writeString(startDateTimeISO);
    dest.writeString(startDateTime);
    dest.writeString(minimumPurchaseQty);
    dest.writeString(perUserLimit);
    dest.writeString(statusString);
    dest.writeString(offerId);
    dest.writeInt(discountType);
    dest.writeString(offerFor);
    dest.writeLong(discountValue);
    dest.writeString(applicableOn);
    dest.writeString(status);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductOfferDetails> CREATOR = new Creator<ProductOfferDetails>() {
    @Override
    public ProductOfferDetails createFromParcel(Parcel in) {
      return new ProductOfferDetails(in);
    }

    @Override
    public ProductOfferDetails[] newArray(int size) {
      return new ProductOfferDetails[size];
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

  public long getEndDateTimeISO() {
    return endDateTimeISO;
  }

  public void setEndDateTimeISO(long endDateTimeISO) {
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

  public String getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
  }

  public String getMinimumPurchaseQty() {
    return minimumPurchaseQty;
  }

  public void setMinimumPurchaseQty(String minimumPurchaseQty) {
    this.minimumPurchaseQty = minimumPurchaseQty;
  }

  public String getPerUserLimit() {
    return perUserLimit;
  }

  public void setPerUserLimit(String perUserLimit) {
    this.perUserLimit = perUserLimit;
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

  public int getDiscountType() {
    return discountType;
  }

  public void setDiscountType(int discountType) {
    this.discountType = discountType;
  }

  public String getOfferFor() {
    return offerFor;
  }

  public void setOfferFor(String offerFor) {
    this.offerFor = offerFor;
  }

  public long getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(long discountValue) {
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

  @Override
  public boolean isValid() {
    return true;
  }
}
