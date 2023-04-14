package chat.hola.com.app.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import chat.hola.com.app.moveCameraObj.AppUtils;
import chat.hola.com.app.moveCameraObj.FetchAddressIntentService;
import com.ezcall.android.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * <h>ChangeLocationActivity</h>
 * <p>
 * In this class we used to show google map and move camera oject feature
 * for user to select the required location. When User will move the
 * camera then parallely address will also getting change. User can also
 * search address by typing with help of AutoComplete address.
 * </p>
 *
 * @author 3Embed
 * @version 1.0
 * @since 24-May-2017
 */
public class LocationFetchActivity extends AppCompatActivity
    implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
  private GoogleMap mMap;
  private GoogleApiClient mGoogleApiClient;
  private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
  private static String TAG = LocationFetchActivity.class.getSimpleName();
  private Activity mActivity;
  private LatLng mCenterLatLong;
  private String selectedLat = "0.0", selectedLng = "0.0";
  private View mapView;

  /**
   * Receiver registered with this activity to get the response from FetchAddressIntentService.
   */
  private AddressResultReceiver mResultReceiver;
  /**
   * The formatted location address.
   */
  protected String mAddressOutput;
  protected String mAreaOutput;
  protected String mCityOutput;
  protected String mStateOutput;
  private TextView mLocationText;
  private AppCompatImageView mIvBack;
  private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
  private String[] permissionsArray;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_fetch);
    initVariables();
  }

  /**
   * <h>InitVariables</h>
   * <p>
   * In this method we used to initialize all data member.
   * </p>
   */
  private void initVariables() {
    mActivity = LocationFetchActivity.this;
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapView = mapFragment.getView();

    mLocationText = (TextView) findViewById(R.id.Locality);
    mIvBack = (AppCompatImageView) findViewById(R.id.ivBack_into);
    RelativeLayout rL_searchLoc = (RelativeLayout) findViewById(R.id.rL_searchLoc);
    rL_searchLoc.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openAutocompleteActivity();
      }
    });

    mIvBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    permissionsArray = new String[] { ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION };

        /*mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }


        });*/
    mapFragment.getMapAsync(this);
    mResultReceiver = new AddressResultReceiver(new Handler());

    if (checkPlayServices()) {
      // If this check succeeds, proceed with normal processing.
      // Otherwise, prompt user to get valid Play Services APK.
      if (!AppUtils.isLocationEnabled(mActivity)) {
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setMessage("Location not enabled!");
        dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
          }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            // TODO Auto-generated method stub

          }
        });
        dialog.show();
      }
      buildGoogleApiClient();
    } else {
      Toast.makeText(mActivity, "Location not supported in this device", Toast.LENGTH_SHORT).show();
    }

    // Confirm location
    RelativeLayout rL_confirm_location = (RelativeLayout) findViewById(R.id.rL_confirm_location);
    rL_confirm_location.setOnClickListener(this);

    // back button
//    RelativeLayout rL_back_btn = (RelativeLayout) findViewById(R.id.rL_back_btn);
//    rL_back_btn.setOnClickListener(this);
  }

  private void openAutocompleteActivity() {
    /**
     * Initialize Places. For simplicity, the API key is hard-coded. In a production
     * environment we recommend using a secure mechanism to manage API keys.
     */
    if (!Places.isInitialized()) {
      Places.initialize(getApplicationContext(), getString(R.string.google_api_key_places));
    }

    // Set the fields to specify which types of place data to return.
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

    // Start the autocomplete intent.
    Intent intent =
        new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near own current location.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    Log.d(TAG, "OnMapReady");
    mMap = googleMap;

    // Change MyLocationButton position
    if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
      // Get the button view
      View locationButton =
          ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(
              Integer.parseInt("2"));
      // and next place it, on bottom right (as Google Maps app)
      RelativeLayout.LayoutParams layoutParams =
          (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
      // position on right bottom
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
      layoutParams.setMargins(0, 0, 30, 30);
    }

    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
      @Override
      public void onCameraIdle() {
        mCenterLatLong = mMap.getCameraPosition().target;
        mMap.clear();
        try {
          Location mLocation = new Location("");
          mLocation.setLatitude(mCenterLatLong.latitude);
          mLocation.setLongitude(mCenterLatLong.longitude);

          startIntentService(mLocation);
          selectedLat = String.valueOf(mCenterLatLong.latitude);
          selectedLng = String.valueOf(mCenterLatLong.longitude);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

    }
  }

  @Override
  public void onConnected(Bundle bundle) {
    onConnectWithLoc();
  }

  private void onConnectWithLoc() {

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (mLastLocation != null) {

      changeMap(mLastLocation);
    } else {
      try {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      LocationRequest mLocationRequest = new LocationRequest();
      mLocationRequest.setInterval(10000);
      mLocationRequest.setFastestInterval(5000);
      mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
          this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onConnectionSuspended(int i) {

    mGoogleApiClient.connect();
  }

  @Override
  public void onLocationChanged(Location location) {

    try {
      if (location != null) changeMap(location);
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override
  protected void onStart() {
    super.onStart();
    try {
      mGoogleApiClient.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
  }

  private boolean checkPlayServices() {
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
        GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST)
            .show();
      }
      return false;
    }
    return true;
  }

  private void changeMap(Location location) {
    Log.d(TAG, "Reaching map" + mMap);

    // check if map is created successfully or not
    if (mMap != null) {
      mMap.getUiSettings().setZoomControlsEnabled(false);
      LatLng latLong;

      latLong = new LatLng(location.getLatitude(), location.getLongitude());

      CameraPosition cameraPosition =
          new CameraPosition.Builder().target(latLong).zoom(17f).tilt(0).build();

      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
      }
      mMap.setMyLocationEnabled(true);
      mMap.getUiSettings().setMyLocationButtonEnabled(true);
      mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

      selectedLat = String.valueOf(location.getLatitude());
      selectedLng = String.valueOf(location.getLongitude());
      startIntentService(location);
    } else {
      Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      // Confirm location
      case R.id.rL_confirm_location:
        Intent intent = new Intent();
        intent.putExtra("latLong", new LatLng(Double.parseDouble(selectedLat),
            Double.parseDouble(selectedLng)).toString());
        intent.putExtra("address", mLocationText.getText().toString());
        setResult(RESULT_OK, intent);

        onBackPressed();
        break;

      // Back button
      case R.id.ivBack_into:
        onBackPressed();
        break;
    }
  }

  /**
   * Receiver for data sent from FetchAddressIntentService.
   */
  private class AddressResultReceiver extends ResultReceiver {
    AddressResultReceiver(Handler handler) {
      super(handler);
    }

    /**
     * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

      // Display the address string or an error message sent from the intent service.
      mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

      mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

      mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
      mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);
      System.out.println(TAG
          + " "
          + "message="
          + mAddressOutput
          + " "
          + "address="
          + mAreaOutput
          + " "
          + "mCityOutput="
          + mCityOutput
          + " "
          + "mStateOutput="
          + mStateOutput);

      displayAddressOutput();
    }
  }

  /**
   * Updates the address in the UI.
   */
  protected void displayAddressOutput() {
    try {
      if (mAreaOutput != null && !mAddressOutput.isEmpty()) {
        mLocationText.setText(mAddressOutput);
      } else {
        mLocationText.setText(mStateOutput);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates an intent, adds location data to it as an extra, and starts the intent service for
   * fetching an address.
   */
  protected void startIntentService(Location mLocation) {
    // Create an intent for passing to the intent service responsible for fetching the address.
    Intent intent = new Intent(this, FetchAddressIntentService.class);

    // Pass the result receiver as an extra to the service.
    intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

    // Pass the location data as an extra to the service.
    intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

    // Start the service. If the service isn't already running, it is instantiated and started
    // (creating a process for it if needed); if it is running then it remains running. The
    // service kills itself automatically once all intents are processed.
    startService(intent);
  }

  /**
   * Called after the autocomplete activity has finished to return its result.
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Check that the result was from the autocomplete widget.
    if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
      if (resultCode == RESULT_OK) {
        // Get the user's selected place from the Intent.
        Place place = Autocomplete.getPlaceFromIntent(data);

        // TODO call location based filter

        LatLng latLong;

        latLong = place.getLatLng();

        //mLocationText.setText(place.getName() + "");

        CameraPosition cameraPosition =
            new CameraPosition.Builder().target(latLong).zoom(17f).tilt(0).build();

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
          // TODO: Consider calling
          //    ActivityCompat#requestPermissions
          // here to request the missing permissions, and then overriding
          //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
          //                                          int[] grantResults)
          // to handle the case where the user grants the permission. See the documentation
          // for ActivityCompat#requestPermissions for more details.
          return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      }
    }
  }

  @Override
  public void onBackPressed() {
    finish();
  }
}