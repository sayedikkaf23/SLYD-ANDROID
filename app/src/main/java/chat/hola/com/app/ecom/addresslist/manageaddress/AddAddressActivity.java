package chat.hola.com.app.ecom.addresslist.manageaddress;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static chat.hola.com.app.Utilities.Constants.ACTION;
import static chat.hola.com.app.Utilities.Constants.ADDRESS;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_HOME_TYPE;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_OTHER_TYPE;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_REQUEST;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_WORK_TYPE;
import static chat.hola.com.app.Utilities.Constants.EDIT_TYPE;
import static chat.hola.com.app.Utilities.Constants.FROM;
import static chat.hola.com.app.Utilities.Constants.INDIA_PHONE_CODE;
import static chat.hola.com.app.Utilities.Constants.LAT;
import static chat.hola.com.app.Utilities.Constants.LONG;
import static chat.hola.com.app.Utilities.Constants.REQUEST_FOR_LOCATION;
import static chat.hola.com.app.Utilities.Constants.RIDE_ADDRESS_SELECTION;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Constants.ZOOM_RANGE;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.Utilities.LocationManagerUtil;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ui.WorkaroundMapFragment;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivitySaveAddressBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.inject.Inject;

/*
 * Purpose – This class Holds Ui for Add and edit Address .
 * @author 3Embed
 * Created on Dec 10, 2019
 * Modified on
 */
public class AddAddressActivity extends DaggerAppCompatActivity implements OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private ActivitySaveAddressBinding mBinding;
  private GoogleMap mGoogleMap;
  private AddAddressViewModel mViewModel;
  private boolean mIsValidPhoneNum;
  private double mLat, mLong;
  private String mAddress = "";
  private String mCity = "";
  private String mCountry = "";
  private String mLocality = "";
  private String mAddressline1 = "";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(
        this,
        R.layout.activity_save_address);
    initialize();
    subscribeToFormData();
    LocationManagerUtil.setMapHeight(this);
  }

  /**
   * subscribe to form data
   */
  private void subscribeToFormData() {
    mViewModel.getLiveFormData().observe(this, addAddressFields -> {
      String errorMessage = EMPTY_STRING;
      switch (addAddressFields) {
        case INVALID_NAME:
          errorMessage = getResources().getString(R.string.invalidName);
          mBinding.etSaveAddressName.setError(errorMessage);
          break;

        case INVALID_PHONE:
          errorMessage = getResources().getString(R.string.invalidPhone);
          if (mBinding.ccpGetNumber.getSelectedCountryCodeWithPlus().equals(INDIA_PHONE_CODE)) {
            errorMessage = getResources().getString(R.string.invalidIndianPhone);
          }
          mBinding.etSaveAddressMob.setError(errorMessage);
          break;

        case INVALID_ADDRESS:
          errorMessage = getResources().getString(R.string.invalidAddressLineOne);
          mBinding.etSaveAddressLine1.setError(errorMessage);
          break;

        case INVALID_AREA:
          errorMessage = getResources().getString(R.string.invalidArea);
          break;

        case INVALID_CITY:
          errorMessage = getResources().getString(R.string.invalidCity);
          break;

        case INVALID_COUNTRY:
          errorMessage = getResources().getString(R.string.invalidCountry);
          break;

        case INVALID_POSTAL_CODE:
          errorMessage = getResources().getString(R.string.invalidPostalCode);
          break;
      }
      if (!errorMessage.isEmpty()) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * this method is using to do all the resource initialization
   */
  private void initialize() {
    mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(AddAddressViewModel.class);
    mViewModel.setAddressType(getString(R.string.addressHomeType), ADDRESS_HOME_TYPE);

    mBinding.setViewModel(mViewModel);
    mBinding.rbAddressHome.setChecked(true);
    mBinding.rgAddressType.setOnCheckedChangeListener((radioGroup, i) -> {
      mBinding.tilSaveAddressOther.setVisibility(GONE);
      switch (i) {
        case R.id.rbAddressHome:
          mViewModel.setAddressType(getString(R.string.addressHomeType), ADDRESS_HOME_TYPE);
          break;
        case R.id.rbAddressWork:
          mViewModel.setAddressType(getString(R.string.addressWorkType), ADDRESS_WORK_TYPE);
          break;
        case R.id.rbAddressOther:
          mBinding.tilSaveAddressOther.setVisibility(VISIBLE);
          mBinding.tilSaveAddressOther.requestFocus();
          mViewModel.setAddressType(mBinding.etSaveAddressOther.getText().toString(),
              ADDRESS_OTHER_TYPE);
          break;
      }
    });
    if (getIntent() != null && getIntent().getIntExtra(ACTION, ZERO) == EDIT_TYPE) {
      AddressListItemData addressListItemData = getIntent().getParcelableExtra(ADDRESS);
      switch (addressListItemData.getTagged()) {
        case ADDRESS_HOME_TYPE:
          mBinding.rbAddressHome.setChecked(true);
          break;
        case ADDRESS_WORK_TYPE:
          mBinding.rbAddressWork.setChecked(true);
          break;
        case ADDRESS_OTHER_TYPE:
          mBinding.rbAddressOther.setChecked(true);
          mBinding.etSaveAddressOther.setVisibility(VISIBLE);
          mBinding.etSaveAddressOther.setText(addressListItemData.getTaggedAs());
          break;
      }
      mViewModel.setAddressDataForEdit(addressListItemData, getIntent().getIntExtra(ACTION, ZERO));
      mBinding.tvTitle.setText(getString(R.string.editAddress));
      setAddressForEdit(getIntent().getParcelableExtra(ADDRESS));
    } else {
      getFuseLocation();
      mBinding.tvTitle.setText(getString(R.string.addNewAddress));
    }
    perMissionForLocation();
    mViewModel.setInitialAddressFieldValues(
        Objects.requireNonNull(mBinding.etSaveAddressLine1.getText()).toString(),
        Objects.requireNonNull(mBinding.etSaveAddressArea.getText()).toString(),
        Objects.requireNonNull(mBinding.etSaveAddressCity.getText()).toString(),
        Objects.requireNonNull(mBinding.etSaveAddressCountry.getText()).toString(),
        Objects.requireNonNull(mBinding.etSaveAddressPostalCode.getText()).toString());
    SupportMapFragment mapFragment;
    setActivityVisibility(false);
    mapFragment = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map));
    assert mapFragment != null;
    mapFragment.getMapAsync(this);
    ((WorkaroundMapFragment) mapFragment).setListener(
        () -> mBinding.scrollView.requestDisallowInterceptTouchEvent(true));
    mBinding.btnAddAddressSave.setOnClickListener(view -> mViewModel.saveBtnClickAction());
    mBinding.ivMapCloseDine.setOnClickListener(view -> onBackPressed());
    mBinding.ccpGetNumber.registerCarrierNumberEditText(mBinding.etSaveAddressMob);
    mBinding.ccpGetNumber.setPhoneNumberValidityChangeListener(isValidNumber -> {
      mIsValidPhoneNum = isValidNumber;
      mViewModel.setPhoneNum(mBinding.ccpGetNumber.getSelectedCountryCode(), isValidNumber);
      mBinding.etSaveAddressMob.setFilters(
          mIsValidPhoneNum && !Objects.requireNonNull(
              mBinding.etSaveAddressMob.getText()).toString().isEmpty()
              ?
              new InputFilter[]{new InputFilter.LengthFilter(
                  mBinding.etSaveAddressMob.getText().toString().length())}
              : new InputFilter[]{});
    });

    mViewModel.getUiActionLiveData().observe(this, new Observer<AddAddressUiAction>() {
      @Override
      public void onChanged(AddAddressUiAction addAddressUiAction) {
        switch (addAddressUiAction) {
          case FINISH:
            AddAddressActivity.this.setResult(RESULT_OK);
            AddAddressActivity.this.finish();
            break;


         /* case RIDE_ADDRESS_DONE:
            mViewModel.setLatLong(String.valueOf(mLat), String.valueOf(mLong), mAddress);
            mViewModel.setInitialAddressFieldValues(mAddress, mAddressline1, mCity, mCountry, "");
            mViewModel.isApiCall =
                (getIntent().hasExtra(IS_NEW_ADDRESS) && getIntent().getBooleanExtra(IS_NEW_ADDRESS,
                    false));
            mViewModel.addAddress();
            if (getIntent().getStringExtra(FROM) != null && getIntent().getStringExtra(FROM).equals(
                DINE_IN)) {
              Log.d("ADDRES34", mLat + ":" + mLong + ":" + mAddress);
              Intent intent = getIntent();
              intent.putExtra(RIDE_DROP_LAT, mLat);
              intent.putExtra(RIDE_DROP_LNG, mLong);
              intent.putExtra(RIDE_DROP_ADDRESS, mLocality);
              setResult(RESULT_OK, intent);
              finish();
            } else {
              Log.d("RIDEADRESS", "onChanged: " + mLat + ":" + mLong + ":" + mAddress + "\n"
                  + userInfoHandler.getRideDropLat() + ":" + userInfoHandler.getRideDropLng() + ":"
                  + userInfoHandler.getRideDropAddress());
              Intent intent = getIntent();
              intent.putExtra(RIDE_DROP_LAT, userInfoHandler.getRideDropLat());
              intent.putExtra(RIDE_DROP_LNG, userInfoHandler.getRideDropLng());
              intent.putExtra(RIDE_DROP_ADDRESS, userInfoHandler.getRideDropAddress());
              intent.putExtra(FROM, ADD_ADDRESS);
              setResult(RESULT_OK, intent);
              finish();
            }
            break;*/

          case RIDE_CURRENT_LOC_ICON:
            setLocationInMap(mLat, mLong);
            break;
        }
      }
    });

   /* mBinding.incRideDropHeader.ivRideAddressClose.setOnClickListener(v -> {
      onBackPressed();
      AddAddressActivity.this.finish();
    });*/
  }

  private boolean isAddressValid() {
    String missingField = EMPTY_STRING;
    return missingField.isEmpty();
  }


  /**
   * This method is using to manage UI based on from which activity it comes from
   */
  private void setActivityVisibility(boolean visibility) {
    mBinding.getRoot().setVisibility(visibility ? GONE : VISIBLE);
    mBinding.scrollView.setVisibility(visibility ? GONE : VISIBLE);
    //mBinding.incRideDropHeader.getRoot().setVisibility(visibility ? VISIBLE : GONE);
    mBinding.btnSaveAddressRide.setVisibility(visibility ? VISIBLE : GONE);
    mBinding.btnAddAddressSave.setVisibility(visibility ? GONE : VISIBLE);
    mBinding.mapRideAddress.setVisibility(visibility ? VISIBLE : GONE);
    mBinding.flMap.setVisibility(visibility ? VISIBLE : GONE);
  }

  /**
   * This method is using to get current location of the device
   */
  private void getFuseLocation() {
    FusedLocationProviderClient fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(this);
    fusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, location -> {
          if (location != null) {
            mLat = location.getLatitude();
            mLong = location.getLongitude();
            if (mGoogleMap != null) {
              setLocationInMap(mLat, mLong);
            }
          }
        });
  }

  /**
   * This method is using to set the Ui Values in Edit Address flow
   *
   * @param addressData mData to set UI
   */
  private void setAddressForEdit(AddressListItemData addressData) {
    mBinding.etSaveAddressMob.setText(addressData.getMobileNumber());
    mBinding.etSaveAddressName.setText(addressData.getName());
    mBinding.etSaveAddressLine1.setText(addressData.getAddLine1());
    mBinding.etSaveAddressLine2.setText(addressData.getAddLine2());
    mBinding.etSaveAddressLandMark.setText(addressData.getLandmark());
    mBinding.etSaveAddressCity.setText(addressData.getCity());
    mBinding.etSaveAddressCountry.setText(addressData.getCountry());
    mBinding.etSaveAddressArea.setText(addressData.getLocality());
    mBinding.etSaveAddressPostalCode.setText(addressData.getPincode());
    if (addressData.getLatitude() != null && !addressData.getLatitude().isEmpty()) {
      mLat = Double.parseDouble(addressData.getLatitude());
    }
    if (addressData.getLongitude() != null && !addressData.getLongitude().isEmpty()) {
      mLong = Double.parseDouble(addressData.getLongitude());
    }
    if (TextUtils.isDigitsOnly(addressData.getMobileNumberCode())) {
      mBinding.ccpGetNumber.setCountryForPhoneCode(
          Integer.parseInt(addressData.getMobileNumberCode()));
    }
  }

  @Override
  public void onCameraIdle() {
    LatLng center = mGoogleMap.getCameraPosition().target;
    Utilities.printLog("Map LatLong Lat-" + center.latitude + "Long-" + center.longitude);
    if (mBinding.tvTitle.getText().toString().equals(
        getResources().getString(R.string.addNewAddress))) {
      getAddressAndSetFromLatLng(center.latitude, center.longitude);
    }
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    this.mGoogleMap = googleMap;
    this.mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
    if (getIntent() != null && getIntent().hasExtra(FROM) && getIntent().getStringExtra(
        FROM).equals(RIDE_ADDRESS_SELECTION)) {
      this.mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

    }
    mBinding.ivRideCurrentLocation.setVisibility(
        getIntent() != null && getIntent().hasExtra(FROM) && getIntent().getStringExtra(
            FROM).equals(RIDE_ADDRESS_SELECTION) ? VISIBLE : GONE);
    this.mGoogleMap.setOnCameraIdleListener(this);
    setLocationInMap(mLat, mLong);
  }

  /**
   * This method is using to set lat long in map
   *
   * @param lat latitude to set
   * @param lng longitude to set
   */
  private void setLocationInMap(double lat, double lng) {
    LatLng latLng = new LatLng(lat, lng);
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_RANGE));
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    mGoogleMap.setMyLocationEnabled(true);
  }

  /**
   * This method is using to get the Address using Latitude and Longitude of the selected location
   * from the map
   */
  private void getAddressAndSetFromLatLng(double latitude, double longitude) {
    Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
    try {

      List<Address> addresses = geocoder.getFromLocation(latitude, longitude, ONE);
      if (addresses.size() > ZERO) {
        Address fetchedAddress = addresses.get(ZERO);
        Utilities.printLog("AddressList-" + addresses.toString());
        mBinding.etSaveAddressLine1.setText(fetchedAddress.getThoroughfare());
        mBinding.etSaveAddressCity.setText(fetchedAddress.getLocality());
        mBinding.etSaveAddressCountry.setText(fetchedAddress.getCountryName());
        mBinding.etSaveAddressPostalCode.setText(fetchedAddress.getPostalCode());
        mBinding.etRideAddressPickupDine.setText(fetchedAddress.getAddressLine(0));
        //mBinding.incRideDropHeader.tvRideAddressDrop.setText(fetchedAddress.getAddressLine(0));
        mAddress = fetchedAddress.getAddressLine(0);
        mCity = fetchedAddress.getLocality();
        mCountry = fetchedAddress.getCountryName();
        mAddressline1 = fetchedAddress.getThoroughfare();
        mLocality =
            String.format("%s, %s", fetchedAddress.getSubLocality(), fetchedAddress.getLocality());
        mViewModel.setLatLong(String.valueOf(fetchedAddress.getLatitude()),
            String.valueOf(fetchedAddress.getLongitude()), fetchedAddress.getAdminArea());
      /*  if (getIntent() != null && getIntent().hasExtra(FROM) && getIntent().getStringExtra(
            FROM).equals(RIDE_ADDRESS_SELECTION)) {
          userInfoHandler.setRideDropLat(String.valueOf(latitude));
          userInfoHandler.setRideDropLng(String.valueOf(longitude));
          userInfoHandler.setRideDropAddress(mAddress);
        }*/
      }
    } catch (IOException e) {
      e.printStackTrace();
      Utilities.printLog("AddressList- Could not get address..!" + e.getMessage());
    }
  }

  /**
   * To check for run time permission for location if post Marshmallow
   * else directly call location API
   */
  private void perMissionForLocation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !(checkSelfPermission(
        ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
      requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_FOR_LOCATION);
    } else {
      if (mGoogleMap != null) {
        mGoogleMap.setMyLocationEnabled(true);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (grantResults[ZERO] == PackageManager.PERMISSION_GRANTED) {
      getFuseLocation();
    } else {
      /*Intent intent = new Intent(this, AddressActivity.class);
      intent.putExtra(COMING_FROM, MAP_PAGE);
      startActivityForResult(intent, ADDRESS_REQUEST);*/
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ADDRESS_REQUEST && data != null) {
      double lat = data.getDoubleExtra(LAT, ZERO);
      double lng = data.getDoubleExtra(LONG, ZERO);
      setLocationInMap(lat, lng);
    }
  }

}