package chat.hola.com.app.my_qr_code;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.qr_code_scanner.QRCodeActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class MyQRCodeActivity extends DaggerAppCompatActivity implements MyQRCodeContract.View {

  @BindView(R.id.iV_qrCode)
  ImageView iV_qrCode;
  @BindView(R.id.iv_refresh)
  ImageView iv_refresh;
  @BindView(R.id.iV_profilePic)
  ImageView iV_profilePic;
  @BindView(R.id.tV_userName)
  TextView tV_userName;

  public Data profileData;

  @Inject
  MyQRCodePrensenter prensenter;
  private Unbinder unbinder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_qrcode);
    unbinder = ButterKnife.bind(this);

    if (getIntent() != null) {
      profileData = (Data) getIntent().getSerializableExtra("profileData");
      prensenter.parseIntent(profileData);
    }
  }

  @Override
  public void initData(Data profileData) {
    tV_userName.setText(profileData.getUserName());
    Glide.with(this).load(profileData.getProfilePic()).asBitmap().centerCrop()
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE)
        //.skipMemoryCache(true)
        .placeholder(R.mipmap.ic_launcher).into(new BitmapImageViewTarget(iV_profilePic) {
      @Override
      protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        iV_profilePic.setImageDrawable(circularBitmapDrawable);
      }
    });
    Glide.with(this).load(profileData.getQrCode()).asBitmap().into(iV_qrCode);
    //iV_qrCode.setImageURI(Uri.parse(profileData.getQrCode()));
  }

  @OnClick(R.id.iV_back)
  public void back() {
    onBackPressed();
  }

  @OnClick(R.id.iV_scanner)
  public void openScanner() {
    Dexter.withActivity(this)
        .withPermissions(Manifest.permission.CAMERA)
        .withListener(new MultiplePermissionsListener() {
          @Override
          public void onPermissionsChecked(MultiplePermissionsReport report) {
            // check if all permissions are granted
            if (report.areAllPermissionsGranted()) {
              Intent intent = new Intent(MyQRCodeActivity.this, QRCodeActivity.class);
              startActivity(intent);
            } else if (!report.isAnyPermissionPermanentlyDenied()) {
              Toast.makeText(MyQRCodeActivity.this, "Need the Camera Permission !",
                  Toast.LENGTH_SHORT).show();
            }

            // check for permanent denial of any permission
            if (report.isAnyPermissionPermanentlyDenied()) {
              // permission is denied permenantly, navigate user to app settings
              Toast.makeText(MyQRCodeActivity.this,
                  "Please give the Camera Permission from settings", Toast.LENGTH_LONG).show();
            }
          }

          @Override
          public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
              PermissionToken token) {
            token.continuePermissionRequest();
          }
        })
        .onSameThread()
        .check();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (unbinder != null) unbinder.unbind();
  }

  @Override
  public void showMessage(String msg, int msgId) {

  }

  @Override
  public void sessionExpired() {

  }

  @Override
  public void isInternetAvailable(boolean flag) {

  }

  @Override
  public void userBlocked() {

  }

  @Override
  public void reload() {

  }
}
