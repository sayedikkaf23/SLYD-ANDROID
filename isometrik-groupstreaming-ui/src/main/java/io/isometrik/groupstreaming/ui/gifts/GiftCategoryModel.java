package io.isometrik.groupstreaming.ui.gifts;

import java.util.ArrayList;

/**
 * The model class containing details of the gift category for the item in the gift categories list.
 */
public class GiftCategoryModel {

  private String giftCategoryName;
  private int giftCategoryImage;
  private boolean selected;
  private ArrayList<GiftsModel> gifts;

  /**
   * Instantiates a new Gift category model.
   *
   * @param giftCategoryName the gift category name
   * @param giftCategoryImage the id gift category image asset
   * @param gifts the gifts
   * @param selected the selected
   */
  GiftCategoryModel(String giftCategoryName, int giftCategoryImage, ArrayList<GiftsModel> gifts,
      boolean selected) {
    this.giftCategoryName = giftCategoryName;
    this.giftCategoryImage = giftCategoryImage;
    this.gifts = gifts;
    this.selected = selected;
  }

  /**
   * Gets gift category name.
   *
   * @return the gift category name
   */
  String getGiftCategoryName() {
    return giftCategoryName;
  }

  /**
   * Gets gifts.
   *
   * @return the gifts
   */
  public ArrayList<GiftsModel> getGifts() {
    return gifts;
  }

  /**
   * Is selected boolean.
   *
   * @return the boolean
   */
  boolean isSelected() {
    return selected;
  }

  /**
   * Sets selected.
   *
   * @param selected the selected
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * Gets gift category image.
   *
   * @return the id of the gift category image asset
   */
  public int getGiftCategoryImage() {
    return giftCategoryImage;
  }
}


