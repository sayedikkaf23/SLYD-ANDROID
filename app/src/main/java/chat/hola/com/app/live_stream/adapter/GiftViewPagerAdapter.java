package chat.hola.com.app.live_stream.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.live_stream.gift.GiftFragment;
import chat.hola.com.app.models.GiftCategories;

public class GiftViewPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private Fragment fragment = null;
    private List<GiftCategories.Data.Category> categories;
    private LiveStreamService liveStreamService;
    private GiftAdapter.GiftListener listener;

    public GiftViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<GiftCategories.Data.Category> categories, LiveStreamService liveStreamService, GiftAdapter.GiftListener listener) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
        this.categories = categories;
        this.liveStreamService = liveStreamService;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        for (int i = 0; i < mNumOfTabs; i++) {
            if (i == position) {
                fragment = GiftFragment.newInstance(i, categories.get(i), liveStreamService, listener);
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getTitle();
    }
}
