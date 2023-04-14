package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class Logos implements Parcelable, ValidItem {

    @Expose
    @SerializedName("logoImageMobile")
    private String logoImageMobile;

    @Expose
    @SerializedName("logoImageThumb")
    private String logoImageThumb;

    @Expose
    @SerializedName("logoImageweb")
    private String logoImageWeb;

    public Logos(String logoImageMobile, String logoImageThumb, String logoImageWeb) {
        this.logoImageMobile = logoImageMobile;
        this.logoImageThumb = logoImageThumb;
        this.logoImageWeb = logoImageWeb;
    }


    protected Logos(Parcel in) {
        logoImageMobile = in.readString();
        logoImageThumb = in.readString();
        logoImageWeb = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(logoImageMobile);
        dest.writeString(logoImageThumb);
        dest.writeString(logoImageWeb);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Logos> CREATOR = new Creator<Logos>() {
        @Override
        public Logos createFromParcel(Parcel in) {
            return new Logos(in);
        }

        @Override
        public Logos[] newArray(int size) {
            return new Logos[size];
        }
    };

    public String getLogoImageMobile() {
        return logoImageMobile;
    }

    public void setLogoImageMobile(String logoImageMobile) {
        this.logoImageMobile = logoImageMobile;
    }

    public String getLogoImageThumb() {
        return logoImageThumb;
    }

    public void setLogoImageThumb(String logoImageThumb) {
        this.logoImageThumb = logoImageThumb;
    }

    public String getLogoImageWeb() {
        return logoImageWeb;
    }

    public void setLogoImageWeb(String logoImageWeb) {
        this.logoImageWeb = logoImageWeb;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
