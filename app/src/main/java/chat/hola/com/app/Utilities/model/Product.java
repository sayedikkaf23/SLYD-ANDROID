package chat.hola.com.app.Utilities.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
  private String productName;
  private String parentId;
  private String childId;
  private String unitId;
  private String actualPrice;
  private String discountPrice;
  private boolean outOfStock;
  private String quantity;
  private String img;
  private String currencySymbol;
  private String currencyCode;
  private String brandName;
  private String catName;
  private int action;
  private String size;
  private String sizeUnit;
  private String singleItemPrice;
  private String mShoppingListId;
  private String centralProductId;
  private String storeId;
  private int cartQuantity;

  public Product(String productName, String parentId, String childId, String unitId,
      String actualPrice, String discountPrice, boolean outOfStock, /*String quantity,*/
      String img, String currencySymbol, String currencyCode, String brandName,
      String catName, int action) {
    this.productName = productName;
    this.parentId = parentId;
    this.childId = childId;
    this.unitId = unitId;
    this.actualPrice = actualPrice;
    this.discountPrice = discountPrice;
    this.outOfStock = outOfStock;
    this.img = img;
    this.currencySymbol = currencySymbol;
    this.currencyCode = currencyCode;
    this.brandName = brandName;
    this.catName = catName;
    this.action = action;
  }

  public Product(String productName, String childId, String unitId, String quantity,
      String centralProductId, String storeId) {
    this.productName = productName;
    this.childId = childId;
    this.unitId = unitId;
    this.quantity = quantity;
    this.centralProductId = centralProductId;
    this.storeId = storeId;
  }

  public Product(String parentId, String childId, String shoppingListId) {
    this.parentId = parentId;
    this.childId = childId;
    mShoppingListId = shoppingListId;
  }

  public Product(String productName, String childId, String unitId, String img,
      String currencySymbol, String currencyCode, int action, String size, String sizeUnit,
      String singleItemPrice, String centralProductId, String storeId) {
    this.productName = productName;
    this.childId = childId;
    this.unitId = unitId;
    this.img = img;
    this.currencySymbol = currencySymbol;
    this.currencyCode = currencyCode;
    this.action = action;
    this.size = size;
    this.sizeUnit = sizeUnit;
    this.storeId = storeId;
    this.singleItemPrice = singleItemPrice;
    this.centralProductId = centralProductId;
  }

  protected Product(Parcel in) {
    productName = in.readString();
    parentId = in.readString();
    childId = in.readString();
    unitId = in.readString();
    actualPrice = in.readString();
    discountPrice = in.readString();
    outOfStock = in.readByte() != 0x00;
    quantity = in.readString();
    img = in.readString();
    currencySymbol = in.readString();
    currencyCode = in.readString();
    brandName = in.readString();
    catName = in.readString();
    action = in.readInt();
    size = in.readString();
    sizeUnit = in.readString();
    singleItemPrice = in.readString();
    mShoppingListId = in.readString();
    centralProductId = in.readString();
    storeId = in.readString();
  }

  public int getCartQuantity() {
    return cartQuantity;
  }

  public void setCartQuantity(int cartQuantity) {
    this.cartQuantity = cartQuantity;
  }

  public static final Creator<Product> CREATOR = new Creator<Product>() {
    @Override
    public Product createFromParcel(Parcel in) {
      return new Product(in);
    }

    @Override
    public Product[] newArray(int size) {
      return new Product[size];
    }
  };

  public String getCentralProductId() {
    return centralProductId;
  }

  public void setCentralProductId(String centralProductId) {
    this.centralProductId = centralProductId;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getShoppingListId() {
    return mShoppingListId;
  }

  public void setShoppingListId(String shoppingListId) {
    mShoppingListId = shoppingListId;
  }

  public int getAction() {
    return action;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getSizeUnit() {
    return sizeUnit;
  }

  public void setSizeUnit(String sizeUnit) {
    this.sizeUnit = sizeUnit;
  }

  public String getSingleItemPrice() {
    return singleItemPrice;
  }

  public void setSingleItemPrice(String singleItemPrice) {
    this.singleItemPrice = singleItemPrice;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getChildId() {
    return childId;
  }

  public void setChildId(String childId) {
    this.childId = childId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getActualPrice() {
    return actualPrice;
  }

  public void setActualPrice(String actualPrice) {
    this.actualPrice = actualPrice;
  }

  public String getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(String discountPrice) {
    this.discountPrice = discountPrice;
  }

  public boolean isOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(boolean outOfStock) {
    this.outOfStock = outOfStock;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(productName);
    parcel.writeString(parentId);
    parcel.writeString(childId);
    parcel.writeString(unitId);
    parcel.writeString(actualPrice);
    parcel.writeString(discountPrice);
    parcel.writeByte((byte) (outOfStock ? 0x01 : 0x00));
    parcel.writeString(quantity);
    parcel.writeString(img);
    parcel.writeString(currencySymbol);
    parcel.writeString(currencyCode);
    parcel.writeString(brandName);
    parcel.writeString(catName);
    parcel.writeInt(action);
    parcel.writeString(size);
    parcel.writeString(sizeUnit);
    parcel.writeString(singleItemPrice);
    parcel.writeString(mShoppingListId);
    parcel.writeString(centralProductId);
    parcel.writeString(storeId);
  }
}