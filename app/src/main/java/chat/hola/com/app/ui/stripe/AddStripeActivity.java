package chat.hola.com.app.ui.stripe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Activities.ChatCameraActivity;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Country;

/**
 * <h1>KycActivity</h1>
 * <p>All KYC functionalities</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 23 Jan 2020
 */
public class AddStripeActivity extends BaseActivity implements AddStripeContract.View, Constants, Constants.Image {

    @Inject
    TypefaceManager font;
    @Inject
    Loader loader;
    @Inject
    AddStripePresenter presenter;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivDoc)
    ImageView ivDoc;
    @BindView(R.id.ibRemoveDoc)
    ImageView ibRemoveDoc;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etEmailId)
    EditText etEmailId;
    @BindView(R.id.etIdNumber)
    EditText etIdNumber;
    @BindView(R.id.etSsn)
    EditText etSsn;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.etZipCode)
    EditText etZipCode;
    @BindView(R.id.etCountry)
    EditText etCountry;
    @BindView(R.id.etDob)
    EditText etDob;
    @BindView(R.id.rgGender)
    RadioGroup rgGender;
    @BindView(R.id.spCountry)
    Spinner spCountry;

    private Map<String, Object> params;
    private List<String> countries = new ArrayList<>();
    private String first_name, last_name,email_id, gender, id_number, phone, ssn_last_4, ip, business_type, line1, city, state, country, postal_code, url, /*email,*/ document = "";
    private int mDay, mMonth, mYear;
    private ImageSourcePicker imageSourcePicker;
    private String lat, lng;
    private Geocoder mGeocoder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_stripe;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(font.getSemiboldFont());
    }

    @Override
    public void initView() {
        super.initView();

        // Initialize Places.
        Places.initialize(getApplicationContext(), BuildConfig.MAP_BUNDLE_KEY);
        presenter.attach(this);
        presenter.getCountry();
        params = (Map<String, Object>) getIntent().getSerializableExtra("params");
        tvTitle.setText(R.string.add_stripe_account);
        mGeocoder = new Geocoder(this, Locale.getDefault());
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        etDob.clearFocus();

        etDob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddStripeActivity.this, new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String inputDateStr = dayOfMonth + "-" + (month + 1) + "-" + year;
                    Date date = null;
                    try {
                        date = inputFormat.parse(inputDateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outputDateStr = outputFormat.format(date);
                    etDob.setText(outputDateStr);
                    mDay = dayOfMonth;
                    mMonth = month + 1;
                    mYear = year;
                }
            }, mYear, mMonth, mDay);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.male:
                    gender = "male";
                    break;
                case R.id.female:
                    gender = "female";
                    break;
                case R.id.other:
                    gender = "other";
                    break;
            }
        });

        etAddress.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etAddress.getRight() - etAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                    return true;
                }
            }
            return false;
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (countries.size() >= i)
                    country = countries.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case AUTOCOMPLETE_REQUEST_CODE:
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    if (place.getLatLng() != null) {
                        getData(place.getLatLng().latitude, place.getLatLng().longitude);
                        lat = String.valueOf(place.getLatLng().latitude);
                        lng = String.valueOf(place.getLatLng().longitude);
                    }
                    break;
                case LOAD_IMAGE:
                    presenter.parseSelectedImage(requestCode, resultCode, data);
                    break;

                case CAPTURE_IMAGE:
                    presenter.parseCapturedImage(requestCode, resultCode, data);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    presenter.parseCropedImage(requestCode, resultCode, data);
                    break;
            }
    }

    @OnClick(R.id.ibBack)
    public void backPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    public void verify() {
        if (validate()) {
            params = new HashMap<>();
            params.put("business_type", business_type);
            params.put("line1", line1);
            params.put("city", city);
            params.put("state", state);
            params.put("country", country);
            params.put("postal_code", postal_code);
            params.put("day", mDay);
            params.put("month", mMonth);
            params.put("year", mYear);
            params.put("url", url);
//            params.put("email", email);
            params.put("email", email_id);
            params.put("first_name", first_name);
            params.put("gender", gender);
            params.put("id_number", id_number);
            params.put("phone", phone);
            params.put("ssn_last_4", ssn_last_4);
            params.put("last_name", last_name);
            params.put("ip", ip);
            presenter.addAccount(params);
        }
    }

    @OnClick(R.id.ivDoc)
    public void uploadFrontDoc() {
        cameraAndGalleryPermissions();
    }

    // check if camera and gallery access permission, if not then ask
    private void cameraAndGalleryPermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            imageSourcePicker = new ImageSourcePicker(AddStripeActivity.this, true, true);
                            imageSourcePicker.setOnSelectImage(callbackProfile);
                            imageSourcePicker.show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            message(R.string.permission_requires);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();


    }


    /**
     * Validates all fields
     *
     * @return true: all fields are valid, false: data is not valid
     */
    private boolean validate() {
        business_type = "individual";
        url = "www.vybes.com";
//        email = sessionManager.getEmail();
        phone = sessionManager.getMobileNumber().replace(sessionManager.getCountryCode(), "");
        ip = sessionManager.getIpAdress();

        line1 = etAddress.getText().toString().trim();
        city = etCity.getText().toString().trim();
        state = etState.getText().toString().trim();
        postal_code = etZipCode.getText().toString().trim();
        first_name = etFirstName.getText().toString().trim();
        last_name = etLastName.getText().toString().trim();
        email_id = etEmailId.getText().toString().trim();
        id_number = etIdNumber.getText().toString().trim();
        ssn_last_4 = etSsn.getText().toString().trim().substring(Math.max(etSsn.getText().toString().trim().length() - 4, 0));

        if (line1 == null || line1.isEmpty()) {
            message("Please enter address");
            return false;
        }

        if (city == null || city.isEmpty()) {
            message("Please enter city");
            return false;
        }

        if (state == null || state.isEmpty()) {
            message("Please enter state");
            return false;
        }

        if (country == null || country.isEmpty()) {
            message("Please enter country");
            return false;
        }

        if (postal_code == null || postal_code.isEmpty()) {
            message("Please enter postal code");
            return false;
        }

        if (ssn_last_4.isEmpty()) {
            message("Please enter ssn");
            return false;
        }

        if (id_number == null || id_number.isEmpty()) {
            message("Please enter document id");
            return false;
        }


        if (first_name == null || first_name.isEmpty()) {
            message("Please enter first name");
            return false;
        }


        if (last_name == null || last_name.isEmpty()) {
            message("Please enter last name");
            return false;
        }

        if (email_id == null || email_id.isEmpty()) {
            message("Please enter email id");
            return false;
        }

        return true;
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
        if (loader.isShowing())
            loader.dismiss();
    }


    ImageSourcePicker.OnSelectImage callbackProfile = new ImageSourcePicker.OnSelectImage() {

        @Override
        public void onCamera() {
            Intent intent = new Intent(AddStripeActivity.this, ChatCameraActivity.class);
            intent.putExtra("requestType", "EditProfile");
            startActivityForResult(intent, CAPTURE_IMAGE);
        }

        @Override
        public void onGallary() {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("image/*");
            startActivityForResult(intent, LOAD_IMAGE);
        }

        @Override
        public void onCancel() {
            //nothing to do.
        }
    };

    @Override
    public void success() {
        finish();
    }

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data).setFixAspectRatio(true).start(this);
    }

    @Override
    public void setDocumentImage(Bitmap bitmap, String uri) {
        document = uri;
        ivDoc.setImageBitmap(bitmap);
        ibRemoveDoc.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCountries(List<Country> countries) {
        if (!countries.isEmpty()) {
            country = countries.get(0).getCountry();
            for (Country c : countries)
                this.countries.add(c.getCountry());

            ArrayAdapter<String> docAdapter = new ArrayAdapter<>(this, R.layout.select_dialog_item, this.countries);
            spCountry.setAdapter(docAdapter);
        }
    }


    @OnClick(R.id.ibRemoveDoc)
    public void removeFrontDoc() {
        document = "";
        ivDoc.setImageDrawable(getDrawable(R.drawable.ic_image));
        ibRemoveDoc.setVisibility(View.GONE);
    }

    private void getData(double lat, double lng) {
        try {
            List<Address> addresses = mGeocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                String full_addr = addresses.get(0).toString();

                line1 = addresses.get(0).getAddressLine(0);
                state = addresses.get(0).getAdminArea();
                Log.i("state", addresses.get(0).getAdminArea() + "-" + addresses.get(0).getSubAdminArea());

                city = addresses.get(0).getLocality();
                postal_code = addresses.get(0).getPostalCode();
                country = addresses.get(0).getCountryCode();

                etAddress.setText(line1);
                etCity.setText(city);
                etState.setText(state);
                etZipCode.setText(postal_code);
                etCountry.setText(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        fullAddress = getFullAddress(lat, lng);
    }

    //Get address from the provided latitude and longitude
    public String getFullAddress(double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address obj = addresses.get(0);
                return obj.getAddressLine(0);
            } else
                return "";

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "Dragged location";
        }
    }
}
