package com.kotlintestgradle.remote.model.response.ecom.getratable;

import android.os.Parcel;
import android.os.Parcelable;
import com.customer.remote.http.model.response.getratable.DriverDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class RatableDetails implements Parcelable, ValidItem {
  @SerializedName("reviewData")
  @Expose
  private ArrayList<RatableProducts> reviewData;
  @SerializedName("sellerData")
  @Expose
  private SellerDetails sellerData;

  @SerializedName("driverData")
  @Expose
  private DriverDetails driverData;
  @SerializedName("message")
  @Expose
  private String message;

  public RatableDetails(
      ArrayList<RatableProducts> reviewData,SellerDetails sellerData, String message) {
    this.reviewData = reviewData;
    this.sellerData=sellerData;
    this.message = message;
  }

  protected RatableDetails(Parcel in) {
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<RatableDetails> CREATOR = new Creator<RatableDetails>() {
    @Override
    public RatableDetails createFromParcel(Parcel in) {
      return new RatableDetails(in);
    }

    @Override
    public RatableDetails[] newArray(int size) {
      return new RatableDetails[size];
    }
  };

  public ArrayList<RatableProducts> getReviewData() {
    return reviewData;
  }

  public void setReviewData(
      ArrayList<RatableProducts> reviewData) {
    this.reviewData = reviewData;
  }

  public SellerDetails getSellerData() {
    return sellerData;
  }

  public void setSellerData(SellerDetails sellerData) {
    this.sellerData = sellerData;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public DriverDetails getDriverData() {
    return driverData;
  }

  public void setDriverData(DriverDetails driverData) {
    this.driverData = driverData;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
