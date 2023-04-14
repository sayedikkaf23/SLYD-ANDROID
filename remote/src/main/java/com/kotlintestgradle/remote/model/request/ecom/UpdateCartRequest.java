package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.model.response.ecom.pdp.PdpOfferDetails;

public class UpdateCartRequest implements Parcelable {

  @SerializedName("userType")
  @Expose
  private int userType;
  @SerializedName("centralProductId")
  @Expose
  private String centralProductId;
  @SerializedName("productId")
  @Expose
  private String productId;
  @SerializedName("unitId")
  @Expose
  private String unitId;
  @SerializedName("storeId")
  @Expose
  private String storeId;
  @SerializedName("storeCategoryId")
  @Expose
  private String storeCategoryId;
  @SerializedName("storeTypeId")
  @Expose
  private int storeTypeId;
  @SerializedName("productName")
  @Expose
  private String productName;
  @SerializedName("estimatedProductPrice")
  @Expose
  private int estimatedProductPrice;
  @SerializedName("extraNotes")
  @Expose
  private String extraNotes;
  @SerializedName("newQuantity")
  @Expose
  private int newQuantity;
  @SerializedName("oldQuantity")
  @Expose
  private int oldQuantity;
  @SerializedName("action")
  @Expose
  private int action;
  @SerializedName("cartType")
  @Expose
  private int cartType;
  @SerializedName("userIp")
  @Expose
  private String userIp;
  @SerializedName("latitude")
  @Expose
  private double latitude;
  @SerializedName("longitude")
  @Expose
  private double longitude;
  @SerializedName("offers")
  @Expose
  private PdpOfferDetails offer;

  public UpdateCartRequest(int userType, String centralProductId,
      String productId, String unitId, String storeId, String storeCategoryId, int storeTypeId,
      String productName,
      int estimatedProductPrice, String extraNotes, int newQuantity, int oldQuantity,
      int action, int cartType, String userIp, double latitude, double longitude) {
    this.userType = userType;
    this.centralProductId = centralProductId;
    this.productId = productId;
    this.unitId = unitId;
    this.storeId = storeId;
    this.storeCategoryId = storeCategoryId;
    this.storeTypeId = storeTypeId;
    this.productName = productName;
    this.estimatedProductPrice = estimatedProductPrice;
    this.extraNotes = extraNotes;
    this.newQuantity = newQuantity;
    this.oldQuantity = oldQuantity;
    this.action = action;
    this.cartType = cartType;
    this.userIp = userIp;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public UpdateCartRequest(int userType, String centralProductId,
                           String productId, String unitId, String storeId, String storeCategoryId, int storeTypeId,
                           String productName,
                           int estimatedProductPrice, String extraNotes, int newQuantity, int oldQuantity,
                           int action, int cartType, String userIp, double latitude, double longitude, PdpOfferDetails offer) {
    this.userType = userType;
    this.centralProductId = centralProductId;
    this.productId = productId;
    this.unitId = unitId;
    this.storeId = storeId;
    this.storeCategoryId = storeCategoryId;
    this.storeTypeId = storeTypeId;
    this.productName = productName;
    this.estimatedProductPrice = estimatedProductPrice;
    this.extraNotes = extraNotes;
    this.newQuantity = newQuantity;
    this.oldQuantity = oldQuantity;
    this.action = action;
    this.cartType = cartType;
    this.userIp = userIp;
    this.latitude = latitude;
    this.longitude = longitude;
    this.offer = offer;
  }


  protected UpdateCartRequest(Parcel in) {
    userType = in.readInt();
    centralProductId = in.readString();
    productId = in.readString();
    unitId = in.readString();
    storeId = in.readString();
    storeCategoryId = in.readString();
    storeTypeId = in.readInt();
    productName = in.readString();
    estimatedProductPrice = in.readInt();
    extraNotes = in.readString();
    newQuantity = in.readInt();
    oldQuantity = in.readInt();
    action = in.readInt();
    cartType = in.readInt();
    userIp = in.readString();
    latitude = in.readDouble();
    longitude = in.readDouble();
    offer = in.readParcelable(PdpOfferDetails.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(userType);
    dest.writeString(centralProductId);
    dest.writeString(productId);
    dest.writeString(unitId);
    dest.writeString(storeId);
    dest.writeString(storeCategoryId);
    dest.writeInt(storeTypeId);
    dest.writeString(productName);
    dest.writeInt(estimatedProductPrice);
    dest.writeString(extraNotes);
    dest.writeInt(newQuantity);
    dest.writeInt(oldQuantity);
    dest.writeInt(action);
    dest.writeInt(cartType);
    dest.writeString(userIp);
    dest.writeDouble(latitude);
    dest.writeDouble(longitude);
    dest.writeParcelable(offer, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<UpdateCartRequest> CREATOR = new Creator<UpdateCartRequest>() {
    @Override
    public UpdateCartRequest createFromParcel(Parcel in) {
      return new UpdateCartRequest(in);
    }

    @Override
    public UpdateCartRequest[] newArray(int size) {
      return new UpdateCartRequest[size];
    }
  };

  public int getUserType() {
    return userType;
  }

  public void setUserType(int userType) {
    this.userType = userType;
  }

  public String getCentralProductId() {
    return centralProductId;
  }

  public void setCentralProductId(String centralProductId) {
    this.centralProductId = centralProductId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public int getStoreTypeId() {
    return storeTypeId;
  }

  public void setStoreTypeId(int storeTypeId) {
    this.storeTypeId = storeTypeId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getEstimatedProductPrice() {
    return estimatedProductPrice;
  }

  public void setEstimatedProductPrice(int estimatedProductPrice) {
    this.estimatedProductPrice = estimatedProductPrice;
  }

  public String getExtraNotes() {
    return extraNotes;
  }

  public void setExtraNotes(String extraNotes) {
    this.extraNotes = extraNotes;
  }

  public int getNewQuantity() {
    return newQuantity;
  }

  public void setNewQuantity(int newQuantity) {
    this.newQuantity = newQuantity;
  }

  public int getOldQuantity() {
    return oldQuantity;
  }

  public void setOldQuantity(int oldQuantity) {
    this.oldQuantity = oldQuantity;
  }

  public int getAction() {
    return action;
  }

  public PdpOfferDetails getOffer() {
    return offer;
  }

  public void setOffer(PdpOfferDetails offer) {
    this.offer = offer;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public int getCartType() {
    return cartType;
  }

  public void setCartType(int cartType) {
    this.cartType = cartType;
  }

  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getStoreCategoryId() {
    return storeCategoryId;
  }

  public void setStoreCategoryId(String storeCategoryId) {
    this.storeCategoryId = storeCategoryId;
  }
}
