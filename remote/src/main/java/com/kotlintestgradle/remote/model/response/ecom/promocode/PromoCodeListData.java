package com.kotlintestgradle.remote.model.response.ecom.promocode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class PromoCodeListData implements Parcelable , ValidItem {
  @SerializedName("start_time")
  @Expose
  private String start_time;
  @SerializedName("code")
  @Expose
  private String code;
  @SerializedName("promo_id")
  @Expose
  private String promo_id;
  @SerializedName("end_time")
  @Expose
  private String end_time;
  @SerializedName("description")
  @Expose
  private ArrayList<PromoCodeListDes> description;
  @SerializedName("data")
  @Expose
  private String title;

  protected PromoCodeListData(Parcel in) {
    start_time = in.readString();
    code = in.readString();
    promo_id = in.readString();
    end_time = in.readString();
    description = in.createTypedArrayList(PromoCodeListDes.CREATOR);
    title = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(start_time);
    dest.writeString(code);
    dest.writeString(promo_id);
    dest.writeString(end_time);
    dest.writeTypedList(description);
    dest.writeString(title);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PromoCodeListData> CREATOR = new Creator<PromoCodeListData>() {
    @Override
    public PromoCodeListData createFromParcel(Parcel in) {
      return new PromoCodeListData(in);
    }

    @Override
    public PromoCodeListData[] newArray(int size) {
      return new PromoCodeListData[size];
    }
  };

  public String getStart_time ()
  {
    return start_time;
  }

  public void setStart_time (String start_time)
  {
    this.start_time = start_time;
  }



  public String getCode ()
  {
    return code;
  }

  public void setCode (String code)
  {
    this.code = code;
  }

  public String getEnd_time ()
  {
    return end_time;
  }

  public void setEnd_time (String end_time)
  {
    this.end_time = end_time;
  }

  public ArrayList<PromoCodeListDes> getDescription ()
  {
    return description;
  }

  public void setDescription (ArrayList<PromoCodeListDes> description)
  {
    this.description = description;
  }

  public String getTitle ()
  {
    return title;
  }

  public void setTitle (String title)
  {
    this.title = title;
  }

  public String getPromo_id() {
    return promo_id;
  }

  public void setPromo_id(String promo_id) {
    this.promo_id = promo_id;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
