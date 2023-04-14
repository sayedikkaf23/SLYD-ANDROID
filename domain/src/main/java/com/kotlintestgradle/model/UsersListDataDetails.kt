package com.kotlintestgradle.model

/**
 * @author 3Embed
 * Mapper for users list response'
 *
 * @since 1.0 (23-Aug-2019)
 */
data class UsersListDataDetails(
    val data: List<LoginDataDetails>
)