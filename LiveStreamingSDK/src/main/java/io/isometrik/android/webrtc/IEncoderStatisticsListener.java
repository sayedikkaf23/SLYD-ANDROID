package io.isometrik.android.webrtc;

import org.webrtc.StatsReport;

/**
 * Created by moda on 28.10.2017.
 */

public interface IEncoderStatisticsListener {

    /**
     * It is called on other thread then UI, if this function changes something on UI
     * run view updates in UI Thread with runOnUIThread
     * @param reports
     */
    public void updateEncoderStatistics(final StatsReport[] reports);
}
