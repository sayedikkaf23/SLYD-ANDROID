package com.kotlintestgradle.remote.model.response.help;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class HelpItemDetails implements Parcelable, ValidItem {
  @SerializedName("desc")
  @Expose
  private String desc;
  @SerializedName("subcat")
  @Expose
  private ArrayList<HelpSubCatListDetails> subcat;
  @SerializedName("link")
  @Expose
  private String link;
  @SerializedName("name")
  @Expose
  private String name;

  protected HelpItemDetails(Parcel in) {
    desc = in.readString();
    subcat = in.createTypedArrayList(HelpSubCatListDetails.CREATOR);
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

  public static final Creator<HelpItemDetails> CREATOR = new Creator<HelpItemDetails>() {
    @Override
    public HelpItemDetails createFromParcel(Parcel in) {
      return new HelpItemDetails(in);
    }

    @Override
    public HelpItemDetails[] newArray(int size) {
      return new HelpItemDetails[size];
    }
  };

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public ArrayList<HelpSubCatListDetails> getSubcat() {
    return subcat;
  }

  public void setSubcat(ArrayList<HelpSubCatListDetails> subcat) {
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

  @Override
  public boolean isValid() {
    return true;
  }
}
