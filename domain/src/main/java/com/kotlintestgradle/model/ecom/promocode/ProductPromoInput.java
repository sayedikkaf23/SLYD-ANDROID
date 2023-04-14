package com.kotlintestgradle.model.ecom.promocode;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.getcart.QuantityData;
import java.util.ArrayList;

public class ProductPromoInput implements Parcelable {
  private String product_id;
  private String centralproduct_id;
  private ArrayList<TaxDetails> taxRequests;
  private String name;
  private String brand_name;
  private float price;
  private float unitPrice;
  private float taxAmount;
  private float delivery_fee;
  private ArrayList<String> category_id;
  private String cityId;
  private ArrayList<String> storeId;
  private ArrayList<String> store_id;
  private QuantityData quantityData;

  public ProductPromoInput() {
  }

  public ProductPromoInput(String product_id, String centralproduct_id, ArrayList<TaxDetails> taxRequests, String name, String brand_name, float price, float unitPrice, float taxAmount, float delivery_fee, ArrayList<String> category_id, String cityId, ArrayList<String> storeId, ArrayList<String> store_id, QuantityData quantityData) {
    this.product_id = product_id;
    this.centralproduct_id = centralproduct_id;
    this.taxRequests = taxRequests;
    this.name = name;
    this.brand_name = brand_name;
    this.price = price;
    this.unitPrice = unitPrice;
    this.taxAmount = taxAmount;
    this.delivery_fee = delivery_fee;
    this.category_id = category_id;
    this.cityId = cityId;
    this.storeId = storeId;
    this.store_id = store_id;
    this.quantityData = quantityData;
  }

  protected ProductPromoInput(Parcel in) {
    product_id = in.readString();
    centralproduct_id = in.readString();
    taxRequests = in.createTypedArrayList(TaxDetails.CREATOR);
    name = in.readString();
    brand_name = in.readString();
    price = in.readFloat();
    unitPrice = in.readFloat();
    taxAmount = in.readFloat();
    delivery_fee = in.readFloat();
    category_id = in.createStringArrayList();
    cityId = in.readString();
    storeId = in.createStringArrayList();
    store_id = in.createStringArrayList();
    quantityData = in.readParcelable(QuantityData.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(product_id);
    dest.writeString(centralproduct_id);
    dest.writeTypedList(taxRequests);
    dest.writeString(name);
    dest.writeString(brand_name);
    dest.writeFloat(price);
    dest.writeFloat(unitPrice);
    dest.writeFloat(taxAmount);
    dest.writeFloat(delivery_fee);
    dest.writeStringList(category_id);
    dest.writeString(cityId);
    dest.writeStringList(storeId);
    dest.writeStringList(store_id);
    dest.writeParcelable(quantityData, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductPromoInput> CREATOR = new Creator<ProductPromoInput>() {
    @Override
    public ProductPromoInput createFromParcel(Parcel in) {
      return new ProductPromoInput(in);
    }

    @Override
    public ProductPromoInput[] newArray(int size) {
      return new ProductPromoInput[size];
    }
  };

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public String getCentralproduct_id() {
    return centralproduct_id;
  }

  public void setCentralproduct_id(String centralproduct_id) {
    this.centralproduct_id = centralproduct_id;
  }

  public ArrayList<TaxDetails> getTaxRequests() {
    return taxRequests;
  }

  public void setTaxRequests(ArrayList<TaxDetails> taxRequests) {
    this.taxRequests = taxRequests;
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

  public float getDelivery_fee() {
    return delivery_fee;
  }

  public void setDelivery_fee(float delivery_fee) {
    this.delivery_fee = delivery_fee;
  }

  public ArrayList<String> getCategory_id() {
    return category_id;
  }

  public void setCategory_id(ArrayList<String> category_id) {
    this.category_id = category_id;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public ArrayList<String> getStoreId() {
    return storeId;
  }

  public void setStoreId(ArrayList<String> storeId) {
    this.storeId = storeId;
  }

  public ArrayList<String> getStore_id() {
    return store_id;
  }

  public void setStore_id(ArrayList<String> store_id) {
    this.store_id = store_id;
  }

  public QuantityData getQuantityData() {
    return quantityData;
  }

  public void setQuantityData(QuantityData quantityData) {
    this.quantityData = quantityData;
  }
}

