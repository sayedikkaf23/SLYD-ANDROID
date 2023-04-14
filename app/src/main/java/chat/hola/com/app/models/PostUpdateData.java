package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.home.model.Data;

public class PostUpdateData implements Serializable {

  /* isLike key used here as true false*/

  @SerializedName("isLike")
  @Expose
  private boolean isLike;

  @SerializedName("postId")
  @Expose
  private String postId;

  @SerializedName("data")
  @Expose
  private Data data;

  @SerializedName("from")
  @Expose
  private String from;

  /*
   * Bug Title: text copy is not editable in the adding of the post tab.
   * Bug Id: DUBAND022
   * Fix Desc: add new edit post body
   * Fix Dev: Hardik
   * Fix Date: 15/4/21
   * */

  @SerializedName("body")
  @Expose
  private Map<String,Object> body;

  public PostUpdateData(boolean isLike, String postId) {
    this.isLike = isLike;
    this.postId = postId;
  }

  public PostUpdateData(boolean isLike, String postId, String from) {
    this.isLike = isLike;
    this.postId = postId;
    this.from = from;
  }

  public PostUpdateData(boolean isLike, String postId, Data data) {
    this.isLike = isLike;
    this.postId = postId;
    this.data = data;
  }

  public boolean isLike() {
    return isLike;
  }

  public void setLike(boolean like) {
    isLike = like;
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public PostUpdateData(boolean allowComment, boolean allowDownload, boolean allowDuet, String postId, Map<String,Object> body) {
    this.allowComment = allowComment;
    this.allowDownload = allowDownload;
    this.allowDuet=allowDuet;
    this.postId = postId;
    this.body = body;
  }

  private boolean allowComment, allowDownload,allowDuet;

  public boolean isAllowComment() {
    return allowComment;
  }

  public boolean isAllowDownload() {
    return allowDownload;
  }

  public boolean isAllowDuet() {
    return allowDuet;
  }

  public Map<String, Object> getBody() {
    return body;
  }
}
