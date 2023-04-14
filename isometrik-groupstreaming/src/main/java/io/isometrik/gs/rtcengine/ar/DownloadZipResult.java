package io.isometrik.gs.rtcengine.ar;

public interface DownloadZipResult {
  void downloadResult(String result, String filePath);

  void zipExtractResult(String result);
}
