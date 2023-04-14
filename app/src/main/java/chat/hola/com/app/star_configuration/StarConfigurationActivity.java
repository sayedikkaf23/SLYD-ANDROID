package chat.hola.com.app.star_configuration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.profileScreen.model.Data;
import dagger.android.support.DaggerAppCompatActivity;

public class StarConfigurationActivity extends DaggerAppCompatActivity implements StarConfigContract.View {

    @BindView(R.id.rL_email)
    RelativeLayout rL_email;
    @BindView(R.id.rL_phoneNumber)
    RelativeLayout rL_phoneNumber;
    Unbinder unbinder;
    private Data profileData;
    @BindView(R.id.chatSwitch)
    SwitchCompat chatSwitch;

    @Inject
    StarConfigPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_configuration);
        unbinder = ButterKnife.bind(this);
        profileData = (Data) getIntent().getSerializableExtra("profileData");

        if (profileData != null && profileData.getStarRequest() != null)
            chatSwitch.setChecked(profileData.getStarRequest().isChatVisible());

        chatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.makeChatVisibility(isChecked);
            }
        });
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.rL_email)
    public void verifyEmail() {
        Intent intent = new Intent(StarConfigurationActivity.this, EmailVerifyActivity.class);
        intent.putExtra("call", Constants.STAR);
        intent.putExtra("profileData", profileData);
        startActivityForResult(intent, 551);
    }

    @OnClick(R.id.rL_phoneNumber)
    public void verifyPhoneNumber() {
        Intent intent = new Intent(StarConfigurationActivity.this, NumberVerifyActivity.class);
        intent.putExtra("call", Constants.STAR);
        intent.putExtra("profileData", profileData);
        startActivityForResult(intent, 552);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 551:
                if (data!=null && data.getBooleanExtra("emailVerified", false)) {
                    String email = data.getStringExtra("email");
                    boolean isVisible = data.getBooleanExtra("isVisible",false);
                    profileData.getStarRequest().setStarUserEmail(email);
                    profileData.getStarRequest().setEmailVisible(isVisible);
                }
                break;
            case 552:
                if (data != null && data.getBooleanExtra("numberVerified", true)) {
                    String phoneNumber = data.getStringExtra("phoneNumber");
                    String countryCode = data.getStringExtra("countryCode");
                    boolean isVisible = data.getBooleanExtra("isVisible", false);
                    profileData.getStarRequest().setStarUserPhoneNumber(phoneNumber);
                    profileData.getStarRequest().setPhoneNumber(phoneNumber.replace(countryCode,""));
                    profileData.getStarRequest().setNumberVisible(isVisible);
                }
                break;
        }
    }
}
