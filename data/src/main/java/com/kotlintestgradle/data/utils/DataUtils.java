package com.kotlintestgradle.data.utils;

import static com.kotlintestgradle.data.utils.DataConstants.LOWER_BOUND;

import java.util.List;

public class DataUtils {
  public static boolean isEmptyArray(List object) {
    return object == null || object.size() == LOWER_BOUND;
  }
}
