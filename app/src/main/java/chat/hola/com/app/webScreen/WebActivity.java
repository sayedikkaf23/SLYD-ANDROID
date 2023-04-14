package chat.hola.com.app.webScreen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.authentication.signup.SignUpActivity;
import com.ezcall.android.R;
import com.google.firebase.auth.FirebaseUser;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.File;
import java.util.Date;
import javax.inject.Inject;

/**
 * Created by ankit on 15/3/18.
 */

public class WebActivity extends DaggerAppCompatActivity {

  @BindView(R.id.webView)
  WebView webView;

  @BindView(R.id.pbProgress)
  ProgressBar pbLoadProgress;

  @BindView(R.id.tvError)
  TextView tvError;

  @BindView(R.id.tvTitle)
  TextView tvTitle;

  @BindView(R.id.btnAccept)
  Button btnAccept;


  private Unbinder unbinder;
  final Activity activity = this;
  private String url = null;
  private String title = null;

  private String countryCode = "", country = "", phoneNumber = "";

  @Inject
  TypefaceManager typefaceManager;
  private boolean clear;
  private Bundle bundle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    unbinder = ButterKnife.bind(this);
    //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    bundle = getIntent().getBundleExtra("url_data");
    if (bundle == null) {
      url = getIntent().getStringExtra("url");
      title = getIntent().getStringExtra("title");
    } else {
      url = bundle.getString("url");
      title = bundle.getString("title");
      country = bundle.getString("country");
      countryCode = bundle.getString("countryCode");
      phoneNumber = bundle.getString("phoneNumber");
      String action = bundle.getString("action");
      if (action != null && action.equals("accept")) {
        btnAccept.setVisibility(View.VISIBLE);
      }
    }

    clear = getIntent().getBooleanExtra("clear", false);

    tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    tvTitle.setText(title);
    initWebview();



  }

  @OnClick(R.id.btnAccept)
  public void accept() {
    Intent intent = new Intent(this, SignUpActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    intent.putExtra("fireBaseUser", (FirebaseUser) bundle.getParcelable("fireBaseUser"));
    if (bundle.getString("loginType") != null) {
      intent.putExtra("loginType", bundle.getString("loginType"));
    }

    if (countryCode != null && !countryCode.isEmpty()) {
      intent.putExtra("countryCode", countryCode);
    }
    if (country != null && !country.isEmpty()) {
      intent.putExtra("country", country);
    }
    if (phoneNumber != null && !phoneNumber.isEmpty()) {
      intent.putExtra("phoneNumber", phoneNumber);
    }

    startActivity(intent);
  }

  /**
   * <p>
   * Enable javaScript and set WebChromeClient and webViewClient
   * to webView.
   * </P>
   */
  @SuppressLint("SetJavaScriptEnabled")
  private void initWebview() {

    if (clear) {
      clearCache(this, 0);
    }

    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setAppCacheEnabled(false);
    webView.loadUrl(url);

    webView.setWebChromeClient(new WebChromeClient() {
      public void onProgressChanged(WebView view, int progress) {
        if (pbLoadProgress != null) {
          pbLoadProgress.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
        }
      }
    });

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        /*view.loadUrl(url);
        return true;*/
        if (url.startsWith("mailto:"))
        {
          startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
          return true;
        }
        return false;

      }
    });

  }



  /*
   * Delete the files older than numDays days from the application cache
   * 0 means all files.
   */
  public static void clearCache(final Context context, final int numDays) {
    Log.i("Cache",
        String.format("Starting cache prune, deleting files older than %d days", numDays));
    int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
    Log.i("Cache", String.format("Cache pruning completed, %d files deleted", numDeletedFiles));
  }

  //helper method for clearCache() , recursive
//returns number of deleted files
  static int clearCacheFolder(final File dir, final int numDays) {

    int deletedFiles = 0;
    if (dir != null && dir.isDirectory()) {
      try {
        for (File child : dir.listFiles()) {

          //first delete subdirectories recursively
          if (child.isDirectory()) {
            deletedFiles += clearCacheFolder(child, numDays);
          }

          //then delete the files and subdirectories in this dir
          //only empty directories can be deleted, so subdirs have been done first
          if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
            if (child.delete()) {
              deletedFiles++;
            }
          }
        }
      } catch (Exception e) {
        Log.e("Cache", String.format("Failed to clean the cache, error %s", e.getMessage()));
      }
    }
    return deletedFiles;
  }


  @Override
  protected void onResume() {
    super.onResume();
    webView.reload();
  }

  @Override
  public void onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  @OnClick(R.id.ivBack)
  public void back() {
    onBackPressed();
  }



}