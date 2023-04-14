package chat.hola.com.app.post.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.models.Place;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>LocationPresenter</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
public class LocationPresenter implements LocationContract.Presenter {

  @Inject
  LocationModel model;
  @Inject
  HowdooService service;

  @Inject
  public LocationPresenter() {
  }

  @Override
  public void location(String text) {
    String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
        + text
        + "&inputtype=textquery&fields=place_id,formatted_address,name,geometry&key="
        + BuildConfig.MAP_BUNDLE_KEY;

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {

            try {
              List<Place> data = new ArrayList<>();
              JSONArray candidates = response.getJSONArray("candidates");
              if (candidates.length() > 0) {
                data.clear();
                for (int i = 0; i < candidates.length(); i++) {
                  Place location = new Place();
                  JSONObject candidate = candidates.getJSONObject(i);
                  location.setId(candidate.getString("place_id"));
                  location.setTitle(candidate.getString("name"));
                  location.setAddress(candidate.getString("formatted_address"));

                  JSONObject latlong =
                      candidate.getJSONObject("geometry").getJSONObject("location");
                  location.setLatitude(String.valueOf(latlong.getDouble("lat")));
                  location.setLogitude(String.valueOf(latlong.getDouble("lng")));

                  data.add(location);
                }
                model.addPlaces(data);
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
  public void nearByLocation(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        return;
      }
    }
    // Initialize Places.
    com.google.android.libraries.places.api.Places.initialize(context,
        context.getString(R.string.google_api_key_places));
    // Create a new Places client instance.
    PlacesClient placesClient =
        com.google.android.libraries.places.api.Places.createClient(context);
    // Use fields to define the data types to return.
    List<com.google.android.libraries.places.api.model.Place.Field> placeFields =
        Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
            com.google.android.libraries.places.api.model.Place.Field.NAME,
            com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
            com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
    // Use the builder to create a FindCurrentPlaceRequest.
    FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

    placesClient.findCurrentPlace(request).addOnSuccessListener((response -> {

      List<Place> list = new ArrayList<>();
      for (com.google.android.libraries.places.api.model.PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
        Place item = new Place();
        item.setId(placeLikelihood.getPlace().getId());
        item.setLatitude(String.valueOf(placeLikelihood.getPlace().getLatLng().latitude));
        item.setLogitude(String.valueOf(placeLikelihood.getPlace().getLatLng().longitude));
        item.setTitle(placeLikelihood.getPlace().getName());
        item.setAddress(placeLikelihood.getPlace().getAddress());
        list.add(item);
      }
      model.addPlaces(list);
    })).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {

      }
    });
  }
}
