package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreLogo implements Parcelable, ValidItem {

	@Expose
	@SerializedName("logoImageThumb")
	private String logoImageThumb;

	@Expose
	@SerializedName("logoImageMobile")
	private String logoImageMobile;

	@Expose
	@SerializedName("logoImageweb")
	private String logoImageweb;

	protected StoreLogo(Parcel in) {
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

	public static final Creator<StoreLogo> CREATOR = new Creator<StoreLogo>() {
		@Override
		public StoreLogo createFromParcel(Parcel in) {
			return new StoreLogo(in);
		}

		@Override
		public StoreLogo[] newArray(int size) {
			return new StoreLogo[size];
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