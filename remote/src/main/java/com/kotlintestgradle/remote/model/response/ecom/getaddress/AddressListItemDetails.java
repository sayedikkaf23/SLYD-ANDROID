package com.kotlintestgradle.remote.model.response.ecom.getaddress;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class AddressListItemDetails implements Parcelable , ValidItem {

  @SerializedName("mobileNumberCode")
  @Expose
  private String mobileNumberCode;

  @SerializedName("country")
  @Expose
  private String country;

  @SerializedName("pincode")
  @Expose
  private String pincode;

  @SerializedName("city")
  @Expose
  private String city;

  @SerializedName("mobileNumber")
  @Expose
  private String mobileNumber;

  @SerializedName("flatNumber")
  @Expose
  private String flatNumber;



  @SerializedName("alternatePhoneCode")
  @Expose
  private String alternatePhoneCode;

  @SerializedName("alternatePhone")
  @Expose
  private String alternatePhone;

  @SerializedName("locality")
  @Expose
  private String locality;

  @SerializedName("placeId")
  @Expose
  private String placeId;

  @SerializedName("createdTimeStamp")
  @Expose
  private String createdTimeStamp;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("createdIsoDate")
  @Expose
  private String createdIsoDate;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("addLine1")
  @Expose
  private String addLine1;

  @SerializedName("_id")
  @Expose
  private String _id;

  @SerializedName("addLine2")
  @Expose
  private String addLine2;

  @SerializedName("state")
  @Expose
  private String state;

  @SerializedName("userType")
  @Expose
  private String userType;

  @SerializedName("landmark")
  @Expose
  private String landmark;

  @SerializedName("taggedAs")
  @Expose
  private String taggedAs;

  @SerializedName("tagged")
  @Expose
  private String tagged;
  @SerializedName("latitude")
  @Expose
  private String latitude;
  @SerializedName("longitude")
  @Expose
  private String longitude;

  @SerializedName("default")
  @Expose
  private boolean defaultAd;

  @SerializedName("cityId")
  @Expose
  private String cityId;
  @SerializedName("countryId")
  @Expose
  private String countryId;

  public AddressListItemDetails(String mobileNumberCode, String country, String pincode,
      String city, String mobileNumber, String flatNumber, String latitude,
      String alternatePhoneCode, String alternatePhone, String locality, String placeId,
      String createdTimeStamp, String userId, String createdIsoDate, String name,
      String addLine1, String _id, String addLine2, String state, String userType,
      String landmark, String taggedAs, String longitude, String tagged) {
    this.mobileNumberCode = mobileNumberCode;
    this.country = country;
    this.pincode = pincode;
    this.city = city;
    this.mobileNumber = mobileNumber;
    this.flatNumber = flatNumber;
    this.latitude = latitude;
    this.alternatePhoneCode = alternatePhoneCode;
    this.alternatePhone = alternatePhone;
    this.locality = locality;
    this.placeId = placeId;
    this.createdTimeStamp = createdTimeStamp;
    this.userId = userId;
    this.createdIsoDate = createdIsoDate;
    this.name = name;
    this.addLine1 = addLine1;
    this._id = _id;
    this.addLine2 = addLine2;
    this.state = state;
    this.userType = userType;
    this.landmark = landmark;
    this.taggedAs = taggedAs;
    this.longitude = longitude;
    this.tagged = tagged;
  }

  protected AddressListItemDetails(Parcel in) {
    mobileNumberCode = in.readString();
    country = in.readString();
    pincode = in.readString();
    city = in.readString();
    mobileNumber = in.readString();
    flatNumber = in.readString();
    latitude = in.readString();
    alternatePhoneCode = in.readString();
    alternatePhone = in.readString();
    locality = in.readString();
    placeId = in.readString();
    createdTimeStamp = in.readString();
    userId = in.readString();
    createdIsoDate = in.readString();
    name = in.readString();
    addLine1 = in.readString();
    _id = in.readString();
    addLine2 = in.readString();
    state = in.readString();
    userType = in.readString();
    landmark = in.readString();
    taggedAs = in.readString();
    tagged = in.readString();
    latitude = in.readString();
    longitude = in.readString();
    defaultAd = in.readByte() != 0;
    cityId = in.readString();
    countryId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mobileNumberCode);
    dest.writeString(country);
    dest.writeString(pincode);
    dest.writeString(city);
    dest.writeString(mobileNumber);
    dest.writeString(flatNumber);
    dest.writeString(latitude);
    dest.writeString(alternatePhoneCode);
    dest.writeString(alternatePhone);
    dest.writeString(locality);
    dest.writeString(placeId);
    dest.writeString(createdTimeStamp);
    dest.writeString(userId);
    dest.writeString(createdIsoDate);
    dest.writeString(name);
    dest.writeString(addLine1);
    dest.writeString(_id);
    dest.writeString(addLine2);
    dest.writeString(state);
    dest.writeString(userType);
    dest.writeString(landmark);
    dest.writeString(taggedAs);
    dest.writeString(tagged);
    dest.writeString(latitude);
    dest.writeString(longitude);
    dest.writeByte((byte) (defaultAd ? 1 : 0));
    dest.writeString(cityId);
    dest.writeString(countryId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<AddressListItemDetails> CREATOR =
      new Creator<AddressListItemDetails>() {
        @Override
        public AddressListItemDetails createFromParcel(Parcel in) {
          return new AddressListItemDetails(in);
        }

        @Override
        public AddressListItemDetails[] newArray(int size) {
          return new AddressListItemDetails[size];
        }
      };

  public String getTagged() {
    return tagged;
  }

  public void setTagged(String tagged) {
    this.tagged = tagged;
  }

  public boolean isDefaultAd() {
    return defaultAd;
  }

  public void setDefaultAd(boolean defaultAd) {
    this.defaultAd = defaultAd;
  }

  public String getMobileNumberCode() {
    return mobileNumberCode;
  }

  public void setMobileNumberCode(String mobileNumberCode) {
    this.mobileNumberCode = mobileNumberCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getFlatNumber() {
    return flatNumber;
  }

  public void setFlatNumber(String flatNumber) {
    this.flatNumber = flatNumber;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
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

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public String getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  public void setCreatedTimeStamp(String createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getCreatedIsoDate() {
    return createdIsoDate;
  }

  public void setCreatedIsoDate(String createdIsoDate) {
    this.createdIsoDate = createdIsoDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddLine1() {
    return addLine1;
  }

  public void setAddLine1(String addLine1) {
    this.addLine1 = addLine1;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getAddLine2() {
    return addLine2;
  }

  public void setAddLine2(String addLine2) {
    this.addLine2 = addLine2;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getLandmark() {
    return landmark;
  }

  public void setLandmark(String landmark) {
    this.landmark = landmark;
  }

  public String getTaggedAs() {
    return taggedAs;
  }

  public void setTaggedAs(String taggedAs) {
    this.taggedAs = taggedAs;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCountryId() {
    return countryId;
  }

  public void setCountryId(String countryId) {
    this.countryId = countryId;
  }

  @Override
  public String toString() {
    return "AddressListItemDetails{" +
        "_id='" + _id + '\'' +
        '}';
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
