package com.volokh.danylo.video_player_manager.ui;

import android.media.MediaPlayer;
import android.util.Log;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MediaPlayerWrapperImpl extends MediaPlayerWrapper{

    public MediaPlayerWrapperImpl() {
      //  super(new MediaPlayer());

        super(new IjkMediaPlayer());

    }
}
