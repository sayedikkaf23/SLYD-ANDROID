package io.isometrik.groupstreaming.ui.viewers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

public class ViewersUtil {

  private AppCompatImageView viewerImageOne, viewerImageTwo, viewerImageThree, viewerImageFour,
      viewerImageFive, ivMore;

  private TextView viewersCount;
  private Context context;

  public ViewersUtil(AppCompatImageView viewerImageOne, AppCompatImageView viewerImageTwo,
      AppCompatImageView viewerImageThree, AppCompatImageView viewerImageFour,
      AppCompatImageView viewerImageFive, AppCompatImageView ivMore, TextView viewersCount,
      Context context) {
    this.viewerImageOne = viewerImageOne;
    this.viewerImageTwo = viewerImageTwo;
    this.viewerImageThree = viewerImageThree;
    this.viewerImageFour = viewerImageFour;
    this.viewerImageFive = viewerImageFive;
    this.ivMore = ivMore;
    this.viewersCount = viewersCount;
    this.context = context;
  }

  public void updateViewers(ArrayList<ViewersListModel> viewersModels) {
    int size = viewersModels.size();

    switch (size) {
      case 0: {
        viewerImageOne.setVisibility(View.GONE);
        viewerImageTwo.setVisibility(View.GONE);
        viewerImageThree.setVisibility(View.GONE);
        viewerImageFour.setVisibility(View.GONE);
        viewerImageFive.setVisibility(View.GONE);

        viewersCount.setVisibility(View.GONE);
        ivMore.setVisibility(View.INVISIBLE);
        break;
      }
      case 1: {

        setupViewerImages(viewerImageOne, viewersModels.get(0).getViewerProfilePic());
        viewerImageOne.setVisibility(View.VISIBLE);
        viewerImageTwo.setVisibility(View.GONE);
        viewerImageThree.setVisibility(View.GONE);
        viewerImageFour.setVisibility(View.GONE);
        viewerImageFive.setVisibility(View.GONE);

        viewersCount.setVisibility(View.GONE);
        ivMore.setVisibility(View.INVISIBLE);
        break;
      }
      case 2: {
        setupViewerImages(viewerImageOne, viewersModels.get(0).getViewerProfilePic());
        viewerImageOne.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageTwo, viewersModels.get(1).getViewerProfilePic());
        viewerImageTwo.setVisibility(View.VISIBLE);
        viewerImageThree.setVisibility(View.GONE);
        viewerImageFour.setVisibility(View.GONE);
        viewerImageFive.setVisibility(View.GONE);

        viewersCount.setVisibility(View.GONE);
        ivMore.setVisibility(View.INVISIBLE);
        break;
      }
      case 3: {
        setupViewerImages(viewerImageOne, viewersModels.get(0).getViewerProfilePic());
        viewerImageOne.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageTwo, viewersModels.get(1).getViewerProfilePic());
        viewerImageTwo.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageThree, viewersModels.get(2).getViewerProfilePic());
        viewerImageThree.setVisibility(View.VISIBLE);
        viewerImageFour.setVisibility(View.GONE);
        viewerImageFive.setVisibility(View.GONE);

        viewersCount.setVisibility(View.GONE);
        ivMore.setVisibility(View.INVISIBLE);
        break;
      }
      case 4: {
        setupViewerImages(viewerImageOne, viewersModels.get(0).getViewerProfilePic());
        viewerImageOne.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageTwo, viewersModels.get(1).getViewerProfilePic());
        viewerImageTwo.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageThree, viewersModels.get(2).getViewerProfilePic());
        viewerImageThree.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageFour, viewersModels.get(3).getViewerProfilePic());
        viewerImageFour.setVisibility(View.VISIBLE);
        viewerImageFive.setVisibility(View.GONE);

        viewersCount.setVisibility(View.GONE);
        ivMore.setVisibility(View.INVISIBLE);
        break;
      }
      case 5: {
        setupViewerImages(viewerImageOne, viewersModels.get(0).getViewerProfilePic());
        viewerImageOne.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageTwo, viewersModels.get(1).getViewerProfilePic());
        viewerImageTwo.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageThree, viewersModels.get(2).getViewerProfilePic());
        viewerImageThree.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageFour, viewersModels.get(3).getViewerProfilePic());
        viewerImageFour.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageFive, viewersModels.get(4).getViewerProfilePic());
        viewerImageFive.setVisibility(View.VISIBLE);

        viewersCount.setVisibility(View.GONE);
        ivMore.setVisibility(View.INVISIBLE);
        break;
      }
      default:
        setupViewerImages(viewerImageOne, viewersModels.get(0).getViewerProfilePic());
        viewerImageOne.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageTwo, viewersModels.get(1).getViewerProfilePic());
        viewerImageTwo.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageThree, viewersModels.get(2).getViewerProfilePic());
        viewerImageThree.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageFour, viewersModels.get(3).getViewerProfilePic());
        viewerImageFour.setVisibility(View.VISIBLE);
        setupViewerImages(viewerImageFive, viewersModels.get(4).getViewerProfilePic());
        viewerImageFive.setVisibility(View.VISIBLE);

        viewersCount.setVisibility(View.VISIBLE);
        viewersCount.setText(context.getString(R.string.ism_viewers_count, String.valueOf(size)));
        ivMore.setVisibility(View.VISIBLE);
    }
  }

  private void setupViewerImages(AppCompatImageView ivViewerImage, String viewerProfilePicUrl) {
    try {
      Glide.with(context)
          .load(viewerProfilePicUrl)
          .asBitmap()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(new BitmapImageViewTarget(ivViewerImage) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(context.getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              ivViewerImage.setImageDrawable(circularBitmapDrawable);
            }
          });
    } catch (IllegalArgumentException | NullPointerException e) {
      e.printStackTrace();
    }
  }
}
