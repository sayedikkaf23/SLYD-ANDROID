package chat.hola.com.app.profileScreen.business;


import java.io.Serializable;

/**
 * <h1>OnBoardData</h1>
 * <p>POJO of on board<p/>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 25 June 2019
 */
public class OnBoardData implements Serializable {
    private String title;
    private String description;
    private int image;

    public OnBoardData(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
