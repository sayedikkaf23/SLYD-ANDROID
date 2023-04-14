package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class AttributesData implements Parcelable {
  public static final Creator<AttributesData> CREATOR = new Creator<AttributesData>() {
    @Override
    public AttributesData createFromParcel(Parcel in) {
      return new AttributesData(in);
    }

    @Override
    public AttributesData[] newArray(int size) {
      return new AttributesData[size];
    }
  };
  private String name;

  private ArrayList<InnerAttributesData> innerAttributes;

  public AttributesData(String name, ArrayList<InnerAttributesData> innerAttributes) {
    this.name = name;
    this.innerAttributes = innerAttributes;
  }

  protected AttributesData(Parcel in) {
    name = in.readString();
    innerAttributes = new ArrayList<InnerAttributesData>();
    in.readTypedList(innerAttributes, InnerAttributesData.CREATOR);

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<InnerAttributesData> getInnerAttributes() {
    return innerAttributes;
  }

  public void setInnerAttributes(ArrayList<InnerAttributesData> innerAttributes) {
    this.innerAttributes = innerAttributes;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeTypedList(innerAttributes);
  }
}
