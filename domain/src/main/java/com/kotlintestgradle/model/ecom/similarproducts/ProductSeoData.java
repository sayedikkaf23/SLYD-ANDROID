package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductSeoData implements Parcelable {
	private String description;
	private String metatags;
	private String title;
	private String slug;

	public ProductSeoData(String description, String metatags, String title, String slug) {
		this.description = description;
		this.metatags = metatags;
		this.title = title;
		this.slug = slug;
	}

	protected ProductSeoData(Parcel in) {
		description = in.readString();
		metatags = in.readString();
		title = in.readString();
		slug = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(description);
		dest.writeString(metatags);
		dest.writeString(title);
		dest.writeString(slug);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ProductSeoData> CREATOR = new Creator<ProductSeoData>() {
		@Override
		public ProductSeoData createFromParcel(Parcel in) {
			return new ProductSeoData(in);
		}

		@Override
		public ProductSeoData[] newArray(int size) {
			return new ProductSeoData[size];
		}
	};

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setMetatags(String metatags){
		this.metatags = metatags;
	}

	public String getMetatags(){
		return metatags;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}
}
