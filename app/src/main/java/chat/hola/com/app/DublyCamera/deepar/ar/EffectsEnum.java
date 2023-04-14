package chat.hola.com.app.DublyCamera.deepar.ar;

/**
 * The enum containing internal names of the effects.
 */
public enum EffectsEnum {
  /**
   * /*Bug Title- Heart and Fire effects are not working at times
   * /*Bug Id-DUBAND023
   * /*Fix Description-Remove heart and fire effects from enum
   * /*Developer Name-Ashutosh
   * /*Fix Date-6/4/21
   **/
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
