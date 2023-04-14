package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SellersItemData implements Parcelable {
	private int targetAmtForFreeDelivery;
	private int driverTypeId;
	private int storeTypeId;
	private String storeFrontType;
	private String cityId;
	private int minOrder;
	private String fullfilledBy;
	private List<Object> storeTax;
	private String cityName;
	private String areaName;
	private boolean autoDispatch;
	private String poInvoiceLink;
	private LogoData logo;
	private int sellerTypeId;
	private String storeType;
	private int storeFrontTypeId;
	private PlansData planData;
	private String fullFillMentCenterId;
	private String driverType;
	private String contactPersonName;
	private String phone;
	private PickupAddressData pickupAddress;
	private boolean autoAcceptOrders;
	private boolean isInventoryCheck;
	private String name;
	private String sellerType;
	private String contactPersonEmail;

	public SellersItemData(int targetAmtForFreeDelivery, int driverTypeId, int storeTypeId, String storeFrontType, String cityId, int minOrder, String fullfilledBy, List<Object> storeTax, String cityName, String areaName, boolean autoDispatch, String poInvoiceLink, LogoData logo, int sellerTypeId, String storeType, int storeFrontTypeId, PlansData planData, String fullFillMentCenterId, String driverType, String contactPersonName, String phone, PickupAddressData pickupAddress, boolean autoAcceptOrders, boolean isInventoryCheck, String name, String sellerType, String contactPersonEmail) {
		this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
		this.driverTypeId = driverTypeId;
		this.storeTypeId = storeTypeId;
		this.storeFrontType = storeFrontType;
		this.cityId = cityId;
		this.minOrder = minOrder;
		this.fullfilledBy = fullfilledBy;
		this.storeTax = storeTax;
		this.cityName = cityName;
		this.areaName = areaName;
		this.autoDispatch = autoDispatch;
		this.poInvoiceLink = poInvoiceLink;
		this.logo = logo;
		this.sellerTypeId = sellerTypeId;
		this.storeType = storeType;
		this.storeFrontTypeId = storeFrontTypeId;
		this.planData = planData;
		this.fullFillMentCenterId = fullFillMentCenterId;
		this.driverType = driverType;
		this.contactPersonName = contactPersonName;
		this.phone = phone;
		this.pickupAddress = pickupAddress;
		this.autoAcceptOrders = autoAcceptOrders;
		this.isInventoryCheck = isInventoryCheck;
		this.name = name;
		this.sellerType = sellerType;
		this.contactPersonEmail = contactPersonEmail;
	}

	protected SellersItemData(Parcel in) {
		targetAmtForFreeDelivery = in.readInt();
		driverTypeId = in.readInt();
		storeTypeId = in.readInt();
		storeFrontType = in.readString();
		cityId = in.readString();
		minOrder = in.readInt();
		fullfilledBy = in.readString();
		cityName = in.readString();
		areaName = in.readString();
		poInvoiceLink = in.readString();
		logo = in.readParcelable(LogoData.class.getClassLoader());
		sellerTypeId = in.readInt();
		storeType = in.readString();
		storeFrontTypeId = in.readInt();
		planData = in.readParcelable(PlansData.class.getClassLoader());
		fullFillMentCenterId = in.readString();
		driverType = in.readString();
		contactPersonName = in.readString();
		phone = in.readString();
		pickupAddress = in.readParcelable(PickupAddressData.class.getClassLoader());
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

	public static final Creator<SellersItemData> CREATOR = new Creator<SellersItemData>() {
		@Override
		public SellersItemData createFromParcel(Parcel in) {
			return new SellersItemData(in);
		}

		@Override
		public SellersItemData[] newArray(int size) {
			return new SellersItemData[size];
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

	public void setLogo(LogoData logo){
		this.logo = logo;
	}

	public LogoData getLogo(){
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

	public void setPlanData(PlansData planData){
		this.planData = planData;
	}

	public PlansData getPlanData(){
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

	public void setPickupAddress(PickupAddressData pickupAddress){
		this.pickupAddress = pickupAddress;
	}

	public PickupAddressData getPickupAddress(){
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
}