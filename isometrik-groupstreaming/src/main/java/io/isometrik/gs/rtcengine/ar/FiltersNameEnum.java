package io.isometrik.gs.rtcengine.ar;

/**
 * The enum containing human readable names of the filters.
 */
public enum FiltersNameEnum {

  None("None"),
  FilmColorPerfection("Film color"),
  Tv80("Tv 80's"),
  DrawingManga("Drawing manga"),
  Sepia("Sepia"),
  BleachByPass("Bleach bypass"),
  RealVhs("Real vhs"),
  BackgroundSegmentation("Background Segmentation"),
  FxDrunk("Fx drunk")
  ;

  private final String value;

  FiltersNameEnum(String value) {
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
