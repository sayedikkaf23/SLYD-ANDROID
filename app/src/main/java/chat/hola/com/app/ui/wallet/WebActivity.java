package chat.hola.com.app.ui.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.base.BaseActivity;

/**
 * <h1>WebActivity</h1>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 29 Jan 2020
 */
public class WebActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pbProgress)
    ProgressBar pbLoadProgress;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private Unbinder unbinder;
    final Activity activity = this;
    private String url = null;
    private String title = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_web);
        unbinder = ButterKnife.bind(this);
        String amount = getIntent().getStringExtra("amount");
        String userId = getIntent().getStringExtra("userId");
        String txn_ref = userId + "_" + System.currentTimeMillis() / 1000;

        url = "http://40.68.161.154/interswitch.php?&amount=" + amount + "&currency=566&txn_ref=" + txn_ref + "&cust_id=" + userId;
        String name = getIntent().getStringExtra("name");

        if (name == null || name.isEmpty())
            name = "Recharge Wallet";
        tvTitle.setText(name);
        initWebview();
    }

    @OnClick(R.id.ivBack)
    public void back() {
        super.onBackPressed();
    }

    /**
     * <p>
     * Enable javaScript and set WebChromeClient and webViewClient
     * to webView.
     * </P>
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebview() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("/images")){
                    Toast.makeText(WebActivity.this, getString(R.string.recharge_success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (pbLoadProgress != null) {
                    pbLoadProgress.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
                }
            }

        });
    }
}
