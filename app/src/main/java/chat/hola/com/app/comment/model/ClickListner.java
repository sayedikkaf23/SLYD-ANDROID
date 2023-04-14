package chat.hola.com.app.comment.model;

/**
 * <h1>ClickListner</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public interface ClickListner {
    void onUserClick(int position, String userId);

    void itemSelect(int position, boolean isSelected);

    void onDeleteComment(int position, Comment comment);

    void onLikeComment(int position, Comment comment, boolean isLike);
}
