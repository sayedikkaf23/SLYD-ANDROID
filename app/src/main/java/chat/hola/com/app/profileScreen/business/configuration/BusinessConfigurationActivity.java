package chat.hola.com.app.profileScreen.business.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.star_configuration.EmailVerifyActivity;
import chat.hola.com.app.star_configuration.NumberVerifyActivity;
import dagger.android.support.DaggerAppCompatActivity;


/**
 * <h1>BusinessConfigurationActivity</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 03 September 2019
 */
public class BusinessConfigurationActivity extends DaggerAppCompatActivity implements BusinessConfigurationContract.View {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;

    @BindView(R.id.scAllowCall)
    SwitchCompat scAllowCall;
    @BindView(R.id.scAllowEmail)
    SwitchCompat scAllowEmail;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    BusinessConfigurationContract.Presenter presenter;

    public static Data.BusinessProfile businessProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_configuration);
        ButterKnife.bind(this);

        businessProfile = (Data.BusinessProfile) getIntent().getSerializableExtra("data");

        title.setTypeface(typefaceManager.getSemiboldFont());
        tvEmail.setTypeface(typefaceManager.getMediumFont());
        tvPhoneNumber.setTypeface(typefaceManager.getMediumFont());

    }

    @OnClick(R.id.tvEmail)
    public void email() {
        Intent intent = new Intent(this, EmailVerifyActivity.class);
        intent.putExtra("business", businessProfile);
        intent.putExtra("call", Constants.BUSINESS);
        startActivity(intent);
    }

    @OnClick(R.id.tvPhoneNumber)
    public void phone() {
        Intent intent = new Intent(this, NumberVerifyActivity.class);
        intent.putExtra("business", businessProfile);
        intent.putExtra("call", Constants.BUSINESS);
        startActivity(intent);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
