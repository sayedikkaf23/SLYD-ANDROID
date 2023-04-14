package com.kotlintestgradle.data

/**
 * @author 3Embed
 * for storing manager
 * whether to read or write to dataBase
 * @since 1.0 (23-Aug-2019)
 */
interface StorageManager {
    fun readFile()
    fun writeFile()
}