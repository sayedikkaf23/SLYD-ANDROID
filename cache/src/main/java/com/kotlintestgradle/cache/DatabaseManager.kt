package com.kotlintestgradle.cache

import com.kotlintestgradle.cache.dao.AddressDao
import com.kotlintestgradle.cache.dao.CartDao
import com.kotlintestgradle.cache.dao.LoginDao
import com.kotlintestgradle.cache.dao.RecentAddressDao

/**
 * @author 3Embed
 *
 *all functions for CURD operations
 *
 * @since 1.0 (23-Aug-2019)
 */
interface DatabaseManager {
    fun login(): LoginDao

    fun address(): AddressDao

    fun cart(): CartDao

    fun recentAddress(): RecentAddressDao
}