package chat.hola.com.app.home.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ankit on 22/2/18.
 */

public class ActivitiesPageAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fraglist = new ArrayList<>();
    ArrayList<String> fragName = new ArrayList<>();
    public ActivitiesPageAdapter(FragmentManager fm) {
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
