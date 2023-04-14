package chat.hola.com.app.poststatus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ezcall.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class PostStatusActivity extends DaggerAppCompatActivity implements PostStatusPresenterImpl.PostStatusPresenterView {

    private static final String TAG = PostStatusActivity.class.getSimpleName();
    @BindView(R.id.iV_font)
    ImageView iV_font;
    @BindView(R.id.iV_bgColor)
    ImageView iV_bgColor;
    @BindView(R.id.eT_status)
    EditText eT_status;
    @BindView(R.id.rL_main)
    RelativeLayout rl_main;
    @BindView(R.id.iV_done)
    ImageView iV_done;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    private Context mContext;

    @Inject
    PostStatusPresenterImpl.PostStatusPresent present;

    Typeface[] typefaces = {Typeface.DEFAULT, Typeface.MONOSPACE, Typeface.SANS_SERIF, Typeface.SERIF, Typeface.DEFAULT_BOLD};

    String[] typeFaceArray = {"Helvetica", "Helvetica-Bold", "Helvetica-BoldOblique", "Helvetica-Light", "Helvetica-LightOblique", "Helvetica-Oblique"};
    int typeFaceCount = 0;

    String[] colors = {"#0000ff",
            "#ff0074",
            "#0aeb7e",
            "#3c7c5c",
            "#bb4055",
            "#81c8c1",
            "#d69537"
    };
    int colorCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_status);
        ButterKnife.bind(this);

        mContext = PostStatusActivity.this;
        rl_main.setBackgroundColor(Color.parseColor(colors[colorCount]));
        eT_status.setTypeface(typefaces[typeFaceCount]);
    }

    @OnClick(R.id.iV_font)
    public void setTypeFace() {
        ++typeFaceCount;
        if (typeFaceCount >= typefaces.length)
            typeFaceCount = 0;
//                eT_status.setTypeface(typefaces[typeFaceCount]);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + typeFaceArray[typeFaceCount] + ".ttf");
        eT_status.setTypeface(typeface);
    }

    @OnClick(R.id.iV_bgColor)
    public void setBgColor() {
        ++colorCount;
        if (colorCount >= colors.length)
            colorCount = 0;

        rl_main.setBackgroundColor(Color.parseColor(colors[colorCount]));
    }

    @OnClick(R.id.iV_done)
    public void done() {
        Log.d(TAG, "done: " + colors[colorCount] + " , " + typefaces[typeFaceCount]);
        scrollView.setVisibility(View.GONE);

        // save the main layout as image in to local
        rl_main.setDrawingCacheEnabled(true);
        Bitmap bitmap = rl_main.getDrawingCache();
//        File file = new File(this.getFilesDir().getPath() + "/" + this.getResources()
//                .getString(R.string.app_name) + "/DownloadedMedia/" + "myStatus" + ".png");

       /*this.getFilesDir().getPath() + "/" + this.getResources()
                .getString(R.string.app_name) + "/DownloadedMedia/"*/

//        File file = new File("/sdcard/" + "myStatus" + ".png");
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */);

            // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, ostream);
                ostream.close();
                rl_main.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rl_main.setDrawingCacheEnabled(false);
            }
            //////////////////////////////////////////////
            present.postStatus(mContext, file.getAbsolutePath(), 3, false, colors[colorCount], eT_status.getText().toString().trim(), setFontType());
        } catch (Exception e) {

        }
    }

    private String setFontType() {
        switch (typeFaceCount) {
            case 0:
                return "default";
            case 1:
                return "monospace";
            case 2:
                return "sans_serif";
            case 3:
                return "serif";
            case 4:
                return "default_bold";

            default:
                return "default";

        }
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void onPostStatusSuccess() {
        Toast.makeText(mContext, "Post Status Successful.", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showProgress() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void reload() {

    }
}
