package chat.hola.com.app.profileScreen;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ankit on 22/2/18.
 */


public class ProfilePageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragList = new ArrayList<>();
    private ArrayList<String> fragName = new ArrayList<>();

    public ProfilePageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String name, String fragmentName, String userId,String userName) {
        Bundle bundle = new Bundle();
        bundle.putString("name", fragmentName);
        bundle.putString("userId", userId);
        bundle.putString("userName",userName);
        fragment.setArguments(bundle);

        fragList.add(fragment);
        fragName.add(name);
    }


    //
//    public void addFragment(Fragment fragment, String name, String fragmentName) {
//        Bundle bundle = new Bundle();
//        bundle.putString("name", fragmentName);
//        fragment.setArguments(bundle);
//
//        fragList.add(fragment);
//        fragName.add(name);
//    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (fragName != null && !fragName.isEmpty())
            return fragName.get(position);
        return super.getPageTitle(position);
    }
}