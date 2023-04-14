package chat.hola.com.app.orders.historyscreen;

import android.os.Bundle;

/**
 * history item interface call back
 */
public interface HistoryItemClick {
  /**
   * onclick method
   * @param action which action performed on history item,based on this we will open respective activity
   * @param bundle bundle data to start an activity
   */
  void onClick(int action,int masterPos,int storePos,int productPos, Bundle bundle);
}
