package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class Logo implements Parcelable {

    private String logoImageMobile;
    private String logoImageThumb;
    private String logoImageWeb;

    public Logo(String logoImageMobile, String logoImageThumb, String logoImageWeb) {
        this.logoImageMobile = logoImageMobile;
        this.logoImageThumb = logoImageThumb;
        this.logoImageWeb = logoImageWeb;
    }

    protected Logo(Parcel in) {
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

    public static final Creator<Logo> CREATOR = new Creator<Logo>() {
        @Override
        public Logo createFromParcel(Parcel in) {
            return new Logo(in);
        }

        @Override
        public Logo[] newArray(int size) {
            return new Logo[size];
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
}
