package chat.hola.com.app.ecom.pdp;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * This interface used for call back when we click on the menu icon
 */
public interface ReviewsMenuClick {
  void onMenuClick(int likeOrMenu, int pos, AppCompatImageView optionMenu, boolean likeOrDislike);
}
