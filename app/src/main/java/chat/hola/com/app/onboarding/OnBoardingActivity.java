package chat.hola.com.app.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import chat.hola.com.app.Activities.SelectLoginActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;

public class OnBoardingActivity extends AppCompatActivity implements ClickListner {

    private ViewPager viewPager;
    private TabLayout indicator;

    private List<Integer> color;
    private List<Integer> image;
    private List<String> title;
    private List<String> description;
    private TypefaceManager typefaceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        typefaceManager = new TypefaceManager(this);

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);

        color = new ArrayList<>();
        color.add(R.color.onboarding_color1);
        color.add(R.color.onboarding_color2);
        color.add(R.color.onboarding_color2);

        image = new ArrayList<>();
        image.add(R.drawable.onboardingbg1);
        image.add(R.drawable.onboardingbg2);
        image.add(R.drawable.onboardingbg3);

        title = new ArrayList<>();
        title.add(getString(R.string.onboarding_title1));
        title.add(getString(R.string.onboarding_title2));
        title.add(getString(R.string.onboarding_title3));

        description = new ArrayList<>();
        description.add(getString(R.string.onboarding_description1));
        description.add(getString(R.string.onboarding_description2));
        description.add(getString(R.string.onboarding_description3));

        viewPager.setAdapter(new SliderAdapter(this, color, image, title, description, typefaceManager, this, true));
        indicator.setupWithViewPager(viewPager, true);

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view) {
                                                               if (viewPager.getCurrentItem() < color.size() - 1) {
                                                                   viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                                               } else {

                                                                   Intent intent = new Intent(OnBoardingActivity.this, SelectLoginActivity.class);
                                                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                   SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OnBoardingActivity.this);
                                                                   prefs.edit().putBoolean("isOnBoardingDone", true).commit();
                                                                   startActivity(intent);

                                                                   finish();
                                                               }
                                                           }
                                                       }
        );
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void next(boolean isLast) {
        startActivity(new Intent(this, DiscoverActivity.class).putExtra("caller", "SaveProfile"));
        finish();
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            OnBoardingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < color.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
