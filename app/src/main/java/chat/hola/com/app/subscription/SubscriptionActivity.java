package chat.hola.com.app.subscription;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivitySubscriptionBinding;

import java.util.ArrayList;

import javax.inject.Inject;

import chat.hola.com.app.subscription.active.ActiveFragment;
import chat.hola.com.app.subscription.cancelled.CancelledFragment;
import dagger.android.support.DaggerAppCompatActivity;

public class SubscriptionActivity extends DaggerAppCompatActivity {

    ActivitySubscriptionBinding mDataBinding;

    @Inject
    ActiveFragment activeFragment;
    @Inject
    CancelledFragment cancelledFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_subscription);
        initViews();
    }

    private void initViews() {
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewPager);
        mDataBinding.back.setOnClickListener(v -> onBackPressed());
        SubscriptionPageAdapter adapter = new SubscriptionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(activeFragment,"Active");
        adapter.addFragment(cancelledFragment,"Cancelled");
        mDataBinding.viewPager.setAdapter(adapter);
    }

    public class SubscriptionPageAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fraglist = new ArrayList<>();
        ArrayList<String> fragName = new ArrayList<>();
        public SubscriptionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String name){
            fraglist.add(fragment);
            fragName.add(name);
        }

        @Override
        public Fragment getItem(int position) {
            return fraglist.get(position);
        }

        @Override
        public int getCount() {
            return fraglist.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(fragName != null && !fragName.isEmpty())
                return fragName.get(position);
            return super.getPageTitle(position);
        }
    }
}