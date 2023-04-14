package io.isometrik.gs.rtcengine.stats;

/**
 * The class to manage StatsData.
 */
public class StatsData {
  private long uid;
  private int width;
  private int height;
  private int framerate;
  private String recvQuality;
  private String sendQuality;

  /**
   * Gets uid.
   *
   * @return the uid
   */
  public long getUid() {
    return uid;
  }

  /**
   * Sets uid.
   *
   * @param uid the uid
   */
  public void setUid(long uid) {
    this.uid = uid;
  }

  /**
   * Gets width.
   *
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets width.
   *
   * @param width the width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Gets height.
   *
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets height.
   *
   * @param height the height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Gets framerate.
   *
   * @return the framerate
   */
  public int getFramerate() {
    return framerate;
  }

  /**
   * Sets framerate.
   *
   * @param framerate the framerate
   */
  public void setFramerate(int framerate) {
    this.framerate = framerate;
  }

  /**
   * Gets receive quality.
   *
   * @return the receive quality
   */
  public String getRecvQuality() {
    return recvQuality;
  }

  /**
   * Sets receive quality.
   *
   * @param recvQuality the receive quality
   */
  public void setRecvQuality(String recvQuality) {
    this.recvQuality = recvQuality;
  }

  /**
   * Gets send quality.
   *
   * @return the send quality
   */
  public String getSendQuality() {
    return sendQuality;
  }

  /**
   * Sets send quality.
   *
   * @param sendQuality the send quality
   */
  public void setSendQuality(String sendQuality) {
    this.sendQuality = sendQuality;
  }
}
