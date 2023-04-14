package com.kotlintestgradle.model.ecom.pdp;

import com.kotlintestgradle.model.ecom.common.ImageData;

public class PdpOfferData {
  private String applicableOnStatus;

  private ImageData images;

  private String offerName;

  private String endDateTimeISO;

  private String endDateTime;

  private String globalClaimCount;

  private String startDateTimeISO;

  private ImageData webimages;

  private String startDateTime;

  private String perUserLimit;

  private String minimumPurchaseQty;

  private String statusString;

  private String offerId;

  private String discountType;

  private String offerFor;

  private String discountValue;

  private String applicableOn;

  private String status;
  private String termscond;

  public PdpOfferData(){

  }
  public PdpOfferData(String applicableOnStatus, ImageData images, String offerName,
      String endDateTimeISO, String endDateTime, String globalClaimCount, String startDateTimeISO,
      ImageData webimages, String startDateTime, String perUserLimit, String minimumPurchaseQty,
      String statusString, String offerId, String discountType, String offerFor,
      String discountValue, String applicableOn, String status,String termscond) {
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
    this.termscond=termscond;
  }
public PdpOfferData( String offerName,
     String offerId, String discountType,
      String discountValue) {
    this.offerName = offerName;
    this.offerId = offerId;
    this.discountType = discountType;
    this.discountValue = discountValue;

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

  public ImageData getWebimages() {
    return webimages;
  }

  public void setWebimages(ImageData webimages) {
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
}
