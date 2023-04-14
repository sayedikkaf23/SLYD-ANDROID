package io.isometrik.groupstreaming.ui.gifts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.utils.MessageTypeEnum;
import io.isometrik.groupstreaming.ui.utils.RecyclerItemClickListener;
import java.util.ArrayList;

/**
 * Bottomsheet dialog fragment to allow selecting a gift{@link GiftsModel} from a gift
 * category{@link GiftCategoryModel} and send it using a
 * callback method in interface
 * GiftsActionCallback{@link GiftsActionCallback}
 *
 * @see GiftsActionCallback
 * @see GiftCategoryModel
 * @see GiftsModel
 */
public class GiftsFragment extends BottomSheetDialogFragment {

  public static final String TAG = "GiftsFragment";

  private View view;
  private Activity activity;
  private boolean giftsAvailable;

  private ArrayList<GiftCategoryModel> giftsCategories;
  private ArrayList<GiftsModel> gifts;
  private GiftsAdapter giftsAdapter;
  private GiftsActionCallback giftsActionCallback;
  @BindView(R2.id.rvGiftCategories)
  RecyclerView rvGiftCategories;
  @BindView(R2.id.rvGifts)
  RecyclerView rvGifts;
  @BindView(R2.id.tvNoGifts)
  TextView tvNoGifts;

  public GiftsFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_gifts, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);

    giftsCategories = new ArrayList<>();
    gifts = new ArrayList<>();
    initializeGiftUIComponents();

    if (!giftsAvailable) {
      Toast.makeText(activity, R.string.ism_no_gifts, Toast.LENGTH_SHORT).show();
    }

    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    activity = null;
  }

  /**
   * Initialize gift's UI components to show overlay view of gifts received as well as to enable
   * sending of gifts and to control the visibilty of the gift's view
   */
  @SuppressWarnings("All")
  private void initializeGiftUIComponents() {
    giftsCategories = GiftsProvider.getGifts();
    if (giftsCategories.size() > 0) {
      gifts.addAll(giftsCategories.get(0).getGifts());
    }
    giftsAvailable = true;
    GiftCategoriesAdapter giftCategoriesAdapter =
        new GiftCategoriesAdapter(giftsCategories, activity);

    rvGiftCategories.setLayoutManager(
        new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
    rvGiftCategories.setAdapter(giftCategoriesAdapter);
    rvGiftCategories.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, rvGiftCategories,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (position >= 0) {

                  ArrayList<GiftsModel> giftsData = giftsCategories.get(position).getGifts();

                  if (giftsData.size() > 0) {

                    tvNoGifts.setVisibility(View.GONE);
                    rvGifts.setVisibility(View.VISIBLE);
                    gifts.clear();
                    gifts.addAll(giftsData);

                    giftsAdapter.notifyDataSetChanged();
                  } else {

                    rvGifts.setVisibility(View.GONE);
                    tvNoGifts.setVisibility(View.VISIBLE);
                  }
                }

                for (int i = 0; i < giftsCategories.size(); i++) {
                  GiftCategoryModel giftCategoryModel = giftsCategories.get(i);
                  giftCategoryModel.setSelected((i == position));
                }
                giftCategoriesAdapter.notifyDataSetChanged();
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }

        ));

    giftsAdapter = new GiftsAdapter(gifts, activity);

    rvGifts.setLayoutManager(new GridLayoutManager(activity, 4));
    rvGifts.setNestedScrollingEnabled(true);
    rvGifts.setAdapter(giftsAdapter);
    rvGifts.addOnItemTouchListener(new RecyclerItemClickListener(activity, rvGifts,
        new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, final int position) {
            if (position >= 0) {
              GiftsModel giftsModel = gifts.get(position);

              giftsActionCallback.sendGift(giftsModel.getMessage(),
                  MessageTypeEnum.GiftMessage.getValue(), giftsModel.getCoinValue(),
                  giftsModel.getGiftName());
            }
          }

          @Override
          public void onItemLongClick(View view, int position) {
          }
        }

    ));

    //To allow scroll on gift's recyclerview
    rvGifts.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    rvGiftCategories.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });
  }

  public void updateParameters(GiftsActionCallback giftsActionCallback) {
    this.giftsActionCallback = giftsActionCallback;
  }
}
