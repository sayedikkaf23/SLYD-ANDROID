package chat.hola.com.app.home.connect;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class ConnectPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public ConnectPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String frag1) {

        mFragmentList.add(fragment);
        mFragmentTitleList.add(frag1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
