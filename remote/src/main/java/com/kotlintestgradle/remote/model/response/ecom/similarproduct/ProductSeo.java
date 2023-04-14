package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ProductSeo implements Parcelable, ValidItem {

	@SerializedName("description")
	private String description;

	@SerializedName("metatags")
	private String metatags;

	@SerializedName("title")
	private String title;

	@SerializedName("slug")
	private String slug;

	protected ProductSeo(Parcel in) {
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

	public static final Creator<ProductSeo> CREATOR = new Creator<ProductSeo>() {
		@Override
		public ProductSeo createFromParcel(Parcel in) {
			return new ProductSeo(in);
		}

		@Override
		public ProductSeo[] newArray(int size) {
			return new ProductSeo[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}