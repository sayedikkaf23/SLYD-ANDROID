package com.kotlintestgradle.data

import android.content.Context
import com.kotlintestgradle.cache.AppDataBase
import com.kotlintestgradle.cache.CustomerDatabase
import com.kotlintestgradle.cache.DatabaseManager
import com.kotlintestgradle.cache.DatabaseManagerImpl
import com.kotlintestgradle.data.interactor.RemoteInteractorImpl
import com.kotlintestgradle.data.preference.PreferenceManager
import com.kotlintestgradle.data.preference.PreferenceManagerImpl
import com.kotlintestgradle.remote.FcmManager
import com.kotlintestgradle.remote.FcmManagerImpl
import com.kotlintestgradle.remote.MqttManager
import com.kotlintestgradle.remote.MqttManagerImpl
import com.kotlintestgradle.remote.NetworkManager
import com.kotlintestgradle.remote.NetworkManagerImpl
import javax.inject.Inject

/**
 * @author 3Embed
 * for implementing dataSource
 * @since 1.0 (23-Aug-2019)
 */
class DataSourceImpl @Inject constructor(context: Context) : DataSource {
    private var databaseManager: DatabaseManager =
        DatabaseManagerImpl(AppDataBase.getInstance(context.applicationContext), CustomerDatabase.getDatabaseInstance(context.applicationContext))
    private var preferenceManager: PreferenceManager =
        PreferenceManagerImpl(context.applicationContext)
    private var networkManager: NetworkManager = NetworkManagerImpl(
        context.applicationContext,
        RemoteInteractorImpl(preferenceManager)
    )
    private var mqttManager: MqttManager = MqttManagerImpl(
        context.applicationContext
    )
    private var fcmManager: FcmManager =
        FcmManagerImpl()
    private var storageManager: StorageManager = StorageManagerImpl()

    companion object : SingletonHolder<DataSource, Context>(::DataSourceImpl)

    override fun db(): DatabaseManager {
        return databaseManager
    }

    override fun preference(): PreferenceManager {
        return preferenceManager
    }

    override fun api(): NetworkManager {
        return networkManager
    }

    override fun mqtt(): MqttManager {
        return mqttManager
    }

    override fun fcm(): FcmManager {
        return fcmManager
    }

    override fun storage(): StorageManager {
        return storageManager
    }
}