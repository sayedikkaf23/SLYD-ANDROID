package io.isometrik.gs.rtcengine.voice;

/**
 * The local voice reverberation names enum.
 */
public enum VoiceReverberationNamesEnum {

  /**
   * The original voice (no local voice reverberation).
   */
  VOICE_REVERBERATION_OFF("None"),

  /**
   * pop music.
   */
  VOICE_REVERBERATION_POPMUSIC("Pop music"),

  /**
   * R&B.
   */
  VOICE_REVERBERATION_RANDB("R&B"),

  /**
   * rock music.
   */
  VOICE_REVERBERATION_ROCKMUSIC("Rock music"),

  /**
   * hip-hop.
   */
  VOICE_REVERBERATION_HIPHOP("Hip-hop"),

  /**
   * pop concert.
   */
  VOICE_REVERBERATION_VOCAL_CONCERT("Concert"),

  /**
   * Karaoke.
   */
  VOICE_REVERBERATION_KARAOKE("Karaoke"),

  /**
   * Recording studio.
   */
  VOICE_REVERBERATION_STUDIO("Studio");

  private final String value;

  VoiceReverberationNamesEnum(String value) {
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
