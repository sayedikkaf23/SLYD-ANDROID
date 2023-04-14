package com.kotlintestgradle.model.ecom.getcart;

public class CartLogsData {
  private String timeStampIso;

  private String statusMsg;

  private String userType;

  private String userId;

  private String status;

  private String timestamp;

  public CartLogsData(String timeStampIso, String statusMsg, String userType, String userId,
      String status, String timestamp) {
    this.timeStampIso = timeStampIso;
    this.statusMsg = statusMsg;
    this.userType = userType;
    this.userId = userId;
    this.status = status;
    this.timestamp = timestamp;
  }

  public String getTimeStampIso() {
    return timeStampIso;
  }

  public void setTimeStampIso(String timeStampIso) {
    this.timeStampIso = timeStampIso;
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg) {
    this.statusMsg = statusMsg;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
