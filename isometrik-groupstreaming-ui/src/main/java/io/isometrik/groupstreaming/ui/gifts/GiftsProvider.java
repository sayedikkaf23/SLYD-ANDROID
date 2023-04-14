package io.isometrik.groupstreaming.ui.gifts;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

/**
 * Helper class to add gifts{@link GiftsModel} in various categories{@link GiftCategoryModel}.
 *
 * @see GiftsModel
 * @see GiftCategoryModel
 */
public class GiftsProvider {

  /**
   * Gets gifts.
   *
   * @return the gifts
   */
  public static ArrayList<GiftCategoryModel> getGifts() {

    GiftsModel giftsModel;
    ArrayList<GiftsModel> vipGiftsModels = new ArrayList<>();

    ArrayList<GiftCategoryModel> giftCategoryModels = new ArrayList<>();

    ArrayList<GiftsModel> classicGiftsModels = new ArrayList<>();

    for (int i = 0; i < 16; i++) {

      switch (i) {

        case 0:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE1.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 1:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE2.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 2:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE3.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 3:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE4.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 4:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE5.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
        case 5:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE6.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 6:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE7.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 7:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE8.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 8:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE9.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 9:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE10.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
        case 10:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE11.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 11:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE12.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 12:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE13.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 13:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE14.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 14:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE15.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        default:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_CLASSIC_URL_IMAGE16.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
      }

      classicGiftsModels.add(giftsModel);
    }

    giftCategoryModels.add(new GiftCategoryModel(GiftCategoryNamesEnum.Classic.getValue(),
        GiftCategoryAssetsEnum.Classic.getValue(), classicGiftsModels, true));

    for (int i = 0; i < 5; i++) {

      switch (i) {

        case 0:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_VIP_URL_GIF1.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 1:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_VIP_URL_GIF2.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 2:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_VIP_URL_GIF3.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 3:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_VIP_URL_GIF4.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        default:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_VIP_URL_GIF5.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
      }

      vipGiftsModels.add(giftsModel);
    }

    giftCategoryModels.add(new GiftCategoryModel(GiftCategoryNamesEnum.Vip.getValue(),
        GiftCategoryAssetsEnum.Vip.getValue(), vipGiftsModels, false));

    ArrayList<GiftsModel> moodsGiftsModels = new ArrayList<>();

    for (int i = 0; i < 8; i++) {

      switch (i) {

        case 0:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE1.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 1:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE2.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 2:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE3.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 3:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE4.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 4:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE5.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
        case 5:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE6.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 6:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE7.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        default:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_MOODS_URL_IMAGE8.getValue(), i, IsometrikUiSdk
              .getInstance()
              .getContext()
              .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
      }

      moodsGiftsModels.add(giftsModel);
    }

    giftCategoryModels.add(new GiftCategoryModel(GiftCategoryNamesEnum.Moods.getValue(),
        GiftCategoryAssetsEnum.Moods.getValue(), moodsGiftsModels, false));

    ArrayList<GiftsModel> collectiblesGiftsModels = new ArrayList<>();

    for (int i = 0; i < 8; i++) {

      switch (i) {

        case 0:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE1.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 1:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE2.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 2:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE3.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 3:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE4.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        case 4:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE5.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
        case 5:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE6.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));
          break;

        case 6:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE7.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;

        default:
          giftsModel = new GiftsModel(GiftsEnum.GIFTS_COLLECTIBLES_URL_IMAGE8.getValue(), i,
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_gift_name, String.valueOf(i)));

          break;
      }

      collectiblesGiftsModels.add(giftsModel);
    }

    giftCategoryModels.add(new GiftCategoryModel(GiftCategoryNamesEnum.Collectibles.getValue(),
        GiftCategoryAssetsEnum.Collectibles.getValue(), collectiblesGiftsModels, false));

    return giftCategoryModels;
  }
}
