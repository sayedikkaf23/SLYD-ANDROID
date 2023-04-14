package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewPostRequest implements Serializable {
    @SerializedName("posts")
    @Expose
    ArrayList<ViewPost> viewPosts;

    public ArrayList<ViewPost> getViewPosts() {
        return viewPosts;
    }

    public void setViewPosts(ArrayList<ViewPost> viewPosts) {
        this.viewPosts = viewPosts;
    }
}
