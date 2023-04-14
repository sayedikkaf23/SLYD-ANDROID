package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class Logo implements Parcelable, ValidItem {

	@Expose
	@SerializedName("logoImageThumb")
	private String logoImageThumb;

	@Expose
	@SerializedName("logoImageMobile")
	private String logoImageMobile;

	@Expose
	@SerializedName("logoImageweb")
	private String logoImageweb;

	protected Logo(Parcel in) {
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

	@Override
	public boolean isValid() {
		return true;
	}
}