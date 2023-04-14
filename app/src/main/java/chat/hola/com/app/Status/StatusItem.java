package chat.hola.com.app.Status;

/**
 * Created by moda on 04/08/17.
 */

public class StatusItem {


    /**
     * type--
     * 1- predefined
     * 0- manually written
     */

    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    private int statusType;


}
