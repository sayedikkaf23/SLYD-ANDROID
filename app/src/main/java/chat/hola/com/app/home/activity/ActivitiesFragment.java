package chat.hola.com.app.home.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.activity.followingTab.FollowingFrag;
import chat.hola.com.app.home.activity.youTab.YouFrag;
import dagger.android.support.DaggerFragment;

/**
 * <h>DiscoverActivity.class</h>
 * <p> This fragment has two tabs {@link FollowingFrag} and {@link YouFrag}
 * which is handle by {@link ActivitiesPageAdapter}.</p>
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class ActivitiesFragment extends DaggerFragment {

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    FollowingFrag followingFrag;
    @Inject
    YouFrag youFrag;
    @Inject
    LandingActivity landingPageActivity;

    private Unbinder unbinder;
    @BindView(R.id.viewPagerActivities)
    ViewPager mViewPager;
    @BindView(R.id.tabLayoutActivities)
    TabLayout mTabLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Inject
    public ActivitiesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        ActivitiesPageAdapter fragmentPageAdapter = new ActivitiesPageAdapter(getChildFragmentManager());
       try {
           fragmentPageAdapter.addFragment(followingFrag, "Follow");
           fragmentPageAdapter.addFragment(youFrag, "YOU");
       }catch(IllegalStateException e){e.printStackTrace();}
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setAdapter(fragmentPageAdapter);
        mViewPager.setCurrentItem(1);
        tabSetup();
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
//        ((LandingActivity)getActivity()).setTitle("Activities",new TypefaceManager(getActivity()).getSemiboldFont());
        return rootView;
    }

    @OnClick(R.id.ibBack)
    public void back() {
        getActivity().onBackPressed();
    }

    private void tabSetup() {
        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_textview, null);
        tabOne.setTypeface(typefaceManager.getSemiboldFont());
        tabOne.setText(getString(R.string.following));
        mTabLayout.getTabAt(0).setCustomView(tabOne);
        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_textview, null);
        tabTwo.setTypeface(typefaceManager.getSemiboldFont());
        tabTwo.setText(getString(R.string.you));
        mTabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    @Override
    public void onResume() {
        super.onResume();
        landingPageActivity.hideActionBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }
}
