package io.isometrik.groupstreaming.ui.utils;

import java.util.Arrays;
import java.util.List;

public class ColorsUtil {

  private static List<String> colors =
      Arrays.asList("#FFCDD2", "#D1C4E9", "#B3E5FC", "#C8E6C9", "#FFF9C4", "#FFCCBC", "#CFD8DC",
          "#F8BBD0", "#C5CAE9", "#B2EBF2", "#DCEDC8", "#FFECB3", "#D7CCC8", "#F5F5F5", "#FFE0B2",
          "#F0F4C3", "#B2DFDB", "#BBDEFB", "#E1BEE7");

  public static String getColorCode(int position) {
    return colors.get(position);
  }
}
