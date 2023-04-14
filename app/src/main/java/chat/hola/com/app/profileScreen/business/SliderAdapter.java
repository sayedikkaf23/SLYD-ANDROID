package chat.hola.com.app.profileScreen.business;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>SliderAdapter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 25 June 2019
 */
public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<OnBoardData> data;
    private TypefaceManager typefaceManager;

    public SliderAdapter(Context context, List<OnBoardData> data, TypefaceManager typefaceManager) {
        this.context = context;
        this.data = data;
        this.typefaceManager = typefaceManager;
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
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.business_item_slider, null);
        OnBoardData value = data.get(position);

        //initialize the views
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);

        //set fonts
        title.setTypeface(typefaceManager.getSemiboldFont());
        description.setTypeface(typefaceManager.getRegularFont());

        //set data
        image.setImageResource(value.getImage());
        title.setText(value.getTitle());
        description.setText(value.getDescription());

        //add view to view pager
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
