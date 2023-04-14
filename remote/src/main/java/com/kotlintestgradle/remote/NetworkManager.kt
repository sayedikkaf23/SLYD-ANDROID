package com.kotlintestgradle.remote

/**
 * @author 3Embed
 *used for network manager
 * @since 1.0 (23-Aug-2019)
 */
interface NetworkManager {
    fun handler(): ApiHandler
}