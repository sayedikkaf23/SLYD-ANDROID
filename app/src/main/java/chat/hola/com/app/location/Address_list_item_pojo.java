package chat.hola.com.app.location;

/**
 * @since 6/2/17.
 */

public class Address_list_item_pojo {
    String id;
    String Address_title;
    String sub_Address;
    String latitude;
    String logitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }

    public String getAddress_title() {
        return Address_title;
    }

    public void setAddress_title(String address_title) {
        Address_title = address_title;
    }

    public String getSub_Address() {
        return sub_Address;
    }

    public void setSub_Address(String sub_Address) {
        this.sub_Address = sub_Address;
    }

    @Override
    public String toString() {
        return "Address_list_item_pojo{" +
                "Address_title='" + Address_title + '\'' +
                ", sub_Address='" + sub_Address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", logitude='" + logitude + '\'' +
                '}';
    }
}
