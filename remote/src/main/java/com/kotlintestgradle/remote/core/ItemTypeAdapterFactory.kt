package com.kotlintestgradle.remote.core

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * @author 3Embed
 *
 *for mapping data we get in the form of jason object
 *
 * used for custom typeAdapter
 *
 * used generic class
 *
 * @since 1.0 (23-Aug-2019)
 */
internal class ItemTypeAdapterFactory private constructor() : TypeAdapterFactory {
    companion object {
        private const val RESPONSE_TAG_MESSAGE = "message"
        private const val RESPONSE_TAG_DATA = "data"
        @JvmStatic
        fun newInstance(): TypeAdapterFactory {
            return ItemTypeAdapterFactory()
        }
    }

    // initializing and returning type adapter
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate: TypeAdapter<T> = gson.getDelegateAdapter(this, type)
        val elementAdapter: TypeAdapter<JsonElement> = gson.getAdapter(JsonElement::class.java)
        return ItemTypeAdapter(delegate, elementAdapter).nullSafe()
    }

    class ItemTypeAdapter<T>(
        private val delegate: TypeAdapter<T>,
        private val elementAdapter: TypeAdapter<JsonElement>
    ) : TypeAdapter<T>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter?, value: T) {
            delegate.write(out, value)
        }

        // reads json object and map to corresponding model class
        @Throws(IOException::class)
        override fun read(`in`: JsonReader?): T {
            val jsonElement = elementAdapter.read(`in`)
            if (jsonElement.isJsonObject) {
                val response: T = getResponse(jsonElement.asJsonObject)
                validateResponse(response)
                return response
            }
            return delegate.fromJsonTree(jsonElement)
        }

        // used to validate response
        private fun validateResponse(response: T) {
            if (response !is ValidItem) {
                // If response doesn't implement ValidItem, throw NetworkException
                val message = "Model must implement ValidItem"
                throw NetworkException(NetworkException.Error.InvalidModelType(), message)
            } else if (!response.isValid()) {
                // If response is in not valid, throw NetworkException
                val message = "Invalid Model from Network Layer"
                throw NetworkException(NetworkException.Error.InvalidModel(), message)
            }
        }

        // map data according to tag
        private fun getResponse(jsonObject: JsonObject): T {
            return if (jsonObject.has(RESPONSE_TAG_DATA)) {
                if (jsonObject.get(RESPONSE_TAG_DATA) is JsonArray) {
                    delegate.fromJsonTree(jsonObject)
                } else {
                    delegate.fromJsonTree(jsonObject.get(RESPONSE_TAG_DATA))
                }
            } else {
                delegate.fromJsonTree(jsonObject)
            }
        }
    }

    @Suppress("unused")
    data class Response<T : ValidItem>(
        @Expose @SerializedName(RESPONSE_TAG_DATA) var success: Boolean = false,
        @Expose @SerializedName(RESPONSE_TAG_MESSAGE) var message: String? = null,
        @Expose @SerializedName(RESPONSE_TAG_DATA) var data: T? = null
    )
}