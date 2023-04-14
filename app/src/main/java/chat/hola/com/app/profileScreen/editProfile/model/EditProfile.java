package chat.hola.com.app.profileScreen.editProfile.model;

/**
 * Created by ankit on 4/4/18.
 */

public class EditProfile {

    private String pic;

    private String firstName;

    private String lastName;

    private String userName;

    private String status;

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getStatus() {
        return status;
    }
}
