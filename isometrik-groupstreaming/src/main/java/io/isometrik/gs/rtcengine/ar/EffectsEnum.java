package io.isometrik.gs.rtcengine.ar;

/**
 * The enum containing internal names of the effects.
 */
public enum EffectsEnum {

  None("none"),
  //Fire("fire"),
  Rain("rain"),
  //Heart("heart"),
  Blizzard("blizzard"),
  PlasticOcean("plastic_ocean"),
  HairSegmentation("hair_segmentation");

  private final String value;

  EffectsEnum(String value) {
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
