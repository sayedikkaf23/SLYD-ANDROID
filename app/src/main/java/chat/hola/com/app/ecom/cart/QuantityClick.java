package chat.hola.com.app.ecom.cart;

import androidx.appcompat.widget.AppCompatTextView;

public interface QuantityClick {
  void onItemClick(AppCompatTextView appCompatTextView, String unitName, int pos, int action);
}
