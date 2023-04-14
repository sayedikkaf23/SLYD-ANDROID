package chat.hola.com.app.welcomeScreen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.authentication.login.LoginActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.onboarding.ClickListner;
import chat.hola.com.app.onboarding.SliderAdapter;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.webScreen.WebActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class WelcomeActivity extends DaggerAppCompatActivity implements ClickListner {


    @Inject
    TypefaceManager typefaceManager;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.bottom_bar)
    LinearLayout bottom_bar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    TabLayout indicator;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.btnGuest)
    Button btnGuest;

    private Unbinder unbinder;
    private List<Integer> color;
    private List<Integer> image;
    private List<String> title;
    private List<String> description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        fullScreenFrame();

        unbinder = ButterKnife.bind(this);

       // Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.welcome);
        //MediaController media_control = new MediaController(this);
        //videoView.setMediaController(media_control);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);

      //  videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        color = new ArrayList<>();
        color.add(R.color.onboarding_color1);
        color.add(R.color.onboarding_color2);

        image = new ArrayList<>();
        image.add(R.drawable.onboarding1);
        image.add(R.drawable.onboarding2);

        title = new ArrayList<>();
        title.add(getString(R.string.onboarding_title1));
        title.add(getString(R.string.onboarding_title2));

        description = new ArrayList<>();
        description.add(getString(R.string.onboarding_description1));
        description.add(getString(R.string.onboarding_description2));

        viewPager.setAdapter(new SliderAdapter(this, color, image, title, description, typefaceManager, this, false));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        mSetTypeFaces();
        bottom_bar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.tanslat));
    }

    public void fullScreenFrame(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    public void next(boolean isLast) {
        startActivity(new Intent(this, DiscoverActivity.class).putExtra("caller", "SaveProfile"));
        finish();
    }



    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            WelcomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager != null) {
                        if (viewPager.getCurrentItem() < color.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                }
            });
        }
    }

    private void mSetTypeFaces() {
        btnLogin.setTypeface(typefaceManager.getSemiboldFont());
        btnSignUp.setTypeface(typefaceManager.getSemiboldFont());
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        Intent intent2 = new Intent(this, LoginActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent2);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.btnSignUp)
    public void signup() {
        Intent intent = new Intent(WelcomeActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.privacyPolicyUrl));
        bundle.putString("title", getResources().getString(R.string.privacyPolicy));
        bundle.putString("action", "accept");
        startActivity(intent.putExtra("url_data", bundle));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.btnGuest)
    public void continueAsGuest(){
        btnGuest.setEnabled(false);
        Intent i = new Intent(this, LandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        supportFinishAfterTransition();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

}
