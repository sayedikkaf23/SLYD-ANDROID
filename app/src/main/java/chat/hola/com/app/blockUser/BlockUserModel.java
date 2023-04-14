package chat.hola.com.app.blockUser;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.blockUser.model.BlockUserAdapter;
import chat.hola.com.app.blockUser.model.User;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>BlockUserModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

class BlockUserModel {

    @Inject
    List<User> comments;
    @Inject
    BlockUserAdapter adapter;
    @Inject
    SessionManager sessionManager;

    @Inject
    BlockUserModel() {
    }

   /*
    public boolean addNewComment()
    {

        return isAdded;
    }*/


    Map<String, Object> getParamsAddComment(String comment) {
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(comment)) {
            map.put("comment", comment);

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

        return map;
    }

    public void setData(List<User> data) {
        if (data != null) {
            this.comments.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public void clearList() {
        this.comments.clear();
    }

    void addToList(User data) {
        data.setProfilePic(sessionManager.getUserProfilePic());
        comments.add(data);
        adapter.notifyDataSetChanged();

    }


    public String getUserId(int position) {
        return comments.get(position).getId();
    }

    public void updateData(int position) {
        comments.remove(position);
        adapter.notifyDataSetChanged();
    }

    public String getUserName(int position) {
        return comments.get(position).getUserName();
    }
}
