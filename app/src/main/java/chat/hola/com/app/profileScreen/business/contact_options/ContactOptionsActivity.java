package chat.hola.com.app.profileScreen.business.contact_options;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ezcall.android.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.profileScreen.business.address.BusinessAddressActivity;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.star_configuration.EmailVerifyActivity;
import chat.hola.com.app.star_configuration.NumberVerifyActivity;

public class ContactOptionsActivity extends AppCompatActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etAddress)
    EditText etAddress;

    private Unbinder unbinder;

    private Data.BusinessProfile businessProfile;

    private static final int FETCH_ADDRESS = 135;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_options);
        unbinder = ButterKnife.bind(this);
        getIntentData();
        initView();
    }

    private void initView() {
        etEmail.setText(businessProfile.getEmail().getId());
        etPhone.setText(businessProfile.getPhone().getCountryCode()
                + businessProfile.getPhone().getNumber());
        etAddress.setText(businessProfile.getAddress());
    }

    private void getIntentData() {
        businessProfile = (Data.BusinessProfile) getIntent().getSerializableExtra("businessProfile");
    }

    @OnClick({R.id.etEmail})
    public void email() {
        Intent intent = new Intent(this, EmailVerifyActivity.class);
        intent.putExtra("business", businessProfile);
        intent.putExtra("call", Constants.BUSINESS);
        startActivityForResult(intent, 551);
    }

    @OnClick({R.id.etPhone})
    public void phone() {
        Intent intent = new Intent(this, NumberVerifyActivity.class);
        intent.putExtra("business", businessProfile);
        intent.putExtra("call", Constants.BUSINESS);
        startActivityForResult(intent, 552);
    }

    @OnClick({R.id.etAddress})
    public void address() {
        Intent intent = new Intent(this, BusinessAddressActivity.class);
//        intent.putExtra("city", city);
//        intent.putExtra("street", street);
//        intent.putExtra("zipcode", zipcode);
        startActivityForResult(intent, FETCH_ADDRESS);
    }

    @OnClick(R.id.ivBack)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.tV_done)
    public void done() {
        setResult(1001, new Intent().putExtra("businessProfile", businessProfile));
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 551) {
            /*Email verification*/
            assert data != null;
            etEmail.setText(data.getStringExtra("email"));
            businessProfile.getEmail().setId(data.getStringExtra("email"));
            businessProfile.getEmail().setVisible(data.getIntExtra("isVisible", businessProfile.getEmail().getVisible()));
        } else if (resultCode == 552) {
            /*phone verification*/
            assert data != null;
            String phone = data.getStringExtra("countryCode") + data.getStringExtra("phoneNumber");
            etPhone.setText(phone);
            businessProfile.getPhone().setNumber(data.getStringExtra("phoneNumber"));
            businessProfile.getPhone().setCountryCode(data.getStringExtra("countryCode"));
        } else if (resultCode == RESULT_OK && requestCode == FETCH_ADDRESS) {

            if (data != null) {
                String city = data.getStringExtra("city");
                String street = data.getStringExtra("street");
                String zipcode = data.getStringExtra("zipcode");
                String lat = data.getStringExtra("lat");
                String lng = data.getStringExtra("lng");
                String address = data.getStringExtra("address");
                etAddress.setText(address);
                businessProfile.setAddress(data.getStringExtra("address"));
                businessProfile.setBusinessStreet(street);
                businessProfile.setBusinessCity(city);
                businessProfile.setBusinessZipCode(zipcode);
                businessProfile.setBusinessLat(lat);
                businessProfile.setBusinessLng(lng);
            }
        }
    }

}
