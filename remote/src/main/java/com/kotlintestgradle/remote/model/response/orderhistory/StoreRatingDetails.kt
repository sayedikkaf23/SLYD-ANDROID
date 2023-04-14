package com.kotlintestgradle.remote.model.response.orderhistory

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreRatingDetails(
  @SerializedName("rating") @Expose val rating : String,
  @SerializedName("reviewTitle") @Expose val reviewTitle : String,
  @SerializedName("isRated") @Expose val isRated : Boolean,
  @SerializedName("reviewDescription") @Expose val reviewDescription : String
) : Parcelable,ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}