package io.isometrik.groupstreaming.ui.utils;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;

/**
 * The type Predefined messages util.
 */
public class PredefinedMessagesUtil {

  /**
   * Get predefined messages string [ ].
   *
   * @return the string [ ]
   */
  public static String[] getPredefinedMessages() {

    String[] stringArray = new String[9];

    String sBlush = getEmoticonByUnicode(0x1F60A);
    String sSmile = getEmoticonByUnicode(0x1F601);
    String sSmileTear = getEmoticonByUnicode(0x1F602);
    String sHappySurprised = getEmoticonByUnicode(0x1F603);
    String sHappyHeart = getEmoticonByUnicode(0x1F60D);
    String sHi = getEmoticonByUnicode(0x270B);
    String sVictory = getEmoticonByUnicode(0x270C);
    String sThumbsUp = getEmoticonByUnicode(0x1F44D);

    stringArray[0] = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_hello);
    stringArray[1] = sSmile;
    stringArray[2] = sBlush + sBlush + sBlush;
    stringArray[3] = sSmileTear + sSmileTear + sSmileTear;
    stringArray[4] = sHappySurprised + sHappySurprised + sHappySurprised;
    stringArray[5] = sHappyHeart + sHappyHeart + sHappyHeart;
    stringArray[6] = sHi + sHi + sHi;
    stringArray[7] = sVictory + sVictory + sVictory;
    stringArray[8] = sThumbsUp;

    return stringArray;
  }

  private static String getEmoticonByUnicode(int unicode) {
    return new String(Character.toChars(unicode));
  }
}
