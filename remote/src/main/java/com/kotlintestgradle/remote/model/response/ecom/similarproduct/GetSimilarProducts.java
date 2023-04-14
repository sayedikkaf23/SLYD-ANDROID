package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ProductsDetails;
import java.util.ArrayList;

public class GetSimilarProducts implements Parcelable, ValidItem {

	@Expose
	@SerializedName("offerBanner")
	private ArrayList<Object> offerBanner;

	@Expose
	@SerializedName("penCount")
	private int penCount;

	@Expose
	@SerializedName("products")
	private ArrayList<ProductsDetails> products;

	protected GetSimilarProducts(Parcel in) {
		penCount = in.readInt();
		products = in.createTypedArrayList(ProductsDetails.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(penCount);
		dest.writeTypedList(products);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<GetSimilarProducts> CREATOR = new Creator<GetSimilarProducts>() {
		@Override
		public GetSimilarProducts createFromParcel(Parcel in) {
			return new GetSimilarProducts(in);
		}

		@Override
		public GetSimilarProducts[] newArray(int size) {
			return new GetSimilarProducts[size];
		}
	};

	public void setOfferBanner(ArrayList<Object> offerBanner){
		this.offerBanner = offerBanner;
	}

	public ArrayList<Object> getOfferBanner(){
		return offerBanner;
	}

	public void setPenCount(int penCount){
		this.penCount = penCount;
	}

	public int getPenCount(){
		return penCount;
	}

	public void setProducts(ArrayList<ProductsDetails> products){
		this.products = products;
	}

	public ArrayList<ProductsDetails> getProducts(){
		return products;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}