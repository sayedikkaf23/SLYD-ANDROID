package chat.hola.com.app.ecom.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavedCardsData {
  @SerializedName("last4")
  @Expose
  private String last4;
  @SerializedName("isDefault")
  @Expose
  private String isDefault;
  @SerializedName("exp_month")
  @Expose
  private String expMonth;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("exp_year")
  @Expose
  private String expYear;
  @SerializedName("brand")
  @Expose
  private String brand;
  @SerializedName("customer")
  @Expose
  private String customer;
  private boolean isChecked;
  private boolean isEnabled;

  public String getLast4() {
    return last4;
  }

  public void setLast4(String last4) {
    this.last4 = last4;
  }

  public String getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }

  public String getExpMonth() {
    return expMonth;
  }

  public void setExp_month(String expMonth) {
    this.expMonth = expMonth;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getExpYear() {
    return expYear;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public boolean isChecked() {
    return isChecked;
  }

  public void setChecked(boolean checked) {
    isChecked = checked;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }
}
