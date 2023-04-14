package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class LogoData implements Parcelable {
	private String logoImageThumb;
	private String logoImageMobile;
	private String logoImageweb;

	public LogoData(String logoImageThumb, String logoImageMobile, String logoImageweb) {
		this.logoImageThumb = logoImageThumb;
		this.logoImageMobile = logoImageMobile;
		this.logoImageweb = logoImageweb;
	}

	protected LogoData(Parcel in) {
		logoImageThumb = in.readString();
		logoImageMobile = in.readString();
		logoImageweb = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(logoImageThumb);
		dest.writeString(logoImageMobile);
		dest.writeString(logoImageweb);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<LogoData> CREATOR = new Creator<LogoData>() {
		@Override
		public LogoData createFromParcel(Parcel in) {
			return new LogoData(in);
		}

		@Override
		public LogoData[] newArray(int size) {
			return new LogoData[size];
		}
	};

	public void setLogoImageThumb(String logoImageThumb){
		this.logoImageThumb = logoImageThumb;
	}

	public String getLogoImageThumb(){
		return logoImageThumb;
	}

	public void setLogoImageMobile(String logoImageMobile){
		this.logoImageMobile = logoImageMobile;
	}

	public String getLogoImageMobile(){
		return logoImageMobile;
	}

	public void setLogoImageweb(String logoImageweb){
		this.logoImageweb = logoImageweb;
	}

	public String getLogoImageweb(){
		return logoImageweb;
	}
}
