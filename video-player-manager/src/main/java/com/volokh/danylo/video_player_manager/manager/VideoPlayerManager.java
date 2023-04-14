package com.volokh.danylo.video_player_manager.manager;

import android.content.res.AssetFileDescriptor;
import android.view.View;

import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.volokh.danylo.video_player_manager.meta.MetaData;

/**
 * This is a general interface for VideoPlayerManager
 * It supports :
 * 1. Start playback of new video by calling:
 *  a) {@link #playNewVideo(MetaData, VideoPlayerView, String,boolean)} if you have direct url or path to video source
 *  b) {@link #playNewVideo(MetaData, VideoPlayerView, AssetFileDescriptor)} is your video file is in assets directory
 * 2. Stop existing playback. {@link #stopAnyPlayback(boolean)}
 * 3. Reset Media Player if it's no longer needed. {@link #resetMediaPlayer()}
 */
public interface VideoPlayerManager<T extends MetaData> {

    /**
     * Call it if you have direct url or path to video source
     * @param metaData - optional Meta Data
     * @param videoPlayerView - the actual video player
     * @param videoUrl - the link to the video source
     * @param videoMuted - whether video is muted or not
     */
    void playNewVideo(T metaData, VideoPlayerView videoPlayerView, String videoUrl,boolean videoMuted);

    /**
     * Call it if you have video source in assets directory
     * @param metaData - optional Meta Data
     * @param videoPlayerView - the actual video player
     * @param assetFileDescriptor -The asset descriptor of the video file
     */
    void playNewVideo(T metaData, VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor);

    /**
     * Call it if you need to stop any playback that is currently playing
     */
    void stopAnyPlayback(boolean notBeforePlayback);

    /**
     * Call it if you no longer need the player
     */
    void resetMediaPlayer();
}
