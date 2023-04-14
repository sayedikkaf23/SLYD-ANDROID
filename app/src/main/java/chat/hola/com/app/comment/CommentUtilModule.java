package chat.hola.com.app.comment;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.CommentAdapter;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>BlockUserUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */
////@ActivityScoped
@Module
public class CommentUtilModule {

    @ActivityScoped
    @Provides
    List<Comment> getComments() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    CommentAdapter commentAdapter(List<Comment> comments, Activity mContext, TypefaceManager typefaceManager) {
        return new CommentAdapter(comments, mContext, typefaceManager);
    }
}
