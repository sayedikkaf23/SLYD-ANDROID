package chat.hola.com.app.profileScreen.business;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.business.form.BusinessProfileFormActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>StartBusinessProfileActivity</h1>
 * It includes on board screens  of business profile.
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 14 August 2019
 */
public class StartBusinessProfileActivity extends DaggerAppCompatActivity {

    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.indicator)
    TabLayout indicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnCreateAccount)
    Button btnCreateAccount;

    private List<OnBoardData> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_start_activity);
        ButterKnife.bind(this);

        //add data
        dataList = new ArrayList<>();
        setData();

        //setup adapter
        viewPager.setAdapter(new SliderAdapter(this, dataList, typefaceManager));
        indicator.setupWithViewPager(viewPager, true);
        btnCreateAccount.setTypeface(typefaceManager.getSemiboldFont());
        title.setTypeface(typefaceManager.getSemiboldFont());

        //slide class
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnCreateAccount)
    public void form() {
        startActivity(new Intent(this, BusinessProfileFormActivity.class));
        finish();
    }

    //add static data for on board
    private void setData() {
        dataList.add(new OnBoardData(getString(R.string.business_title1), getString(R.string.business_description1), R.drawable.business_onboard1));
        dataList.add(new OnBoardData(getString(R.string.business_title2), getString(R.string.business_description2), R.drawable.business_onboard2));
        dataList.add(new OnBoardData(getString(R.string.business_title3), getString(R.string.business_description3), R.drawable.business_onboard3));
    }

    //class is use for automatic slide screens
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            StartBusinessProfileActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < dataList.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
