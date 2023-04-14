package com.kotlintestgradle.model.ecom.sellerlist;

public class ExchangePolicyData {

  private int noofdays;

  private boolean isExchange;

  public ExchangePolicyData(int noofdays, boolean isExchange) {
    this.noofdays = noofdays;
    this.isExchange = isExchange;
  }

  public int getNoofdays() {
    return noofdays;
  }

  public void setNoofdays(int noofdays) {
    this.noofdays = noofdays;
  }

  public boolean isExchange() {
    return isExchange;
  }

  public void setExchange(boolean exchange) {
    isExchange = exchange;
  }
}
