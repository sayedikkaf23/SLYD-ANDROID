package io.isometrik.gs.rtcengine.ar;

/**
 * The enum containing human readable names of the masks.
 */
public enum MasksNameEnum {

  None("None"),
  Aviators("Aviators"),
  BigMouth("Big mouth"),
  Dalmatian("Dalmatian"),
  Flowers("Flowers"),
  Koala("Koala"),
  Lion("Lion"),
  SmallFace("Small face"),
  TeddyCigar("Teddy cigar"),
  Kanye("Kanye west"),
  TripleFace("Triple face"),
  SleepingMask("Sleeping mask"),
  Fatify("Fatify"),
  Obama("Obama"),
  MudMask("Mud mask"),
  Pug("Pug"),
  Slash("Slash"),
  TwistedFace("Twisted face"),
  GrumpyCat("Grumpy cat"),
  Beard("Beard"),
  Topology("Topology"),
  TinySunglasses("Tiny sunglasses"),
  TapeFace("Tape face"),
  Scuba("Scuba"),
  Pumpkin("Pumpkin"),
  ManlyFace("Manly face"),
  Frankenstein("Frankenstein"),
  FlowerCrown("Flower crown"),
  FairyLights("Fairy lights"),
  Alien("Alien");

  private final String value;

  MasksNameEnum(String value) {
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
