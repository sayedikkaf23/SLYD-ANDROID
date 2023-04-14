package chat.hola.com.app.models;


import java.io.Serializable;

/**
 * <h1>Place</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
public class Place implements Serializable {
    private String id;
    private String title;
    private String address;
    private String latitude;
    private String logitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
