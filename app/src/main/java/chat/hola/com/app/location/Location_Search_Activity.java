package chat.hola.com.app.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.App_permission_23;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Http_download;
import chat.hola.com.app.Utilities.PlaceDetailsJSONParser;
import chat.hola.com.app.Utilities.PlaceJSONParser;
import chat.hola.com.app.manager.session.SessionManager;
import com.ezcall.android.R;
import com.google.gson.Gson;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h2>Location_Search_Activity</h2>
 * <p>
 * Google place api
 * </P>
 *
 * @author 3Embed.
 * @since 02/02/2017.
 */
public class Location_Search_Activity extends DaggerAppCompatActivity
    implements App_permission_23.Permission_Callback, Location_service.GetLocationListener,
    View.OnClickListener {

  private String LOCATION_TAG = "location_tag";

  @Inject
  App_permission_23 app_permission_23;
  AlertDialog.Builder builder;
  private Net_work_failed_aleret net_work_failed_aleret;
  Location_service location_service = null;
  private boolean isTurn_button_click = false;
  private RelativeLayout location_premission_denined;
  private LinearLayout permission_garanted;
  public Dialog alert_dialog;
  //public Typeface RobotoRegular, RobotoMedium, RobotoBold;
  //  private ProgressBar location_progress_bar;
  private int PROXIMITY_RADIUS = 500000;
  final int PLACES = 0;
  final int PLACES_DETAILS = 1;
  private ArrayList<String> preference_list;
  private AddressAdapterNew adapter;
  private double current_latitude = 0.0, current_logitude = 0.0;
  private Address_list_holder address_list_holder;
  private ArrayList<Address_list_item_pojo> actual_Address_List;
  private SessionManager sessionManager;
  private EditText search_text;
  private boolean isReleted_Address_found = false;
  public static String LOCATION_NAME = "locationName", LOCATION_DETAILS = "locationDetails",
      LATITUDE = "latitude", LOGITUDE = "longitude", MESSAGE = "message";
  private ProgressDialog dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location__search_);
    net_work_failed_aleret = Net_work_failed_aleret.getInstance();
    AppController appController = AppController.getInstance();
    dialog = new ProgressDialog(this);
    dialog.setMessage("Please wait...");
    builder = new AlertDialog.Builder(this);
    builder.setMessage("We need permission to access your current location, please grant");

    TextView title = (TextView) findViewById(R.id.page_title);
    title.setTypeface(appController.getSemiboldFont());

    /*
     * Creating the list for the text.*/
    preference_list = new ArrayList<>();
    address_list_holder = new Address_list_holder();
    actual_Address_List = address_list_holder.getList_of_address();
    sessionManager = new SessionManager(this);
    init_XML_content();
  }

  @Override
  protected void onStart() {
    super.onStart();
    /*
     * Asking for permission of location.*/
    askFor_Phone_Permission(LOCATION_TAG);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    app_permission_23.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * <h2>init_XML_content</h2>
   * <p>
   * Initializing all the xml content.
   * </P>
   */
  private void init_XML_content() {
    RelativeLayout back_button = (RelativeLayout) findViewById(R.id.back_button);
    back_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });
    permission_garanted = (LinearLayout) findViewById(R.id.permission_garanted);
    location_premission_denined = (RelativeLayout) findViewById(R.id.location_premission_denined);
    TextView turn_on_button = (TextView) findViewById(R.id.turn_on_button);
    turn_on_button.setOnClickListener(this);
    //  location_progress_bar = (ProgressBar) findViewById(R.id.location_progress_bar);
    RecyclerView location_list = (RecyclerView) findViewById(R.id.location_list);
    location_list.setLayoutManager(new LinearLayoutManager(this));
    actual_Address_List.clear();
    adapter =
        new AddressAdapterNew(actual_Address_List, new AddressAdapterNew.OnItemClickListener() {
          @Override
          public void onItemClick(int position) {
            Address_list_item_pojo data_item = actual_Address_List.get(position);
            String latitude = data_item.getLatitude();
            String logitude = data_item.getLogitude();
            if (latitude != null
                && !latitude.isEmpty()
                && logitude != null
                && !logitude.isEmpty()) {
              Intent intent = new Intent();
              intent.putExtra(LOCATION_NAME, data_item.getAddress_title());
              intent.putExtra(LOCATION_DETAILS, data_item.getSub_Address());
              intent.putExtra(LATITUDE, data_item.getLatitude());
              intent.putExtra(LOGITUDE, data_item.getLogitude());
              intent.putExtra(MESSAGE, "1");
              setResult(RESULT_OK, intent);
              finish();
            } else {
              Data_holder data_holder = new Data_holder();
              data_holder.isNearest_data = 2;
              data_holder.Search_text_data = preference_list.get(position);
              data_holder.index_number = position;
              /**
               * Down loading the data.*/
              new PlacesTask().execute(data_holder);
            }
          }
        });
    location_list.setAdapter(adapter);
    //        location_list.addOnItemTouchListener(this, new RecyclerItemClickListener.OnItemClickListener() {
    //            @Override
    //            public void onItemClick(View view, int position) {
    //                Address_list_item_pojo data_item = actual_Address_List.get(position);
    //                String latitude = data_item.getLatitude();
    //                String logitude = data_item.getLogitude();
    //                if (latitude != null && !latitude.isEmpty() && logitude != null && !logitude.isEmpty()) {
    //                    Intent intent = new Intent();
    //                    intent.putExtra(LOCATION_NAME, data_item.getAddress_title());
    //                    intent.putExtra(LOCATION_DETAILS, data_item.getSub_Address());
    //                    intent.putExtra(LATITUDE, data_item.getLatitude());
    //                    intent.putExtra(LOGITUDE, data_item.getLogitude());
    //                    intent.putExtra(MESSAGE, "1");
    //                    setResult(RESULT_OK, intent);
    //                    Location_Search_Activity.this.finish();
    //                } else {
    //                    Data_holder data_holder = new Data_holder();
    //                    data_holder.isNearest_data = 2;
    //                    data_holder.Search_text_data = preference_list.get(position);
    //                    ;
    //                    data_holder.index_number = position;
    //                    /**
    //                     * Down loading the data.*/
    //                    new PlacesTask().execute(data_holder);
    //                }
    //            }
    //        }));
    search_text = (EditText) findViewById(R.id.search_text);
    search_text.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String data = charSequence.toString().trim();
        if (!data.isEmpty()) {
          Data_holder data_holder = new Data_holder();
          data_holder.isNearest_data = 1;
          data_holder.latitude = current_latitude;
          data_holder.logitude = current_logitude;
          data_holder.Search_text_data = data;
          /**
           * Doing the task.*/
          new PlacesTask().execute(data_holder);
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });
  }

  /**
   * Getting the Current Location of the user.
   */
  private void get_Current_Location() {
    // location_progress_bar.setVisibility(View.VISIBLE);
    dialog.show();
    /**
     * Checking the location service.*/
    if (location_service == null) {
      location_service = new Location_service(this, this);
    } else {
      /**
       * Checking the location setting.*/
      location_service.checkLocationSettings();
    }
  }

  /**
   * Dispatch incoming result to the correct fragment.
   *
   * @param requestCode contains the request code.
   * @param resultCode contains the result code.
   * @param data contains the result data.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Location_service.REQUEST_CHECK_SETTINGS) {
      if (resultCode == Activity.RESULT_OK) {
        location_service.checkLocationSettings();
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * Take care of popping the fragment back stack or finishing the activity
   * as appropriate.
   */
  @Override
  public void onBackPressed() {
    if (isReleted_Address_found) {
      Intent intent = new Intent();
      intent.putExtra(MESSAGE, "0");
      setResult(RESULT_OK, intent);
      this.finish();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.turn_on_button:
        isTurn_button_click = true;
        askFor_Phone_Permission(LOCATION_TAG);
        break;
    }
  }

  /**
   * <h2>askFor_Phone_Permission</h2>
   * <p>
   * Method is going to ask for phone permission .
   * it is required for the read phone state in android.
   * </P>
   */
  private void askFor_Phone_Permission(String tag) {
    ArrayList<App_permission_23.Permission> permissions = new ArrayList<>();
    if (tag.equals(LOCATION_TAG)) {
      permissions.add(App_permission_23.Permission.LOCATION);
      app_permission_23.getPermission(LOCATION_TAG, permissions, null, this);
    }
  }

  /**
   * <h1>isNetwork_Available</h1>
   * <P>
   * Checking the network is available or not.
   * </P>
   */
  //    private boolean isNetwork_Available()
  //    {
  //        return Utility.isNetworkAvailable(this);
  //    }

  /**
   * <h1>InternetConnection_CallBack</h1>
   * <p>
   * Set connection call back.
   * </P>
   */
  private void InternetConnection_CallBack() {
    net_work_failed_aleret.net_work_fail(this,
        new Net_work_failed_aleret.Internet_connection_Callback() {
          @Override
          public void onSucessConnection(String connection_Type) {

          }

          @Override
          public void onErrorConnection(String error) {

          }
        });
  }

  /**
   * Permission granted call back.
   *
   * @param isAllGranted is all permission granted.
   * @param tag contains the asked tag.
   */
  @Override
  public void onPermissionGranted(boolean isAllGranted, String tag) {
    if (LOCATION_TAG.equalsIgnoreCase(tag) && isAllGranted) {
      permission_layout_handeler(true);
      get_Current_Location();
      //            if(isNetwork_Available())
      //            {
      //                get_Current_Location();
      //            }else
      //            {
      //                InternetConnection_CallBack();
      //            }
    }
  }

  /**
   * Permission not granted call back.
   *
   * @param deniedPermission is all permission denied.
   * @param tag contains the asked tag.
   */
  @Override
  public void onPermissionDenied(ArrayList<String> deniedPermission, String tag) {
    showPermissionMessage(deniedPermission, tag);
    //        String[] stringArray = deniedPermission.toArray(new String[0]);
    //        if (tag.equalsIgnoreCase(LOCATION_TAG)) {
    //            app_permission_23.ask_permission_directory(stringArray);
    //        }
  }

  @Override
  public void onPermissionRotation(ArrayList<String> rotationPermission, String tag) {
    isTurn_button_click = false;
    String[] permission = rotationPermission.toArray(new String[0]);
    app_permission_23.ask_permission_rotational(permission);
  }

  /**
   * Permission granted permanently denied call back.
   *
   * @param tag contains the asked tag.
   */
  @Override
  public void onPermissionPermanent_Denied(String tag) {
    gotoSettings();
    //        if (tag.equalsIgnoreCase(LOCATION_TAG)) {
    //            if (isTurn_button_click) {
    //                open_Setting_dialog();
    //            } else {
    //                permission_layout_handeler(false);
    //            }
    //            isTurn_button_click = false;
    //        }
  }

  @Override
  public void updateLocation(Location location) {
    //location_progress_bar.setVisibility(View.GONE);
    dialog.dismiss();
    /**
     * Updating location to the location service.
     * and stopping the location update.*/
    location_service.stop_Location_Update();
    current_latitude = location.getLatitude();
    current_logitude = location.getLongitude();

    Data_holder data_holder = new Data_holder();
    data_holder.isNearest_data = 0;
    data_holder.latitude = current_latitude;
    data_holder.logitude = current_logitude;
    /**
     * Doing the task.*/
    new PlacesTask().execute(data_holder);
  }

  @Override
  public void location_Error(String error) {
    //  location_progress_bar.setVisibility(View.GONE);
    dialog.dismiss();
    //Error on location.
  }

  private void gotoSettings() {

    builder.setPositiveButton("Setting", (dialogInterface, i) -> {
      Intent intent = new Intent();
      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      Uri uri = Uri.fromParts("package", getPackageName(), null);
      intent.setData(uri);
      startActivity(intent);
    });
    builder.show();
  }

  private void showPermissionMessage(ArrayList<String> deniedPermission, String tag) {
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        String[] stringArray = deniedPermission.toArray(new String[0]);
        if (tag.equalsIgnoreCase(LOCATION_TAG)) {
          app_permission_23.ask_permission_directory(stringArray);
        }
      }
    });

    builder.show();
  }

  /**
   * <h2>Handling the layout of the UI.</h2>
   */
  private void permission_layout_handeler(boolean isGranted) {
    if (isGranted) {
      permission_garanted.setVisibility(View.VISIBLE);
      location_premission_denined.setVisibility(View.GONE);
    } else {
      permission_garanted.setVisibility(View.GONE);
      location_premission_denined.setVisibility(View.VISIBLE);
    }
  }

  /**
   * <h2>open_Setting_dialog</h2>
   * <p>
   * Image that the image
   * </P>
   */
  private void open_Setting_dialog() {
    /**
     * closing the key pad.*/
    // Utility.close_soft_input_keypad(this);

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    LayoutInflater inflater = this.getLayoutInflater();
    @SuppressLint("InflateParams")
    View dialogView = inflater.inflate(R.layout.permission_denined_setting_layout, null);
    alertDialog.setView(dialogView);

    TextView header_title = (TextView) dialogView.findViewById(R.id.heading_text);
    // header_title.setTypeface(RobotoRegular);

    TextView setting_click = (TextView) dialogView.findViewById(R.id.setting_click);
    //setting_click.setTypeface(RobotoMedium);

    setting_click.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", Location_Search_Activity.this.getPackageName(), null);
        intent.setData(uri);
        Location_Search_Activity.this.startActivity(intent);

        if (alert_dialog != null) {
          alert_dialog.cancel();
          alert_dialog.dismiss();
        }
      }
    });
    alert_dialog = alertDialog.show();
    alert_dialog.show();
  }

  /**
   * Fetches all places from GooglePlaces AutoComplete Web Service
   */
  private class PlacesTask extends AsyncTask<Data_holder, Void, String> {
    boolean isSucess = false;
    int isFor_what = 0;
    int index_data = 0;

    @Override
    protected String doInBackground(Data_holder... place) {
      // For storing data from web service
      String data = "";
      Data_holder data_item = place[0];
      isFor_what = data_item.isNearest_data;
      index_data = data_item.index_number;
      String url_link;
      if (isFor_what == 0) {
        url_link = getUrl_forsquare(data_item.latitude, data_item.logitude);
      } else if (isFor_what == 1) {
        url_link = getUrl_Google_search(data_item.Search_text_data, data_item.latitude,
            data_item.logitude);
      } else {
        url_link = getPlaceDetailsUrl(data_item.Search_text_data);
      }
      try {
        /**
         * Downloading the data details.*/
        isSucess = true;
        data = new Http_download().downloadUrl(url_link);
      } catch (Exception e) {
        isSucess = false;
        e.printStackTrace();
      }
      return data;
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      //  location_progress_bar.setVisibility(View.VISIBLE);
      //   dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      //   location_progress_bar.setVisibility(View.GONE);
      //   dialog.dismiss();
      if (isSucess) {
        if (isFor_what == 0) {
          /**
           * Parsing the forsquare response.*/
          parse_Forsquare_response(result);
        } else if (isFor_what == 1) {
          /**
           * Parsing the result getting from google. place*/
          new ParserTask(PLACES, 0).execute(result);
        } else {
          /**
           * Parsing the result getting from google. place*/
          new ParserTask(PLACES_DETAILS, index_data).execute(result);
        }
      } else {
        //Handel error of the response.
      }
    }
  }

  /**
   * <h2>parse_Forsquare_response</h2>
   * <p>
   * Parsing the for square api link data details using the location api.
   * </P>
   *
   * @param result contains the resutl from for square .
   */
  private void parse_Forsquare_response(String result) {

    actual_Address_List.clear();

    try {
      JSONObject jsonObject = new JSONObject(result);
      JSONObject response_data = jsonObject.getJSONObject("response");
      JSONArray jsonArray = response_data.getJSONArray("venues");
      Address_list_item_pojo temp_item;
      for (int count = 0; count < jsonArray.length(); count++) {
        temp_item = new Address_list_item_pojo();
        JSONObject data = jsonArray.getJSONObject(count);
        temp_item.setAddress_title(data.getString("name"));
        JSONObject sub_item = data.getJSONObject("location");
        String sub_details = "", latitude = "", logitude = "";
        if (sub_item.has("address")) {
          sub_details = sub_item.getString("address");
        }
        if (sub_item.has("lat")) {
          latitude = sub_item.getString("lat");
          logitude = sub_item.getString("lng");
        }
        temp_item.setSub_Address(sub_details);
        temp_item.setLatitude(latitude);
        temp_item.setLogitude(logitude);
        actual_Address_List.add(temp_item);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    if (actual_Address_List.size() > 0) {
      search_text.setHint(R.string.find_location_text);
      search_text.setEnabled(true);
      String adresses = new Gson().toJson(address_list_holder);
      sessionManager.setAdresses(adresses);
      isReleted_Address_found = true;
      adapter.notifyDataSetChanged();
    }
  }

  /**
   * A class to parse the Google Places in JSON format
   */
  private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
    JSONObject jObject;
    int parserType = 0;
    int index_place = 0;

    ParserTask(int type, int index) {
      this.parserType = type;
      this.index_place = index;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(String... jsonData) {
      List<HashMap<String, String>> places = null;
      try {
        if (jsonData[0] != null) {
          jObject = new JSONObject(jsonData[0]);
          switch (parserType) {
            case PLACES:
              PlaceJSONParser placeJsonParser = new PlaceJSONParser();
              places = placeJsonParser.parse(jObject);
              break;
            case PLACES_DETAILS:
              PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
              places = placeDetailsJsonParser.parse(jObject);
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return places;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      //  location_progress_bar.setVisibility(View.VISIBLE);
      dialog.show();
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> result) {
      switch (parserType) {
        case PLACES:
          if (result != null) {
            preference_list.clear();
            actual_Address_List.clear();
            for (int i = 0; i < result.size(); i++) {
              String address_Details = result.get(i).get("description");
              preference_list.add(result.get(i).get("reference"));
              String header_name[] = address_Details.split(",");
              Address_list_item_pojo temp_item = new Address_list_item_pojo();
              temp_item.setAddress_title(header_name[0]);
              temp_item.setSub_Address(address_Details);
              actual_Address_List.add(temp_item);
            }
            adapter.notifyDataSetChanged();
          }
          break;
        case PLACES_DETAILS:
          if (result != null && result.size() > 0) {
            double latitude = Double.parseDouble(result.get(0).get("lat"));
            double longitude = Double.parseDouble(result.get(0).get("lng"));
            Address_list_item_pojo temp_item = actual_Address_List.get(index_place);
            Intent intent = new Intent();
            intent.putExtra(LOCATION_NAME, temp_item.getAddress_title());
            intent.putExtra(LOCATION_DETAILS, temp_item.getSub_Address());
            intent.putExtra(LATITUDE, "" + latitude);
            intent.putExtra(LOGITUDE, "" + longitude);
            intent.putExtra(MESSAGE, "1");
            setResult(RESULT_OK, intent);
            Location_Search_Activity.this.finish();
          }
          break;
      }
      // location_progress_bar.setVisibility(View.GONE);
      dialog.dismiss();
    }
  }

  /**
   * <h2>getUrl_Google_search</h2>
   * <p>
   * Google location search data.
   * </P>
   */
  private String getUrl_Google_search(String search_Data, double currentLatitude,
      double currentLongitude) {
    String key = "key=" + Constants.Google.GOOGLE_API_KEY;

    String input = "";
    try {
      input = "input=" + URLEncoder.encode(search_Data, "utf-8");
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }
    String types = "establishment|geocode&location="
        + currentLatitude
        + ","
        + currentLongitude
        + "&radius=500000po&language=en";
    String parameters = input + "&" + types + "&limit=" + 50 + "&" + key;
    // Output format
    String output = "json";
    // Building the url to the web service
    String url =
        "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;
    return url;
  }

  /**
   * <h2>getUrl_forsquare</h2>
   * <p>
   * Getting the url to load the requested url data.
   * </P>
   *
   * @param latitude contains your searched location latitude.
   * @param longitude contains your searched location logitude.
   */
  private String getUrl_forsquare(double latitude, double longitude) {
    StringBuilder googlePlacesUrl =
        new StringBuilder("https://api.foursquare.com/v2/venues/search?");
    googlePlacesUrl.append("client_id=");
    googlePlacesUrl.append("");
    googlePlacesUrl.append("&client_secret=");
    googlePlacesUrl.append("");
    googlePlacesUrl.append("&v=");
    googlePlacesUrl.append("20170202");
    googlePlacesUrl.append("&ll=");
    googlePlacesUrl.append(latitude);
    googlePlacesUrl.append(",");
    googlePlacesUrl.append(longitude);
    googlePlacesUrl.append("&sortByDistance=");
    googlePlacesUrl.append("1");
    googlePlacesUrl.append("&radius=");
    googlePlacesUrl.append(PROXIMITY_RADIUS);
    googlePlacesUrl.append("&limit=");
    googlePlacesUrl.append(30);
    googlePlacesUrl.append("&locale=");
    googlePlacesUrl.append("en");
    return new String(googlePlacesUrl);
  }

  /**
   * Creating the request list.
   */
  private String getPlaceDetailsUrl(String ref) {
    String key = "key=" + Constants.Google.GOOGLE_API_KEY;
    // reference of place
    String reference = "reference=" + ref;
    // Sensor enabled
    String sensor = "sensor=false";
    // Building the parameters to the web service
    String parameters = reference + "&" + sensor + "&" + key;
    // Output format
    String output = "json";
    // Building the url to the web service
    return "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
  }

  class Data_holder {
    int isNearest_data;
    double latitude;
    double logitude;
    String Search_text_data;
    int index_number;
  }

  /**
   * Closing the keyPAd of device
   */
  private void close_SOft_Input_KeyPad() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
  }
}
