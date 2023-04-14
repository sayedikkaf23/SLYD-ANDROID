package com.kotlintestgradle.remote

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author 3Embed
 *
 *used for checking internet
 *
 * @since 1.0 (23-Aug-2019)
 */
class ConnectivityInterceptor(context: Context) : Interceptor {
    private var contxt: Context = context

    @Throws(IOException::class) override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline(contxt)) {
            throw NoConnectivityException()
        }
        val builder = chain.request()
            .newBuilder()
        return chain.proceed(builder.build())
    }

    @SuppressLint("MissingPermission") fun isOnline(context: Context): Boolean {
        val connectivityManager =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No network available, please check your WiFi or Data connection"
}