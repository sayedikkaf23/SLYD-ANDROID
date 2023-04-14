package com.kotlintestgradle.model

/**
 * @author 3Embed
 * Mapper for participants list response'
 *
 * @since 1.0 (23-Aug-2019)
 */
data class ParticipantsListDataDetails(
    val users: List<LoginDataDetails>
)