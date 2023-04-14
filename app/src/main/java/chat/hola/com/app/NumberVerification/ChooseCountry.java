package chat.hola.com.app.NumberVerification;
/*
 * Created by moda on 15/07/16.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.CountryCode;


/**
 * Activity to select the country for autofilling of code at time of number verification
 */
public class ChooseCountry extends AppCompatActivity implements
        Comparator<Country> {


    /**
     * This class allows the user to choose the country while logging in
     */


    private CountryListAdapter adapter;

    private List<Country> allCountriesList;

    private List<Country> selectedCountriesList;

    private Bus bus = AppController.getBus();
    private CoordinatorLayout searchRoot;
    private Typeface fontBold;
    private AppController appController;
    EditText searchEditText;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_picker);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        appController = AppController.getInstance();
        fontBold = appController.getSemiboldFont();
        ImageView close = (ImageView) findViewById(R.id.close);
        searchRoot = (CoordinatorLayout) findViewById(R.id.root2);
        AppController appController = AppController.getInstance();
        //fontBold=appController.getSemiboldFont();

        getAllCountries();
        TextView title = (TextView) findViewById(R.id.title);
        // Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Condensed.ttf");
        title.setTypeface(fontBold);


        searchEditText = (EditText) findViewById(R.id.country_code_picker_search);
        ListView countryListView = (ListView) findViewById(R.id.country_code_picker_listview);
        searchEditText.setTypeface(appController.getRegularFont());

        adapter = new CountryListAdapter(ChooseCountry.this, selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (position >= 0) {
                        Country country = selectedCountriesList.get(position);


                        Intent intent = new Intent();
                        intent.putExtra("FLAG", country.getFlag());
                        intent.putExtra("MESSAGE", country.getName());
                        intent.putExtra("CODE", country.getDialCode());
                        intent.putExtra("MAX", country.getMaxDigits());
                        intent.putExtra("CODE_NAME", country.getCode());

                        setResult(RESULT_OK, intent);
                        supportFinishAfterTransition();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();

            }
        });

        bus.register(this);
        searchEditText.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imm != null) {
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }

    /**
     * For adding the country picker
     */


    public List<Country> getAllCountries() {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<>();

                String allCountriesCode = readEncodedJsonString(ChooseCountry.this);


                JSONArray countrArray = new JSONArray(allCountriesCode);

                for (int i = 0; i < countrArray.length(); i++) {
                    JSONObject jsonObject = countrArray.getJSONObject(i);
                    String countryName = jsonObject.getString("name");
                    String countryDialCode = jsonObject.getString("dial_code");
                    String countryCode = jsonObject.getString("code");
                    int maxDigits = jsonObject.getInt("max_digits");

                    Country country = new Country();
                    country.setCode(countryCode);
                    country.setName(countryName);
                    country.setDialCode(countryDialCode);
                    country.setMaxDigits(maxDigits);
                    allCountriesList.add(country);
                }

                Collections.sort(allCountriesList, this);

                selectedCountriesList = new ArrayList<>();
                selectedCountriesList.addAll(allCountriesList);

                // Return
                return allCountriesList;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String readEncodedJsonString(Context context)
            throws java.io.IOException {
        byte[] data = Base64.decode(CountryCode.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }


    @SuppressLint("DefaultLocale")
    private void search(String text) {
        selectedCountriesList.clear();

        for (Country country : allCountriesList) {
            if (country.getName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }

        if (selectedCountriesList.size() == 0) {
            showNoSearchResults();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }

    private void showNoSearchResults() {

        Toast.makeText(this, getString(R.string.string_956), Toast.LENGTH_SHORT).show();

//        TSnackbar snackbar = TSnackbar
//                .make(searchRoot, getString(R.string.string_956), TSnackbar.LENGTH_SHORT);
//
//
//        snackbar.setMaxWidth(3000); //for fullsize on tablets
//        View snackbarView = snackbar.getView();
//        snackbarView.setBackgroundColor(Color.WHITE);
//
//
//        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
//        textView.setTextColor(ContextCompat.getColor(ChooseCountry.this, R.color.color_text_black));
//        snackbar.show();


    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(ChooseCountry.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }
}