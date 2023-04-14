package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.kotlintestgradle.remote.RemoteConstants.COMMENT;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRESCRIPTION_REQUIRED;
import static com.kotlintestgradle.remote.RemoteConstants.REASON;
import static com.kotlintestgradle.remote.RemoteConstants.TYPE;


public class CancelOrderRequest implements Parcelable {

  @Expose
  @SerializedName("type")
  private String type;
  @Expose
  @SerializedName("orderId")
  private String orderId;
  @Expose
  @SerializedName("reason")
  private String reason;
  @Expose
  @SerializedName("comment")
  private String comment;
  @Expose
  @SerializedName("prescriptionRequired")
  private boolean prescriptionRequired;

  public CancelOrderRequest(String type, String orderId, String reason, String comment) {
    this.type = type;
    this.orderId = orderId;
    this.reason = reason;
    this.comment = comment;
  }

  public CancelOrderRequest(String type, String orderId, String reason, String comment, boolean prescriptionRequired) {
    this.type = type;
    this.orderId = orderId;
    this.reason = reason;
    this.comment = comment;
    this.prescriptionRequired = prescriptionRequired;
  }

  protected CancelOrderRequest(Parcel in) {
    type = in.readString();
    orderId = in.readString();
    reason = in.readString();
    comment = in.readString();
  }

  public static final Creator<CancelOrderRequest> CREATOR = new Creator<CancelOrderRequest>() {
    @Override
    public  CancelOrderRequest createFromParcel(Parcel in) {
      return new  CancelOrderRequest(in);
    }

    @Override
    public  CancelOrderRequest[] newArray(int size) {
      return new  CancelOrderRequest[size];
    }
  };

  public  ArrayMap<String, Object> getQuery() {
   /* HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put(TYPE, type);
    hashMap.put(ORDER_ID, orderId);
    hashMap.put("reason", reason);
    hashMap.put("comment", comment);*/
    ArrayMap<String, Object> userIdMapper = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
      userIdMapper = new ArrayMap<>();
      userIdMapper.put(TYPE, type);
      userIdMapper.put(ORDER_ID, orderId);
      userIdMapper.put(REASON, reason);
      userIdMapper.put(COMMENT, comment);
      userIdMapper.put(PRESCRIPTION_REQUIRED, prescriptionRequired);
    }
    return userIdMapper;
  }
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isPrescriptionRequired() {
    return prescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    this.prescriptionRequired = prescriptionRequired;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type);
    dest.writeString(orderId);
    dest.writeString(reason);
    dest.writeString(comment);
  }
}
