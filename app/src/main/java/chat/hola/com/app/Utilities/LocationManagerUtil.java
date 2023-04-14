package chat.hola.com.app.Utilities;

import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.ezcall.android.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/*
 * Purpose – This class holds LocationData related operations
 * @author 3Embed
 * Created on Nov 25, 2019
 * Modified on
 */
public class LocationManagerUtil {
  public static double CURRENT_LAT = 0.0;
  public static double CURRENT_LAN = 0.0;

  public static void setMapHeight(AppCompatActivity activity) {
    int width = activity.getResources().getDisplayMetrics().widthPixels;
    int height = activity.getResources().getDisplayMetrics().heightPixels / 3;
    Utilities.printLog("ht is " + height);
    height = height + 50;
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
    SupportMapFragment mapFragment =
        (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
    Objects.requireNonNull(mapFragment.getView()).setLayoutParams(layoutParams);
    View mapView = mapFragment.getView();
    if (mapView != null && mapView.findViewById(Integer.valueOf(1)) != null) {
      View locationButton = ((View) mapView.findViewById(
          Integer.valueOf(1)).getParent()).findViewById(Integer.valueOf(2));
      RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams)
          locationButton.getLayoutParams();
      layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
      layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
      layoutParams1.setMargins(0, 0, 30, 30);
    }
  }

  public interface FusedLocationListener {
    /**
     * This method is using as an callback of current location
     *
     * @param latitude  current latitude
     * @param longitude current longitude
     */
    void onSuccess(double latitude, double longitude);
  }

  public interface OnPlaceIdToAddressConverter {
    /**
     * this method is using as an callback method of placeId to address converter
     *
     * @param address   address of place ID
     * @param latitude  latitude of placeID
     * @param longitude longitude of placeId
     */
    void onPlaceIdToAddressSuccess(String address, double latitude, double longitude);

    void onRidePlaceIdToAddressSuccess(String address, double latitude, double longitude);

    void onAreaNameAddressSuccess(String fullAddress, String address, double latitude,
        double longitude);
  }


  public static boolean isGpsEnabled(Context context) {
    boolean isEnabled;
    final LocationManager manager = (LocationManager) context.getSystemService(
        Context.LOCATION_SERVICE);
    isEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    return isEnabled;
  }

  /**
   * This method is using to get current location of the device
   */
  public static double[] getFuseLocation(Activity context,
      FusedLocationListener fusedLocationListener) {
    final double[] mLatLong = new double[TWO];
    FusedLocationProviderClient fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context);
    fusedLocationClient.getLastLocation().addOnFailureListener(
        e -> Utilities.printLog("latLong" + e.getMessage()))
        .addOnSuccessListener(context, location -> {
          if (location != null) {
            mLatLong[ZERO] = location.getLatitude();
            mLatLong[ONE] = location.getLongitude();
            if (fusedLocationListener != null) {
              fusedLocationListener.onSuccess(location.getLatitude(), location.getLongitude());
            }
            CURRENT_LAT = mLatLong[ZERO];
            CURRENT_LAN = mLatLong[ONE];
            Utilities.printLog("latLong" + mLatLong[ZERO] + "" + mLatLong[ONE]);
          }
        });
    return mLatLong;
  }

  /**
   * this method
   * @param context
   * @return
   */
  public static String getCurrentLocation(Activity context) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      LocationManager lm = (LocationManager) context.getApplicationContext().getSystemService(
          Context.LOCATION_SERVICE);
      Geocoder geocoder = new Geocoder(context.getApplicationContext());
      for (String provider : lm.getAllProviders()) {
        @SuppressWarnings("ResourceType")
        Location location = lm.getLastKnownLocation(provider);
        if (location != null) {
          try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
              stringBuilder.append(addresses.get(0).getAdminArea()).append(",").append(
                  addresses.get(0).getLocality()).append(",").append(
                  addresses.get(0).getPostalCode());
              break;
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }
}