package io.isometrik.groupstreaming.ui.gifts;

/**
 * Interface containing callback method to send a gift in a broadcast.
 */
public interface GiftsActionCallback {

  /**
   * Send a gift in a broadcast.
   *
   * @param message the gift media url
   * @param messageType message type
   * @param coinsValue coins value of gift been sent
   * @param giftName name of the gift been sent
   */
  void sendGift(String message, int messageType, int coinsValue, String giftName);
}
