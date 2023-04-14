package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotifyProductAvailabilityRequest implements Parcelable {

  @SerializedName("childproductId")
  @Expose
  private String childProductId;

  @SerializedName("email")
  @Expose
  private String email;

  @SerializedName("parentproductId")
  @Expose
  private String parentProductId;

  public NotifyProductAvailabilityRequest(String childProductId, String email,
      String parentProductId) {
    this.childProductId = childProductId;
    this.email = email;
    this.parentProductId = parentProductId;
  }

  protected NotifyProductAvailabilityRequest(Parcel in) {
    childProductId = in.readString();
    email = in.readString();
    parentProductId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(childProductId);
    dest.writeString(email);
    dest.writeString(parentProductId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<NotifyProductAvailabilityRequest> CREATOR =
      new Creator<NotifyProductAvailabilityRequest>() {
        @Override
        public NotifyProductAvailabilityRequest createFromParcel(Parcel in) {
          return new NotifyProductAvailabilityRequest(in);
        }

        @Override
        public NotifyProductAvailabilityRequest[] newArray(int size) {
          return new NotifyProductAvailabilityRequest[size];
        }
      };

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }
}
