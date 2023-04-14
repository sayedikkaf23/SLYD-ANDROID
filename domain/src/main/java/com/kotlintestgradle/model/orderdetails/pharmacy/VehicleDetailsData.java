package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class VehicleDetailsData implements Parcelable {
	private boolean towing;
	private String vehicleImage;
	private String plateNo;
	private ArrayList<String> goodTypes;
	private String typeName;
	private String vehicleMake;
	private String colour;
	private String vehicleYear;
	private String vehicleModel;
	private String typeId;
	private String id;
	private String vehicleId;
	private String vehicleMapIcon;

	public VehicleDetailsData(boolean towing, String vehicleImage, String plateNo, ArrayList<String> goodTypes, String typeName, String vehicleMake, String colour, String vehicleYear, String vehicleModel, String typeId, String id, String vehicleId, String vehicleMapIcon) {
		this.towing = towing;
		this.vehicleImage = vehicleImage;
		this.plateNo = plateNo;
		this.goodTypes = goodTypes;
		this.typeName = typeName;
		this.vehicleMake = vehicleMake;
		this.colour = colour;
		this.vehicleYear = vehicleYear;
		this.vehicleModel = vehicleModel;
		this.typeId = typeId;
		this.id = id;
		this.vehicleId = vehicleId;
		this.vehicleMapIcon = vehicleMapIcon;
	}

	protected VehicleDetailsData(Parcel in) {
		towing = in.readByte() != 0;
		vehicleImage = in.readString();
		plateNo = in.readString();
		goodTypes = in.createStringArrayList();
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
		dest.writeStringList(goodTypes);
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

	public static final Creator<VehicleDetailsData> CREATOR = new Creator<VehicleDetailsData>() {
		@Override
		public VehicleDetailsData createFromParcel(Parcel in) {
			return new VehicleDetailsData(in);
		}

		@Override
		public VehicleDetailsData[] newArray(int size) {
			return new VehicleDetailsData[size];
		}
	};

	public void setTowing(boolean towing){
		this.towing = towing;
	}

	public boolean isTowing(){
		return towing;
	}

	public void setVehicleImage(String vehicleImage){
		this.vehicleImage = vehicleImage;
	}

	public String getVehicleImage(){
		return vehicleImage;
	}

	public void setPlateNo(String plateNo){
		this.plateNo = plateNo;
	}

	public String getPlateNo(){
		return plateNo;
	}

	public void setGoodTypes(ArrayList<String> goodTypes){
		this.goodTypes = goodTypes;
	}

	public ArrayList<String> getGoodTypes(){
		return goodTypes;
	}

	public void setTypeName(String typeName){
		this.typeName = typeName;
	}

	public String getTypeName(){
		return typeName;
	}

	public void setVehicleMake(String vehicleMake){
		this.vehicleMake = vehicleMake;
	}

	public String getVehicleMake(){
		return vehicleMake;
	}

	public void setColour(String colour){
		this.colour = colour;
	}

	public String getColour(){
		return colour;
	}

	public void setVehicleYear(String vehicleYear){
		this.vehicleYear = vehicleYear;
	}

	public String getVehicleYear(){
		return vehicleYear;
	}

	public void setVehicleModel(String vehicleModel){
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleModel(){
		return vehicleModel;
	}

	public void setTypeId(String typeId){
		this.typeId = typeId;
	}

	public String getTypeId(){
		return typeId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setVehicleId(String vehicleId){
		this.vehicleId = vehicleId;
	}

	public String getVehicleId(){
		return vehicleId;
	}

	public void setVehicleMapIcon(String vehicleMapIcon){
		this.vehicleMapIcon = vehicleMapIcon;
	}

	public String getVehicleMapIcon(){
		return vehicleMapIcon;
	}
}
