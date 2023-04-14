package com.kotlintestgradle.model.ecom.home;

import com.kotlintestgradle.model.ecom.common.ImageData;

public class CategoryOfferData {
  private String startDateTimeISO;
  private String applicableOnStatus;
  private ImageData images;
  private String offerName;
  private String mobimage;
  private String discountValue;
  private long endDateTimeISO;
  private int discountType;

  private String endDateTime;

  private String globalClaimCount;

  private String startDateTime;

  private String minimumPurchaseQty;

  private String perUserLimit;

  private String statusString;

  private String offerId;

  private String offerFor;

  private String applicableOn;

  private String status;

  public CategoryOfferData(String startDateTimeISO, String applicableOnStatus,
      ImageData images, String offerName,
      String mobimage, double discountValue, long endDateTimeISO, int discountType) {
    this.startDateTimeISO = startDateTimeISO;
    this.applicableOnStatus = applicableOnStatus;
    this.images = images;
    this.offerName = offerName;
    this.mobimage = mobimage;
    this.discountValue = ((int) discountValue) + "";
    this.endDateTimeISO = endDateTimeISO;
    this.discountType = discountType;
  }

  public CategoryOfferData(String startDateTimeISO, String applicableOnStatus,
      ImageData images, String offerName, String discountValue, long endDateTimeISO,
      int discountType,
      String endDateTime, String globalClaimCount, String startDateTime,
      String minimumPurchaseQty, String perUserLimit, String statusString, String offerId,
      String offerFor, String applicableOn, String status) {
    this.startDateTimeISO = startDateTimeISO;
    this.applicableOnStatus = applicableOnStatus;
    this.images = images;
    this.offerName = offerName;
    this.discountValue = discountValue;
    this.endDateTimeISO = endDateTimeISO;
    this.discountType = discountType;
    this.endDateTime = endDateTime;
    this.globalClaimCount = globalClaimCount;
    this.startDateTime = startDateTime;
    this.minimumPurchaseQty = minimumPurchaseQty;
    this.perUserLimit = perUserLimit;
    this.statusString = statusString;
    this.offerId = offerId;
    this.offerFor = offerFor;
    this.applicableOn = applicableOn;
    this.status = status;
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

  public String getOfferFor() {
    return offerFor;
  }

  public void setOfferFor(String offerFor) {
    this.offerFor = offerFor;
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

  public int getDiscountType() {
    return discountType;
  }

  public void setDiscountType(int discountType) {
    this.discountType = discountType;
  }

  public long getEndDateTimeISO() {
    return endDateTimeISO;
  }

  public void setEndDateTimeISO(long endDateTimeISO) {
    this.endDateTimeISO = endDateTimeISO;
  }

  public String getMobimage() {
    return mobimage;
  }

  public void setMobimage(String mobimage) {
    this.mobimage = mobimage;
  }

  public String getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public String getStartDateTimeISO() {
    return startDateTimeISO;
  }

  public void setStartDateTimeISO(String startDateTimeISO) {
    this.startDateTimeISO = startDateTimeISO;
  }

  public String getApplicableOnStatus() {
    return applicableOnStatus;
  }

  public void setApplicableOnStatus(String applicableOnStatus) {
    this.applicableOnStatus = applicableOnStatus;
  }

  public ImageData getImages() {
    return images;
  }

  public void setImages(ImageData images) {
    this.images = images;
  }

  public String getOfferName() {
    return offerName;
  }

  public void setOfferName(String offerName) {
    this.offerName = offerName;
  }
}
