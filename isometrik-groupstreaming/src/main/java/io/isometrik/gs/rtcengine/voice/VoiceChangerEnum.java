package io.isometrik.gs.rtcengine.voice;

/**
 * The local voice changer option enum.
 */
public enum VoiceChangerEnum {

  /**
   * the original voice (no local voice change).
   */
  VOICE_CHANGER_OFF(0),

  /**
   * An old man's voice.
   */
  VOICE_CHANGER_OLDMAN(1),

  /**
   * A little boy's voice.
   */
  VOICE_CHANGER_BABYBOY(2),

  /**
   * A little girl's voice.
   */
  VOICE_CHANGER_BABYGIRL(3),

  /**
   * Zhu Bajie's voice (Zhu Bajie is a character from Journey to the West who has a voice like a
   * growling bear).
   */
  VOICE_CHANGER_ZHUBAJIE(4),

  /**
   * Ethereal vocal effects..
   */
  VOICE_CHANGER_ETHEREAL(5),

  /**
   * Hulk's voice.
   */
  VOICE_CHANGER_HULK(6);

  private final int value;

  VoiceChangerEnum(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final int getValue() {
    return this.value;
  }
}
