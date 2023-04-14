package io.isometrik.gs.rtcengine.stats;

import java.util.Locale;

/**
 * The class to manage local stats data.
 *
 * @see io.isometrik.gs.rtcengine.stats.StatsData
 */
public class LocalStatsData extends StatsData {
  private static final String FORMAT = "Local\n"
      + "%dx%d %dfps\n"
      + "LastMile delay: %d ms\n"
      + "Video tx/rx (kbps): %d/%d\n"
      + "Audio tx/rx (kbps): %d/%d\n"
      + "CPU: app/total %.1f%%/%.1f%%\n"
      + "Quality tx/rx: %s/%s\n"
      + "Loss tx/rx: %d%%/%d%%";

  private int lastMileDelay;
  private int videoSend;
  private int videoRecv;
  private int audioSend;
  private int audioRecv;
  private double cpuApp;
  private double cpuTotal;
  private int sendLoss;
  private int recvLoss;

  @Override
  public String toString() {
    return String.format(Locale.getDefault(), FORMAT, getWidth(), getHeight(),
        getFramerate(), getLastMileDelay(), getVideoSendBitrate(), getVideoRecvBitrate(),
        getAudioSendBitrate(), getAudioRecvBitrate(), getCpuApp(), getCpuTotal(), getSendQuality(),
        getRecvQuality(), getSendLoss(), getRecvLoss());
  }

  /**
   * Gets last mile delay.
   *
   * @return the last mile delay
   */
  public int getLastMileDelay() {
    return lastMileDelay;
  }

  /**
   * Sets last mile delay.
   *
   * @param lastMileDelay the last mile delay
   */
  public void setLastMileDelay(int lastMileDelay) {
    this.lastMileDelay = lastMileDelay;
  }

  /**
   * Gets video send bitrate.
   *
   * @return the video send bitrate
   */
  public int getVideoSendBitrate() {
    return videoSend;
  }

  /**
   * Sets video send bitrate.
   *
   * @param videoSend the video send
   */
  public void setVideoSendBitrate(int videoSend) {
    this.videoSend = videoSend;
  }

  /**
   * Gets video recv bitrate.
   *
   * @return the video recv bitrate
   */
  public int getVideoRecvBitrate() {
    return videoRecv;
  }

  /**
   * Sets video recv bitrate.
   *
   * @param videoRecv the video recv
   */
  public void setVideoRecvBitrate(int videoRecv) {
    this.videoRecv = videoRecv;
  }

  /**
   * Gets audio send bitrate.
   *
   * @return the audio send bitrate
   */
  public int getAudioSendBitrate() {
    return audioSend;
  }

  /**
   * Sets audio send bitrate.
   *
   * @param audioSend the audio send
   */
  public void setAudioSendBitrate(int audioSend) {
    this.audioSend = audioSend;
  }

  /**
   * Gets audio receive bitrate.
   *
   * @return the audio receive bitrate
   */
  public int getAudioRecvBitrate() {
    return audioRecv;
  }

  /**
   * Sets audio recv bitrate.
   *
   * @param audioRecv the audio recv
   */
  public void setAudioRecvBitrate(int audioRecv) {
    this.audioRecv = audioRecv;
  }

  /**
   * Gets cpu app.
   *
   * @return the cpu app
   */
  public double getCpuApp() {
    return cpuApp;
  }

  /**
   * Sets cpu app.
   *
   * @param cpuApp the cpu app
   */
  public void setCpuApp(double cpuApp) {
    this.cpuApp = cpuApp;
  }

  /**
   * Gets cpu total.
   *
   * @return the cpu total
   */
  public double getCpuTotal() {
    return cpuTotal;
  }

  /**
   * Sets cpu total.
   *
   * @param cpuTotal the cpu total
   */
  public void setCpuTotal(double cpuTotal) {
    this.cpuTotal = cpuTotal;
  }

  /**
   * Gets send loss.
   *
   * @return the send loss
   */
  public int getSendLoss() {
    return sendLoss;
  }

  /**
   * Sets send loss.
   *
   * @param sendLoss the send loss
   */
  public void setSendLoss(int sendLoss) {
    this.sendLoss = sendLoss;
  }

  /**
   * Gets receive loss.
   *
   * @return the receive loss
   */
  public int getRecvLoss() {
    return recvLoss;
  }

  /**
   * Sets receive loss.
   *
   * @param recvLoss the receive loss
   */
  public void setRecvLoss(int recvLoss) {
    this.recvLoss = recvLoss;
  }
}
