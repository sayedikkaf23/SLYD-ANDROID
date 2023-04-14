package chat.hola.com.app.search.locations;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import dagger.android.support.DaggerFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class LocationsFragment extends DaggerFragment
    implements LocationAdapter.ClickListner, LocationsContract.View {

  private double current_latitude = 0.0, current_logitude = 0.0;

  @Inject
  LocationsPresenter presenter;
  @Inject
  SessionManager sessionManager;
  @Inject
  BlockDialog dialog;
  @Inject
  TypefaceManager typefaceManager;

  @BindView(R.id.howdooSugTv)
  TextView howdooSugTv;
  @BindView(R.id.peopleRv)
  RecyclerView peopleRv;
  @BindView(R.id.llEmpty)
  LinearLayout llEmpty;
  @BindView(R.id.ivEmpty)
  ImageView ivEmpty;

  private Unbinder unbinder;
  private EditText searchInputEt;
  private List<Location> data;
  private LocationAdapter madapter;
  private RecyclerView.LayoutManager mlayoutManager;
  LocationManager locationManager;

  public LocationsFragment() {
  }

  @Override
  public void userBlocked() {
    dialog.show();
  }

  @Override
  public void sessionExpired() {
    sessionManager.sessionExpired(getContext());
  }

  @Override
  public void isInternetAvailable(boolean flag) {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_people, container, false);
    unbinder = ButterKnife.bind(this, view);
    searchInputEt = getActivity().findViewById(R.id.searchInputEt);
    data = new ArrayList<>();

    locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

    requestStoragePermission();

    return view;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private void requestStoragePermission() {
    Dexter.withActivity(this.getActivity())
        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        .withListener(new MultiplePermissionsListener() {
          @Override
          public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (report.areAllPermissionsGranted()) {
              getCurrentPlaceItems();
            }

            // check for permanent denial of any permission
            if (report.isAnyPermissionPermanentlyDenied()) {
              // show alert dialog navigating to Settings
              //                            showSettingsDialog();
            }
          }

          @Override
          public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
              PermissionToken token) {
            token.continuePermissionRequest();
          }
        })
        .
            withErrorListener(new PermissionRequestErrorListener() {
              @Override
              public void onError(DexterError error) {
                Toast.makeText(getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
              }
            })
        .onSameThread()
        .check();
  }

  private void getCurrentPlaceItems() {

    // Initialize Places.
    com.google.android.libraries.places.api.Places.initialize(getActivity(),
        getActivity().getString(R.string.google_api_key_places));

    // Create a new Places client instance.
    PlacesClient placesClient = Places.createClient(getActivity());

    // Use fields to define the data types to return.
    List<Place.Field> placeFields =
        Arrays.asList(Place.Field.ADDRESS, Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

    // Use the builder to create a FindCurrentPlaceRequest.
    FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

    placesClient.findCurrentPlace(request)
        .addOnSuccessListener((new OnSuccessListener<FindCurrentPlaceResponse>() {
          @Override
          public void onSuccess(FindCurrentPlaceResponse response) {

            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
              Location location = new Location();
              location.setId(placeLikelihood.getPlace().getId());
              location.setName(placeLikelihood.getPlace().getName());
              location.setLatitude(placeLikelihood.getPlace().getLatLng().latitude);
              location.setLongitude(placeLikelihood.getPlace().getLatLng().longitude);
              data.add(location);
            }
            madapter.setData(data);
          }
        }))
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception exception) {

          }
        });
  }

  private void showSettingsDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Need Permissions");
    builder.setMessage(
        "This app needs permission to use this feature. You can grant them in app settings.");
    builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        openSettings();
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    builder.show();
  }

  private void openSettings() {
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
    intent.setData(uri);
    startActivityForResult(intent, 101);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    searchInputEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().isEmpty()) {
          requestStoragePermission();
        } else {
          searchPlace(charSequence.toString());
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
    madapter = new LocationAdapter(getContext(), data, typefaceManager);
    madapter.setClickListner(this);
    peopleRv.setHasFixedSize(true);
    mlayoutManager = new LinearLayoutManager(getContext());
    peopleRv.setLayoutManager(mlayoutManager);
    peopleRv.setItemAnimator(new DefaultItemAnimator());
    peopleRv.setAdapter(madapter);

    super.onViewCreated(view, savedInstanceState);
  }

  private void searchPlace(String text) {
    String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
        + text
        + "&inputtype=textquery&fields=place_id,name,geometry&key="
        + BuildConfig.MAP_BUNDLE_KEY;

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {

            try {
              JSONArray candidates = response.getJSONArray("candidates");
              if (candidates.length() > 0) {
                data.clear();
                for (int i = 0; i < candidates.length(); i++) {
                  Location location = new Location();
                  JSONObject candidate = candidates.getJSONObject(i);
                  location.setId(candidate.getString("place_id"));
                  location.setName(candidate.getString("name"));

                  JSONObject latlong =
                      candidate.getJSONObject("geometry").getJSONObject("location");
                  location.setLatitude(latlong.getDouble("lat"));
                  location.setLongitude(latlong.getDouble("lng"));

                  data.add(location);
                }
                madapter.setData(data);
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, volleyError -> Log.e("Error:", volleyError.toString()));

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    AppController.getInstance().addToRequestQueue(jsonObjReq, "searchPlaces");
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.attachView(this);
    searchPlace(searchInputEt.getText().toString());
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter.detachView();
  }

  @Override
  public void showMessage(String msg, int msgId) {
    if (msg != null && !msg.isEmpty()) {
      Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    } else if (msgId != 0) {
      Toast.makeText(getContext(), getResources().getString(msgId), Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void reload() {

  }

  @Override
  public void onItemClick(int position) {
    chat.hola.com.app.home.model.Location location = new chat.hola.com.app.home.model.Location();
    location.setLatitude(data.get(position).getLatitude());
    location.setLongitude(data.get(position).getLongitude());
    startActivity(new Intent(getContext(), TrendingDetail.class).putExtra("placeId",
        data.get(position).getId())
        .putExtra("call", "location")
        .putExtra("location", data.get(position).getName())
        .putExtra("latlong", location));
  }

  //    @Override
  //    public void onLocationChanged(Location location) {
  //        current_latitude = location.getLatitude();
  //        current_logitude = location.getLongitude();
  //        Log.i("lat-long", "" + current_latitude + "-" + current_logitude);
  //    }
  //
  //    @Override
  //    public void onStatusChanged(String provider, int status, Bundle extras) {
  //
  //    }
  //
  //    @Override
  //    public void onProviderEnabled(String provider) {
  //
  //    }
  //
  //    @Override
  //    public void onProviderDisabled(String provider) {
  //
  //    }
}
