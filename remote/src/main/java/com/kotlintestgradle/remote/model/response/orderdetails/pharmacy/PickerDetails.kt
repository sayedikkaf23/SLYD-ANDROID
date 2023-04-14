package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PickerDetaisl(
  @SerializedName("type") @Expose val type: String,
  @SerializedName("name") @Expose val name: String,
  @SerializedName("phone") @Expose val phone: String,
  @SerializedName("email") @Expose  val email: String,
  @SerializedName("fcmTopic") @Expose  val fcmTopic: String,
  @SerializedName("mqttTopic") @Expose val mqttTopic: String,
  @SerializedName("profilePic") @Expose val profilePic: String
) : Parcelable, ValidItem {
  override fun isValid(): Boolean {
    return true
  }

  constructor(parcel: Parcel) : this(
      parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!!,
      parcel.readString()!!, parcel.readString()!!, parcel.readString()!!
  ) {
  }


}