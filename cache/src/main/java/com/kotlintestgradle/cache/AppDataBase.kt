package com.kotlintestgradle.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlintestgradle.cache.dao.LoginDao
import com.kotlintestgradle.cache.entity.Login

/**
 * @author 3Embed
 *
 * To manage data items that can be accessed, updated
 * & maintain relationships between them
 *
 * @since 1.0 (23-Aug-2019)
 */
@Database(entities = [Login::class], version = Migrations.DB_VERSION, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun loginDao(): LoginDao

    companion object {
        private const val DB_NAME = "appData.db"
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }



        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDataBase::class.java, DB_NAME
        ).build()
    }
}