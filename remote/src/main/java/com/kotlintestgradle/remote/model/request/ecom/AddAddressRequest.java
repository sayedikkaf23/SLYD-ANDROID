package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAddressRequest implements Parcelable {

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("mobileNumber")
  @Expose
  private String mobileNumber;

  @SerializedName("mobileNumberCode")
  @Expose
  private String mobileNumberCode;

  @SerializedName("alternatePhoneCode")
  @Expose
  private String alternatePhoneCode;

  @SerializedName("alternatePhone")
  @Expose
  private String alternatePhone;

  @SerializedName("locality")
  @Expose
  private String locality;

  @SerializedName("addLine1")
  @Expose
  private String addLine1;

  @SerializedName("addLine2")
  @Expose
  private String addLine2;

  @SerializedName("flatNumber")
  @Expose
  private String flatNumber;

  @SerializedName("landmark")
  @Expose
  private String landmark;

  @SerializedName("city")
  @Expose
  private String city;

  @SerializedName("state")
  @Expose
  private String state;

  @SerializedName("country")
  @Expose
  private String country;

  @SerializedName("placeId")
  @Expose
  private String placeId;

  @SerializedName("pincode")
  @Expose
  private String pincode;

  @SerializedName("latitude")
  @Expose
  private String latitude;

  @SerializedName("longitude")
  @Expose
  private String longitude;

  @SerializedName("taggedAs")
  @Expose
  private String taggedAs;

  @SerializedName("addressId")
  @Expose
  private String addressId;

  @SerializedName("tagged")
  @Expose
  private int tagged;

  @SerializedName("default")
  @Expose
  private boolean isDefault;

  public AddAddressRequest(String userId, String name, String mobileNumber,
      String mobileNumberCode, String locality, String addLine1, String addLine2,
      String landmark, String city, String country,
      String placeId, String pincode, String latitude, String longitude, String taggedAs,
      String state, int tagged, boolean isDefault) {
    this.userId = userId;
    this.name = name;
    this.mobileNumber = mobileNumber;
    this.mobileNumberCode = mobileNumberCode;
    this.locality = locality;
    this.addLine1 = addLine1;
    this.addLine2 = addLine2;
    this.flatNumber = flatNumber;
    this.landmark = landmark;
    this.city = city;
    this.state = state;
    this.country = country;
    this.placeId = placeId;
    this.pincode = pincode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.taggedAs = taggedAs;
    this.tagged = tagged;
    this.isDefault = isDefault;
  }

  public AddAddressRequest(String userId, String name, String mobileNumber,
      String mobileNumberCode,
      String locality, String addLine1, String addLine2, String flatNumber, String landmark,
      String city, String state, String country, String placeId, String pincode, String latitude,
      String longitude, String taggedAs, String addressId, boolean isDefault) {
    this.userId = userId;
    this.name = name;
    this.mobileNumber = mobileNumber;
    this.mobileNumberCode = mobileNumberCode;
    this.alternatePhoneCode = alternatePhoneCode;
    this.alternatePhone = alternatePhone;
    this.locality = locality;
    this.addLine1 = addLine1;
    this.addLine2 = addLine2;
    this.flatNumber = flatNumber;
    this.landmark = landmark;
    this.city = city;
    this.state = state;
    this.country = country;
    this.placeId = placeId;
    this.pincode = pincode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.taggedAs = taggedAs;
    this.addressId = addressId;
    this.isDefault = isDefault;
  }

  protected AddAddressRequest(Parcel in) {
    userId = in.readString();
    name = in.readString();
    mobileNumber = in.readString();
    mobileNumberCode = in.readString();
    alternatePhoneCode = in.readString();
    alternatePhone = in.readString();
    locality = in.readString();
    addLine1 = in.readString();
    addLine2 = in.readString();
    flatNumber = in.readString();
    landmark = in.readString();
    city = in.readString();
    state = in.readString();
    country = in.readString();
    placeId = in.readString();
    pincode = in.readString();
    latitude = in.readString();
    longitude = in.readString();
    taggedAs = in.readString();
    addressId = in.readString();
    tagged = in.readInt();
    isDefault = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(userId);
    dest.writeString(name);
    dest.writeString(mobileNumber);
    dest.writeString(mobileNumberCode);
    dest.writeString(alternatePhoneCode);
    dest.writeString(alternatePhone);
    dest.writeString(locality);
    dest.writeString(addLine1);
    dest.writeString(addLine2);
    dest.writeString(flatNumber);
    dest.writeString(landmark);
    dest.writeString(city);
    dest.writeString(state);
    dest.writeString(country);
    dest.writeString(placeId);
    dest.writeString(pincode);
    dest.writeString(latitude);
    dest.writeString(longitude);
    dest.writeString(taggedAs);
    dest.writeString(addressId);
    dest.writeInt(tagged);
    dest.writeByte((byte) (isDefault ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<AddAddressRequest> CREATOR = new Creator<AddAddressRequest>() {
    @Override
    public AddAddressRequest createFromParcel(Parcel in) {
      return new AddAddressRequest(in);
    }

    @Override
    public AddAddressRequest[] newArray(int size) {
      return new AddAddressRequest[size];
    }
  };

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean aDefault) {
    isDefault = aDefault;
  }

  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public int getTagged() {
    return tagged;
  }

  public void setTagged(int tagged) {
    this.tagged = tagged;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getMobileNumberCode() {
    return mobileNumberCode;
  }

  public void setMobileNumberCode(String mobileNumberCode) {
    this.mobileNumberCode = mobileNumberCode;
  }

  public String getAlternatePhoneCode() {
    return alternatePhoneCode;
  }

  public void setAlternatePhoneCode(String alternatePhoneCode) {
    this.alternatePhoneCode = alternatePhoneCode;
  }

  public String getAlternatePhone() {
    return alternatePhone;
  }

  public void setAlternatePhone(String alternatePhone) {
    this.alternatePhone = alternatePhone;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getAddLine1() {
    return addLine1;
  }

  public void setAddLine1(String addLine1) {
    this.addLine1 = addLine1;
  }

  public String getAddLine2() {
    return addLine2;
  }

  public void setAddLine2(String addLine2) {
    this.addLine2 = addLine2;
  }

  public String getFlatNumber() {
    return flatNumber;
  }

  public void setFlatNumber(String flatNumber) {
    this.flatNumber = flatNumber;
  }

  public String getLandmark() {
    return landmark;
  }

  public void setLandmark(String landmark) {
    this.landmark = landmark;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getTaggedAs() {
    return taggedAs;
  }

  public void setTaggedAs(String taggedAs) {
    this.taggedAs = taggedAs;
  }
}
