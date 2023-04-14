package com.kotlintestgradle.model.ecom.sellerlist;

public class ReplacementPolicyData {

  private int noofdays;

  private boolean isReplacement;

  public ReplacementPolicyData(int noofdays, boolean isReplacement) {
    this.noofdays = noofdays;
    this.isReplacement = isReplacement;
  }

  public int getNoofdays() {
    return noofdays;
  }

  public void setNoofdays(int noofdays) {
    this.noofdays = noofdays;
  }

  public boolean isReplacement() {
    return isReplacement;
  }

  public void setReplacement(boolean replacement) {
    isReplacement = replacement;
  }
}
