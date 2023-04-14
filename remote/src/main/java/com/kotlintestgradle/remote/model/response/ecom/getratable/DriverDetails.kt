package com.customer.remote.http.model.response.getratable

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem
import com.kotlintestgradle.remote.model.response.ecom.getratable.RatedAttributes
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class DriverDetails(
  @SerializedName("driverName") @Expose val driverName:String?,
  @SerializedName("profilePic") @Expose val profilePic:String?,
  @SerializedName("driverId") @Expose val driverId:String?,
  @SerializedName("driverReview") @Expose val driverReview:String?,
  @SerializedName("rating") @Expose val rating:String?,
  @SerializedName("attribute") @Expose val attribute: ArrayList<RatedAttributes?>
) : Parcelable,ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}