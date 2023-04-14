package chat.hola.com.app.socialDetail;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import chat.hola.com.app.home.model.Data;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 8/13/2018.
 */
public class PostPager extends FragmentPagerAdapter {
    List<Data> list;

    public PostPager(FragmentManager fm, List<Data> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", list.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
