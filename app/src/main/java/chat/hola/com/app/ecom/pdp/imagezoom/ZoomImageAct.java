package chat.hola.com.app.ecom.pdp.imagezoom;

import static chat.hola.com.app.Utilities.Constants.MOBILE_IMAGES;
import static chat.hola.com.app.Utilities.Constants.POSITION;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_IMAGE;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityZoomImageBinding;
import com.kotlintestgradle.model.ecom.common.ImageData;
import java.util.ArrayList;

/*
 * Purpose – This class Holds UI for image scroller .
 * @author 3Embed
 * Created on Feb 27, 2020
 * Modified on
 */
public class ZoomImageAct extends AppCompatActivity implements ImageListAdapter.OnImageClick {

  private ActivityZoomImageBinding mBinding;
  private ArrayList<ImageData> mMobileImages = new ArrayList<>();
  private int mPos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_zoom_image);
    initialize();
  }

  /**
   * This method is using to initialize basic activity resources
   */
  private void initialize() {
    if (getIntent().hasExtra(PRODUCT_IMAGE)) {
      try {
        mMobileImages = (ArrayList<ImageData>) getIntent().getSerializableExtra(MOBILE_IMAGES);
        mPos = getIntent().getIntExtra(POSITION, ZERO);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (mMobileImages != null && mMobileImages.size() > ZERO) {
      mBinding.vpImagesList.setVisibility(View.VISIBLE);
      ImageListAdapter adapter = new ImageListAdapter(mMobileImages, this);
      mBinding.rvImageList.setAdapter(adapter);
      ZoomSlider imageAdapter = new ZoomSlider(this, mMobileImages);
      mBinding.vpImagesList.setAdapter(imageAdapter);
      mBinding.vpImagesList.setCurrentItem(mPos);
      mBinding.vpImagesList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
          adapter.setSelectedIndex(position);
          adapter.notifyDataSetChanged();
          mBinding.rvImageList.smoothScrollToPosition(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
      });
    }

    mBinding.ivClose.setOnClickListener(view -> onBackPressed());
  }

  @Override
  public void onImageClickListener(int position) {

    mBinding.vpImagesList.setCurrentItem(position, false);
  }
}
