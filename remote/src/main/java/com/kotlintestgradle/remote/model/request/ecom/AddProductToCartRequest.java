package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.model.response.ecom.pdp.PdpOfferDetails;

public class AddProductToCartRequest implements Parcelable {
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
  @SerializedName("quantity")
  @Expose
  private int quantity;
  @SerializedName("cartType")
  @Expose
  private int cartType;
  @SerializedName("notes")
  @Expose
  private String notes;
  @SerializedName("offers")
  @Expose
  private PdpOfferDetails offers;
  @SerializedName("userIp")
  @Expose
  private String userIp;
  @SerializedName("userPostCode")
  @Expose
  private String userPostCode;
  @SerializedName("latitude")
  @Expose
  private double latitude;
  @SerializedName("longitude")
  @Expose
  private double longitude;
  @SerializedName("city")
  @Expose
  private String city;
  @SerializedName("storeTypeId")
  @Expose
  private int storeTypeId;
  @SerializedName("productName")
  @Expose
  private String productName;
  @SerializedName("estimatedProductPrice")
  @Expose
  private String estimatedProductPrice;
  @SerializedName("storeCategoryId")
  @Expose
  private String storeCategoryId;
  @SerializedName("deliveryAddressId")
  @Expose
  private String deliveryAddressId;
  @SerializedName("extraNotes")
  @Expose
  private String extraNotes;

  @SerializedName("action")
  @Expose
  private int action;


  public AddProductToCartRequest(int userType, String centralProductId,
      String productId, String unitId, String storeId, int quantity, int cartType,
      String notes, String userIp,String storeCategoryId, String userPostCode, double latitude, double longitude,
      String city, int storeTypeId, String productName, String estimatedProductPrice,
      String extraNotes,int action) {
    this.userType = userType;
    this.centralProductId = centralProductId;
    this.productId = productId;
    this.unitId = unitId;
    this.storeId = storeId;
    this.quantity = quantity;
    this.cartType = cartType;
    this.notes = notes;
    this.userIp = userIp;
    this.storeCategoryId=storeCategoryId;
    this.userPostCode = userPostCode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = city;
    this.storeTypeId = storeTypeId;
    this.productName = productName;
    this.estimatedProductPrice = estimatedProductPrice;
    this.extraNotes = extraNotes;
    this.action = action;
  }

  public AddProductToCartRequest(int userType, String centralProductId, String productId, String unitId,
                                 String storeId, int quantity, int cartType, String notes, PdpOfferDetails offers,
                                 String userIp, String userPostCode, double latitude, double longitude, String city,
                                 int storeTypeId, String productName, String estimatedProductPrice, String storeCategoryId,
                                 String deliveryAddressId, String extraNotes, int action) {
    this.userType = userType;
    this.centralProductId = centralProductId;
    this.productId = productId;
    this.unitId = unitId;
    this.storeId = storeId;
    this.quantity = quantity;
    this.cartType = cartType;
    this.notes = notes;
    this.offers = offers;
    this.userIp = userIp;
    this.userPostCode = userPostCode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = city;
    this.storeTypeId = storeTypeId;
    this.productName = productName;
    this.estimatedProductPrice = estimatedProductPrice;
    this.storeCategoryId = storeCategoryId;
    this.deliveryAddressId = deliveryAddressId;
    this.extraNotes = extraNotes;
    this.action = action;
  }



  public AddProductToCartRequest(int userType, String centralProductId,
                                 String productId, String storeId, int quantity, int cartType,
                                 String storeCategoryId , int storeTypeId, int action) {
    this.userType = userType;
    this.centralProductId = centralProductId;
    this.productId = productId;
    this.storeId = storeId;
    this.quantity = quantity;
    this.cartType = cartType;
    this.storeCategoryId=storeCategoryId;
    this.storeTypeId = storeTypeId;
    this.action = action;
  }

  public AddProductToCartRequest(int userType, String centralProductId,
      String productId, String unitId, String storeId, int quantity, int cartType,
      String notes, String userIp,String storeCategoryId ,String userPostCode, double latitude,
      double longitude, String city, int storeTypeId, String productName,
      String estimatedProductPrice, String extraNotes, PdpOfferDetails offers) {
    this.userType = userType;
    this.centralProductId = centralProductId;
    this.productId = productId;
    this.unitId = unitId;
    this.storeId = storeId;
    this.quantity = quantity;
    this.cartType = cartType;
    this.notes = notes;
    this.offers = offers;
    this.userIp = userIp;
    this.storeCategoryId=storeCategoryId;
    this.userPostCode = userPostCode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = city;
    this.storeTypeId = storeTypeId;
    this.productName = productName;
    this.estimatedProductPrice = estimatedProductPrice;
    this.extraNotes = extraNotes;
  }

  protected AddProductToCartRequest(Parcel in) {
    userType = in.readInt();
    centralProductId = in.readString();
    productId = in.readString();
    unitId = in.readString();
    storeId = in.readString();
    quantity = in.readInt();
    cartType = in.readInt();
    notes = in.readString();
    offers = in.readParcelable(PdpOfferDetails.class.getClassLoader());
    userIp = in.readString();
    userPostCode = in.readString();
    latitude = in.readDouble();
    longitude = in.readDouble();
    city = in.readString();
    storeTypeId = in.readInt();
    productName = in.readString();
    estimatedProductPrice = in.readString();
    storeCategoryId = in.readString();
    deliveryAddressId = in.readString();
    extraNotes = in.readString();
    action = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(userType);
    dest.writeString(centralProductId);
    dest.writeString(productId);
    dest.writeString(unitId);
    dest.writeString(storeId);
    dest.writeInt(quantity);
    dest.writeInt(cartType);
    dest.writeString(notes);
    dest.writeParcelable(offers, flags);
    dest.writeString(userIp);
    dest.writeString(userPostCode);
    dest.writeDouble(latitude);
    dest.writeDouble(longitude);
    dest.writeString(city);
    dest.writeInt(storeTypeId);
    dest.writeString(productName);
    dest.writeString(estimatedProductPrice);
    dest.writeString(storeCategoryId);
    dest.writeString(deliveryAddressId);
    dest.writeString(extraNotes);
    dest.writeInt(action);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<AddProductToCartRequest> CREATOR = new Creator<AddProductToCartRequest>() {
    @Override
    public AddProductToCartRequest createFromParcel(Parcel in) {
      return new AddProductToCartRequest(in);
    }

    @Override
    public AddProductToCartRequest[] newArray(int size) {
      return new AddProductToCartRequest[size];
    }
  };

  public PdpOfferDetails getOffers() {
    return offers;
  }

  public void setOffers(PdpOfferDetails offers) {
    this.offers = offers;
  }

  public int getUserType() {
    return userType;
  }

  public void setUserType(int userType) {
    this.userType = userType;
  }

  /*public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }*/

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

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getCartType() {
    return cartType;
  }

  public void setCartType(int cartType) {
    this.cartType = cartType;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  public String getUserPostCode() {
    return userPostCode;
  }

  public void setUserPostCode(String userPostCode) {
    this.userPostCode = userPostCode;
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

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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

  public String getEstimatedProductPrice() {
    return estimatedProductPrice;
  }

  public void setEstimatedProductPrice(String estimatedProductPrice) {
    this.estimatedProductPrice = estimatedProductPrice;
  }

  public String getExtraNotes() {
    return extraNotes;
  }

  public void setExtraNotes(String extraNotes) {
    this.extraNotes = extraNotes;
  }

  public String getStoreCategoryId() {
    return storeCategoryId;
  }

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public void setStoreCategoryId(String storeCategoryId) {
    this.storeCategoryId = storeCategoryId;
  }

  public String getDeliveryAddressId() {
    return deliveryAddressId;
  }

  public void setDeliveryAddressId(String deliveryAddressId) {
    this.deliveryAddressId = deliveryAddressId;
  }
}
