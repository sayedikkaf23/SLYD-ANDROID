package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

import java.util.List;

public class Accounting implements Parcelable, ValidItem {

	@Expose
	@SerializedName("unitPrice")
	private double unitPrice;

	@Expose
	@SerializedName("taxableAmount")
	private double taxableAmount;

	@Expose
	@SerializedName("finalUnitPrice")
	private double finalUnitPrice;

	@Expose
	@SerializedName("appEarningWithTax")
	private int appEarningWithTax;

	@Expose
	@SerializedName("totalQuantity")
	private int totalQuantity;

	@Expose
	@SerializedName("pgEarning")
	private int pgEarning;

	@Expose
	@SerializedName("payBy")
	private PayBy payBy;

	@SerializedName("driverEarning")
	@Expose
	private int driverEarning;

	@SerializedName("currencySymbol")
	@Expose
	private String currencySymbol;

	@SerializedName("addOnsAmount")
	@Expose
	private int addOnsAmount;

	@SerializedName("tax")
	@Expose
	private List<Object> tax;

	@SerializedName("subTotal")
	@Expose
	private double subTotal;

	@SerializedName("storeEarning")
	@Expose
	private int storeEarning;

	@SerializedName("offerDiscount")
	@Expose
	private double offerDiscount;

	@SerializedName("deliveryFee")
	@Expose
	private int deliveryFee;

	@SerializedName("deliveryDetails")
	@Expose
	private DeliveryDetails deliveryDetails;

	@SerializedName("taxAmount")
	@Expose
	private double taxAmount;

	@SerializedName("promoDiscount")
	@Expose
	private int promoDiscount;

	@SerializedName("currencyCode")
	@Expose
	private String currencyCode;

	@SerializedName("finalTotal")
	@Expose
	private double finalTotal;

	@SerializedName("tip")
	@Expose
	private int tip;

	@SerializedName("appEarning")
	@Expose
	private int appEarning;

	protected Accounting(Parcel in) {
		unitPrice = in.readDouble();
		taxableAmount = in.readDouble();
		finalUnitPrice = in.readDouble();
		appEarningWithTax = in.readInt();
		pgEarning = in.readInt();
		totalQuantity = in.readInt();
		payBy = in.readParcelable(PayBy.class.getClassLoader());
		driverEarning = in.readInt();
		currencySymbol = in.readString();
		addOnsAmount = in.readInt();
		subTotal = in.readDouble();
		storeEarning = in.readInt();
		offerDiscount = in.readDouble();
		deliveryFee = in.readInt();
		deliveryDetails = in.readParcelable(DeliveryDetails.class.getClassLoader());
		taxAmount = in.readInt();
		promoDiscount = in.readInt();
		currencyCode = in.readString();
		finalTotal = in.readDouble();
		tip = in.readInt();
		appEarning = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(unitPrice);
		dest.writeDouble(taxableAmount);
		dest.writeDouble(finalUnitPrice);
		dest.writeInt(appEarningWithTax);
		dest.writeInt(pgEarning);
		dest.writeParcelable(payBy, flags);
		dest.writeInt(driverEarning);
		dest.writeString(currencySymbol);
		dest.writeInt(addOnsAmount);
		dest.writeDouble(subTotal);
		dest.writeInt(storeEarning);
		dest.writeInt(totalQuantity);
		dest.writeDouble(offerDiscount);
		dest.writeInt(deliveryFee);
		dest.writeParcelable(deliveryDetails, flags);
		dest.writeDouble(taxAmount);
		dest.writeInt(promoDiscount);
		dest.writeString(currencyCode);
		dest.writeDouble(finalTotal);
		dest.writeInt(tip);
		dest.writeInt(appEarning);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Accounting> CREATOR = new Creator<Accounting>() {
		@Override
		public Accounting createFromParcel(Parcel in) {
			return new Accounting(in);
		}

		@Override
		public Accounting[] newArray(int size) {
			return new Accounting[size];
		}
	};

	public int getTip() {
		return tip;
	}

	public void setTip(int tip) {
		this.tip = tip;
	}

	public void setUnitPrice(double unitPrice){
		this.unitPrice = unitPrice;
	}

	public double getUnitPrice(){
		return unitPrice;
	}

	public void setTaxableAmount(double taxableAmount){
		this.taxableAmount = taxableAmount;
	}

	public double getTaxableAmount(){
		return taxableAmount;
	}

	public void setFinalUnitPrice(double finalUnitPrice){
		this.finalUnitPrice = finalUnitPrice;
	}

	public double getFinalUnitPrice(){
		return finalUnitPrice;
	}

	public void setAppEarningWithTax(int appEarningWithTax){
		this.appEarningWithTax = appEarningWithTax;
	}

	public int getAppEarningWithTax(){
		return appEarningWithTax;
	}

	public void setPgEarning(int pgEarning){
		this.pgEarning = pgEarning;
	}

	public int getPgEarning(){
		return pgEarning;
	}

	public void setPayBy(PayBy payBy){
		this.payBy = payBy;
	}

	public PayBy getPayBy(){
		return payBy;
	}

	public void setDriverEarning(int driverEarning){
		this.driverEarning = driverEarning;
	}

	public int getDriverEarning(){
		return driverEarning;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setAddOnsAmount(int addOnsAmount){
		this.addOnsAmount = addOnsAmount;
	}

	public int getAddOnsAmount(){
		return addOnsAmount;
	}

	public void setTax(List<Object> tax){
		this.tax = tax;
	}

	public List<Object> getTax(){
		return tax;
	}

	public void setSubTotal(double subTotal){
		this.subTotal = subTotal;
	}

	public double getSubTotal(){
		return subTotal;
	}

	public void setStoreEarning(int storeEarning){
		this.storeEarning = storeEarning;
	}

	public int getStoreEarning(){
		return storeEarning;
	}

	public void setOfferDiscount(double offerDiscount){
		this.offerDiscount = offerDiscount;
	}

	public double getOfferDiscount(){
		return offerDiscount;
	}

	public void setDeliveryFee(int deliveryFee){
		this.deliveryFee = deliveryFee;
	}

	public int getDeliveryFee(){
		return deliveryFee;
	}

	public void setDeliveryDetails(DeliveryDetails deliveryDetails){
		this.deliveryDetails = deliveryDetails;
	}

	public DeliveryDetails getDeliveryDetails(){
		return deliveryDetails;
	}

	public void setTaxAmount(int taxAmount){
		this.taxAmount = taxAmount;
	}

	public double getTaxAmount(){
		return taxAmount;
	}

	public void setPromoDiscount(int promoDiscount){
		this.promoDiscount = promoDiscount;
	}

	public int getPromoDiscount(){
		return promoDiscount;
	}

	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public void setFinalTotal(double finalTotal){
		this.finalTotal = finalTotal;
	}

	public double getFinalTotal(){
		return finalTotal;
	}

	public void setAppEarning(int appEarning){
		this.appEarning = appEarning;
	}

	public int getAppEarning(){
		return appEarning;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}