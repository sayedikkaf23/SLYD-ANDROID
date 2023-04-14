package com.kotlintestgradle.remote

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.kotlintestgradle.remote.core.ItemTypeAdapterFactory
import com.kotlintestgradle.remote.core.RestInterceptor
import com.kotlintestgradle.remote.util.getDeviceId
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author 3Embed
 *used to implement network manager
 * @since 1.0 (23-Aug-2019)
 */
class NetworkManagerImpl(private val context: Context, remoteInteractor: RemoteInteractor) : NetworkManager {
    private var interceptor: RestInterceptor =
            RestInterceptor(getDeviceId(context), remoteInteractor)
    private var connectivityInterceptor = ConnectivityInterceptor(context)
    private val restApi: RestApi = restApi(context)
    private var apiHandler: ApiHandlerImpl = ApiHandlerImpl(restApi, getDeviceId(context))

    companion object {
        const val CONNECT_TIME_OUT = 10
        const val READ_TIME_OUT = 90
    }

    override fun handler(): ApiHandler {
        return apiHandler
    }

    private fun restApi(context: Context): RestApi {
        return build(
            BuildConfig.BASEURL,
//            getOkHttpClient(context),
                UnsafeOkHttpClient.getUnsafeOkHttpClient(context),

                createGsonConverterFactory(ItemTypeAdapterFactory.newInstance())
        ).create(
            RestApi::class.java
        )
    }

    private fun createGsonConverterFactory(typeAdapterFactory: TypeAdapterFactory): GsonConverterFactory {
        val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory)
            .excludeFieldsWithoutExposeAnnotation().create()
        return GsonConverterFactory.create(gson)
    }

    private fun build(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(
            SSLCertificate.initSSL(context)
                .getSocketFactory(), SSLCertificate.systemDefaultTrustManager()
        )
        val certificatePinner = CertificatePinner.Builder().add(
            BuildConfig.DOMAIN,
            BuildConfig.SHA_256
        ).build()

        builder.connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
        // interceptor for token and header
        if (!BuildConfig.DEBUG) {
            builder.certificatePinner(certificatePinner)
        }
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        builder.addInterceptor(interceptor)
        builder.addInterceptor(connectivityInterceptor)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        // interceptor for logging
        return builder.build()
    }
}