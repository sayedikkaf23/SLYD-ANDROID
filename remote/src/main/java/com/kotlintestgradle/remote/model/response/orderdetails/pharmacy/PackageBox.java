package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageBox implements Parcelable, ValidItem {

	@Expose
	@SerializedName("image")
	private String image;

	@Expose
	@SerializedName("volumeCapacity")
	private int volumeCapacity;

	@Expose
	@SerializedName("voulumeCapacityUnit")
	private String voulumeCapacityUnit;

	@Expose
	@SerializedName("widthCapacityUnitName")
	private String widthCapacityUnitName;

	@Expose
	@SerializedName("weight")
	private int weight;

	@Expose
	@SerializedName("heightCapacityUnitName")
	private String heightCapacityUnitName;

	@Expose
	@SerializedName("voulumeCapacityUnitName")
	private String voulumeCapacityUnitName;

	@Expose
	@SerializedName("weightCapacityUnitName")
	private String weightCapacityUnitName;

	@Expose
	@SerializedName("widthCapacityUnit")
	private String widthCapacityUnit;

	@Expose
	@SerializedName("heightCapacity")
	private int heightCapacity;

	@Expose
	@SerializedName("weightCapacityUnit")
	private String weightCapacityUnit;

	@Expose
	@SerializedName("lengthCapacityUnitName")
	private String lengthCapacityUnitName;

	@Expose
	@SerializedName("name")
	private Name name;

	@Expose
	@SerializedName("heightCapacityUnit")
	private String heightCapacityUnit;

	@Expose
	@SerializedName("lengthCapacityUnit")
	private String lengthCapacityUnit;

	@Expose
	@SerializedName("widthCapacity")
	private int widthCapacity;

	@Expose
	@SerializedName("lengthCapacity")
	private int lengthCapacity;

	@Expose
	@SerializedName("boxId")
	private String boxId;


	protected PackageBox(Parcel in) {
		image = in.readString();
		volumeCapacity = in.readInt();
		voulumeCapacityUnit = in.readString();
		widthCapacityUnitName = in.readString();
		weight = in.readInt();
		heightCapacityUnitName = in.readString();
		voulumeCapacityUnitName = in.readString();
		weightCapacityUnitName = in.readString();
		widthCapacityUnit = in.readString();
		heightCapacity = in.readInt();
		weightCapacityUnit = in.readString();
		lengthCapacityUnitName = in.readString();
		name = in.readParcelable(Name.class.getClassLoader());
		heightCapacityUnit = in.readString();
		lengthCapacityUnit = in.readString();
		widthCapacity = in.readInt();
		lengthCapacity = in.readInt();
		boxId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(image);
		dest.writeInt(volumeCapacity);
		dest.writeString(voulumeCapacityUnit);
		dest.writeString(widthCapacityUnitName);
		dest.writeInt(weight);
		dest.writeString(heightCapacityUnitName);
		dest.writeString(voulumeCapacityUnitName);
		dest.writeString(weightCapacityUnitName);
		dest.writeString(widthCapacityUnit);
		dest.writeInt(heightCapacity);
		dest.writeString(weightCapacityUnit);
		dest.writeString(lengthCapacityUnitName);
		dest.writeParcelable(name, flags);
		dest.writeString(heightCapacityUnit);
		dest.writeString(lengthCapacityUnit);
		dest.writeInt(widthCapacity);
		dest.writeInt(lengthCapacity);
		dest.writeString(boxId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PackageBox> CREATOR = new Creator<PackageBox>() {
		@Override
		public PackageBox createFromParcel(Parcel in) {
			return new PackageBox(in);
		}

		@Override
		public PackageBox[] newArray(int size) {
			return new PackageBox[size];
		}
	};

	public void setImage(String image) {
		this.image = image;
	}

	public void setVolumeCapacity(int volumeCapacity) {
		this.volumeCapacity = volumeCapacity;
	}

	public void setVoulumeCapacityUnit(String voulumeCapacityUnit) {
		this.voulumeCapacityUnit = voulumeCapacityUnit;
	}

	public void setWidthCapacityUnitName(String widthCapacityUnitName) {
		this.widthCapacityUnitName = widthCapacityUnitName;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setHeightCapacityUnitName(String heightCapacityUnitName) {
		this.heightCapacityUnitName = heightCapacityUnitName;
	}

	public void setVoulumeCapacityUnitName(String voulumeCapacityUnitName) {
		this.voulumeCapacityUnitName = voulumeCapacityUnitName;
	}

	public void setWeightCapacityUnitName(String weightCapacityUnitName) {
		this.weightCapacityUnitName = weightCapacityUnitName;
	}

	public void setWidthCapacityUnit(String widthCapacityUnit) {
		this.widthCapacityUnit = widthCapacityUnit;
	}

	public void setHeightCapacity(int heightCapacity) {
		this.heightCapacity = heightCapacity;
	}

	public void setWeightCapacityUnit(String weightCapacityUnit) {
		this.weightCapacityUnit = weightCapacityUnit;
	}

	public void setLengthCapacityUnitName(String lengthCapacityUnitName) {
		this.lengthCapacityUnitName = lengthCapacityUnitName;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public void setHeightCapacityUnit(String heightCapacityUnit) {
		this.heightCapacityUnit = heightCapacityUnit;
	}

	public void setLengthCapacityUnit(String lengthCapacityUnit) {
		this.lengthCapacityUnit = lengthCapacityUnit;
	}

	public void setWidthCapacity(int widthCapacity) {
		this.widthCapacity = widthCapacity;
	}

	public void setLengthCapacity(int lengthCapacity) {
		this.lengthCapacity = lengthCapacity;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public String getImage(){
		return image;
	}

	public int getVolumeCapacity(){
		return volumeCapacity;
	}

	public String getVoulumeCapacityUnit(){
		return voulumeCapacityUnit;
	}

	public String getWidthCapacityUnitName(){
		return widthCapacityUnitName;
	}

	public int getWeight(){
		return weight;
	}

	public String getHeightCapacityUnitName(){
		return heightCapacityUnitName;
	}

	public String getVoulumeCapacityUnitName(){
		return voulumeCapacityUnitName;
	}

	public String getWeightCapacityUnitName(){
		return weightCapacityUnitName;
	}

	public String getWidthCapacityUnit(){
		return widthCapacityUnit;
	}

	public int getHeightCapacity(){
		return heightCapacity;
	}

	public String getWeightCapacityUnit(){
		return weightCapacityUnit;
	}

	public String getLengthCapacityUnitName(){
		return lengthCapacityUnitName;
	}

	public Name getName(){
		return name;
	}

	public String getHeightCapacityUnit(){
		return heightCapacityUnit;
	}

	public String getLengthCapacityUnit(){
		return lengthCapacityUnit;
	}

	public int getWidthCapacity(){
		return widthCapacity;
	}

	public int getLengthCapacity(){
		return lengthCapacity;
	}

	public String getBoxId(){
		return boxId;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
