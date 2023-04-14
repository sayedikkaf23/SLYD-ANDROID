package com.kotlintestgradle.data

import com.kotlintestgradle.cache.DatabaseManager
import com.kotlintestgradle.remote.FcmManager
import com.kotlintestgradle.remote.MqttManager
import com.kotlintestgradle.remote.NetworkManager

/**
 * @author 3Embed
 * for saving data to dataBase
 * @since 1.0 (23-Aug-2019)
 */
interface DataSource {
    fun db(): DatabaseManager
    fun preference(): com.kotlintestgradle.data.preference.PreferenceManager
    fun api(): NetworkManager
    fun mqtt(): MqttManager
    fun fcm(): FcmManager
    fun storage(): StorageManager
}