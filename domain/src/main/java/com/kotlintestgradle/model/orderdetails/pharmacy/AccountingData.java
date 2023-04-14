package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AccountingData implements Parcelable {
	private double unitPrice;
	private double taxableAmount;
	private double finalUnitPrice;
	private int appEarningWithTax;
	private int pgEarning;
	private PayByData payBy;
	private int driverEarning;
	private String currencySymbol;
	private int addOnsAmount;
	private List<Object> tax;
	private double subTotal;
	private int storeEarning;
	private double offerDiscount;
	private double deliveryFee;
	private DeliveryDetailsData deliveryDetails;
	private double taxAmount;
	private int promoDiscount;
	private String currencyCode;
	private double finalTotal;
	private int appEarning;
	private double tip;

	public AccountingData(double unitPrice, double taxableAmount, double finalUnitPrice, int appEarningWithTax, int pgEarning, PayByData payBy, int driverEarning, String currencySymbol, int addOnsAmount, List<Object> tax, double subTotal, int storeEarning, double offerDiscount, double deliveryFee, DeliveryDetailsData deliveryDetails, double taxAmount, int promoDiscount, String currencyCode, double finalTotal, int appEarning, double tip) {
		this.unitPrice = unitPrice;
		this.taxableAmount = taxableAmount;
		this.finalUnitPrice = finalUnitPrice;
		this.appEarningWithTax = appEarningWithTax;
		this.pgEarning = pgEarning;
		this.payBy = payBy;
		this.driverEarning = driverEarning;
		this.currencySymbol = currencySymbol;
		this.addOnsAmount = addOnsAmount;
		this.tax = tax;
		this.subTotal = subTotal;
		this.storeEarning = storeEarning;
		this.offerDiscount = offerDiscount;
		this.deliveryFee = deliveryFee;
		this.deliveryDetails = deliveryDetails;
		this.taxAmount = taxAmount;
		this.promoDiscount = promoDiscount;
		this.currencyCode = currencyCode;
		this.finalTotal = finalTotal;
		this.appEarning = appEarning;
		this.tip = tip;
	}

	protected AccountingData(Parcel in) {
		unitPrice = in.readDouble();
		taxableAmount = in.readDouble();
		finalUnitPrice = in.readDouble();
		appEarningWithTax = in.readInt();
		pgEarning = in.readInt();
		payBy = in.readParcelable(PayByData.class.getClassLoader());
		driverEarning = in.readInt();
		currencySymbol = in.readString();
		addOnsAmount = in.readInt();
		subTotal = in.readDouble();
		storeEarning = in.readInt();
		offerDiscount = in.readDouble();
		deliveryFee = in.readDouble();
		deliveryDetails = in.readParcelable(DeliveryDetailsData.class.getClassLoader());
		taxAmount = in.readDouble();
		promoDiscount = in.readInt();
		currencyCode = in.readString();
		finalTotal = in.readDouble();
		appEarning = in.readInt();
		tip = in.readDouble();
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
		dest.writeDouble(offerDiscount);
		dest.writeDouble(deliveryFee);
		dest.writeParcelable(deliveryDetails, flags);
		dest.writeDouble(taxAmount);
		dest.writeInt(promoDiscount);
		dest.writeString(currencyCode);
		dest.writeDouble(finalTotal);
		dest.writeInt(appEarning);
		dest.writeDouble(tip);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<AccountingData> CREATOR = new Creator<AccountingData>() {
		@Override
		public AccountingData createFromParcel(Parcel in) {
			return new AccountingData(in);
		}

		@Override
		public AccountingData[] newArray(int size) {
			return new AccountingData[size];
		}
	};

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public double getFinalUnitPrice() {
		return finalUnitPrice;
	}

	public void setFinalUnitPrice(double finalUnitPrice) {
		this.finalUnitPrice = finalUnitPrice;
	}

	public int getAppEarningWithTax() {
		return appEarningWithTax;
	}

	public void setAppEarningWithTax(int appEarningWithTax) {
		this.appEarningWithTax = appEarningWithTax;
	}

	public int getPgEarning() {
		return pgEarning;
	}

	public void setPgEarning(int pgEarning) {
		this.pgEarning = pgEarning;
	}

	public PayByData getPayBy() {
		return payBy;
	}

	public void setPayBy(PayByData payBy) {
		this.payBy = payBy;
	}

	public int getDriverEarning() {
		return driverEarning;
	}

	public void setDriverEarning(int driverEarning) {
		this.driverEarning = driverEarning;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public int getAddOnsAmount() {
		return addOnsAmount;
	}

	public void setAddOnsAmount(int addOnsAmount) {
		this.addOnsAmount = addOnsAmount;
	}

	public List<Object> getTax() {
		return tax;
	}

	public void setTax(List<Object> tax) {
		this.tax = tax;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public int getStoreEarning() {
		return storeEarning;
	}

	public void setStoreEarning(int storeEarning) {
		this.storeEarning = storeEarning;
	}

	public double getOfferDiscount() {
		return offerDiscount;
	}

	public void setOfferDiscount(double offerDiscount) {
		this.offerDiscount = offerDiscount;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public DeliveryDetailsData getDeliveryDetails() {
		return deliveryDetails;
	}

	public void setDeliveryDetails(DeliveryDetailsData deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public int getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(int promoDiscount) {
		this.promoDiscount = promoDiscount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(double finalTotal) {
		this.finalTotal = finalTotal;
	}

	public int getAppEarning() {
		return appEarning;
	}

	public void setAppEarning(int appEarning) {
		this.appEarning = appEarning;
	}

	public double getTip() {
		return tip;
	}

	public void setTip(double tip) {
		this.tip = tip;
	}
}