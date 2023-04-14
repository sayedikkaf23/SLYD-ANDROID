package chat.hola.com.app.blockUser.model;

/**
 * <h1>ClickListner</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public interface ClickListner {
    void onUserClick(int position);

    void itemSelect(int position, boolean isSelected);

    void btnUnblock(int adapterPosition);
}
