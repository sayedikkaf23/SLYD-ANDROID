package chat.hola.com.app.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ezcall.android.R;

public class LowBalanceDialog extends Dialog {

    @SuppressLint("UseCompatLoadingForDrawables")
    public LowBalanceDialog(@NonNull Context context, int image, String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_balance);

        ImageView img = findViewById(R.id.image);
        img.setImageDrawable(context.getDrawable(image));

        TextView msg = findViewById(R.id.tvMessage);
        msg.setText(message);

        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dismiss());


    }
}
