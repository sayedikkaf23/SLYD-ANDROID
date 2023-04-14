package com.kotlintestgradle.model.orderdetails;

public class DeliveryStatusData {

  private String statusText;

  private String time;

  private String formatedDate;

  private String status;

  public DeliveryStatusData(String statusText, String time, String formatedDate,
      String status) {
    this.statusText = statusText;
    this.time = time;
    this.formatedDate = formatedDate;
    this.status = status;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getFormatedDate() {
    return formatedDate;
  }

  public void setFormatedDate(String formatedDate) {
    this.formatedDate = formatedDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
