package com.kotlintestgradle.model.ecom.promocode;

public class ApplyPromoCodeData  {

  private float mTotalAmt;

  private float price;

  private float mDeliveryFee;

  private float mReducedAmt;
  public ApplyPromoCodeData(float totalAmt, float price, float deliveryFee,
      float reducedAmt) {
    this.mTotalAmt = totalAmt;
    this.price = price;
    this.mDeliveryFee = deliveryFee;
    this.mReducedAmt = reducedAmt;
  }



  public float getTotalAmt() {
    return mTotalAmt;
  }

  public void setTotalAmt(float totalAmt) {
    this.mTotalAmt = totalAmt;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getDeliveryFee() {
    return mDeliveryFee;
  }

  public void setDeliveryFee(float deliveryFee) {
    this.mDeliveryFee = deliveryFee;
  }

  public float getReducedAmt() {
    return mReducedAmt;
  }

  public void setReducedAmt(float reducedAmt) {
    this.mReducedAmt = reducedAmt;
  }


}
