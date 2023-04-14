package chat.hola.com.app.ecom.pdp.imagezoom;

import static chat.hola.com.app.Utilities.Constants.REVIEW_IMAGE_WIDTH;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.viewpager.widget.PagerAdapter;
import chat.hola.com.app.Utilities.customimageview.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ezcall.android.R;
import com.kotlintestgradle.model.ecom.common.ImageData;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/*
 * Purpose – This class Holds UI all the images .
 * @author 3Embed
 * Created on Feb 27, 2020
 * Modified on
 */
public class ZoomSlider extends PagerAdapter {
  private ArrayList<ImageData> mImageData;
  private LayoutInflater mInflater;
  private Activity mContext;

  ZoomSlider(Activity context, ArrayList<ImageData> images) {
    this.mContext = context;
    this.mImageData = images;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getCount() {
    return mImageData.size();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup view, int position) {
    View imageLayout = mInflater.inflate(R.layout.single_image_row, view, false);
    assert imageLayout != null;
    final PhotoView imageView = imageLayout.findViewById(R.id.imageIv);
    String imageUrl="";
    imageUrl = mImageData.get(position).getExtraLarge()==null?mImageData.get(position).getImage():mImageData.get(position).getExtraLarge();

    if (!TextUtils.isEmpty(imageUrl)) {
      Glide.with(mContext).load(imageUrl.trim())
          .asBitmap()
          .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
          into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
              imageView.setImageBitmap(resource);
            }
          });
    }
    view.addView(imageLayout, ZERO);
    return imageLayout;
  }

  @Override
  public boolean isViewFromObject(View view, @NotNull Object object) {
    return view.equals(object);
  }
}
