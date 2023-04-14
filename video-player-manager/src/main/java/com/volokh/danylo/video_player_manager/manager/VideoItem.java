package com.volokh.danylo.video_player_manager.manager;

import androidx.appcompat.widget.AppCompatImageView;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

/**
 * This is basic interface for Items in Adapter of the list.
 */
public interface VideoItem {
    void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager, AppCompatImageView ivRecord,boolean newPost);
    void stopPlayback(VideoPlayerManager videoPlayerManager);
}
