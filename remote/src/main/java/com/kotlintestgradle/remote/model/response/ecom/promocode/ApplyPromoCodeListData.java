package com.kotlintestgradle.remote.model.response.ecom.promocode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ApplyPromoCodeListData implements Parcelable, ValidItem {
  @SerializedName("total_amt")
  @Expose
  private Float total_amt;
  @SerializedName("price")
  @Expose
  private Float price;
  @SerializedName("delivery_fee")
  @Expose
  private Float delivery_fee;
  @SerializedName("reduced_amt")
  @Expose
  private Float reduced_amt;

  public ApplyPromoCodeListData(Float total_amt, Float price, Float delivery_fee,
      Float reduced_amt) {
    this.total_amt = total_amt;
    this.price = price;
    this.delivery_fee = delivery_fee;
    this.reduced_amt = reduced_amt;
  }

  protected ApplyPromoCodeListData(Parcel in) {
    if (in.readByte() == 0) {
      total_amt = null;
    } else {
      total_amt = in.readFloat();
    }
    if (in.readByte() == 0) {
      price = null;
    } else {
      price = in.readFloat();
    }
    if (in.readByte() == 0) {
      delivery_fee = null;
    } else {
      delivery_fee = in.readFloat();
    }
    if (in.readByte() == 0) {
      reduced_amt = null;
    } else {
      reduced_amt = in.readFloat();
    }
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (total_amt == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeFloat(total_amt);
    }
    if (price == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeFloat(price);
    }
    if (delivery_fee == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeFloat(delivery_fee);
    }
    if (reduced_amt == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeFloat(reduced_amt);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ApplyPromoCodeListData> CREATOR =
      new Creator<ApplyPromoCodeListData>() {
        @Override
        public ApplyPromoCodeListData createFromParcel(Parcel in) {
          return new ApplyPromoCodeListData(in);
        }

        @Override
        public ApplyPromoCodeListData[] newArray(int size) {
          return new ApplyPromoCodeListData[size];
        }
      };

  public Float getTotal_amt() {
    return total_amt;
  }

  public void setTotal_amt(Float total_amt) {
    this.total_amt = total_amt;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Float getDelivery_fee() {
    return delivery_fee;
  }

  public void setDelivery_fee(Float delivery_fee) {
    this.delivery_fee = delivery_fee;
  }

  public Float getReduced_amt() {
    return reduced_amt;
  }

  public void setReduced_amt(Float reduced_amt) {
    this.reduced_amt = reduced_amt;
  }


  @Override
  public boolean isValid() {
    return true;
  }
}
