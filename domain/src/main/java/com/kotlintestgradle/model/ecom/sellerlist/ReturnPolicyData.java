package com.kotlintestgradle.model.ecom.sellerlist;

public class ReturnPolicyData {

  private boolean mIsReturn;

  private int noOfDays;

  public ReturnPolicyData(boolean isReturn, int noOfDays) {
    this.mIsReturn = isReturn;
    this.noOfDays = noOfDays;
  }

  public boolean isReturn() {
    return mIsReturn;
  }

  public void setReturn(boolean isReturn) {
    mIsReturn = isReturn;
  }

  public int getNoOfDays() {
    return noOfDays;
  }

  public void setNoOfDays(int noOfDays) {
    this.noOfDays = noOfDays;
  }
}
