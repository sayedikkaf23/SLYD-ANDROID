package chat.hola.com.app.acceptRequest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.squareup.otto.Bus;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;
import org.json.JSONObject;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class AcceptRequestActivity extends DaggerAppCompatActivity
    implements AcceptRequestContract.View {
  private Unbinder unbinder;
  private Bus bus = AppController.getBus();

  @Inject
  SessionManager sessionManager;
  @Inject
  TypefaceManager typefaceManager;
  @Inject
  AcceptRequestPresenter mPresenter;

  @BindView(R.id.ivBack)
  public ImageView ivBack;
  @BindView(R.id.tvTitle)
  public TextView tvTitle;
  @BindView(R.id.ivProfilePic)
  public ImageView ivProfilePic;
  @BindView(R.id.tvFullName)
  public TextView tvFullName;
  @BindView(R.id.tvUserName)
  public TextView tvUserName;
  @BindView(R.id.etMessage)
  public EditText etMessage;
  @BindView(R.id.tvFrom)
  public TextView tvFrom;
  @BindView(R.id.tvMobileNumber)
  public TextView tvMobileNumber;
  @BindView(R.id.btnSend)
  public Button btnSend;
  @BindView(R.id.tvHideMyPost)
  public TextView tvHideMyPost;
  @BindView(R.id.tvViewProfile)
  public TextView tvViewProfile;
  @BindView(R.id.block)
  public TextView block;
  @BindView(R.id.accept)
  public TextView accept;
  @BindView(R.id.reject)
  public TextView reject;
  @BindView(R.id.swHideMyPost)
  public Switch swHideMyPost;
  @BindView(R.id.llOptions)
  public LinearLayout llOptions;
  @BindView(R.id.tvMessageTitle)
  public TextView tvMessageTitle;
  @BindView(R.id.tvMessage)
  public TextView tvMessage;
  @BindView(R.id.llMessage)
  public LinearLayout llMessage;

  private String userId, msg, firstName, lastName, userName;

  @Override
  public void userBlocked() {
  }

  @Inject
  public AcceptRequestActivity() {
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_accept_reuest);
    bus.register(this);
    unbinder = ButterKnife.bind(this);
    mPresenter.attachView(this);

    btnSend.setTypeface(typefaceManager.getSemiboldFont());
    tvHideMyPost.setTypeface(typefaceManager.getSemiboldFont());
    tvMessageTitle.setTypeface(typefaceManager.getSemiboldFont());
    tvViewProfile.setTypeface(typefaceManager.getSemiboldFont());
    accept.setTypeface(typefaceManager.getSemiboldFont());
    reject.setTypeface(typefaceManager.getSemiboldFont());
    block.setTypeface(typefaceManager.getSemiboldFont());
    tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    tvFullName.setTypeface(typefaceManager.getRegularFont());
    tvUserName.setTypeface(typefaceManager.getSemiboldFont());
    etMessage.setTypeface(typefaceManager.getRegularFont());
    tvFrom.setTypeface(typefaceManager.getRegularFont());
    tvMobileNumber.setTypeface(typefaceManager.getRegularFont());
    tvMessage.setTypeface(typefaceManager.getRegularFont());

    String call = getIntent().getStringExtra("call");
    userId = getIntent().getStringExtra("userId");
    firstName = getIntent().getStringExtra("firstName");
    lastName = getIntent().getStringExtra("lastName");
    userName = getIntent().getStringExtra("userName");
    msg = getIntent().getStringExtra("message");
    if (call != null && !call.isEmpty()) updateUi(call);

    Glide.with(this)
        .load(getIntent().getStringExtra("profilePic"))
        .asBitmap()
        .centerCrop()
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        .into(new BitmapImageViewTarget(ivProfilePic) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            ivProfilePic.setImageDrawable(circularBitmapDrawable);
          }
        });

    try {
      tvFullName.setVisibility(View.VISIBLE);
      tvFullName.setText(firstName + getString(R.string.double_inverted_comma) + lastName);

      tvUserName.setText(userName.substring(0, 1).toUpperCase() + userName.substring(1));
      tvMobileNumber.setText(sessionManager.getUserName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    swHideMyPost.setOnCheckedChangeListener(
        (buttonView, isChecked) -> mPresenter.hideMyPost(isChecked, userId));
  }

  @OnClick(R.id.tvViewProfile)
  public void gotoProfile() {
    startActivity(new Intent(this, ProfileActivity.class).putExtra("userId", userId));
  }

  private void updateUi(String call) {
    switch (call) {
      case "send":
        btnSend.setVisibility(View.VISIBLE);
        llOptions.setVisibility(View.GONE);
        llMessage.setVisibility(View.GONE);
        etMessage.setVisibility(View.VISIBLE);
        String message = getString(R.string.message_hi) + userName + getString(R.string.comma) +
                getString(R.string.message_I_am) +
                AppController.getInstance().getUserName();
        etMessage.setText(message);
        break;
      case "receive":
        btnSend.setVisibility(View.GONE);
        llOptions.setVisibility(View.VISIBLE);
        etMessage.setVisibility(View.GONE);
        llMessage.setVisibility(msg != null && !msg.isEmpty() ? View.VISIBLE : View.GONE);
        if (msg != null && !msg.isEmpty()) tvMessage.setText(msg);
        break;
    }
  }

  @OnClick(R.id.accept)
  public void accept() {
    enable(false);
    mPresenter.accept(userId);
  }

  @OnClick(R.id.reject)
  public void reject() {
    enable(false);
    mPresenter.reject(userId);
  }

  @OnClick(R.id.block)
  public void block() {
    enable(false);
    mPresenter.block(userId);
  }

  @OnClick(R.id.ivBack)
  public void back() {
    super.onBackPressed();
  }

  @OnClick(R.id.btnSend)
  public void send() {
    enable(false);
    mPresenter.send(userId, etMessage.getText().toString());
  }

  @Override
  public void showMessage(String msg, int msgId) {
    Toast.makeText(this, msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId),
        Toast.LENGTH_SHORT).show();
  }

  @Override
  public void sessionExpired() {
    sessionManager.sessionExpired(this);
  }

  @Override
  public void isInternetAvailable(boolean flag) {
  }

  @Override
  public void onDestroy() {
    mPresenter.detachView();
    if (unbinder != null) unbinder.unbind();
    super.onDestroy();
  }

  @Override
  public void reload() {
  }

  @Override
  public void finishActivity() {
    try {
      JSONObject obj = new JSONObject();
      obj.put("eventName", "reloadFriends");
      bus.post(obj);
    } catch (Exception e) {

    }
    finish();
  }

  @Override
  public void enable(boolean enable) {
    btnSend.setEnabled(enable);
    accept.setEnabled(enable);
    accept.setClickable(enable);
    reject.setEnabled(enable);
    reject.setClickable(enable);
    block.setEnabled(enable);
    block.setClickable(enable);
  }
}