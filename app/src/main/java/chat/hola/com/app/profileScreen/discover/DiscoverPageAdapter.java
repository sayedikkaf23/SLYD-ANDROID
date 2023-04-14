package chat.hola.com.app.profileScreen.discover;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * <h>DiscoverPageAdapter</h>
 * @author 3Embed.
 * @since 02/03/18.
 */

class DiscoverPageAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fraglist = new ArrayList<>();
    ArrayList<String> fragName = new ArrayList<>();
    public DiscoverPageAdapter(FragmentManager fm) {
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
            return fragName.get(position).toUpperCase();
        return super.getPageTitle(position);
    }
}
