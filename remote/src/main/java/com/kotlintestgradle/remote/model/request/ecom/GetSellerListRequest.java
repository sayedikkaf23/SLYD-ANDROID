package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;

public class GetSellerListRequest implements Parcelable {
  private String loginType;
  private String productId;
  private String parentProductId;

  public GetSellerListRequest(String loginType, String productId, String parentProductId) {
    this.loginType = loginType;
    this.productId = productId;
    this.parentProductId = parentProductId;
  }

  protected GetSellerListRequest(Parcel in) {
    loginType = in.readString();
    productId = in.readString();
    parentProductId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(loginType);
    dest.writeString(productId);
    dest.writeString(parentProductId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<GetSellerListRequest> CREATOR = new Creator<GetSellerListRequest>() {
    @Override
    public GetSellerListRequest createFromParcel(Parcel in) {
      return new GetSellerListRequest(in);
    }

    @Override
    public GetSellerListRequest[] newArray(int size) {
      return new GetSellerListRequest[size];
    }
  };

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }
}
