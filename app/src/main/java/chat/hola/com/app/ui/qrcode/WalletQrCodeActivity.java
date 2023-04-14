package chat.hola.com.app.ui.qrcode;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.qr_code_scanner.QRCodeActivity;

/**
 * <h1>WalletQrCode</h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 29 Jan 2020
 */
public class WalletQrCodeActivity extends BaseActivity implements WalletQrCodeContract.View {

    @Inject
    Loader loader;
    @Inject
    TypefaceManager font;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvMobileNumber)
    TextView tvMobileNumber;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_qrcode;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvUserName.setTypeface(font.getSemiboldFont());
        tvMessage.setTypeface(font.getSemiboldFont());
        tvName.setTypeface(font.getRegularFont());
        tvMobileNumber.setTypeface(font.getRegularFont());
    }

    @OnClick(R.id.rlScanner)
    public void scanner() {
        Intent intent = new Intent(this, QRCodeActivity.class);
        intent.putExtra("from", "wallet");
        startActivity(intent);
    }

    @Override
    public void initView() {
        super.initView();
        displayInformation(sessionManager.getQrCode());
    }

    @OnClick(R.id.btnClose)
    public void okClose() {
        super.onBackPressed();
    }

    @Override
    public void displayInformation(String qrCode) {

        Glide.with(this).load(qrCode).asBitmap().into(ivQrCode);

        String name = sessionManager.getFirstName();
        if (sessionManager.getLastName() != null)
            name = name + " " + sessionManager.getLastName();
        tvName.setText(name);
        tvUserName.setText(sessionManager.getUserName());
        tvMobileNumber.setText(sessionManager.getMobileNumber());
    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(int message) {
        message(getString(message));
    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader != null)
            loader.dismiss();
    }
}
