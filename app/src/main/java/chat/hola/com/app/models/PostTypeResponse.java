package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>PostTypeResponse</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class PostTypeResponse implements Serializable {
    public List<PostType> getPostTypes() {
        return postTypes;
    }

    public void setPostTypes(List<PostType> postTypes) {
        this.postTypes = postTypes;
    }

    @SerializedName("data")
    @Expose
    List<PostType> postTypes;


    public class PostType implements Serializable {
        @SerializedName("_id")
        @Expose
        String id;
        @SerializedName("text")
        @Expose
        String text;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
