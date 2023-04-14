package chat.hola.com.app.preview.model;

import android.content.Context;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;

/**
 * Created by DELL on 4/24/2018.
 */

public class SliderAdapter extends FragmentPagerAdapter {

    private Context context;
    private ArrayList<Data> data;

    public SliderAdapter(FragmentManager manager,Context context, ArrayList<Data> data) {
        super(manager);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageURI(Uri.parse(Utilities.getModifiedImageLink(data.get(position).getImageUrl1())));//data.get(position).getImageUrl1()));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}