package chat.hola.com.app.ui.dialog;

import static chat.hola.com.app.Utilities.Constants.CANCEL;

import android.app.Activity;
import android.app.AlertDialog;
import com.ezcall.android.R;

/*
 * Purpose – This class holds different types of reusable Dialogs
 * @author 3Embed
 * Created on Nov 25, 2019
 * Modified on
 */
public class CustomDialogUtil {
  /**
   * This method is using to show basic alert Dialog
   *
   * @param activity     activity reference to show dialog
   * @param clickHandler clickListener class subclass reference
   * @param title        Alert dialog title
   * @param message      Alert dialog message
   */
  public static void basicAlertDialog(Activity activity,
      SimpleAlertDialogClickHandler clickHandler, String title, String message, int type) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.ok, (dialog, id) -> {
      clickHandler.onOkClickListener(type);
    });
    builder.setNegativeButton(R.string.allCancel,
        (dialog, id) -> {
          dialog.dismiss();
          clickHandler.onOkClickListener(CANCEL);
        });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  public interface DialogOutOfStockNotifyListener {
    void onNotifyMail(String mail);
  }

  public interface DialogCallBackListener {
    void onLogOutClickListener();
  }

  public interface ReplaceStoreCallBackListener {
    void onYesClickListener(int count, int action);
  }

  public interface SimpleAlertDialogClickHandler {
    /**
     * This method is using as ok button click callback
     */
    void onOkClickListener(int type);
  }
}
