package com.kotlintestgradle.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.kotlintestgradle.data.interactor.SessionExpireObserver
import com.kotlintestgradle.model.ecom.location.LocationData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author 3Embed
 * for implementing local database
 * @since 1.0 (23-Aug-2019)
 */
class PreferenceManagerImpl(val context: Context) :
    PreferenceManager {
    override fun clearPreference() {
        isLoggedIn = false
        SessionExpireObserver.getInstance.publishData("success")
    }

    override fun getStoreCatId(): String?{
        return sharedPreferences.getString("storeCatId", "")
    }

    override fun storeTotalAmount(total: String?) {
        sharedPreferences.edit().putString("cartTotal", total)
        sharedPreferences.edit().commit()
    }

    override fun getTotalAmount(): String? {
        return sharedPreferences.getString("cartTotal", "")
    }

    override fun storeTotalProductCount(count: Int) {
        sharedPreferences.edit().putInt("totalCount",
            count)
        sharedPreferences.edit().commit()
    }

    override fun getTotalProductCount(): Int {
        return sharedPreferences.getInt("totalCount", 0)
    }

    override fun isCurrentLocationEmpty(): Boolean {
        return sharedPreferences.getBoolean("isAddressEmpty",
            true)
    }

    override fun setIsCurrentLocationEmpty(isCurrentLocationEmpty: Boolean) {
        sharedPreferences.edit().putBoolean("isAddressEmpty",
            isCurrentLocationEmpty)
        sharedPreferences.edit().commit()
    }

    override fun getCurrentAddress(): LocationData? {
        val data = sharedPreferences.getString("currentAddress", "")
        return Gson().fromJson(data, LocationData::class.java)
    }

    override fun setCurrentAddress(locationData: LocationData?) {
        val location: String
        if (locationData != null) {
            location = Gson().toJson(locationData)
            setIsCurrentLocationEmpty(locationData.getLatitude() === 0.0)
        } else {
            location = ""
        }
        sharedPreferences.edit().putString("currentAddress", location)
        sharedPreferences.edit().commit()
    }

    private val name: String = "sharedPref"
    private val sharedPreferences =
            if (Build.VERSION.SDK_INT >= 23) {
                val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                EncryptedSharedPreferences.create(
                    name,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } else {
                context.getSharedPreferences(name, Context.MODE_PRIVATE)
            }
    override var authToken: String? by StringPreference(sharedPreferences, AUTH_TOKEN, "")
    override var languageCode: String? by StringPreference(sharedPreferences, LANGUAGE_CODE, "en")
    override var userId: String? by StringPreference(sharedPreferences, USER_ID, "")
    override var mqttTopic: String? by StringPreference(sharedPreferences, MQTT_TOPIC, "")
    override var willTopic: String? by StringPreference(sharedPreferences, WILL_TOPIC, "")
    override var fcmTopic: String? by StringPreference(sharedPreferences, FCM_TOPIC, "")
    override var isLoggedIn: Boolean by BooleanPreference(sharedPreferences, IS_LOGGED, false)

    companion object {
        // all object name
        const val LANGUAGE_CODE = "language_code"
        const val AUTH_TOKEN = "auth_token"
        const val IS_LOGGED = "isLoggedIn"
        const val USER_ID = "userId"
        const val MQTT_TOPIC = "mqttTopic"
        const val WILL_TOPIC = "willTopic"
        const val FCM_TOPIC = "fcmTopic"
    }
}

class BooleanPreference(
    private var preferences: SharedPreferences,
    private var name: String,
    private var defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Boolean
    ) {
        preferences.edit().putBoolean(name, value).apply()
    }
}

class StringPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String? = null
) : ReadWriteProperty<Any, String?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
            sharedPreferences.getString(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
            sharedPreferences.edit()
                .putString(key, value)
                .apply()
}

class IntPreference(
    private var preferences: SharedPreferences,
    private var name: String,
    private var defaultValue: Int
) : ReadWriteProperty<Any, Int> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Int {
        return preferences.getInt(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Int
    ) {
        preferences.edit().putInt(name, value).apply()
    }
}

class LongPreference(
    private var preferences: SharedPreferences,
    private var name: String,
    private var defaultValue: Long
) : ReadWriteProperty<Any, Long> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Long {
        return preferences.getLong(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Long
    ) {
        preferences.edit().putLong(name, value).apply()
    }
}
