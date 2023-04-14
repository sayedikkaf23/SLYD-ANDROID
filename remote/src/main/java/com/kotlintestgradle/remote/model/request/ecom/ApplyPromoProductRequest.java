package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.model.response.ecom.getcart.QuantityDetails;
import java.util.ArrayList;

public class ApplyPromoProductRequest implements Parcelable {

  @Expose
  @SerializedName("product_id")
  private String product_id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("brandName")
  private String brand_name;
  @Expose
  @SerializedName("price")
  private float price;
  @Expose
  @SerializedName("delivery_fee")
  private float delivery_fee;
  @Expose
  @SerializedName("category_id")
  private ArrayList<String> category_id;
  @Expose
  @SerializedName("centralproduct_id")
  private String centralproduct_id;
  @Expose
  @SerializedName("tax")
  private ArrayList<TaxRequest> tax;
  @Expose
  @SerializedName("quantity")
  private QuantityDetails quantity;
  @Expose
  @SerializedName("store_id")
  private ArrayList<String> store_id;
  @Expose
  @SerializedName("cityId")
  private String cityId;
  @Expose
  @SerializedName("unitPrice")
  private float unitPrice;
  @Expose
  @SerializedName("taxAmount")
  private float taxAmount;

  public ApplyPromoProductRequest() {
  }

  public ApplyPromoProductRequest(String product_id, String name, String brand_name, float price,
                                  float delivery_fee, ArrayList<String> category_id, String centralproduct_id,
                                  ArrayList<TaxRequest> tax, QuantityDetails quantity, ArrayList<String> store_id,
                                  String cityId, float unitPrice, float taxAmount) {
    this.product_id = product_id;
    this.name = name;
    this.brand_name = brand_name;
    this.price = price;
    this.delivery_fee = delivery_fee;
    this.category_id = category_id;
    this.centralproduct_id = centralproduct_id;
    this.tax = tax;
    this.quantity = quantity;
    this.store_id = store_id;
    this.cityId = cityId;
    this.unitPrice = unitPrice;
    this.taxAmount = taxAmount;
  }

  protected ApplyPromoProductRequest(Parcel in) {
    product_id = in.readString();
    name = in.readString();
    brand_name = in.readString();
    price = in.readFloat();
    delivery_fee = in.readFloat();
    category_id = in.createStringArrayList();
    centralproduct_id = in.readString();
    tax = in.createTypedArrayList(TaxRequest.CREATOR);
    quantity = in.readParcelable(QuantityDetails.class.getClassLoader());
    store_id = in.createStringArrayList();
    cityId = in.readString();
    unitPrice = in.readFloat();
    taxAmount = in.readFloat();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(product_id);
    dest.writeString(name);
    dest.writeString(brand_name);
    dest.writeFloat(price);
    dest.writeFloat(delivery_fee);
    dest.writeStringList(category_id);
    dest.writeString(centralproduct_id);
    dest.writeTypedList(tax);
    dest.writeParcelable(quantity, flags);
    dest.writeStringList(store_id);
    dest.writeString(cityId);
    dest.writeFloat(unitPrice);
    dest.writeFloat(taxAmount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ApplyPromoProductRequest> CREATOR = new Creator<ApplyPromoProductRequest>() {
    @Override
    public ApplyPromoProductRequest createFromParcel(Parcel in) {
      return new ApplyPromoProductRequest(in);
    }

    @Override
    public ApplyPromoProductRequest[] newArray(int size) {
      return new ApplyPromoProductRequest[size];
    }
  };

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBrand_name() {
    return brand_name;
  }

  public void setBrand_name(String brand_name) {
    this.brand_name = brand_name;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getDelivery_fee() {
    return delivery_fee;
  }

  public void setDelivery_fee(int delivery_fee) {
    this.delivery_fee = delivery_fee;
  }



  public ArrayList<String> getCategory_id() {
    return category_id;
  }

  public void setCategory_id(ArrayList<String> category_id) {
    this.category_id = category_id;
  }

  public String getCentralproduct_id() {
    return centralproduct_id;
  }

  public void setCentralproduct_id(String centralproduct_id) {
    this.centralproduct_id = centralproduct_id;
  }

  public ArrayList<TaxRequest> getTax() {
    return tax;
  }

  public void setTax(ArrayList<TaxRequest> tax) {
    this.tax = tax;
  }

  public QuantityDetails getQuantity() {
    return quantity;
  }

  public void setQuantity(QuantityDetails quantity) {
    this.quantity = quantity;
  }

  public ArrayList<String> getStore_id() {
    return store_id;
  }

  public void setStore_id(ArrayList<String> store_id) {
    this.store_id = store_id;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public float getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(float unitPrice) {
    this.unitPrice = unitPrice;
  }

  public float getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(float taxAmount) {
    this.taxAmount = taxAmount;
  }

  public void setDelivery_fee(float delivery_fee) {
    this.delivery_fee = delivery_fee;
  }

}

