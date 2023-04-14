package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PdpRequest implements Parcelable {
  @SerializedName("productId")
  @Expose
  private String productId;

  @SerializedName("parentProductId")
  @Expose
  private String parentProductId;

  public PdpRequest(String productId, String parentProductId) {
    this.productId = productId;
    this.parentProductId = parentProductId;
  }

  protected PdpRequest(Parcel in) {
    productId = in.readString();
    parentProductId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(productId);
    dest.writeString(parentProductId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PdpRequest> CREATOR = new Creator<PdpRequest>() {
    @Override
    public PdpRequest createFromParcel(Parcel in) {
      return new PdpRequest(in);
    }

    @Override
    public PdpRequest[] newArray(int size) {
      return new PdpRequest[size];
    }
  };

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }
}
