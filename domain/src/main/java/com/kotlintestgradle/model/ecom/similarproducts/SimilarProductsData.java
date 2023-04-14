package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import java.util.ArrayList;
import java.util.List;

public class SimilarProductsData implements Parcelable {
	private List<Object> offerBanner;
	private int penCount;
	private ArrayList<ProductsData> products;

	public SimilarProductsData(List<Object> offerBanner, int penCount, ArrayList<ProductsData> products) {
		this.offerBanner = offerBanner;
		this.penCount = penCount;
		this.products = products;
	}

	protected SimilarProductsData(Parcel in) {
		penCount = in.readInt();
		products = in.createTypedArrayList(ProductsData.CREATOR);
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

	public static final Creator<SimilarProductsData> CREATOR = new Creator<SimilarProductsData>() {
		@Override
		public SimilarProductsData createFromParcel(Parcel in) {
			return new SimilarProductsData(in);
		}

		@Override
		public SimilarProductsData[] newArray(int size) {
			return new SimilarProductsData[size];
		}
	};

	public void setOfferBanner(List<Object> offerBanner){
		this.offerBanner = offerBanner;
	}

	public List<Object> getOfferBanner(){
		return offerBanner;
	}

	public void setPenCount(int penCount){
		this.penCount = penCount;
	}

	public int getPenCount(){
		return penCount;
	}

	public void setProducts(ArrayList<ProductsData> products){
		this.products = products;
	}

	public ArrayList<ProductsData> getProducts(){
		return products;
	}
}