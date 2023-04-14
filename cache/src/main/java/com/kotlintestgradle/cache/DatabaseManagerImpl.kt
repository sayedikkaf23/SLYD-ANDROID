package com.kotlintestgradle.cache

import com.kotlintestgradle.cache.dao.AddressDao
import com.kotlintestgradle.cache.dao.CartDao
import com.kotlintestgradle.cache.dao.LoginDao
import com.kotlintestgradle.cache.dao.RecentAddressDao

/**
 * @author 3Embed
 *
 *To implement all CURD operations
 * @since 1.0 (23-Aug-2019)
 */
class DatabaseManagerImpl(var appDataBase: AppDataBase, var mCustomerDatabase: CustomerDatabase
) : DatabaseManager {
    override fun login(): LoginDao {
        return appDataBase.loginDao()
    }

    override fun address(): AddressDao {
        return mCustomerDatabase.addressDao()
    }

    override fun cart(): CartDao {
        return mCustomerDatabase.cartDao()
    }

    override fun recentAddress(): RecentAddressDao {
        return mCustomerDatabase.recentAddressDao()
    }
}