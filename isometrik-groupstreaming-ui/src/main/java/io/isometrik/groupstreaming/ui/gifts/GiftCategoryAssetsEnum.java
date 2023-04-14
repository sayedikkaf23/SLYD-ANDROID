package io.isometrik.groupstreaming.ui.gifts;

import io.isometrik.groupstreaming.ui.R;

/**
 * Enum for the gift category assets.
 */
public enum GiftCategoryAssetsEnum {

  Classic(R.drawable.ism_ic_gift_category_classic),
  Vip(R.drawable.ism_ic_gift_category_vip),
  Moods(R.drawable.ism_ic_gift_category_moods),
  Collectibles(R.drawable.ism_ic_gift_category_collectibles);

  private final int value;

  GiftCategoryAssetsEnum(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public int getValue() {
    return this.value;
  }
}
