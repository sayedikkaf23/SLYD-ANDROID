package chat.hola.com.app.DublyCamera.utilities;

import chat.hola.com.app.AppController;

public class FiltersConfig {

  public static final boolean DOWNLOAD_REQUIRED = true;
  public static final String BANUBA_EFFECTS =
      "https://dubly.s3.ap-south-1.amazonaws.com/ar_filters/banuba/effects.zip";
  public static final String DEEPAR_EFFECTS =
      "https://dubly.s3.ap-south-1.amazonaws.com/ar_filters/deepar/deepar.zip";

  public static final String BANUBA_FILENAME = "banuba.zip";
  public static final String DEEPAR_FILENAME = "deepar.zip";
  public static final String BANUBA_DIRECTORY =
      AppController.getInstance().getFilesDir() + "/arfilters/banuba/";
  public static final String DEEPAR_DIRECTORY =
      AppController.getInstance().getFilesDir() + "/arfilters/";
  //public static final String BANUBA_SIZE = "88.5 MB";
  public static final String BANUBA_SIZE = "73.2 MB";
  public static final String DEEPAR_SIZE = "50.0 MB";
}
