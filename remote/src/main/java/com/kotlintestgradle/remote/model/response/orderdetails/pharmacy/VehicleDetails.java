package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("towing")
	private boolean towing;

	@Expose
	@SerializedName("vehicleImage")
	private String vehicleImage;

	@Expose
	@SerializedName("plateNo")
	private String plateNo;

/*	@Expose
	@SerializedName("goodTypes")
	private ArrayList<String> goodTypes;*/

	@Expose
	@SerializedName("typeName")
	private String typeName;

	@Expose
	@SerializedName("vehicleMake")
	private String vehicleMake;

	@Expose
	@SerializedName("colour")
	private String colour;

	@Expose
	@SerializedName("vehicleYear")
	private String vehicleYear;

	@Expose
	@SerializedName("vehicleModel")
	private String vehicleModel;

	@Expose
	@SerializedName("typeId")
	private String typeId;

	@Expose
	@SerializedName("_id")
	private String id;

	@Expose
	@SerializedName("vehicleId")
	private String vehicleId;

	@Expose
	@SerializedName("vehicleMapIcon")
	private String vehicleMapIcon;

	protected VehicleDetails(Parcel in) {
		towing = in.readByte() != 0;
		vehicleImage = in.readString();
		plateNo = in.readString();
	//	goodTypes = in.createStringArrayList();
		typeName = in.readString();
		vehicleMake = in.readString();
		colour = in.readString();
		vehicleYear = in.readString();
		vehicleModel = in.readString();
		typeId = in.readString();
		id = in.readString();
		vehicleId = in.readString();
		vehicleMapIcon = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (towing ? 1 : 0));
		dest.writeString(vehicleImage);
		dest.writeString(plateNo);
		//dest.writeStringList(goodTypes);
		dest.writeString(typeName);
		dest.writeString(vehicleMake);
		dest.writeString(colour);
		dest.writeString(vehicleYear);
		dest.writeString(vehicleModel);
		dest.writeString(typeId);
		dest.writeString(id);
		dest.writeString(vehicleId);
		dest.writeString(vehicleMapIcon);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<VehicleDetails> CREATOR = new Creator<VehicleDetails>() {
		@Override
		public VehicleDetails createFromParcel(Parcel in) {
			return new VehicleDetails(in);
		}

		@Override
		public VehicleDetails[] newArray(int size) {
			return new VehicleDetails[size];
		}
	};

	public boolean isTowing(){
		return towing;
	}

	public String getVehicleImage(){
		return vehicleImage;
	}

	public String getPlateNo(){
		return plateNo;
	}

/*	public ArrayList<String> getGoodTypes(){
		return goodTypes;
	}*/

	public String getTypeName(){
		return typeName;
	}

	public String getVehicleMake(){
		return vehicleMake;
	}

	public String getColour(){
		return colour;
	}

	public String getVehicleYear(){
		return vehicleYear;
	}

	public String getVehicleModel(){
		return vehicleModel;
	}

	public String getTypeId(){
		return typeId;
	}

	public String getId(){
		return id;
	}

	public String getVehicleId(){
		return vehicleId;
	}

	public String getVehicleMapIcon(){
		return vehicleMapIcon;
	}

	public void setTowing(boolean towing) {
		this.towing = towing;
	}

	public void setVehicleImage(String vehicleImage) {
		this.vehicleImage = vehicleImage;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	/*public void setGoodTypes(String goodTypes) {
		this.goodTypes = goodTypes;
	}*/

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public void setVehicleYear(String vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setVehicleMapIcon(String vehicleMapIcon) {
		this.vehicleMapIcon = vehicleMapIcon;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
