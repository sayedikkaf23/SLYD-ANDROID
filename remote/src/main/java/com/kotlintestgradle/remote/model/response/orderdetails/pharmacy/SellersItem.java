package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SellersItem implements Parcelable, ValidItem {

	@Expose
	@SerializedName("targetAmtForFreeDelivery")
	private int targetAmtForFreeDelivery;

	@Expose
	@SerializedName("driverTypeId")
	private int driverTypeId;

	@Expose
	@SerializedName("storeTypeId")
	private int storeTypeId;

	@Expose
	@SerializedName("storeFrontType")
	private String storeFrontType;

	@Expose
	@SerializedName("cityId")
	private String cityId;

	@Expose
	@SerializedName("minOrder")
	private int minOrder;

	@SerializedName("fullfilledBy")
	@Expose
	private String fullfilledBy;

	@Expose
	@SerializedName("storeTax")
	private List<Object> storeTax;

	@Expose
	@SerializedName("cityName")
	private String cityName;

	@Expose
	@SerializedName("areaName")
	private String areaName;

	@Expose
	@SerializedName("autoDispatch")
	private boolean autoDispatch;

	@Expose
	@SerializedName("poInvoiceLink")
	private String poInvoiceLink;

	@Expose
	@SerializedName("logo")
	private Logo logo;

	@Expose
	@SerializedName("sellerTypeId")
	private int sellerTypeId;

	@SerializedName("storeType")
	@Expose
	private String storeType;

	@Expose
	@SerializedName("storeFrontTypeId")
	private int storeFrontTypeId;

	@Expose
	@SerializedName("planData")
	private PlanData planData;

	@Expose
	@SerializedName("fullFillMentCenterId")
	private String fullFillMentCenterId;

	@Expose
	@SerializedName("driverType")
	private String driverType;

	@Expose
	@SerializedName("contactPersonName")
	private String contactPersonName;

	@Expose
	@SerializedName("phone")
	private String phone;

	@Expose
	@SerializedName("pickupAddress")
	private PickupAddress pickupAddress;

	@Expose
	@SerializedName("autoAcceptOrders")
	private boolean autoAcceptOrders;

	@Expose
	@SerializedName("isInventoryCheck")
	private boolean isInventoryCheck;

	@Expose
	@SerializedName("name")
	private String name;

	@Expose
	@SerializedName("sellerType")
	private String sellerType;

	@Expose
	@SerializedName("contactPersonEmail")
	private String contactPersonEmail;

	protected SellersItem(Parcel in) {
		targetAmtForFreeDelivery = in.readInt();
		driverTypeId = in.readInt();
		storeTypeId = in.readInt();
		storeFrontType = in.readString();
		cityId = in.readString();
		minOrder = in.readInt();
		fullfilledBy = in.readString();
		cityName = in.readString();
		areaName = in.readString();
		//autoDispatch = in.readBoolean();
		poInvoiceLink = in.readString();
		logo = in.readParcelable(Logo.class.getClassLoader());
		sellerTypeId = in.readInt();
		storeType = in.readString();
		storeFrontTypeId = in.readInt();
		planData = in.readParcelable(PlanData.class.getClassLoader());
		fullFillMentCenterId = in.readString();
		driverType = in.readString();
		contactPersonName = in.readString();
		phone = in.readString();
		pickupAddress = in.readParcelable(PickupAddress.class.getClassLoader());
		autoAcceptOrders = in.readByte() != 0;
		isInventoryCheck = in.readByte() != 0;
		name = in.readString();
		sellerType = in.readString();
		contactPersonEmail = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(targetAmtForFreeDelivery);
		dest.writeInt(driverTypeId);
		dest.writeInt(storeTypeId);
		dest.writeString(storeFrontType);
		dest.writeString(cityId);
		dest.writeInt(minOrder);
		dest.writeString(fullfilledBy);
		dest.writeString(cityName);
		dest.writeString(areaName);
		dest.writeString(poInvoiceLink);
		dest.writeParcelable(logo, flags);
		dest.writeInt(sellerTypeId);
		dest.writeString(storeType);
		dest.writeInt(storeFrontTypeId);
		dest.writeParcelable(planData, flags);
		dest.writeString(fullFillMentCenterId);
		dest.writeString(driverType);
		dest.writeString(contactPersonName);
		dest.writeString(phone);
		dest.writeParcelable(pickupAddress, flags);
		dest.writeByte((byte) (autoAcceptOrders ? 1 : 0));
		dest.writeByte((byte) (isInventoryCheck ? 1 : 0));
		dest.writeString(name);
		dest.writeString(sellerType);
		dest.writeString(contactPersonEmail);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SellersItem> CREATOR = new Creator<SellersItem>() {
		@Override
		public SellersItem createFromParcel(Parcel in) {
			return new SellersItem(in);
		}

		@Override
		public SellersItem[] newArray(int size) {
			return new SellersItem[size];
		}
	};

	public void setTargetAmtForFreeDelivery(int targetAmtForFreeDelivery){
		this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
	}

	public int getTargetAmtForFreeDelivery(){
		return targetAmtForFreeDelivery;
	}

	public void setDriverTypeId(int driverTypeId){
		this.driverTypeId = driverTypeId;
	}

	public int getDriverTypeId(){
		return driverTypeId;
	}

	public void setStoreTypeId(int storeTypeId){
		this.storeTypeId = storeTypeId;
	}

	public int getStoreTypeId(){
		return storeTypeId;
	}

	public void setStoreFrontType(String storeFrontType){
		this.storeFrontType = storeFrontType;
	}

	public String getStoreFrontType(){
		return storeFrontType;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	public void setMinOrder(int minOrder){
		this.minOrder = minOrder;
	}

	public int getMinOrder(){
		return minOrder;
	}

	public void setFullfilledBy(String fullfilledBy){
		this.fullfilledBy = fullfilledBy;
	}

	public String getFullfilledBy(){
		return fullfilledBy;
	}

	public void setStoreTax(List<Object> storeTax){
		this.storeTax = storeTax;
	}

	public List<Object> getStoreTax(){
		return storeTax;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getAreaName(){
		return areaName;
	}

	public void setAutoDispatch(boolean autoDispatch){
		this.autoDispatch = autoDispatch;
	}

	public boolean getAutoDispatch(){
		return autoDispatch;
	}

	public void setPoInvoiceLink(String poInvoiceLink){
		this.poInvoiceLink = poInvoiceLink;
	}

	public String getPoInvoiceLink(){
		return poInvoiceLink;
	}

	public void setLogo(Logo logo){
		this.logo = logo;
	}

	public Logo getLogo(){
		return logo;
	}

	public void setSellerTypeId(int sellerTypeId){
		this.sellerTypeId = sellerTypeId;
	}

	public int getSellerTypeId(){
		return sellerTypeId;
	}

	public void setStoreType(String storeType){
		this.storeType = storeType;
	}

	public String getStoreType(){
		return storeType;
	}

	public void setStoreFrontTypeId(int storeFrontTypeId){
		this.storeFrontTypeId = storeFrontTypeId;
	}

	public int getStoreFrontTypeId(){
		return storeFrontTypeId;
	}

	public void setPlanData(PlanData planData){
		this.planData = planData;
	}

	public PlanData getPlanData(){
		return planData;
	}

	public void setFullFillMentCenterId(String fullFillMentCenterId){
		this.fullFillMentCenterId = fullFillMentCenterId;
	}

	public String getFullFillMentCenterId(){
		return fullFillMentCenterId;
	}

	public void setDriverType(String driverType){
		this.driverType = driverType;
	}

	public String getDriverType(){
		return driverType;
	}

	public void setContactPersonName(String contactPersonName){
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonName(){
		return contactPersonName;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setPickupAddress(PickupAddress pickupAddress){
		this.pickupAddress = pickupAddress;
	}

	public PickupAddress getPickupAddress(){
		return pickupAddress;
	}

	public void setAutoAcceptOrders(boolean autoAcceptOrders){
		this.autoAcceptOrders = autoAcceptOrders;
	}

	public boolean isAutoAcceptOrders(){
		return autoAcceptOrders;
	}

	public void setIsInventoryCheck(boolean isInventoryCheck){
		this.isInventoryCheck = isInventoryCheck;
	}

	public boolean isIsInventoryCheck(){
		return isInventoryCheck;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSellerType(String sellerType){
		this.sellerType = sellerType;
	}

	public String getSellerType(){
		return sellerType;
	}

	public void setContactPersonEmail(String contactPersonEmail){
		this.contactPersonEmail = contactPersonEmail;
	}

	public String getContactPersonEmail(){
		return contactPersonEmail;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}