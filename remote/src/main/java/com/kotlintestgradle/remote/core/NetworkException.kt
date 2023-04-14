package com.kotlintestgradle.remote.core

/**
 * @author 3Embed
 *
 *if any network exception is there this class is called
 *
 * @since 1.0 (23-Aug-2019)
 */
data class NetworkException(
    private val error: Error,
    override val message: String
) : Throwable(message) {
    sealed class Error(val code: String) {
        class InvalidModel : Error("N-001")
        class InvalidModelType : Error("N-002")
        class ServerError : Error("N-003")
        class Failure(code: String = "") : Error(code)
    }
}