package chat.hola.com.app.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.AppController;
import com.ezcall.android.R;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.TypefaceManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by moda on 19/08/17.
 */

public class Terms extends DaggerAppCompatActivity {

    @Inject
    Bus bus;
    @Inject
    TypefaceManager typefaceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        WebView webView = (WebView) findViewById(R.id.webView);

        webView.requestFocus(View.FOCUS_DOWN);

        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setLoadWithOverviewMode(true);
        websettings.setUseWideViewPort(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setPluginState(WebSettings.PluginState.ON);

        // Show a progress dialog to the user
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.progress_dialog_loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

                pDialog.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(Terms.this);
                builder.setMessage("SSL Certificate is not valid for url requested:");
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {


                pDialog.dismiss();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                pDialog.dismiss();
                return true;
            }
        });

        // Load the authorization URL into the webView
        webView.loadUrl(ApiOnServer.TERMS);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         onBackPressed();

                                     }
                                 }
        );

        TextView title = (TextView) findViewById(R.id.title);

        title.setTypeface(typefaceManager.getRegularFont(), Typeface.BOLD);
        bus.register(this);
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(Terms.this, ChatMessageScreen.class);

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

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
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

}
