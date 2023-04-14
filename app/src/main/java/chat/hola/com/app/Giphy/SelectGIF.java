package chat.hola.com.app.Giphy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectGIF extends AppCompatActivity {
  private String tag_string_req = "string_req";
  private RecyclerView recyclerView;
  private SelectGIF_Adapter mAdapter;
  private ArrayList<Giphy_Item> mGifData = new ArrayList<>();
  private boolean forStickers;

  private RelativeLayout root;

  private ProgressDialog pDialog;
  private Bus bus = AppController.getBus();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_gif);

    recyclerView = (RecyclerView) findViewById(R.id.rvGif);
    recyclerView.setHasFixedSize(true);
    mAdapter = new SelectGIF_Adapter(this, mGifData);

    root = (RelativeLayout) findViewById(R.id.root);

    recyclerView.setItemAnimator(new DefaultItemAnimator());

    recyclerView.setAdapter(mAdapter);
    setupActivity(getIntent());

    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,
        new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position) {

            String gifUrl = mGifData.get(position).getGifUrl();

            Intent intent = new Intent();
            intent.putExtra("gifUrl", gifUrl);
            setResult(RESULT_OK, intent);
            finish();
          }

          @Override
          public void onItemLongClick(View view, int position) {

          }
        }));

    ImageView iv = (ImageView) findViewById(R.id.close);
    iv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    hideKeyboard(SelectGIF.this);
    final EditText search_Text = (EditText) findViewById(R.id.search_Text);

    search_Text.addTextChangedListener(new

                                           TextWatcher() {

                                             public void afterTextChanged(Editable s) {

                                             }

                                             public void beforeTextChanged(CharSequence s,
                                                 int start, int count, int after) {
                                             }

                                             public void onTextChanged(CharSequence s, int start,
                                                 int before, int count) {

                                               if (s.length() > 2 && !(s.toString()
                                                   .trim()
                                                   .isEmpty())) {

                                                 getGiphy(s.toString());
                                               }
                                             }
                                           });

    Button search_button = (Button) findViewById(R.id.search_button);
    search_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = search_Text.getText().toString();
        if (!text.isEmpty()) {
          getGiphy(text);
        } else {

          if (root != null) {
            Snackbar snackbar;
            if (forStickers) {
              snackbar = Snackbar.make(root, R.string.Enter_Sticker, Snackbar.LENGTH_SHORT);
            } else {

              snackbar = Snackbar.make(root, R.string.Enter_Giphy, Snackbar.LENGTH_SHORT);
            }

            snackbar.show();
            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        }
      }
    });

    bus.register(this);
  }

  private void getGiphy(String search_data) {
    String url;
    if (search_data != null && !search_data.isEmpty()) {
      if (search_data.contains(" ")) {
        String temp_search_data = "";
        String data[] = search_data.split(" ");
        for (String item : data) {
          if (item != null && !item.isEmpty()) {
            if (!temp_search_data.isEmpty()) {
              temp_search_data = temp_search_data + item;
            } else {
              temp_search_data = item;
            }
          }
        }
        search_data = temp_search_data;
      }
        if (forStickers) {
            url = ApiOnServer.SEARCH_STICKERS + search_data + ApiOnServer.GIPHY_APIKEY;
        } else {
            url = ApiOnServer.SEARCH_GIFS + search_data + ApiOnServer.GIPHY_APIKEY;
        }
    } else {
        if (forStickers) {
            url = ApiOnServer.TRENDING_STICKERS;
        } else {
            url = ApiOnServer.TRENDING_GIFS;
        }
    }

    getGIFs(url);
  }

  public void getGIFs(final String giphyUrl) {

    if (giphyUrl.equals(ApiOnServer.TRENDING_GIFS) || giphyUrl.equals(
        ApiOnServer.TRENDING_STICKERS)) {

      pDialog = new ProgressDialog(SelectGIF.this);
      pDialog.setCancelable(false);

      pDialog.setMessage(getString(R.string.progress_dialog_loading));

      pDialog.show();
    }

    StringRequest strReq = new StringRequest(Request.Method.GET, giphyUrl,
        new com.android.volley.Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            handleResponse(response);

            if (pDialog != null && pDialog.isShowing()) {

              pDialog.dismiss();
            }
          }
        }, new com.android.volley.Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        if (pDialog != null && pDialog.isShowing()) {

          pDialog.dismiss();
        }
        if (root != null) {
          Snackbar snackbar =
              Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        if (giphyUrl.equals(ApiOnServer.TRENDING_GIFS) || giphyUrl.equals(
            ApiOnServer.TRENDING_STICKERS)) {

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              onBackPressed();
            }
          }, 500);
        }
      }
    });
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);
    // Add the request to the RequestQueue.
    queue.add(strReq);
    //        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

  }

  private void handleResponse(String response) {
    try {
      JSONObject jsonObject = new JSONObject(response);
      JSONArray jsonArray = jsonObject.getJSONArray("data");
      mGifData.clear();
      mAdapter.notifyDataSetChanged();
      recyclerView.setVisibility(View.VISIBLE);
      if (jsonArray != null) {
        for (int count = 0; count < jsonArray.length(); count++) {
          JSONObject jsonObject1 = jsonArray.getJSONObject(count);
          JSONObject images = jsonObject1.getJSONObject("images");
          String url_gif;
            if (forStickers) {
                url_gif = images.getJSONObject("fixed_width_still").getString("url");
            } else {
                url_gif = images.getJSONObject("fixed_width").getString("url");
            }

          Giphy_Item item_temp = new Giphy_Item();
          item_temp.setGifUrl(url_gif);
          mGifData.add(item_temp);

          mAdapter.notifyItemInserted(count);
        }
        //                mAdapter.notifyDataSetChanged();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static void hideKeyboard(Context ctx) {
    InputMethodManager inputManager =
        (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

    // check if no view has focus:
    View v = ((Activity) ctx).getCurrentFocus();
    if (v == null) return;

    inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);


    /*
     *      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
     *     imm.hideSoftInputFromWindow(search_Text.getWindowToken(), 0);
     */
  }

  @Override
  protected void onPause() {
    super.onPause();

    hideKeyboard(SelectGIF.this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    mGifData.clear();
    runOnUiThread(new Runnable() {
      @Override
      public void run() {

        mAdapter.notifyDataSetChanged();
      }
    });
    setupActivity(intent);
  }

  private void setupActivity(Intent intent) {

    Bundle bundle = intent.getExtras();

    if (bundle != null) {
      forStickers = bundle.getBoolean("getStickers", false);
    }
    TextView tv = (TextView) findViewById(R.id.title);
    if (forStickers) {
      tv.setText(getResources().getString(R.string.stickers));
    } else {
      tv.setText(getResources().getString(R.string.gif));
    }
    GridLayoutManager mGridLayoutManager;
      if (forStickers) {
          mGridLayoutManager = new GridLayoutManager(this, 3);
      } else {
          mGridLayoutManager = new GridLayoutManager(this, 2);
      }

    GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        return 1;
      }
    };

    mGridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
    recyclerView.setLayoutManager(mGridLayoutManager);



    /*
     * Getting the trending gif.
     *
     */
    getGiphy("");
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

  private void minimizeCallScreen(JSONObject obj) {
    try {
      Intent intent = new Intent(SelectGIF.this, ChatMessageScreen.class);

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

  @Subscribe
  public void getMessage(JSONObject object) {
    try {
      if (object.getString("eventName").equals("callMinimized")) {

        minimizeCallScreen(object);
      }
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