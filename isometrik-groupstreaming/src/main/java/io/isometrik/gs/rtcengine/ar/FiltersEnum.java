package io.isometrik.gs.rtcengine.ar;

/**
 * The enum containing internal names of the filters.
 */
public enum FiltersEnum {

  None("none"),
  FilmColorPerfection("filmcolorperfection"),
  Tv80("tv80"),
  DrawingManga("drawingmanga"),
  Sepia("sepia"),
  BleachByPass("bleachbypass"),
  RealVhs("realvhs"),
  BackgroundSegmentation("background_segmentation"),
  FxDrunk("fxdrunk");

  private final String value;

  FiltersEnum(String value) {
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
