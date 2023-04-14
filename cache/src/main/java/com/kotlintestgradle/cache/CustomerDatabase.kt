package com.kotlintestgradle.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlintestgradle.cache.dao.AddressDao
import com.kotlintestgradle.cache.dao.CartDao
import com.kotlintestgradle.cache.dao.RecentAddressDao
import com.kotlintestgradle.cache.entity.RecentAddress
import com.kotlintestgradle.cache.entity.UserAddress
import com.kotlintestgradle.cache.entity.UserCart

@Database(entities = [UserAddress::class, UserCart::class, RecentAddress::class], version = 9)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
    abstract fun cartDao(): CartDao
    abstract fun recentAddressDao(): RecentAddressDao

    companion object {
        private lateinit var INSTANCE: CustomerDatabase
        private const val DB_NAME = "customerDatabase.db"
        fun getDatabaseInstance(context: Context?): CustomerDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(CustomerDatabase::class.java) {
                    if (!::INSTANCE.isInitialized) {
                        INSTANCE = Room.databaseBuilder(context!!,
                            CustomerDatabase::class.java, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}