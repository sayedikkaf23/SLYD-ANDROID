package chat.hola.com.app.comment;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.CommentAdapter;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>CommentModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

public class CommentModel {

    @Inject
    List<Comment> comments;
    @Inject
    CommentAdapter adapter;
    @Inject
    SessionManager sessionManager;

    @Inject
    CommentModel() {
    }

   /*
    public boolean addNewComment()
    {

        return isAdded;
    }*/


    Map<String, Object> getParamsAddComment(String postId,String comment) {
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(comment)) {
            map.put("comment", comment);
            map.put("postId", postId);

            //hashtag
            String regexPattern = "(#\\w+)";
            Pattern p = Pattern.compile(regexPattern);
            Matcher m = p.matcher(comment);
            StringBuilder hashtag = new StringBuilder();
            while (m.find()) {
                hashtag.append(",").append(m.group(1));
            }
            map.put("hashTags", hashtag.toString());

            //userTag
            String regexPattern1 = "(@\\w+)";
            Pattern p1 = Pattern.compile(regexPattern1);
            Matcher m1 = p1.matcher(comment);
            int i = 0;
            ArrayList<String> strings = new ArrayList<>();
            String userTag[] = new String[i + 1];
            while (m1.find()) {
                strings.add(m1.group(1).replace("@", ""));
                // userTag[i++] = m1.group(1).replace("@", "");
            }
            map.put("userTags", strings);

        }
        map.put("postedBy", AppController.getInstance().getUserId());
        map.put("ip", sessionManager.getIpAdress());
        map.put("city", sessionManager.getCity());
        map.put("country", sessionManager.getCountry());
        return map;
    }

    public void setData(List<Comment> data) {
        if (data != null) {
            this.comments.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public void clearList() {
        this.comments.clear();
    }

    void addToList(Comment data) {
        data.setProfilePic(sessionManager.getUserProfilePic());
        data.setCommentedBy(sessionManager.getUserName());
        data.setCommentedByUserId(AppController.getInstance().getUserId());
        comments.add(0, data);
        adapter.notifyDataSetChanged();
    }

    public String getUserId(int position) {
        return comments.get(position).getCommentedByUserId();
    }

    public void selectItem(int position, boolean isSelected) {
        comments.get(position).setSelected(isSelected);
    }

    public int getTotalComments() {
        return comments.size();
    }

    public void commentDeleted(int position, String commentId) {
        comments.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void commentLiked(int position, String commentId, boolean isLike) {

    }
}
