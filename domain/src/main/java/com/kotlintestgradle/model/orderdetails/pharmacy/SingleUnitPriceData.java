package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SingleUnitPriceData implements Parcelable {
	private double unitPrice;
	private double subTotalAmount;
	private double taxableAmount;
	private double finalUnitPrice;
	private int resellerCommission;
	private int addOnsAmount;
	private List<Object> tax;
	private double subTotal;
	private int resellerCommissionType;
	private double offerDiscount;
	private double normalUnitPrice;
	private int expressUnitPrice;
	private int taxAmount;
	private String reason;

	public SingleUnitPriceData(double unitPrice, double subTotalAmount, double taxableAmount, double finalUnitPrice, int resellerCommission, int addOnsAmount, List<Object> tax, double subTotal, int resellerCommissionType, double offerDiscount, double normalUnitPrice, int expressUnitPrice, int taxAmount, String reason) {
		this.unitPrice = unitPrice;
		this.subTotalAmount = subTotalAmount;
		this.taxableAmount = taxableAmount;
		this.finalUnitPrice = finalUnitPrice;
		this.resellerCommission = resellerCommission;
		this.addOnsAmount = addOnsAmount;
		this.tax = tax;
		this.subTotal = subTotal;
		this.resellerCommissionType = resellerCommissionType;
		this.offerDiscount = offerDiscount;
		this.normalUnitPrice = normalUnitPrice;
		this.expressUnitPrice = expressUnitPrice;
		this.taxAmount = taxAmount;
		this.reason = reason;
	}

	protected SingleUnitPriceData(Parcel in) {
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

	public static final Creator<SingleUnitPriceData> CREATOR = new Creator<SingleUnitPriceData>() {
		@Override
		public SingleUnitPriceData createFromParcel(Parcel in) {
			return new SingleUnitPriceData(in);
		}

		@Override
		public SingleUnitPriceData[] newArray(int size) {
			return new SingleUnitPriceData[size];
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
}