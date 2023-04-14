package com.kotlintestgradle.model.ecom.getaddress;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

public class AddressListItemData implements Parcelable {
  public static final Creator<AddressListItemData> CREATOR = new Creator<AddressListItemData>() {
    @Override
    public AddressListItemData createFromParcel(Parcel in) {
      return new AddressListItemData(in);
    }

    @Override
    public AddressListItemData[] newArray(int size) {
      return new AddressListItemData[size];
    }
  };
  private String mobileNumberCode;
  private String country;
  private String pincode;
  private String city;
  private String mobileNumber;
  private String flatNumber;
  private String latitude;
  private String alternatePhoneCode;
  private String alternatePhone;
  private String locality;
  private String placeId;
  private String createdTimeStamp;
  private String userId;
  private String createdIsoDate;
  private String name;
  private String addLine1;
  private String mId;
  private String addLine2;
  private String state;
  private String userType;
  private String landmark;
  private String taggedAs;
  private String longitude;
  private boolean isDefault;
  private int tagged;
  private String cityId;
  private String countryId;

  public AddressListItemData(String mobileNumberCode, String country, String pincode,
      String city, String mobileNumber, String flatNumber, String latitude,
      String alternatePhoneCode, String alternatePhone, String locality, String placeId,
      String createdTimeStamp, String userId, String createdIsoDate, String name,
      String addLine1, String id, String addLine2, String state, String userType,
      String landmark, String taggedAs, String longitude, boolean isDefault, String tagged,String cityId,String countryId) {
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
    this.mId = id;
    this.addLine2 = addLine2;
    this.state = state;
    this.userType = userType;
    this.landmark = landmark;
    this.taggedAs = taggedAs;
    this.longitude = longitude;
    this.isDefault = isDefault;
    if (!TextUtils.isEmpty(tagged) && TextUtils.isDigitsOnly(tagged)) {
      this.tagged = Integer.parseInt(tagged);
    }
    Log.d("exe","longitude"+longitude+"latitude"+latitude);
    this.cityId=cityId;
    this.countryId=countryId;
  }

  public AddressListItemData() {
  }

  protected AddressListItemData(Parcel in) {
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
    mId = in.readString();
    addLine2 = in.readString();
    state = in.readString();
    userType = in.readString();
    landmark = in.readString();
    taggedAs = in.readString();
    longitude = in.readString();
    taggedAs = in.readString();
    cityId = in.readString();
    countryId = in.readString();
    tagged = in.readInt();
    isDefault = in.readByte() != 0;
  }

  public AddressListItemData(String id) {
    this.mId = id;
  }

  public int getTagged() {
    return tagged;
  }

  public void setTagged(int tagged) {
    this.tagged = tagged;
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

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
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

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    this.mId = id;
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
  public int describeContents() {
    return 0;
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
    dest.writeString(mId);
    dest.writeString(cityId);
    dest.writeString(countryId);
    dest.writeString(addLine2);
    dest.writeString(state);
    dest.writeString(userType);
    dest.writeString(landmark);
    dest.writeString(taggedAs);
    dest.writeString(longitude);
    dest.writeString(taggedAs);
    dest.writeInt(tagged);
    dest.writeByte((byte) (isDefault ? 1 : 0));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AddressListItemData)) {
      return false;
    }
    AddressListItemData itemData = (AddressListItemData) o;
    return getId().equalsIgnoreCase(itemData.getId());
  }

  @Override
  public String toString() {
    return "AddressListItemData{" +
        "mobileNumberCode='" + mobileNumberCode + '\'' +
        ", country='" + country + '\'' +
        ", pincode='" + pincode + '\'' +
        ", city='" + city + '\'' +
        ", mobileNumber='" + mobileNumber + '\'' +
        ", flatNumber='" + flatNumber + '\'' +
        ", latitude=" + latitude +
        ", alternatePhoneCode='" + alternatePhoneCode + '\'' +
        ", alternatePhone='" + alternatePhone + '\'' +
        ", locality='" + locality + '\'' +
        ", placeId='" + placeId + '\'' +
        ", createdTimeStamp='" + createdTimeStamp + '\'' +
        ", userId='" + userId + '\'' +
        ", createdIsoDate='" + createdIsoDate + '\'' +
        ", name='" + name + '\'' +
        ", addLine1='" + addLine1 + '\'' +
        ", mId='" + mId + '\'' +
        ", cityId='" + cityId + '\'' +
        ", countryId='" + countryId + '\'' +
        ", addLine2='" + addLine2 + '\'' +
        ", state='" + state + '\'' +
        ", userType='" + userType + '\'' +
        ", landmark='" + landmark + '\'' +
        ", taggedAs='" + taggedAs + '\'' +
        ", longitude=" + longitude +
        ", isDefault=" + isDefault +
        ", tagged=" + tagged +
        '}';
  }
}
