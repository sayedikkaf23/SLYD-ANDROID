package io.isometrik.gs.rtcengine.voice;

/**
 * The local voice changer names enum.
 */
public enum VoiceChangeNamesEnum {

  /**
   * the original voice (no local voice change).
   */
  VOICE_CHANGE_OFF("None"),

  /**
   * An old man's voice.
   */
  VOICE_CHANGE_OLDMAN("Old man"),

  /**
   * A little boy's voice.
   */
  VOICE_CHANGE_BABYBOY("Baby boy"),

  /**
   * A little girl's voice.
   */
  VOICE_CHANGE_BABYGIRL("Baby girl"),

  /**
   * Zhu Bajie's voice (Zhu Bajie is a character from Journey to the West who has a voice like a
   * growling bear).
   */
  VOICE_CHANGE_ZHUBAJIE("Growling Bear"),

  /**
   * Ethereal vocal effects..
   */
  VOICE_CHANGE_ETHEREAL("Ethereal"),

  /**
   * Hulk's voice.
   */
  VOICE_CHANGE_HULK("Hulk");

  private final String value;

  VoiceChangeNamesEnum(String value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final String getValue() {
    return this.value;
  }


}
