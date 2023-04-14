package com.appscrip.myapplication.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log

class HeadsetIntentReceiver : BroadcastReceiver() {
    private val TAG = HeadsetIntentReceiver::class.java.simpleName

    init {
        Log.d(TAG, "Created")
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            val state = intent.getIntExtra("state", -1)
            val mAudioManager =
                    context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            when (state) {
                0 -> {
                    Log.d(TAG, "Headset unplugged")
                    mAudioManager.isSpeakerphoneOn = true
                }
                1 -> {
                    Log.d(TAG, "Headset plugged")
                    mAudioManager.isSpeakerphoneOn = false
                }
                else -> Log.d(TAG, "Error")
            }
        }
    }
}
