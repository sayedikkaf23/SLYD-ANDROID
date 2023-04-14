package chat.hola.com.app.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ezcall.android.R;

public class ConfirmDialog extends Dialog {

    public ConfirmDialog(@NonNull Context context, int image, String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_confirm);

        Button btnNotNow = findViewById(R.id.btnNo);
        btnNotNow.setOnClickListener(v -> dismiss());

        ImageView imageView = findViewById(R.id.ivImage);
        imageView.setImageDrawable(context.getDrawable(image));

        TextView tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText(message);
    }

    public ConfirmDialog(@NonNull Context context, int image, String message, String positiveButton, String negativeButton) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_confirm);

        Button btnNo = findViewById(R.id.btnNo);
        Button btnYes = findViewById(R.id.btnYes);

        if (negativeButton == null)
            btnNo.setVisibility(View.GONE);

        btnYes.setText(positiveButton);
        btnNo.setText(negativeButton);

        btnNo.setOnClickListener(v -> dismiss());

        ImageView imageView = findViewById(R.id.ivImage);
        imageView.setImageDrawable(context.getDrawable(image));

        TextView tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText(message);
    }
}
