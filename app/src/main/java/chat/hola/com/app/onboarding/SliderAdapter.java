package chat.hola.com.app.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import chat.hola.com.app.Utilities.TypefaceManager;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<Integer> colors;
    private List<Integer> images;
    private List<String> titles;
    private List<String> descriptions;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private boolean isVisible;

    public SliderAdapter(Context context, List<Integer> color, List<Integer> image, List<String> title, List<String> description, TypefaceManager typefaceManager, ClickListner clickListner, boolean isVisible) {
        this.context = context;
        this.colors = color;
        this.images = image;
        this.titles = title;
        this.descriptions = description;
        this.typefaceManager = typefaceManager;
        this.clickListner = clickListner;
        this.isVisible = isVisible;
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_onboard, null);

        TextView title = view.findViewById(R.id.tvTitle);
        title.setTypeface(typefaceManager.getBoldFont());
        TextView description = view.findViewById(R.id.tvDescription);
        description.setTypeface(typefaceManager.getSemiboldFont());
        RelativeLayout root = view.findViewById(R.id.root);
        ImageView image = view.findViewById(R.id.ivImage);

//        Button next = view.findViewById(R.id.btnNext);
//        next.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//        next.setText(position < colors.size() ? R.string.skip : R.string.enter);
//        next.setOnClickListener(v -> clickListner.next(position == colors.size()));

        image.setImageResource(images.get(position));
        title.setText(titles.get(position));
        description.setText(descriptions.get(position));
        root.setBackgroundColor(context.getResources().getColor(colors.get(position)));

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
