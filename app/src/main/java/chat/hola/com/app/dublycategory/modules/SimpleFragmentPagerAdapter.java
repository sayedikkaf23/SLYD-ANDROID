package chat.hola.com.app.dublycategory.modules;

import android.content.Context;

import com.ezcall.android.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import chat.hola.com.app.dublycategory.fagments.DubFragment;
import chat.hola.com.app.dublycategory.favourite.DubFavFragment;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/19/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private int position;
//    private String call;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm){//}, String call) {
        super(fm);
        mContext = context;
//        this.call = call;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        DubFragment dubFragment = new DubFragment();
        //dubFragment.setCall(call);
        dubFragment.getPosition(position);

        DubFavFragment dubFavouriteFragment = new DubFavFragment();
        //  dubFavouriteFragment.setCall(call);
        dubFavouriteFragment.getPosition(position);

        switch (position) {
            case 0:
                return dubFragment;
            case 1:
                return dubFavouriteFragment;
            default:
                return dubFragment;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_hot_songs);
            case 1:
                return mContext.getString(R.string.tab_my_favs);
            default:
                return null;
        }
    }

    public void setPosition(int position) {
        this.position = position;
        getItem(position);
    }
}
