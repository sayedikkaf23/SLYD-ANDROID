package chat.hola.com.app.home.trending;

import java.util.ArrayList;

/**
 * Created by ankit on 27/2/18.
 */

public class Trending {
    private int profileImg;
    private int profileImgBg;
    private String title;
    private int Subscribers;
    private ArrayList<Integer> postImages;

    public Trending(int profileImg,int profileImgBg, String title, int subscribers, ArrayList<Integer> postImages) {

        this.profileImg = profileImg;
        this.profileImgBg = profileImgBg;
        this.title = title;
        this.Subscribers = subscribers;
        this.postImages = postImages;
    }

    public int getProfileImg() {
        return profileImg;
    }

    public int getProfileImgBg() {
        return profileImgBg;
    }

    public String getTitle() {
        return title;
    }

    public int getSubscribers() {
        return Subscribers;
    }

    public ArrayList<Integer> getPostImages() {
        return postImages;
    }
}
