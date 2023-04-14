package com.kotlintestgradle.remote.core

import com.kotlintestgradle.remote.ApiHandler
import com.kotlintestgradle.remote.RemoteInteractor
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.*

/**
 * @author 3Embed
 *
 *for handling exception from api
 *
 *
 * @since 1.0 (23-Aug-2019)
 */
class RestInterceptor(
    val deviceId: String,
    val remoteInteractor: RemoteInteractor
) : Interceptor {
    private var host: String? = null
    private var apiHandler: ApiHandler? = null
    //  val SESSION_EXPIRED_CODE = "RIL4G_S_SECO_7025"
    fun apiHandler(apiHandler: ApiHandler) {
        this.apiHandler = apiHandler
    }

    // @return error
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            var request = chain.request()
            val host = this.host
            if (host != null) {
                val newUrl = request.url().newBuilder().host(host).build()
                request = request.newBuilder().url(newUrl).build()
            }
            val builder = request.newBuilder()
            val response = chain.proceed(builder.build())

            /*if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val body = response.body()
                val bodyToString = body.toString()
                // if (bodyToString != null && bodyToString.contains(SESSION_EXPIRED_CODE)) {
                // RUN BLOCKING!!
                val refreshTokenResponse = runBlocking {
                    // val refreshToken = remoteInteractor.getRefreshToken()
                }
                // }
                remoteInteractor.clearToken()
            } else {*/
                when (response.code()) {
                    HTTP_INTERNAL_ERROR -> throw IOException("Request cannot be completed. Please try again!")
                    HTTP_GATEWAY_TIMEOUT -> throw IOException("Request timeout. Please try again!")
                    HTTP_CLIENT_TIMEOUT -> throw IOException("Request timeout. Please try again!")
                    HTTP_BAD_GATEWAY -> throw IOException("Request cannot be completed. Please try again!")
                    HTTP_PRECON_FAILED -> throw IOException("password field is missing or invalid!")
                    HTTP_NOT_FOUND -> throw IOException("404")
                    HTTP_CONFLICT -> throw IOException("409")
                    HTTP_UNAUTHORIZED -> throw IOException("401")
                    HTTP_LENGTH_REQUIRED -> throw IOException("411")
                    HTTP_BAD_REQUEST -> {
                        val obj = JSONObject(response.body()?.string())
                        throw IOException(obj.getString("message"))
                    }
                }
           /* }*/
            return response
        }
    }
}