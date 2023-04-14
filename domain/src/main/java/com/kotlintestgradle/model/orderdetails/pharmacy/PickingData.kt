package com.kotlintestgradle.model.orderdetails.pharmacy

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

data class PickingData(
  val type: String,
  val name: String,
  val phone: String,
  val email: String,
  val fcmTopic: String,
  val mqttTopic: String,
  val profilePic: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
      parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!!,
      parcel.readString()!!, parcel.readString()!!, parcel.readString()!!
  ) {
  }

  override fun writeToParcel(
    parcel: Parcel,
    flags: Int
  ) {
    parcel.writeString(type)
    parcel.writeString(name)
    parcel.writeString(phone)
    parcel.writeString(email)
    parcel.writeString(fcmTopic)
    parcel.writeString(mqttTopic)
    parcel.writeString(profilePic)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<PickingData> {
    override fun createFromParcel(parcel: Parcel): PickingData {
      return PickingData(parcel)
    }

    override fun newArray(size: Int): Array<PickingData?> {
      return arrayOfNulls(size)
    }
  }
}