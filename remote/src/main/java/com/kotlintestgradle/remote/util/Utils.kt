package com.kotlintestgradle.remote.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.util.Random

/**
 * Created by 3Embed on 15/03/19.
 */
@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun getRandomNumber(): String {
    val r = Random()
    return r.nextInt(Integer.MAX_VALUE).toString()
}