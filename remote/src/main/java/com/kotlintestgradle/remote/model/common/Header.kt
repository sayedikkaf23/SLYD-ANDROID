package com.kotlintestgradle.remote.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
    @Expose @SerializedName("lang") var language: String
)