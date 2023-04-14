package chat.hola.com.app.home.comment;

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
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>CommentModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */
public class CommentFragmentModel {

    @Inject
    List<Comment> comments;
    @Inject
    CommentFragmentAdapter adapter;
    @Inject
    SessionManager sessionManager;

    @Inject
    CommentFragmentModel() {
    }

   /*
    public boolean addNewComment()
    {

        return isAdded;
    }*/




    public void setData(List<Comment> data) {
        if (data != null) {
            this.comments.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public void clearList() {
        this.comments.clear();
    }

  public   void addToList(Comment data) {
        data.setProfilePic(sessionManager.getUserProfilePic());
        data.setCommentedBy(sessionManager.getUserName());
        data.setCommentedByUserId(AppController.getInstance().getUserId());
        comments.add(0, data);
        adapter.notifyDataSetChanged();
    }




}
