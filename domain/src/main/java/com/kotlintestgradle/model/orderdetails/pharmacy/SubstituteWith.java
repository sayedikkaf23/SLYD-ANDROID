package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class SubstituteWith implements Parcelable {
	private String centralProductId;
	private ImagesData images;
	private String brandName;
	private QuantityData quantity;
	private String color;
	private double originalPrice;
	private String name;
	private String unitId;
	private String id;

	public SubstituteWith(String centralProductId, ImagesData images, String brandName, QuantityData quantity, String color, double originalPrice, String name, String unitId, String id) {
		this.centralProductId = centralProductId;
		this.images = images;
		this.brandName = brandName;
		this.quantity = quantity;
		this.color = color;
		this.originalPrice = originalPrice;
		this.name = name;
		this.unitId = unitId;
		this.id = id;
	}

	protected SubstituteWith(Parcel in) {
		centralProductId = in.readString();
		images = in.readParcelable(ImagesData.class.getClassLoader());
		brandName = in.readString();
		quantity = in.readParcelable(QuantityData.class.getClassLoader());
		color = in.readString();
		originalPrice = in.readDouble();
		name = in.readString();
		unitId = in.readString();
		id = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(centralProductId);
		dest.writeParcelable(images, flags);
		dest.writeString(brandName);
		dest.writeParcelable(quantity, flags);
		dest.writeString(color);
		dest.writeDouble(originalPrice);
		dest.writeString(name);
		dest.writeString(unitId);
		dest.writeString(id);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SubstituteWith> CREATOR = new Creator<SubstituteWith>() {
		@Override
		public SubstituteWith createFromParcel(Parcel in) {
			return new SubstituteWith(in);
		}

		@Override
		public SubstituteWith[] newArray(int size) {
			return new SubstituteWith[size];
		}
	};

	public void setCentralProductId(String centralProductId){
		this.centralProductId = centralProductId;
	}

	public String getCentralProductId(){
		return centralProductId;
	}

	public void setImages(ImagesData images){
		this.images = images;
	}

	public ImagesData getImages(){
		return images;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setQuantity(QuantityData quantity){
		this.quantity = quantity;
	}

	public QuantityData getQuantity(){
		return quantity;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setOriginalPrice(double originalPrice){
		this.originalPrice = originalPrice;
	}

	public double getOriginalPrice(){
		return originalPrice;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUnitId(String unitId){
		this.unitId = unitId;
	}

	public String getUnitId(){
		return unitId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}
