package io.isometrik.gs.rtcengine.ar;

/**
 * The enum containing human readable names of the effects.
 */
public enum EffectsNameEnum {

  None("None"),
  Fire("Fire"),
  Rain("Rain"),
  Heart("Heart"),
  Blizzard("Blizzard"),
  PlasticOcean("Plastic ocean"),
  HairSegmentation("Hair segmentation");

  private final String value;

  EffectsNameEnum(String value) {
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
