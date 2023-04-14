package io.isometrik.groupstreaming.ui.gifts;

/**
 * Enum for the gift category names.
 */
public enum GiftCategoryNamesEnum {

  Classic("Classic"),
  Vip("VIP"),
  Moods("Moods"),
  Collectibles("Collectibles");

  private final String value;

  GiftCategoryNamesEnum(String value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return this.value;
  }
}
