package io.isometrik.gs.rtcengine.voice;

/**
 * The class to wrap the detail of the voice effects which includes its type, name, internal name
 * and
 * path.
 */
public class VoiceEffect {

  static final String EffectTypeVoiceChange = "voiceChange";

  static final String EffectTypeReverberation = "reverberation";

  private int effectNameInternal;
  private String effectType;
  private String effectName;
  private boolean selected;

  /**
   * Instantiates a new voice effect.
   *
   * @param effectNameInternal the effect name internal
   * @param effectType the effect type
   * @param effectName the effect name
   */
  VoiceEffect(int effectNameInternal, String effectType, String effectName) {
    this.effectNameInternal = effectNameInternal;
    this.effectType = effectType;
    this.effectName = effectName;
  }

  /**
   * Gets effect name internal.
   *
   * @return the effect name internal
   */
  public int getEffectNameInternal() {
    return effectNameInternal;
  }

  /**
   * Gets effect name.
   *
   * @return the name of the voice effect
   */
  public String getEffectName() {
    return effectName;
  }

  /**
   * Gets effect type.
   *
   * @return the type of the voice effect
   */
  public String getEffectType() {
    return effectType;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
