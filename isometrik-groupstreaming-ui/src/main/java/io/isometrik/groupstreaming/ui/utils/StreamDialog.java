package io.isometrik.groupstreaming.ui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import io.isometrik.groupstreaming.ui.R;

/**
 * The type Stream dialog.
 */
public class StreamDialog {

  /**
   * Gets stream dialog.
   *
   * @param context the context
   * @param message the message
   * @param dialogType the dialog type
   * @return the stream dialog
   */
  public static AlertDialog.Builder getStreamDialog(Context context, String message,
      int dialogType) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
    @SuppressLint("InflateParams")
    final View vStreamOffline =
        ((Activity) context).getLayoutInflater().inflate(R.layout.ism_dialog_stream_offline, null);

    TextView tvMessage = vStreamOffline.findViewById(R.id.tvMessage);
    tvMessage.setText(message);

    AppCompatImageView ivEmoji = vStreamOffline.findViewById(R.id.ivEmoji);
    String url = "";
    if (dialogType == StreamDialogEnum.StreamOffline.getValue()) {
      //Stream Offline
      url = Constants.STREAM_OFFLINE_URL;
    } else if (dialogType == StreamDialogEnum.KickedOut.getValue()) {
      //Kicked Out
      url = Constants.KICKED_OUT_URL;
    }
    try {
      Glide.with(context)
          .load(url)
          .centerCrop()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(ivEmoji);
    } catch (NullPointerException | IllegalArgumentException e) {
      e.printStackTrace();
    }
    alertDialog.setView(vStreamOffline);
    return alertDialog;
  }
}
