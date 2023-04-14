package com.kotlintestgradle.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kotlintestgradle.cache.entity.Login

/**
 * @author 3Embed
 * Data Access Objects or DAOs are used to access our data when we implement Room
 * @since 1.0(23-Aug-2019)
 *
 */
@Dao
interface LoginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(login: Login): Long

    @Query("SELECT * FROM loginData")
    fun loadAll(): MutableList<Login>

    @Delete
    fun delete(photo: Login)

    @Query("DELETE FROM loginData")
    fun deleteAll()

    @Update
    fun update(login: Login)
}