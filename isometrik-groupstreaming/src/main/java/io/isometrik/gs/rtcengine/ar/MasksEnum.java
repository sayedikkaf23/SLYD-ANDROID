package io.isometrik.gs.rtcengine.ar;

/**
 * The enum containing internal names of the masks.
 */
public enum MasksEnum {

  None("none"),
  Aviators("aviators"),
  BigMouth("bigmouth"),
  Dalmatian("dalmatian"),
  Flowers("flowers"),
  Koala("koala"),
  Lion("lion"),
  SmallFace("smallface"),
  TeddyCigar("teddycigar"),
  Kanye("kanye"),
  TripleFace("tripleface"),
  SleepingMask("sleepingmask"),
  Fatify("fatify"),
  Obama("obama"),
  MudMask("mudmask"),
  Pug("pug"),
  Slash("slash"),
  TwistedFace("twistedface"),
  GrumpyCat("grumpycat"),
  Beard("beard"),
  Topology("topology"),
  TinySunglasses("tiny_sunglasses"),
  TapeFace("tape_face"),
  Scuba("scuba"),
  Pumpkin("pumpkin"),
  ManlyFace("manly_face"),
  Frankenstein("frankenstein"),
  FlowerCrown("flower_crown"),
  FairyLights("fairy_lights"),
  Alien("alien")
  ;

  private final String value;

  MasksEnum(String value) {
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
