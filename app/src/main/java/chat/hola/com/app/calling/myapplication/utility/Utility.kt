package com.appscrip.myapplication.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import java.util.regex.Pattern

/**
 * @author 3Embed
 * For using common functions
 * @since 1.0(23-Aug-2019)
 */
object Utility {
    /**
     * used to convert html text to required string
     * @param html - html string to be converted
     */
    @JvmStatic
    fun setTextHTML(html: String): Spanned {
        val result: Spanned =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(html)
                }
        return result
    }

    /**
     *used to get deviceID
     * @param context - pass context
     *
     */
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): Long {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            .filter { it.isDigit() }.toLong()
    }

    /**
     *used to generate the group Id
     * @param context - pass context
     *
     */
    fun generateGroupId(context: Context): Long {
        return getDeviceId(context) + System.currentTimeMillis()
    }

    /**
     * to verify email format offline
     */
    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    /**
     * Gets the version name from version code. Note! Needs to be updated
     * when new versions arrive, or will return a single letter. Like Android 8.0 - Oreo
     * yields "O" as a version name.
     * @return version name of device's OS
     */
    fun getOsVersionName(): String {
        val fields = Build.VERSION_CODES::class.java!!.getFields()
        var name = fields[Build.VERSION.SDK_INT + 1].getName()

        if (name == "O") name = "Oreo"
        if (name == "N") name = "Nougat"
        if (name == "M") name = "Marshmallow"
        if (name == "P") name = "PIE"

        if (name.startsWith("O_")) name = "Oreo++"
        if (name.startsWith("N_")) name = "Nougat++"

        return name
    }

    fun Any.shortToast(ctx: Context) =
            Toast.makeText(ctx, this.toString(), Toast.LENGTH_SHORT).show()

    fun Any.longToast(ctx: Context) =
            Toast.makeText(ctx, this.toString(), Toast.LENGTH_SHORT).show()

    @JvmStatic
    fun getDeviceConfiguration(context: Context, deviceConfiguration: DeviceConfiguration) {
        val displayMetrics = DisplayMetrics()
        (context as Activity)
            .windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels

        deviceConfiguration.deviceConfig(deviceWidth, deviceHeight)
    }

    interface DeviceConfiguration {
        fun deviceConfig(width: Int, height: Int)
    }

    /**
     * used to know if mobile has already a normal call
     */
    fun isCallActive(context: Context): Boolean {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        Log.d("Utility", "is call active ${manager.mode}")
        return manager.mode == AudioManager.MODE_IN_CALL
    }
}