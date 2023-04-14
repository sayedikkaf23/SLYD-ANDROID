package chat.hola.com.app.ui.validate;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;

/**
 * <h1></h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 29 Jan 2020
 */
public class ValidateActivity extends BaseActivity {

    @Inject
    TypefaceManager font;

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.btnOk)
    Button btnOk;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_validate;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        btnOk.setTypeface(font.getSemiboldFont());
        tvMessage.setTypeface(font.getSemiboldFont());
        tvDescription.setTypeface(font.getRegularFont());
    }

    @Override
    public void initView() {
        super.initView();
        int drawable = getIntent().getIntExtra("image", R.drawable.ic_wallet_image);
        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");
        tvMessage.setText(title);
        tvDescription.setText(message);
        ivImage.setImageDrawable(getDrawable(drawable));
    }

    @OnClick(R.id.btnOk)
    public void okButton() {
        finish();
    }
}
