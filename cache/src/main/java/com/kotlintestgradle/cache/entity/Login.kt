package com.kotlintestgradle.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 3Embed
 * this class is used to create table and its column name
 * @since 1.0 (23-Aug-2019)
 */
@Entity(tableName = "loginData")
data class Login(
    @ColumnInfo(name = "accountType") var accountType: String?,
    @ColumnInfo(name = "token") var token: String?,
    @PrimaryKey @ColumnInfo(name = "sid") var sid: String,
    @ColumnInfo(name = "firstName") var firstName: String?,
    @ColumnInfo(name = "lastName") var lastName: String?,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = "countryCode") var countryCode: String?,
    @ColumnInfo(name = "referralCode") var referralCode: Int?,
    @ColumnInfo(name = "profilePic") var profilePic: String?,
    @ColumnInfo(name = "fcmTopic") var fcmTopic: String?,
    @ColumnInfo(name = "mqttTopic") var mqttTopic: String?,
    @ColumnInfo(name = "googleMapApiKeyTopic") var googleMapApiKeyTopic: String?
)