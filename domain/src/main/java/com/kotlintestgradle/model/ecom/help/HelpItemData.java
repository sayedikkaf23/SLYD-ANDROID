package com.kotlintestgradle.model.ecom.help;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class HelpItemData implements Parcelable {

  private String desc;

  private ArrayList<HelpSubCatListData> subcat;

  private String link;

  private String name;

  public HelpItemData(String desc,
      ArrayList<HelpSubCatListData> subcat, String link, String name) {
    this.desc = desc;
    this.subcat = subcat;
    this.link = link;
    this.name = name;
  }

  protected HelpItemData(Parcel in) {
    desc = in.readString();
    subcat = in.createTypedArrayList(HelpSubCatListData.CREATOR);
    link = in.readString();
    name = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(desc);
    dest.writeTypedList(subcat);
    dest.writeString(link);
    dest.writeString(name);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<HelpItemData> CREATOR = new Creator<HelpItemData>() {
    @Override
    public HelpItemData createFromParcel(Parcel in) {
      return new HelpItemData(in);
    }

    @Override
    public HelpItemData[] newArray(int size) {
      return new HelpItemData[size];
    }
  };

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public ArrayList<HelpSubCatListData> getSubcat() {
    return subcat;
  }

  public void setSubcat(ArrayList<HelpSubCatListData> subcat) {
    this.subcat = subcat;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
