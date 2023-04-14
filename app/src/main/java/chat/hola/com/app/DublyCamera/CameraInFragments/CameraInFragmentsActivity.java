package chat.hola.com.app.DublyCamera.CameraInFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import com.ezcall.android.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Bus;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraInFragmentsActivity extends AppCompatActivity {
  private final int SELECT_DUB_SOUND_REQUEST = 0;

  private Bus bus = AppController.getBus();
  TypefaceManager typefaceManager;
  @BindView(R.id.close)
  RelativeLayout close;

  @BindView(R.id.gallery)
  RelativeLayout gallery;
  @BindView(R.id.next)
  RelativeLayout next;
  @BindView(R.id.tvNext)
  TextView tvNext;
  @BindView(R.id.title)
  TextView title;
  @BindView(R.id.tv)
  TextView tv;
  @BindView(R.id.bottomNavigation)
  TabLayout tabLayout;
  @BindView(R.id.viewPager)
  ViewPager viewPager;

  private int fragmentSelected;
  private VideoCaptureFragment videoCaptureFragment;

  private GalleryFragment galleryFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.camera_in_fragments);
    ButterKnife.bind(this);
    typefaceManager = new TypefaceManager(this);
    tv.setTypeface(typefaceManager.getSemiboldFont());
    title.setTypeface(typefaceManager.getSemiboldFont());
    tvNext.setTypeface(typefaceManager.getSemiboldFont());

    setupViewPager(viewPager);
    viewPager.setOffscreenPageLimit(2);
    tabLayout.setupWithViewPager(viewPager);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        fragmentSelected = position;
        updateFragmentSelectedTitle(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });

    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleFragmentSelectedClose(fragmentSelected);
      }
    });

    gallery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        galleryFragment.showBucketPicker(gallery);
      }
    });

    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (galleryFragment != null) {

          // galleryFragment.refreshRequired(true);
          galleryFragment.shareSelectedMedia();
        }
      }
    });

    bus.register(this);
    hideSystemUI();
  }

  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    galleryFragment = new GalleryFragment();
    try {
      adapter.addFragment(galleryFragment, "GALLERY");
      adapter.addFragment(new ImageCaptureFragment(), "PHOTO");
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
    videoCaptureFragment = new VideoCaptureFragment();
    try {
      adapter.addFragment(videoCaptureFragment, "VIDEO");
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }

    viewPager.setAdapter(adapter);
    changeTabsFont();
  }

  private void changeTabsFont() {
    ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
    int tabsCount = vg.getChildCount();
    for (int j = 0; j < tabsCount; j++) {
      ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
      int tabChildsCount = vgTab.getChildCount();
      for (int i = 0; i < tabChildsCount; i++) {
        View tabViewChild = vgTab.getChildAt(i);
        if (tabViewChild instanceof TextView) {
          ((TextView) tabViewChild).setTypeface(typefaceManager.getRegularFont(), Typeface.NORMAL);
          ((TextView) tabViewChild).setTextSize(getResources().getDimension(R.dimen.title));
          ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.star_black));
        }
      }
    }
  }

  /*
   * View pager adapter.*/
  private class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
      return mFragmentList.size();
    }

    void addFragment(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mFragmentTitleList.get(position);
    }
  }

  //For the camera capture view to be shown as fullscreen
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
      hideSystemUI();
    } else {

      showSystemUI();
    }
  }

  private void hideSystemUI() {
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        // Set the content to appear under the system bars so that the
        // content doesn't resize when the system bars hide and show.
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // Hide the nav bar and status bar
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN);
  }

  //
  //    // Shows the system bars by removing all the flags
  //// except for the ones that make the content appear under the system bars.
  private void showSystemUI() {
    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }

  @Override
  protected void onDestroy() {
    //Have to delete files no matter,video fragment is visible or not

    if (videoCaptureFragment != null) {
      try {

        videoCaptureFragment.deleteFilesOnExit();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    bus.unregister(this);
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SELECT_DUB_SOUND_REQUEST) {
      try {
        JSONObject obj = new JSONObject();
        obj.put("eventName", "selectDubSoundResult");
        obj.put("requestCode", SELECT_DUB_SOUND_REQUEST);
        if (resultCode == Activity.RESULT_OK) {

          obj.put("resultCode", Activity.RESULT_OK);

          obj.put("audio", data.getStringExtra("audio"));
          obj.put("name", data.getStringExtra("name"));
          obj.put("musicId", data.getStringExtra("musicId"));
        } else if (resultCode == Activity.RESULT_CANCELED) {
          //Write your code if there's no result
          obj.put("resultCode", Activity.RESULT_CANCELED);
        }
        bus.post(obj);
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void updateFragmentSelectedTitle(int fragmentPosition) {

    switch (fragmentPosition) {

      case 0:
        gallery.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);

        break;

      case 1:

        gallery.setVisibility(View.GONE);
        next.setVisibility(View.GONE);

        title.setText(getString(R.string.photo));
        title.setVisibility(View.VISIBLE);
        break;

      case 2:
        gallery.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        title.setText(getString(R.string.Video));
        title.setVisibility(View.VISIBLE);
        break;
    }
  }

  private void handleFragmentSelectedClose(int fragmentPosition) {

    switch (fragmentPosition) {

      case 0:

        onBackPressed();
        break;

      case 1:

        onBackPressed();
        break;

      case 2:
        if (videoCaptureFragment != null) videoCaptureFragment.onBackPressed();
        break;
    }
  }

  public void updateTitle(String title) {

    tv.setText(title);
  }

  public void closeVideoFragment() {

    super.onBackPressed();
  }

  @Override
  public void onBackPressed() {

    if (fragmentSelected == 2) {//To avoid crash when timer is running
      if (videoCaptureFragment != null) videoCaptureFragment.onBackPressed();
    } else {
      super.onBackPressed();
    }
  }
}
