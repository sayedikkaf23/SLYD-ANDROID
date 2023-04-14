package com.kotlintestgradle.cache

import androidx.room.migration.Migration

/**
 * @author 3Embed
 *
 *For changing the versions of database
 *
 * @since 1.0 (23-Aug-2019)
 */
class Migrations {
    companion object {
        const val DB_VERSION = 1

        fun getMigrations(): Array<Migration> {
            return arrayOf()
        }
    }
}