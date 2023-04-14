package io.isometrik.gs.rtcengine.ar;

import android.content.SharedPreferences;

public class FiltersConfig {

  private static boolean downloadRequired = false;
  private static String filesDirectory = "";
  private static SharedPreferences sharedPreferences;
  public static final String DEEPAR_EFFECTS =
      "https://dubly.s3.ap-south-1.amazonaws.com/ar_filters/deepar/deepar.zip";

  public static final String DEEPAR_FILENAME = "deepar.zip";

  public static final String DEEPAR_DIRECTORY = "/arfilters/";

  public static final String DEEPAR_SIZE = "50.0 MB";

  public static boolean isDownloadRequired() {

    return downloadRequired;
  }

  public static void setDownloadRequired(boolean downloadRequired) {

    FiltersConfig.downloadRequired = downloadRequired;
  }

  public static String getFilesDirectory() {

    return filesDirectory;
  }

  public static void setFilesDirectory(String filesDirectory) {

    FiltersConfig.filesDirectory = filesDirectory;
  }

  public static void setSharedPreferences(SharedPreferences sharedPreferences) {
    FiltersConfig.sharedPreferences = sharedPreferences;
  }

  public static boolean isFiltersDownloadedAlready() {
    return sharedPreferences.getBoolean("deeparFiltersDownloaded", false);
  }

  public static void setFiltersDownloadedAlready() {
    sharedPreferences.edit().putBoolean("deeparFiltersDownloaded", true).apply();
  }
}