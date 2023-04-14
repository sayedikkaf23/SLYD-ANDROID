package chat.hola.com.app.activities_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.activity.ActivitiesPageAdapter;
import chat.hola.com.app.home.activity.followingTab.FollowingFrag;
import chat.hola.com.app.home.activity.youTab.YouFrag;
import com.ezcall.android.R;
import com.google.android.material.tabs.TabLayout;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

public class UserActivitiesActivity extends DaggerAppCompatActivity
    implements UserActivitiesContract.View {

  @Inject
  TypefaceManager typefaceManager;
  @Inject
  FollowingFrag followingFrag;
  @Inject
  YouFrag youFrag;

  @BindView(R.id.viewPagerActivities)
  ViewPager mViewPager;
  @BindView(R.id.tabLayoutActivities)
  TabLayout mTabLayout;
  @BindView(R.id.tvTitle)
  TextView tvTitle;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_activities);
    ButterKnife.bind(this);

    ActivitiesPageAdapter fragmentPageAdapter =
        new ActivitiesPageAdapter(getSupportFragmentManager());
    try {
      fragmentPageAdapter.addFragment(followingFrag, "Follow");
      fragmentPageAdapter.addFragment(youFrag, "YOU");
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
    mTabLayout.setupWithViewPager(mViewPager, true);
    mViewPager.setAdapter(fragmentPageAdapter);
    mViewPager.setCurrentItem(1);
    tabSetup();
    tvTitle.setTypeface(typefaceManager.getSemiboldFont());
  }

  @OnClick(R.id.ibBack)
  public void back() {
    super.onBackPressed();
  }

  private void tabSetup() {
    //their exist some easier way to setfont to tab
    //need to replace.
    TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_textview, null);
    tabOne.setTypeface(typefaceManager.getSemiboldFont());
    tabOne.setText(getString(R.string.following));
    mTabLayout.getTabAt(0).setCustomView(tabOne);
    TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_textview, null);
    tabTwo.setTypeface(typefaceManager.getSemiboldFont());
    tabTwo.setText(getString(R.string.you));
    mTabLayout.getTabAt(1).setCustomView(tabTwo);
  }

  @Override
  public void showMessage(String msg, int msgId) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
  public void reload() {

  }
}
