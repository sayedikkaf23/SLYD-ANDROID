package com.kotlintestgradle.data.repository

import com.kotlintestgradle.cache.dao.LoginDao
import com.kotlintestgradle.data.DataSource

/**
 * @author 3Embed
 * for saving data to dataBase
 * @since 1.0 (23-Aug-2019)
 */
open class BaseRepository(dataSource: DataSource) {
    private val loginDao: LoginDao = dataSource.db().login()
}