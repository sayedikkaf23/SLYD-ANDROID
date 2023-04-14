package com.kotlintestgradle

enum class CallDisconnectType(val tag: String) {
    REQUEST("request"), CALL("call"), BUSY("busy")
}