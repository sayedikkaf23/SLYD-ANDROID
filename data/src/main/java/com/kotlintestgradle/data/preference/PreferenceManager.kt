package com.kotlintestgradle.data.preference

import com.kotlintestgradle.model.ecom.location.LocationData

/**
 * @author 3Embed
 * for local dataBase
 * @since 1.0 (23-Aug-2019)
 */
interface PreferenceManager {
    var languageCode: String?
    var authToken: String?
    var isLoggedIn: Boolean
    var userId: String?
    var mqttTopic: String?
    var willTopic: String?
    var fcmTopic: String?
    fun clearPreference()
    fun getStoreCatId(): String?
    fun storeTotalAmount(total: String?)
    fun getTotalAmount(): String?
    fun storeTotalProductCount(count: Int)
    fun getTotalProductCount(): Int
    fun isCurrentLocationEmpty(): Boolean
    fun setIsCurrentLocationEmpty(isCurrentLocationEmpty: Boolean)
    fun setCurrentAddress(locationData: LocationData?)
    fun getCurrentAddress(): LocationData?

}
