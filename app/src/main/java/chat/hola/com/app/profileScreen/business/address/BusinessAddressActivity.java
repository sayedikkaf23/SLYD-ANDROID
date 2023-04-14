package chat.hola.com.app.profileScreen.business.address;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>BusinessAddressActivity</h1>
 * <p>It registers the business address<p/>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 27 August 2019
 */
public class BusinessAddressActivity extends DaggerAppCompatActivity implements BusinessAddressContract.View {


    private static final int AUTOCOMPLETE_REQUEST_CODE = 13432;
    private Geocoder mGeocoder;

    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etStreet)
    EditText etStreet;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etZipCode)
    EditText etZipCode;
    @BindView(R.id.message)
    TextView message;
    private String fullAddress;
    String city;
    String street;
    String zipcode;
    String lat;
    String lng;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_address_activity);
        ButterKnife.bind(this);
        mGeocoder = new Geocoder(this, Locale.getDefault());

        // Initialize Places.
        Places.initialize(getApplicationContext(), BuildConfig.MAP_BUNDLE_KEY);

        title.setTypeface(typefaceManager.getSemiboldFont());
        message.setTypeface(typefaceManager.getRegularFont());
        etCity.setTypeface(typefaceManager.getRegularFont());
        etStreet.setTypeface(typefaceManager.getRegularFont());
        etZipCode.setTypeface(typefaceManager.getRegularFont());

        city = getIntent().getStringExtra("city");
        street = getIntent().getStringExtra("street");
        zipcode = getIntent().getStringExtra("zipcode");

        if (city != null)
            etCity.setText(city);
        if (street != null)
            etStreet.setText(street);
        if (zipcode != null)
            etZipCode.setText(zipcode);

        etStreet.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etStreet.getRight() - etStreet.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                    return true;
                }
            }
            return false;
        });
    }


    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnDone)
    public void done() {
        city = etCity.getText().toString();
        street = etStreet.getText().toString();
        zipcode = etZipCode.getText().toString();
        if (fullAddress == null || fullAddress.isEmpty())
            fullAddress = street + "," + city + "-" + zipcode;
        if (city.isEmpty() || street.isEmpty() || zipcode.isEmpty()) {
            Toast.makeText(this, "Please add full address", Toast.LENGTH_SHORT).show();
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("address", fullAddress);
            returnIntent.putExtra("city", city);
            returnIntent.putExtra("street", street);
            returnIntent.putExtra("zipcode", zipcode);
            returnIntent.putExtra("lat", lat);
            returnIntent.putExtra("lng", lng);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AutocompleteActivity.RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (place.getLatLng() != null) {
                getData(place.getLatLng().latitude, place.getLatLng().longitude);
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getData(double lat, double lng) {
        try {
            List<Address> addresses = mGeocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                String full_addr = addresses.get(0).toString();

                street = addresses.get(0).getAddressLine(0);

                city = addresses.get(0).getLocality();
                zipcode = addresses.get(0).getPostalCode();

                int start = street.indexOf(city) - 2;
                int end = street.length();

                StringBuffer buf = new StringBuffer(street);
                street = String.valueOf(buf.replace(start, end, "")).trim();

                etStreet.setText(street);
                etCity.setText(city);
                etZipCode.setText(zipcode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fullAddress = getFullAddress(lat, lng);
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


