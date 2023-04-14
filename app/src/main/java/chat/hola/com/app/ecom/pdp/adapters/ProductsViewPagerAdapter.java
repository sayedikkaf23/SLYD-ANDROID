package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.MOBILE_IMAGES;
import static chat.hola.com.app.Utilities.Constants.POSITION;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_IMAGE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.google.android.gms.common.Scopes.PROFILE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
//import chat.hola.com.app.ecom.pdp.imagezoom.ZoomImageAct;
import chat.hola.com.app.ecom.pdp.imagezoom.ZoomImageAct;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemPdpProductsViewPagerListBinding;
import com.kotlintestgradle.model.ecom.common.ImageData;
import java.util.ArrayList;

/**
 * adapter class for products
 */
public class ProductsViewPagerAdapter extends PagerAdapter {
  private ArrayList<ImageData> mImageDataArrayList;
  private Activity mContext;

  /**
   * constructor for this products view pager adapter
   *
   * @param context activity reference for the pdp activity.
   * @param images  images arrayList.
   */
  public ProductsViewPagerAdapter(Activity context, ArrayList<ImageData> images) {
    this.mContext = context;
    this.mImageDataArrayList = images;
  }

  @Override
  public int getCount() {
    return mImageDataArrayList != null ? mImageDataArrayList.size() : ZERO;
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, final int position) {
    ItemPdpProductsViewPagerListBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(container.getContext()),
        R.layout.item_pdp_products_view_pager_list, container, false);
    String imageUrl = mImageDataArrayList.get(position).getMedium();
    Utilities.printLog("exe" + imageUrl);
    if (imageUrl != null && !"".equals(imageUrl)) {
      Glide.with(mContext).load(imageUrl.trim())
          .asBitmap()
          .into(new BitmapImageViewTarget(binding.ivViewPager) {
            @Override
            protected void setResource(Bitmap resource) {
              binding.ivViewPager.setImageBitmap(resource);
            }
          });
    }
    binding.ivViewPager.setOnClickListener(v -> {
      if (mContext instanceof ProductDetailsActivity) {
        Intent intent = new Intent(mContext, ZoomImageAct.class);
        intent.putExtra(PRODUCT_IMAGE, imageUrl);
        intent.putExtra(MOBILE_IMAGES, mImageDataArrayList);
        intent.putExtra(POSITION, position);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            mContext, binding.ivViewPager, PROFILE);
        mContext.startActivity(intent, options.toBundle());
      }
    });
    ViewPager vp = (ViewPager) container;
    vp.addView(binding.getRoot(), ZERO);
    return binding.getRoot();
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    ViewPager vp = (ViewPager) container;
    View view = (View) object;
    vp.removeView(view);
  }
}