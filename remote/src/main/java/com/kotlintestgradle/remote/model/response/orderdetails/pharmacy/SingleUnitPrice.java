package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleUnitPrice implements Parcelable, ValidItem {

	@Expose
	@SerializedName("unitPrice")
	private double unitPrice;

	@Expose
	@SerializedName("subTotalAmount")
	private double subTotalAmount;

	@Expose
	@SerializedName("taxableAmount")
	private double taxableAmount;

	@Expose
	@SerializedName("finalUnitPrice")
	private double finalUnitPrice;

	@Expose
	@SerializedName("resellerCommission")
	private int resellerCommission;

	@Expose
	@SerializedName("addOnsAmount")
	private int addOnsAmount;

	@Expose
	@SerializedName("tax")
	private List<Object> tax;

	@Expose
	@SerializedName("subTotal")
	private double subTotal;

	@Expose
	@SerializedName("resellerCommissionType")
	private int resellerCommissionType;

	@Expose
	@SerializedName("offerDiscount")
	private double offerDiscount;

	@Expose
	@SerializedName("normalUnitPrice")
	private double normalUnitPrice;

	@Expose
	@SerializedName("expressUnitPrice")
	private int expressUnitPrice;

	@Expose
	@SerializedName("taxAmount")
	private int taxAmount;

	@Expose
	@SerializedName("reason")
	private String reason;

	protected SingleUnitPrice(Parcel in) {
		unitPrice = in.readDouble();
		subTotalAmount = in.readDouble();
		taxableAmount = in.readDouble();
		finalUnitPrice = in.readDouble();
		resellerCommission = in.readInt();
		addOnsAmount = in.readInt();
		subTotal = in.readDouble();
		resellerCommissionType = in.readInt();
		offerDiscount = in.readDouble();
		normalUnitPrice = in.readDouble();
		expressUnitPrice = in.readInt();
		taxAmount = in.readInt();
		reason = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(unitPrice);
		dest.writeDouble(subTotalAmount);
		dest.writeDouble(taxableAmount);
		dest.writeDouble(finalUnitPrice);
		dest.writeInt(resellerCommission);
		dest.writeInt(addOnsAmount);
		dest.writeDouble(subTotal);
		dest.writeInt(resellerCommissionType);
		dest.writeDouble(offerDiscount);
		dest.writeDouble(normalUnitPrice);
		dest.writeInt(expressUnitPrice);
		dest.writeInt(taxAmount);
		dest.writeString(reason);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SingleUnitPrice> CREATOR = new Creator<SingleUnitPrice>() {
		@Override
		public SingleUnitPrice createFromParcel(Parcel in) {
			return new SingleUnitPrice(in);
		}

		@Override
		public SingleUnitPrice[] newArray(int size) {
			return new SingleUnitPrice[size];
		}
	};

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setUnitPrice(double unitPrice){
		this.unitPrice = unitPrice;
	}

	public double getUnitPrice(){
		return unitPrice;
	}

	public void setSubTotalAmount(double subTotalAmount){
		this.subTotalAmount = subTotalAmount;
	}

	public double getSubTotalAmount(){
		return subTotalAmount;
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

	public void setResellerCommission(int resellerCommission){
		this.resellerCommission = resellerCommission;
	}

	public int getResellerCommission(){
		return resellerCommission;
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

	public void setResellerCommissionType(int resellerCommissionType){
		this.resellerCommissionType = resellerCommissionType;
	}

	public int getResellerCommissionType(){
		return resellerCommissionType;
	}

	public void setOfferDiscount(double offerDiscount){
		this.offerDiscount = offerDiscount;
	}

	public double getOfferDiscount(){
		return offerDiscount;
	}

	public void setNormalUnitPrice(double normalUnitPrice){
		this.normalUnitPrice = normalUnitPrice;
	}

	public double getNormalUnitPrice(){
		return normalUnitPrice;
	}

	public void setExpressUnitPrice(int expressUnitPrice){
		this.expressUnitPrice = expressUnitPrice;
	}

	public int getExpressUnitPrice(){
		return expressUnitPrice;
	}

	public void setTaxAmount(int taxAmount){
		this.taxAmount = taxAmount;
	}

	public int getTaxAmount(){
		return taxAmount;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}