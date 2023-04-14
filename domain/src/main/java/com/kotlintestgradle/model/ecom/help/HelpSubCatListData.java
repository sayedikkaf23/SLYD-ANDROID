package com.kotlintestgradle.model.ecom.help;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by dell on 13-Apr-18.
 */

public class HelpSubCatListData implements Parcelable {

  private String desc;

  private String link;

  private String name;

  public HelpSubCatListData(String desc, String link, String name) {
    this.desc = desc;
    this.link = link;
    this.name = name;
  }

  protected HelpSubCatListData(Parcel in) {
    desc = in.readString();
    link = in.readString();
    name = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(desc);
    dest.writeString(link);
    dest.writeString(name);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<HelpSubCatListData> CREATOR = new Creator<HelpSubCatListData>() {
    @Override
    public HelpSubCatListData createFromParcel(Parcel in) {
      return new HelpSubCatListData(in);
    }

    @Override
    public HelpSubCatListData[] newArray(int size) {
      return new HelpSubCatListData[size];
    }
  };

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
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
