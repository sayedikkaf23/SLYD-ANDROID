package chat.hola.com.app.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Window;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/30/2018.
 */
public class BlockDialog extends Dialog {
    private Activity activity;
    private Context context;


    @Inject
    public BlockDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BlockDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_block);
        ButterKnife.bind(this);
        setCancelable(false);
    }

    @OnClick(R.id.ok)
    public void ok() {
        this.dismiss();
    }
}
