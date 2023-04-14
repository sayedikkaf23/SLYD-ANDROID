package io.isometrik.gs.rtcengine.voice;

/**
 * The local voice reverberation option enum.
 */
public enum VoiceReverberationEnum {

  /**
   * The original voice (no local voice reverberation).
   */
  AUDIO_REVERB_OFF(0),

  /**
   * pop music.
   */
  AUDIO_REVERB_POPULAR(1),

  /**
   * R&B.
   */
  AUDIO_REVERB_RNB(2),

  /**
   * rock music.
   */
  AUDIO_REVERB_ROCK(3),

  /**
   * hip-hop.
   */
  AUDIO_REVERB_HIPHOP(4),

  /**
   * pop concert.
   */
  AUDIO_REVERB_VOCAL_CONCERT(5),

  /**
   * Karaoke.
   */
  AUDIO_REVERB_KTV(6),

  /**
   * Recording studio.
   */
  AUDIO_REVERB_STUDIO(7);

  private final int value;

  VoiceReverberationEnum(int value) {
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
