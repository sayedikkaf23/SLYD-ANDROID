package io.isometrik.gs.rtcengine.stats;

import java.util.Locale;

/**
 * The class to manage remote stats data.
 *
 * @see io.isometrik.gs.rtcengine.stats.StatsData
 */
public class RemoteStatsData extends StatsData {
  private static final String FORMAT = "Remote\n"
      + "%dx%d %dfps\n"
      + "Quality tx/rx: %s/%s\n"
      + "Video delay: %d ms\n"
      + "Audio net delay/jitter: %dms/%dms\n"
      + "Audio loss/quality: %d%%/%s";

  private int videoDelay;
  private int audioNetDelay;
  private int audioNetJitter;
  private int audioLoss;
  private String audioQuality;

  @Override
  public String toString() {
    return String.format(Locale.getDefault(), FORMAT,  getWidth(), getHeight(),
        getFramerate(), getSendQuality(), getRecvQuality(), getVideoDelay(), getAudioNetDelay(),
        getAudioNetJitter(), getAudioLoss(), getAudioQuality());
  }

  /**
   * Gets format.
   *
   * @return the format
   */
  public static String getFORMAT() {
    return FORMAT;
  }

  /**
   * Gets video delay.
   *
   * @return the video delay
   */
  public int getVideoDelay() {
    return videoDelay;
  }

  /**
   * Sets video delay.
   *
   * @param videoDelay the video delay
   */
  public void setVideoDelay(int videoDelay) {
    this.videoDelay = videoDelay;
  }

  /**
   * Gets audio net delay.
   *
   * @return the audio net delay
   */
  public int getAudioNetDelay() {
    return audioNetDelay;
  }

  /**
   * Sets audio net delay.
   *
   * @param audioNetDelay the audio net delay
   */
  public void setAudioNetDelay(int audioNetDelay) {
    this.audioNetDelay = audioNetDelay;
  }

  /**
   * Gets audio net jitter.
   *
   * @return the audio net jitter
   */
  public int getAudioNetJitter() {
    return audioNetJitter;
  }

  /**
   * Sets audio net jitter.
   *
   * @param audioNetJitter the audio net jitter
   */
  public void setAudioNetJitter(int audioNetJitter) {
    this.audioNetJitter = audioNetJitter;
  }

  /**
   * Gets audio loss.
   *
   * @return the audio loss
   */
  public int getAudioLoss() {
    return audioLoss;
  }

  /**
   * Sets audio loss.
   *
   * @param audioLoss the audio loss
   */
  public void setAudioLoss(int audioLoss) {
    this.audioLoss = audioLoss;
  }

  /**
   * Gets audio quality.
   *
   * @return the audio quality
   */
  public String getAudioQuality() {
    return audioQuality;
  }

  /**
   * Sets audio quality.
   *
   * @param audioQuality the audio quality
   */
  public void setAudioQuality(String audioQuality) {
    this.audioQuality = audioQuality;
  }
}
