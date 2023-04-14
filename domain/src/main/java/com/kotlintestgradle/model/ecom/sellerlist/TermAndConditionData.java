package com.kotlintestgradle.model.ecom.sellerlist;

public class TermAndConditionData {

  private String en;

  private String es;

  public TermAndConditionData(String en, String es) {
    this.en = en;
    this.es = es;
  }

  public String getEn() {
    return en;
  }

  public void setEn(String en) {
    this.en = en;
  }

  public String getEs() {
    return es;
  }

  public void setEs(String es) {
    this.es = es;
  }
}
